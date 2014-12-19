package cs4962.shadowhunters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Queue;


public class GameBoardActivity extends Activity {
    private Game mGame;
    private boolean mMoved;
    private boolean mCardDrawn;
    private int mNumShadowsDead = 0;
    private int mNumHuntersDead = 0;
    private int mNumNeutralsDead = 0;
    static final int PLAYER_ATTACK_REQUEST = 10;
    static final int CARD_REQUEST = 11;
    static final int WEIRDWOODS_ATTACK_REQUEST = 12;
    static final int WEIRDWOODS_HEAL_REQUEST = 13;
    private ArrayList<AreaCard> mGroupOne = new ArrayList<AreaCard>();
    private ArrayList<AreaCard> mGroupTwo = new ArrayList<AreaCard>();
    private ArrayList<AreaCard> mGroupThree = new ArrayList<AreaCard>();
    private File mTurnFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        PlayerList pList = PlayerList.getInstance();
        mTurnFile = new File(getFilesDir(), "Turn.txt");
        if (mTurnFile.exists()) {
            loadTurn();
        }
        mGame = new Game(new File(getFilesDir(), "Game.txt"));
        Player p = mGame.getCurrentPlayer();
        if (p.getBoardPosition() != null) {
            mMoved = true;
        }
        ArrayList<AreaCard> board = mGame.getBoard();
        // build damage GUI
        buildDamageGui();
        // build board GUI
        buildBoardGui(board);


        // Players list button listener
        Button playersListBtn = (Button)findViewById(R.id.boardPlayersListButton);
        playersListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to players list
                Intent playerListIntent = new Intent(GameBoardActivity.this, PlayerListActivity.class);
                startActivity(playerListIntent);
                finish();
            }
        });

        Button moveBtn = (Button)findViewById(R.id.turnMoveBtn);
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mMoved)
                    moveDialog();
            }
        });

        Button actionBtn = (Button)findViewById(R.id.turnActionBtn);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMoved)
                    actionDialog();
            }
        });

        Button attackBtn = (Button)findViewById(R.id.turnAttackBtn);
        attackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMoved) {
                    attackDialog();
                }
            }
        });

        if (mTurnFile.exists()) {
            // resume turn
            resumeTurnDialog();
        }
        else {
            // start turn
            turnDialog();
        }
    }

    public interface OnPlayerKilledListener {
        public void onPlayerKilled(Player p);
    }

    private void buildDamageGui() {
        // Get player list
        PlayerList pList = PlayerList.getInstance();
        // Get linear layout to add the views to
        LinearLayout damageLayout = (LinearLayout)findViewById(R.id.damageLinearLayout);
        damageLayout.removeAllViews();
        ArrayList<DamageView> data = new ArrayList<DamageView>();
        // Draw 15 damage views since damage range is 0-14
        for (int i = 14; i >= 0; i--) {
            TextView damageNumber = new TextView(GameBoardActivity.this);
            damageNumber.setText(i+"");
            damageNumber.setLines(1);
            LinearLayout damageRow = new LinearLayout(GameBoardActivity.this);
            damageRow.setOrientation(LinearLayout.HORIZONTAL);
            DamageView damageView = new DamageView(GameBoardActivity.this);
            damageView.setBackgroundColor(Color.GRAY);
            damageView.setPadding(0, 5, 0, 0);
            // Draw damage square views in each damage view for number of players
            // Color them if the player is at that damage level
            for (int color : pList.getIdentifiers()) {
                Player p = pList.getPlayer(color);
                DamageSquareView damageSquareView = new DamageSquareView(GameBoardActivity.this);
                damageSquareView.setPadding(5, 5, 5, 5);
                damageSquareView.setDamageValue(i);
                if (p.getDamage() == i) {
                    damageSquareView.setPlayerColor(color);
                } else {
                    damageSquareView.setPlayerColor(Color.GRAY);
                }
                damageView.addView(damageSquareView);
            }

            damageRow.addView(damageNumber, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            damageRow.addView(damageView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            damageLayout.addView(damageRow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));
        }
    }

    private void buildBoardGui(ArrayList<AreaCard> board) {
        for (int i = 0; i < 2; i++) {
            LinearLayout boardGroupOne = (LinearLayout)findViewById(R.id.boardGroupOne);
            TextView cardView = new TextView(GameBoardActivity.this);
            //cardView.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle));
            cardView.setGravity(Gravity.CENTER);
            AreaCard mCard = board.get(i);
            mGroupOne.add(mCard);
            String cardText = "(" + mCard.getRollOne() + ")";
            if (mCard.getRollTwo() != 0)
                cardText += " (" + mCard.getRollTwo() + ")\n\n";
            else
                cardText += "\n\n";
            cardText += mCard.getName() + "\n\n" + mCard.getText();
            cardView.setText(cardText);
            boardGroupOne.addView(cardView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        }
        for (int i = 2; i < 4; i++) {
            LinearLayout boardGroupTwo = (LinearLayout)findViewById(R.id.boardGroupTwo);
            TextView cardView = new TextView(GameBoardActivity.this);
            //cardView.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle));
            cardView.setGravity(Gravity.CENTER);
            AreaCard mCard = board.get(i);
            mGroupTwo.add(mCard);
            String cardText = "(" + mCard.getRollOne() + ")";
            if (mCard.getRollTwo() != 0)
                cardText += " (" + mCard.getRollTwo() + ")\n\n";
            else
                cardText += "\n\n";
            cardText += mCard.getName() + "\n\n" + mCard.getText();
            cardView.setText(cardText);
            boardGroupTwo.addView(cardView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        }
        for (int i = 4; i < 6; i++) {
            LinearLayout boardGroupThree = (LinearLayout)findViewById(R.id.boardGroupThree);
            TextView cardView = new TextView(GameBoardActivity.this);
            //cardView.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle));
            cardView.setGravity(Gravity.CENTER);
            AreaCard mCard = board.get(i);
            mGroupThree.add(mCard);
            String cardText = "(" + mCard.getRollOne() + ")";
            if (mCard.getRollTwo() != 0)
                cardText += " (" + mCard.getRollTwo() + ")\n\n";
            else
                cardText += "\n\n";
            cardText += mCard.getName() + "\n\n" + mCard.getText();
            cardView.setText(cardText);
            boardGroupThree.addView(cardView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        }
    }

    private Card drawCard(String color) {
        Player p = mGame.getCurrentPlayer();
        AreaCard boardPosition = p.getBoardPosition();
        if (boardPosition == null) {
            return null;
        }
        boolean validBoardPosition = true;

        if (color.equals("BLACK")) {
            // Valid black card draws are for underworld gate (4,5) and cemetery (8)
            if (boardPosition.getRollOne() == 4 || boardPosition.getRollOne() == 8) {
                Queue<BlackCard> bCards = mGame.getBlackCards();
                if (!bCards.isEmpty()) {
                    return bCards.remove();
                    //cardDialog(bCard);
                }
                else {
                    // Re build card deck
                    mGame.shuffleCards("BLACK");
                    return mGame.getBlackCards().remove();
                }
            }
            else {
                validBoardPosition = false;
            }
        }
        else if (color.equals("WHITE")) {
            // Valid white card draws are for underworld gate (4,5) and church (6)
            if (boardPosition.getRollOne() == 4 || boardPosition. getRollOne() == 6) {
                Queue<WhiteCard> wCards = mGame.getWhiteCards();
                if (!wCards.isEmpty()) {
                    return wCards.remove();
                    //cardDialog(wCard);
                }
                else {
                    // Re build card deck
                    mGame.shuffleCards("WHITE");
                    return mGame.getWhiteCards().remove();
                }
            }
            else {
                validBoardPosition = false;
            }
        }
        else if (color.equals("GREEN")) {
            // Valid green card draws are for hermit's cabin (2,3) and underworld gate (4,5)
            if (boardPosition.getRollOne() == 2 || boardPosition.getRollOne() == 4) {
                // draw green card
                Queue<GreenCard> gCards = mGame.getGreenCards();
                if (!gCards.isEmpty()) {
                    return gCards.remove();
                    //cardDialog(gCard);
                }
                else {
                    // Re build card deck
                    mGame.shuffleCards("GREEN");
                    return mGame.getGreenCards().remove();
                }
            }
            else {
                validBoardPosition = false;
            }
        }

        if (!validBoardPosition) {
            // Alert user they are not in the proper position to draw that card
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Invalid Action");
            builder.setMessage("You cannot draw a " + color + " card while in your current board position.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return null;
    }

    private void resumeTurnDialog() {
        // Get player
        Player p = mGame.getCurrentPlayer();
        AreaCard position = p.getBoardPosition();

        // Setup dialog layout
        ScrollView scrollLayout = new ScrollView(this);
        scrollLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final LinearLayout turnDialogLayout = new LinearLayout(this);
        turnDialogLayout.setOrientation(LinearLayout.VERTICAL);

        // Get turn layout view objects
        TextView turnText = new TextView(this);
        String name = p.getName();
        //turnText.setText("1. Roll to move\n\n2. Decide to take action at the area card you move to (Optional)\n\n3. Decide to attack another player within range. (Optional)");
        if (mMoved) {
            turnText.setText("You are at : " + position.getName());
        }
        else {
            turnText.setText("Please complete the Move action to continue turn.");
        }
        turnDialogLayout.addView(turnText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final Button characterButton = new Button(this);
        characterButton.setText("Show Character");
        characterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get image view
                if (characterButton.getText().equals("Show Character")) {
                    Player p = mGame.getCurrentPlayer();
                    Character c = p.getCharacter();
                    ImageView characterImage = new ImageView(GameBoardActivity.this);
                    characterImage.setAdjustViewBounds(true);
                    characterImage.setImageDrawable(getCharacterImage(c.getName()));
                    turnDialogLayout.addView(characterImage, 1);
                    characterButton.setText("Hide Character");
                }
                else {
                    turnDialogLayout.removeViewAt(1);
                    characterButton.setText("Show Character");
                }
            }
        });
        /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)characterButton.getLayoutParams();
        params.gravity = Gravity.CENTER;
        characterButton.setLayoutParams(params);*/
        turnDialogLayout.addView(characterButton, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Resume Turn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                turnDialogLayout.removeViewAt(1);
                dialog.dismiss();
            }
        });

        scrollLayout.addView(turnDialogLayout);

        builder.setView(scrollLayout);

        builder.setTitle(name + "'s Turn");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void turnDialog() {
        // Get player
        Player p = mGame.getCurrentPlayer();

        // Setup dialog layout
        ScrollView scrollLayout = new ScrollView(this);
        scrollLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final LinearLayout turnDialogLayout = new LinearLayout(this);
        turnDialogLayout.setOrientation(LinearLayout.VERTICAL);

        // Get turn layout view objects
        TextView turnText = new TextView(this);
        String name = p.getName();
        turnText.setText("1. Roll to move\n\n2. Decide to take action at the area card you move to (Optional)\n\n3. Decide to attack another player within range. (Optional)");
        turnDialogLayout.addView(turnText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final Button characterButton = new Button(this);
        characterButton.setText("Show Character");
        characterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get image view
                if (characterButton.getText().equals("Show Character")) {
                    Player p = mGame.getCurrentPlayer();
                    Character c = p.getCharacter();
                    ImageView characterImage = new ImageView(GameBoardActivity.this);
                    characterImage.setAdjustViewBounds(true);
                    characterImage.setImageDrawable(getCharacterImage(c.getName()));
                    turnDialogLayout.addView(characterImage, 1);
                    characterButton.setText("Hide Character");
                }
                else {
                    turnDialogLayout.removeViewAt(1);
                    characterButton.setText("Show Character");
                }
            }
        });
        /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)characterButton.getLayoutParams();
        params.gravity = Gravity.CENTER;
        characterButton.setLayoutParams(params);*/
        turnDialogLayout.addView(characterButton, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Start Turn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                turnDialogLayout.removeViewAt(1);
                dialog.dismiss();
            }
        });

        scrollLayout.addView(turnDialogLayout);

        builder.setView(scrollLayout);

        builder.setTitle(name + "'s Turn");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void moveDialog() {
        AreaCard currentPosition = mGame.getCurrentPlayer().getBoardPosition();
        LinearLayout moveDialogLayout = new LinearLayout(this);
        moveDialogLayout.setOrientation(LinearLayout.VERTICAL);
        moveDialogLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final TextView resultText = new TextView(this);
        moveDialogLayout.addView(resultText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Player p = mGame.getCurrentPlayer();
        int movementPosition = Utils.rollDie(6) + Utils.rollDie(4);
        //int movementPosition = 7;
        // Check to see if they got a 7
        if (movementPosition == 7) {
            // If 7 roll, inject new ListView to the dialog so they can select a board position
            final Spinner boardList = new Spinner(GameBoardActivity.this);
            ArrayList<String> data = new ArrayList<String>();
            data.add("Select area card");
            data.add("Hermit's Cabin");
            data.add("Underworld Gate");
            data.add("Church");
            data.add("Cemetery");
            data.add("Weird Woods");
            data.add("Erstwhile Altar");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(GameBoardActivity.this, android.R.layout.simple_spinner_item, data);
            boardList.setAdapter(adapter);

            moveDialogLayout.addView(boardList, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            resultText.setText("You rolled a 7. Please select the area card you wish to move to.");
            boardList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    String result = (String)adapterView.getItemAtPosition(pos);
                    if (result.equals("Select area card")) {
                        return;
                    }
                    if (mGame.getCurrentPlayer().getBoardPosition() != null && result.equals(mGame.getCurrentPlayer().getBoardPosition().getName())) {
                        resultText.setText("You rolled a 7. Please select the area card you wish to move to.\n\nYou selected the area card you are currently at, please try again.");
                        return;
                    }
                    for (int i = 0; i < mGame.getBoard().size(); i++) {
                        if (mGame.getBoard().get(i).getName().equals(result)) {
                            mGame.getCurrentPlayer().setBoardPosition(mGame.getBoard().get(i));
                            PlayerList.getInstance().getPlayer(mGame.getCurrentPlayer().getColor()).setBoardPosition(mGame.getBoard().get(i));
                            PlayerList.getInstance().savePlayerList(new File(getFilesDir(), "PlayerList.txt"));
                            mGame.saveGame(new File(getFilesDir(), "Game.txt"));
                        }
                    }
                    String text = "You rolled a 7. Please select the area card you wish to move to.";
                    text += "\n\nYou are moving to: " + result;
                    resultText.setText(text);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            return;
        }


        if (currentPosition != null) {
            // Check to make sure they didn't roll to the same position they are already at
            while ((movementPosition == currentPosition.getRollOne() || movementPosition == currentPosition.getRollTwo())) {
                movementPosition = Utils.rollDie(6) + Utils.rollDie(4);
            }
        }

        // Get the new areacard
        AreaCard newPosition = null;

        for (int i = 0; i < mGame.getBoard().size(); i++) {
            if (mGame.getBoard().get(i).getRollOne() == movementPosition || mGame.getBoard().get(i).getRollTwo() == movementPosition) {
                newPosition = mGame.getBoard().get(i);
            }
        }

        mGame.getCurrentPlayer().setBoardPosition(newPosition);
        PlayerList.getInstance().getPlayer(mGame.getCurrentPlayer().getColor()).setBoardPosition(newPosition);
        PlayerList.getInstance().savePlayerList(new File(getFilesDir(), "PlayerList.txt"));
        mGame.saveGame(new File(getFilesDir(), "Game.txt"));

        resultText.setText("You rolled a [" + movementPosition + "].\n\nMoved to area card - " + newPosition.getName() + "\n\n" + newPosition.getText());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Continue Turn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMoved = true;
                saveTurn();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("End Turn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                endTurn();
            }
        });

        builder.setView(moveDialogLayout);

        builder.setTitle(p.getName() + "'s Movement Result");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void actionDialog() {
        Player p = mGame.getCurrentPlayer();
        AreaCard position = p.getBoardPosition();
        String positionName = position.getName();

        LayoutInflater inflater = this.getLayoutInflater();
        LinearLayout actionLayout = (LinearLayout) inflater.inflate(R.layout.card_draw, null, false);
        TextView infoText = (TextView)actionLayout.findViewById(R.id.areaCardActionText);
        infoText.setText("You are at : " + position.getName() + "\n\n" + position.getText() + "\n\n");
        TextView greenCardDraw = (TextView)actionLayout.findViewById(R.id.greenCardDraw);
        TextView blackCardDraw = (TextView)actionLayout.findViewById(R.id.blackCardDraw);
        TextView whiteCardDraw = (TextView)actionLayout.findViewById(R.id.whiteCardDraw);
        LinearLayout weirdWoodsLayout = (LinearLayout)actionLayout.findViewById(R.id.weirdWoodsBtns);

        if (positionName.equals("Underworld Gate")) {
            // Show all 3 card draws
            greenCardDraw.setVisibility(View.VISIBLE);
            blackCardDraw.setVisibility(View.VISIBLE);
            whiteCardDraw.setVisibility(View.VISIBLE);
        }
        else if (positionName.equals("Hermit's Cabin")) {
            // Show green draw
            greenCardDraw.setVisibility(View.VISIBLE);
        }
        else if (positionName.equals("Cemetery")) {
            // Show black draw
            blackCardDraw.setVisibility(View.VISIBLE);
        }
        else if (positionName.equals("Church")) {
            // Show white draw
            whiteCardDraw.setVisibility(View.VISIBLE);
        }
        else if (positionName.equals("Erstwhile Altar")) {
            // Steal equipment, show player list
            Intent stealIntent = new Intent(GameBoardActivity.this, PlayerListActivity.class);
            stealIntent.putExtra("action", "steal");
        }
        else {
            // Damage or heal, show player list
            weirdWoodsLayout.setVisibility(View.VISIBLE);
            Button healBtn = (Button)weirdWoodsLayout.findViewById(R.id.weirdWoodsHealBtn);
            healBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent healIntent = new Intent(GameBoardActivity.this, PlayerListActivity.class);
                    healIntent.putExtra("weirdwoods", "heal");
                    healIntent.putExtra("amount", 1);
                }
            });
            Button dmgBtn = (Button)weirdWoodsLayout.findViewById(R.id.weirdWoodsDmgBtn);
            dmgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent dmgIntent = new Intent(GameBoardActivity.this, PlayerListActivity.class);
                    dmgIntent.putExtra("weirdwoods", "damage");
                    dmgIntent.putExtra("amount", 2);
                    startActivityForResult(dmgIntent, WEIRDWOODS_ATTACK_REQUEST);
                }
            });
        }
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setPositiveButton("Continue Turn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveTurn();
                dialog.dismiss();
            }
        });

        dialogBuilder.setNegativeButton("End Turn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMoved = false;
                saveTurn();
                dialog.dismiss();
                endTurn();
            }
        });

        dialogBuilder.setView(actionLayout);
        dialogBuilder.setTitle("Area Card Action");
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        // green card button
        greenCardDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCardDrawn) {
                    return;
                }
                // green card draw selected
                Card card = drawCard("GREEN");
                if (card == null)
                    return;
                Intent cardIntent = new Intent(GameBoardActivity.this, CardActivity.class);
                cardIntent.putExtra("cardName", card.getName());
                cardIntent.putExtra("cardText", card.getText());
                cardIntent.putExtra("color", "GREEN");
                startActivityForResult(cardIntent, CARD_REQUEST);
            }
        });

        // black card button
        blackCardDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCardDrawn) {
                    return;
                }
                // black card draw
                Card card = drawCard("BLACK");
                if (card == null)
                    return;

                Intent cardIntent = new Intent(GameBoardActivity.this, CardActivity.class);
                cardIntent.putExtra("cardName", card.getName());
                cardIntent.putExtra("cardText", card.getText());
                cardIntent.putExtra("color", "BLACK");
                startActivityForResult(cardIntent, CARD_REQUEST);
            }
        });

        // white card button
        whiteCardDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCardDrawn) {
                    return;
                }
                // white card draw
                Card card = drawCard("WHITE");
                if (card == null)
                    return;
                Intent cardIntent = new Intent(GameBoardActivity.this, CardActivity.class);
                cardIntent.putExtra("cardName", card.getName());
                cardIntent.putExtra("cardText", card.getText());
                cardIntent.putExtra("color", "WHITE");
                startActivityForResult(cardIntent, CARD_REQUEST);
            }
        });
    }

    private void attackDialog() {
        Intent attackIntent = new Intent(GameBoardActivity.this, PlayerListActivity.class);
        attackIntent.putExtra("attack", true);
        startActivityForResult(attackIntent, PLAYER_ATTACK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CARD_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mCardDrawn = true;
            }
        }
        if (requestCode == WEIRDWOODS_ATTACK_REQUEST) {
            if (resultCode == RESULT_OK) {
                Player attackedPlayer = data.getParcelableExtra("playerToAttack");
                Player pListPlayer = PlayerList.getInstance().getPlayer(attackedPlayer.getColor());
                int dmgAmount = data.getIntExtra("dmgAmount", 2);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                TextView test = new TextView(this);

                test.setText("Successfully attacked " + attackedPlayer.getName() + " for " + dmgAmount);
                builder.setTitle("Attack Success");
                int damage = attackedPlayer.getDamage() + dmgAmount;
                if (damage > attackedPlayer.getCharacter().getHealth()) {
                    damage = attackedPlayer.getCharacter().getHealth();
                    attackedPlayer.setRevealed(true);
                    attackedPlayer.setDead(true);
                    pListPlayer.setRevealed(true);
                    pListPlayer.setDead(true);
                    String team = pListPlayer.getCharacter().getTeam();
                    playerDead(team);
                }
                attackedPlayer.setDamage(damage);
                pListPlayer.setDamage(damage);
                PlayerList.getInstance().savePlayerList(new File(getFilesDir(), "PlayerList.txt"));

                builder.setView(test);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMoved = false;
                        saveTurn();
                        //GameBoardActivity.this.findViewById(R.id.damageLinearLayout).invalidate();
                        buildDamageGui();
                        dialog.dismiss();
                        //endTurn();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        if (requestCode == WEIRDWOODS_HEAL_REQUEST) {
            if (resultCode == RESULT_OK) {
                Player healedPlayer = data.getParcelableExtra("playerToHeal");
                Player pListPlayer = PlayerList.getInstance().getPlayer(healedPlayer.getColor());
                int healAmount = data.getIntExtra("healAmount", 1);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                TextView test = new TextView(this);

                test.setText("Successfully healed " + healedPlayer.getName() + " for " + healAmount);
                builder.setTitle("Heal Success");
                int damage = healedPlayer.getDamage() - healAmount;
                if (damage < 0) {
                    damage = 0;
                }
                healedPlayer.setDamage(damage);
                pListPlayer.setDamage(damage);
                PlayerList.getInstance().savePlayerList(new File(getFilesDir(), "PlayerList.txt"));

                builder.setView(test);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMoved = false;
                        saveTurn();
                        //GameBoardActivity.this.findViewById(R.id.damageLinearLayout).invalidate();
                        buildDamageGui();
                        dialog.dismiss();
                        //endTurn();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        if (requestCode == PLAYER_ATTACK_REQUEST) {
            if (resultCode == RESULT_OK) {
                Player attackedPlayer = data.getParcelableExtra("playerToAttack");
                Player pListPlayer = PlayerList.getInstance().getPlayer(attackedPlayer.getColor());
                // Determine if selected player is in attack range
                AreaCard position = attackedPlayer.getBoardPosition();
                Player currentPlayer = mGame.getCurrentPlayer();
                AreaCard currentPosition = currentPlayer.getBoardPosition();
                boolean inRange = inRange(getBoardGroup(currentPosition), position);
                /*if (mGroupOne.contains(position) && mGroupOne.contains(currentPosition)) {
                    inRange = true;
                }
                else if (mGroupTwo.contains(position) && mGroupTwo.contains(currentPosition)) {
                    inRange = true;
                }
                else if (mGroupThree.contains(position) && mGroupThree.contains(currentPosition)) {
                    inRange = true;
                }*/
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                TextView test = new TextView(this);
                if (inRange) {
                    int attackDmg = Math.abs(Utils.rollDie(6) - Utils.rollDie(4));
                    if (attackDmg > 0) {
                        test.setText("Successfully attacked " + attackedPlayer.getName() + " for " + attackDmg);
                        builder.setTitle("Attack Success");
                        int damage = attackedPlayer.getDamage() + attackDmg;
                        if (damage > attackedPlayer.getCharacter().getHealth()) {
                            damage = attackedPlayer.getCharacter().getHealth();
                            attackedPlayer.setRevealed(true);
                            attackedPlayer.setDead(true);
                            pListPlayer.setRevealed(true);
                            pListPlayer.setDead(true);
                            String team = pListPlayer.getCharacter().getTeam();
                            playerDead(team);
                        }
                        attackedPlayer.setDamage(damage);
                        pListPlayer.setDamage(damage);
                        PlayerList.getInstance().savePlayerList(new File(getFilesDir(), "PlayerList.txt"));
                    }
                    else {
                        test.setText("You rolled a 0. Attack failed.");
                        builder.setTitle("Attack Failure");
                    }
                }
                else {
                    test.setText("ATTACK FAILED. PLAYER SELECTED NOT IN RANGE.");
                    builder.setTitle("Attack Failure");
                }
                builder.setView(test);
                builder.setPositiveButton("End Turn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMoved = false;
                        saveTurn();
                        //GameBoardActivity.this.findViewById(R.id.damageLinearLayout).invalidate();
                        buildDamageGui();
                        dialog.dismiss();
                        endTurn();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private int getBoardGroup (AreaCard card) {
        String name = card.getName();
        int retVal = 0;
        for (int i = 0; i < mGroupOne.size(); i++) {
            String cardName = mGroupOne.get(i).getName();
            if (cardName.equals(name)) {
                retVal = 1;
            }
        }
        for (int i = 0; i < mGroupTwo.size(); i++) {
            String cardName = mGroupTwo.get(i).getName();
            if (cardName.equals(name)) {
                retVal = 2;
            }
        }
        for (int i = 0; i < mGroupThree.size(); i++) {
            String cardName = mGroupThree.get(i).getName();
            if (cardName.equals(name)) {
                retVal = 3;
            }
        }
        return retVal;
    }

    private boolean inRange(int boardGroup, AreaCard card) {
        boolean inRange = false;
        String name = card.getName();
        switch (boardGroup) {
            case 1:
                for (int i = 0; i < mGroupOne.size(); i++) {
                    String cardName = mGroupOne.get(i).getName();
                    if (cardName.equals(name)) {
                        inRange = true;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < mGroupTwo.size(); i++) {
                    String cardName = mGroupTwo.get(i).getName();
                    if (cardName.equals(name)) {
                        inRange = true;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < mGroupThree.size(); i++) {
                    String cardName = mGroupThree.get(i).getName();
                    if (cardName.equals(name)) {
                        inRange = true;
                    }
                }
                break;
        }
        return inRange;
    }

    private void endTurn() {
        // Rotate players
        Player p = mGame.getPlayers().pop();
        if (!p.isDead()) {
            mGame.setCurrentPlayer(p);
            mGame.getPlayers().addLast(p);
        }

        turnDialog();
    }

    private void saveTurn() {
        Gson gson = new Gson();
        // Save moved boolean
        String jsonMoved = gson.toJson(mMoved);
        String jsonCardDrawn = gson.toJson(mCardDrawn);
        String jsonHuntersDead = gson.toJson(mNumHuntersDead);
        String jsonShadowsDead = gson.toJson(mNumShadowsDead);
        String jsonNeutralsDead = gson.toJson(mNumNeutralsDead);

        try {
            FileWriter textWriter = new FileWriter(mTurnFile);
            BufferedWriter bufferedTextWriter = new BufferedWriter(textWriter);
            bufferedTextWriter.write(jsonMoved + "\n");
            bufferedTextWriter.write(jsonCardDrawn + "\n");
            bufferedTextWriter.write(jsonHuntersDead + "\n");
            bufferedTextWriter.write(jsonShadowsDead + "\n");
            bufferedTextWriter.write(jsonNeutralsDead);
            bufferedTextWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTurn() {
        try {
            FileReader textReader = new FileReader(mTurnFile);
            BufferedReader bufferedTextReader = new BufferedReader(textReader);

            String jsonMoved = bufferedTextReader.readLine();
            String jsonCardDrawn = bufferedTextReader.readLine();
            String jsonHuntersDead = bufferedTextReader.readLine();
            String jsonShadowsDead = bufferedTextReader.readLine();
            String jsonNeutralsDead = bufferedTextReader.readLine();

            Gson gson = new Gson();

            mMoved = gson.fromJson(jsonMoved, Boolean.class);
            mCardDrawn = gson.fromJson(jsonCardDrawn, Boolean.class);
            mNumHuntersDead = gson.fromJson(jsonHuntersDead, Integer.class);
            mNumShadowsDead = gson.fromJson(jsonShadowsDead, Integer.class);
            mNumNeutralsDead = gson.fromJson(jsonNeutralsDead, Integer.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playerDead(String team) {
        int numPlayers = PlayerList.getInstance().getIdentifiers().size();
        int numHunters = 0;
        int numShadows = 0;
        int numNeutrals = 0;
        for (int color: PlayerList.getInstance().getIdentifiers()) {
            Player p = PlayerList.getInstance().getPlayer(color);
            if (p.getCharacter().getTeam().equals("HUNTER")) {
                numHunters++;
            }
            else if (p.getCharacter().getTeam().equals("SHADOW")) {
                numShadows++;
            }
            else if (p.getCharacter().getTeam().equals("NEUTRAL")) {
                numNeutrals++;
            }
        }
        if (team.equals("HUNTER")) {
            mNumHuntersDead++;
            if (mNumHuntersDead == numHunters) {
                // All hunters dead, shadows win
                endGame("SHADOWS");
            }
        }
        else if (team.equals("SHADOW")) {
            mNumShadowsDead++;
            if (mNumShadowsDead == numShadows) {
                // All shadows dead, hunters win
                endGame("HUNTERS");
            }
        }
        else if (team.equals("NEUTRAL")) {
            mNumNeutralsDead++;
            if (numNeutrals == 3 && mNumNeutralsDead == numNeutrals) {
                // 3 neutrals dead, shadows win
                endGame("SHADOWS");
            }
        }
    }

    private void endGame (String winningTeam) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView endView = new TextView(this);
        endView.setText(winningTeam + " WON THE GAME");
        builder.setTitle("GAME OVER");
        builder.setView(endView);
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File gameFile = new File(getFilesDir(), "Game.txt");
                gameFile.delete();
                dialogInterface.dismiss();
                Intent newGame = new Intent(GameBoardActivity.this, HomeActivity.class);
                startActivity(newGame);
                finish();
            }
        });
        builder.setNegativeButton("Close App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File gameFile = new File(getFilesDir(), "Game.txt");
                gameFile.delete();
                finish();
                System.exit(0);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Drawable getCharacterImage(String name) {
        if (name.equals("Allie")) {
            return getResources().getDrawable(R.drawable.allie);
        }
        else if (name.equals("Bob")) {
            return getResources().getDrawable(R.drawable.bob);
        }
        else if (name.equals("Charles")) {
            return getResources().getDrawable(R.drawable.charles);
        }
        else if (name.equals("Daniel")) {
            return getResources().getDrawable(R.drawable.daniel);
        }
        else if (name.equals("Emi")) {
            return getResources().getDrawable(R.drawable.emi);
        }
        else if (name.equals("Franklin")) {
            return getResources().getDrawable(R.drawable.franklin);
        }
        else if (name.equals("George")) {
            return getResources().getDrawable(R.drawable.george);
        }
        else if (name.equals("Unknown")) {
            return getResources().getDrawable(R.drawable.unknown);
        }
        else if (name.equals("Vampire")) {
            return getResources().getDrawable(R.drawable.vampire);
        }
        else {
            return getResources().getDrawable(R.drawable.werewolf);
        }
    }
}
