package cs4962.shadowhunters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;


public class GameBoardActivity extends Activity {
    private Game mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        PlayerList pList = PlayerList.getInstance();
        mGame = new Game(new File(getFilesDir(), "Game.txt"));
        Player p = mGame.getCurrentPlayer();
        ArrayList<AreaCard> board = mGame.getBoard();
        // build damage GUI
        buildDamageGui();
        // build board GUI
        buildBoardGui(board);


        // green card button
        TextView greenCardDraw = (TextView)findViewById(R.id.greenCardDraw);
        greenCardDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // green card draw selected
                drawCard("GREEN");
            }
        });

        // black card button
        TextView blackCardDraw = (TextView)findViewById(R.id.blackCardDraw);
        blackCardDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // black card draw
                drawCard("BLACK");
            }
        });

        // white card button
        TextView whiteCardDraw = (TextView)findViewById(R.id.whiteCardDraw);
        whiteCardDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // white card draw
                drawCard("BLACK");
            }
        });

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

        turnDialog();
    }

    private void buildDamageGui() {
        // Get player list
        PlayerList pList = PlayerList.getInstance();
        // Get linear layout to add the views to
        LinearLayout damageLayout = (LinearLayout)findViewById(R.id.damageLinearLayout);
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
            String cardText = "(" + mCard.getRollOne() + ")";
            if (mCard.getRollTwo() != 0)
                cardText += " (" + mCard.getRollTwo() + ")\n\n";
            else
                cardText += "\n\n";
            cardText += mCard.getName() + "\n\n" + mCard.getText();
            cardView.setText(cardText);
            boardGroupThree.addView(cardView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        }
        // Get layout to put the views in
        /*LinearLayout boardLayout = (LinearLayout)findViewById(R.id.boardLinearLayout);
        BoardView boardView = new BoardView(GameBoardActivity.this);
        for (int i = 0; i < board.size(); i++) {
            TextView aCardView = new TextView(GameBoardActivity.this);
            aCardView.setBackgroundColor(Color.GRAY);
            aCardView.setTextColor(Color.WHITE);
            aCardView.setTextSize(10);
            AreaCard mCard = board.get(i);
            String cardText = "(" + mCard.getRollOne() + ")";
            if (mCard.getRollTwo() != 0)
                cardText += " (" + mCard.getRollTwo() + ")\n\n";
            else
                cardText += "\n\n";
            cardText += mCard.getName() + "\n\n" + mCard.getText();
            aCardView.setText("TEST");
            //AreaCardView aCardView = new AreaCardView(GameBoardActivity.this);
            //aCardView.setCard(board.get(i));
            aCardView.setPadding(5, 5, 5, 5);
            boardView.addView(aCardView);
        }
        boardLayout.addView(boardView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));*/
        /*AreaCard one = board.get(0);
        AreaCard two = board.get(1);
        AreaCard three = board.get(2);
        AreaCard four = board.get(3);
        AreaCard five = board.get(4);
        AreaCard six = board.get(5);

        TextView text1 = (TextView)findViewById(R.id.boardSpot0);
        String cardText1 = "(" + one.getRollOne() + ")";
        if (one.getRollTwo() != 0)
            cardText1 += " (" + one.getRollTwo() + ")\n\n";
        else
            cardText1 += "\n\n";
        cardText1 += one.getName() + "\n\n" + one.getText();
        text1.setText(cardText1);

        TextView text2 = (TextView)findViewById(R.id.boardSpot1);
        String cardText2 = "(" + two.getRollOne() + ")";
        if (two.getRollTwo() != 0)
            cardText2 += " (" + two.getRollTwo() + ")\n\n";
        else
            cardText2 += "\n\n";
        cardText2 += two.getName() + "\n\n" + two.getText();
        text2.setText(cardText2);

        TextView text3 = (TextView)findViewById(R.id.boardSpot2);
        String cardText3 = "(" + three.getRollOne() + ")";
        if (three.getRollTwo() != 0)
            cardText3 += " (" + three.getRollTwo() + ")\n\n";
        else
            cardText3 += "\n\n";
        cardText3 += three.getName() + "\n\n" + three.getText();
        text3.setText(cardText3);

        TextView text4 = (TextView)findViewById(R.id.boardSpot3);
        String cardText4 = "(" + four.getRollOne() + ")";
        if (four.getRollTwo() != 0)
            cardText4 += " (" + four.getRollTwo() + ")\n\n";
        else
            cardText4 += "\n\n";
        cardText4 += four.getName() + "\n\n" + four.getText();
        text4.setText(cardText4);

        TextView text5 = (TextView)findViewById(R.id.boardSpot4);
        String cardText5 = "(" + five.getRollOne() + ")";
        if (five.getRollTwo() != 0)
            cardText5 += " (" + five.getRollTwo() + ")\n\n";
        else
            cardText5 += "\n\n";
        cardText5 += five.getName() + "\n\n" + five.getText();
        text5.setText(cardText5);

        TextView text6 = (TextView)findViewById(R.id.boardSpot5);
        String cardText6 = "(" + six.getRollOne() + ")";
        if (six.getRollTwo() != 0)
            cardText6 += " (" + six.getRollTwo() + ")\n\n";
        else
            cardText6 += "\n\n";
        cardText6 += six.getName() + "\n\n" + six.getText();
        text6.setText(cardText6);*/
    }

    private void drawCard(String color) {
        Player p = mGame.getCurrentPlayer();
        AreaCard boardPosition = p.getBoardPosition();
        if (boardPosition == null) {
            return;
        }
        boolean validBoardPosition = true;

        if (color.equals("BLACK")) {
            // Valid black card draws are for underworld gate (4,5) and cemetery (8)
            if (boardPosition.getRollOne() == 4 || boardPosition.getRollOne() == 8) {
                Queue<BlackCard> bCards = mGame.getBlackCards();
                if (!bCards.isEmpty()) {
                    BlackCard bCard = bCards.remove();
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
                    WhiteCard wCard = wCards.remove();
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
                    GreenCard gCard = gCards.remove();
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
    }

    private void turnDialog() {
        // Get player
        Player p = mGame.getCurrentPlayer();

        // Inflate turn layout into view
        View turnView = View.inflate(this, R.layout.activity_turn, null);

        // Get turn layout view objects
        TextView turnText = (TextView)turnView.findViewById(R.id.turnTextView);
        String name = p.getName();
        turnText.setText("Player " + name + "'s Turn\n\n1. Roll to move\n\n2. Decide to take action at the area card you move to\n\n3. Decide to attack another player within range.");
        final TextView resultText = (TextView)turnView.findViewById(R.id.turnResultTextView);

        // Setup button listeners
        final Button moveBtn = (Button)turnView.findViewById(R.id.turnMoveBtn);
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int movementPosition = Utils.rollDie(6) + Utils.rollDie(4);
                // Check to see if they got a 7
                if (movementPosition == 7) {
                    // If 7 roll, inject new ListView to the dialog so they can select a board position
                    Spinner boardList = new Spinner(GameBoardActivity.this);
                    ArrayList<String> data = new ArrayList<String>();
                    data.add("Hermit's Cabin");
                    data.add("Underworld Gate");
                    data.add("Church");
                    data.add("Cemetery");
                    data.add("Weird Woods");
                    data.add("Erstwhile Altar");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(GameBoardActivity.this, android.R.layout.simple_spinner_item, data);
                    boardList.setAdapter(adapter);
                    LinearLayout turnLayout = (LinearLayout)findViewById(R.id.turnLayout);
                    turnLayout.addView(boardList, 1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    resultText.setText("ROLLED A 7. PICK YOUR SPOT.");
                    return;
                }

                AreaCard currentPosition = mGame.getCurrentPlayer().getBoardPosition();
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

                resultText.setText("You rolled a [" + movementPosition + "].\n\nMove to area card - " + newPosition.getName());

                moveBtn.setVisibility(View.INVISIBLE);
            }
        });

        Button actionBtn = (Button)turnView.findViewById(R.id.turnActionBtn);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button attackBtn = (Button)turnView.findViewById(R.id.turnAttackBtn);
        attackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("End Turn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setView(turnView);

        builder.setTitle("Turn");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
