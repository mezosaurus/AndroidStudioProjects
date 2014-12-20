package cs4962.shadowhunters;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class CardActivity extends Activity {
    private String mCardColor;
    private String mCardName;
    private String mCardText;
    private Player mCurrentPlayer;
    private boolean deActiveSelectPlayerBtn = false;
    static final int CARD_PLAYER_REQUEST = 20;
    private boolean mDamage = false;
    private boolean mHeal = false;
    private boolean mShowCard = false;
    private int mAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_detail);
        Intent i = getIntent();
        if (i.hasExtra("cardName")) {
            mCardName = i.getStringExtra("cardName");
        }
        if (i.hasExtra("cardText")) {
            mCardText = i.getStringExtra("cardText");
        }
        if (i.hasExtra("color")) {
            mCardColor = i.getStringExtra("color");
        }
        if (i.hasExtra("currentPlayer")) {
            mCurrentPlayer = i.getParcelableExtra("currentPlayer");
        }

        //LinearLayout layout = new LinearLayout(this);
        //layout.setOrientation(LinearLayout.VERTICAL);

        //TextView cardNameView = new TextView(this);
        TextView cardNameView = (TextView)findViewById(R.id.cardNameView);
        cardNameView.setText(mCardName);
        if (mCardColor.equals("GREEN")) {
            cardNameView.setBackgroundColor(Color.GREEN);
            cardNameView.setTextColor(Color.BLACK);
        }
        else if (mCardColor.equals("BLACK")) {
            cardNameView.setBackgroundColor(Color.BLACK);
            cardNameView.setTextColor(Color.WHITE);
            if (mCardName.equals("Banana Peel")) {
                deActiveSelectPlayerBtn = true;
            }
            else if (mCardName.equals("Diabolic Ritual")) {
                deActiveSelectPlayerBtn = true;
                if (mCurrentPlayer.getCharacter().getTeam().equals("SHADOW")) {
                    Button revealBtn = new Button(this);
                    revealBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent gameBoardIntent = new Intent(CardActivity.this, GameBoardActivity.class);
                            gameBoardIntent.putExtra("reveal", true);
                            setResult(RESULT_OK, gameBoardIntent);
                            finish();
                        }
                    });
                    revealBtn.setText("Reveal");
                    LinearLayout buttonsLayout = (LinearLayout) findViewById(R.id.cardDetailButtons);
                    buttonsLayout.addView(revealBtn, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                }
            }
        }
        else {
            cardNameView.setBackgroundColor(Color.WHITE);
            cardNameView.setTextColor(Color.BLACK);
            if (mCardName.equals("Flare of Judgement") || mCardName.equals("Holy Water of Healing")) {
                deActiveSelectPlayerBtn = true;
            }
            if (mCardName.equals("Advent")) {
                deActiveSelectPlayerBtn = true;
                // reveal identity if hunter
                String team = mCurrentPlayer.getCharacter().getTeam();
                if (team.equals("HUNTER")) {
                    Button revealBtn = new Button(this);
                    revealBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent gameBoardIntent = new Intent(CardActivity.this, GameBoardActivity.class);
                            gameBoardIntent.putExtra("reveal", true);
                            setResult(RESULT_OK, gameBoardIntent);
                            finish();
                        }
                    });
                    revealBtn.setText("Reveal");
                    LinearLayout buttonsLayout = (LinearLayout) findViewById(R.id.cardDetailButtons);
                    buttonsLayout.addView(revealBtn, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                }
            }

            else if(mCardName.equals("Chocolate")) {
                deActiveSelectPlayerBtn = true;
                // name begins with A, E, or U can reveal identity and full heal
                String charName = mCurrentPlayer.getCharacter().getName();
                boolean valid = charName.startsWith("A") || charName.startsWith("E") || charName.startsWith("U");
                if (valid) {
                    Button revealBtn = new Button(this);
                    revealBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent gameBoardIntent = new Intent(CardActivity.this, GameBoardActivity.class);
                            gameBoardIntent.putExtra("reveal", true);
                            setResult(RESULT_OK, gameBoardIntent);
                            finish();
                        }
                    });
                    revealBtn.setText("Reveal");
                    LinearLayout buttonsLayout = (LinearLayout) findViewById(R.id.cardDetailButtons);
                    buttonsLayout.addView(revealBtn, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                }
            }
            else if(mCardName.equals("Disenchant Mirror")) {
                deActiveSelectPlayerBtn = true;
                // name begins with V or W, must reveal
                String charName = mCurrentPlayer.getCharacter().getName();
                boolean valid = charName.startsWith("V") || charName.startsWith("W");
                if (valid) {
                    Button revealBtn = new Button(this);
                    revealBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent gameBoardIntent = new Intent(CardActivity.this, GameBoardActivity.class);
                            gameBoardIntent.putExtra("forcedReveal", true);
                            setResult(RESULT_OK, gameBoardIntent);
                            finish();
                        }
                    });
                    revealBtn.setText("Reveal");
                    LinearLayout buttonsLayout = (LinearLayout) findViewById(R.id.cardDetailButtons);
                    buttonsLayout.addView(revealBtn, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                }
            }
        }
        /*cardNameView.setBackgroundColor(Color.GRAY);
        cardNameView.setTextColor(Color.BLACK);
        cardNameView.setTextSize(50);
        cardNameView.setGravity(Gravity.CENTER);
        cardNameView.setPadding(10, 10, 10, 50);*/

        //TextView cardTextView = new TextView(this);
        TextView cardTextView = (TextView)findViewById(R.id.cardTextView);
        cardTextView.setText(mCardText);
        /*cardTextView.setGravity(Gravity.CENTER);
        cardTextView.setTextSize(20);*/

        //Button closeBtn = new Button(this);
        Button closeBtn = (Button)findViewById(R.id.cardCloseBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameBoardIntent = new Intent(CardActivity.this, GameBoardActivity.class);
                if (mCardName.equals("Banana Peel")) {
                    new Intent(CardActivity.this, GameBoardActivity.class);
                    gameBoardIntent.putExtra("bananapeel", true);
                    gameBoardIntent.putExtra("amount", 1);
                    setResult(RESULT_OK, gameBoardIntent);
                }
                else if(mCardName.equals("Flare of Judgement")) {
                    // 2 damage to all players except current player
                    gameBoardIntent.putExtra("flare", true);
                    setResult(RESULT_OK, gameBoardIntent);
                }
                else if(mCardName.equals("Holy Water of Healing")) {
                    // heal 2 damage
                    gameBoardIntent.putExtra("selfheal", true);
                    gameBoardIntent.putExtra("healAmount", 2);
                    setResult(RESULT_OK, gameBoardIntent);
                }
                else {
                    setResult(RESULT_CANCELED, gameBoardIntent);
                }
                finish();
            }
        });

        Button giveToPlayer = (Button)findViewById(R.id.cardGiveBtn);
        if (deActiveSelectPlayerBtn)
            giveToPlayer.setVisibility(View.INVISIBLE);
        giveToPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deActiveSelectPlayerBtn) {
                    return;
                }
                // open player list to select player to give card to
                Intent cardIntent = new Intent(CardActivity.this, PlayerListActivity.class);
                cardIntent.putExtra("cardrequest", true);
                cardIntent.putExtra("player", mCurrentPlayer);
                startActivityForResult(cardIntent, CARD_PLAYER_REQUEST);
            }
        });
        /*layout.addView(cardNameView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(cardTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(closeBtn, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(layout);*/
    }

    private void greenCardHandler(Player p) {
        String playerName = p.getName();
        String characterName = p.getCharacter().getName();
        String name = mCardName;
        String team = p.getCharacter().getTeam();
        int health = p.getCharacter().getHealth();

        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        ImageView characterCard = new ImageView(this);
        TextView view = new TextView(this);
        dialogLayout.addView(view, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final Intent greenCardIntent = new Intent(CardActivity.this, GameBoardActivity.class);
        greenCardIntent.putExtra("player", p);
        String dialogText = "Given card: " + mCardText + "\n\n" + playerName;
        if (name.equals("Aid")) {
            // hunter heal 1
            if (team.equals("HUNTER")) {
                dialogText += " healed 1 damage.";
                greenCardIntent.putExtra("heal", true);
                greenCardIntent.putExtra("amount", 1);
            }
            else {
                dialogText += " was not effected.";
            }
        }
        else if (name.equals("Anger")) {
            // hunter, shadow 1 damage
            if (team.equals("HUNTER") || team.equals("SHADOW")) {
                dialogText += " took 1 damage.";
                greenCardIntent.putExtra("damage", true);
                greenCardIntent.putExtra("amount", 1);
            }
            else {
                dialogText += " was not effected.";
            }
        }
        else if (name.equals("Blackmail")) {
            // hunter, neutral 1 damage
            if (team.equals("HUNTER") || team.equals("NEUTRAL")) {
                dialogText += " took 1 damage.";
                greenCardIntent.putExtra("damage", true);
                greenCardIntent.putExtra("amount", 1);
            }
            else {
                dialogText += " was not effected.";
            }
        }
        else if (name.equals("Bully")) {
            // less than or equal to 11 1 damage
            if (health <= 11) {
                dialogText += " took 1 damage.";
                greenCardIntent.putExtra("damage", true);
                greenCardIntent.putExtra("amount", 1);
            }
            else {
                dialogText += " was not effected.";
            }
        }
        else if (name.equals("Exorcism")) {
            // shadow 2 damage
            if (team.equals("SHADOW")) {
                dialogText += " took 2 damage.";
                greenCardIntent.putExtra("damage", true);
                greenCardIntent.putExtra("amount", 2);
            }
            else {
                dialogText += " was not effected.";
            }
        }
        else if (name.equals("Greed")) {
            // neutral, shadow 1 damage
            if (team.equals("SHADOW") || team.equals("NEUTRAL")) {
                dialogText += " took 1 damage.";
                greenCardIntent.putExtra("damage", true);
                greenCardIntent.putExtra("amount", 1);
            }
            else {
                dialogText += " was not effected.";
            }
        }
        else if (name.equals("Huddle")) {
            // shadow heal 1
            if (team.equals("SHADOW")) {
                dialogText += " healed 1 damage.";
                greenCardIntent.putExtra("heal", true);
                greenCardIntent.putExtra("amount", 1);
            }
            else {
                dialogText += " was not effected.";
            }
        }
        else if (name.equals("Nurturance")) {
            // neutral heak 1
            if (team.equals("NEUTRAL")) {
                dialogText += " healed 1 damage.";
                greenCardIntent.putExtra("heal", true);
                greenCardIntent.putExtra("amount", 1);
            }
            else {
                dialogText += " was not effected.";
            }
        }
        else if (name.equals("Prediction")) {
            // show card
            characterCard.setAdjustViewBounds(true);
            characterCard.setPadding(0, 20, 0, 0);
            characterCard.setImageDrawable(getCharacterImage(characterName));
            dialogLayout.addView(characterCard, 1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        else if (name.equals("Slap")) {
            // hunter 1 damage
            if (team.equals("HUNTER")) {
                dialogText += " took 1 damage.";
                greenCardIntent.putExtra("damage", true);
                greenCardIntent.putExtra("amount", 1);
            }
            else {
                dialogText += " was not effected.";
            }
        }
        else if (name.equals("Spell")) {
            // shadow 1 damage
            if (team.equals("SHADOW")) {
                dialogText += " took 1 damage.";
                greenCardIntent.putExtra("damage", true);
                greenCardIntent.putExtra("amount", 1);
            }
            else {
                dialogText += " was not effected.";
            }
        }
        else if (name.equals("Tough Lesson")) {
            // greater than or equal to 12 2 damage
            if (health >= 12) {
                dialogText += " took 2 damage.";
                greenCardIntent.putExtra("damage", true);
                greenCardIntent.putExtra("amount", 2);
            }
            else {
                dialogText += " was not effected.";
            }
        }
        view.setText(dialogText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Green Card Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK, greenCardIntent);
                finish();
                dialog.dismiss();
            }
        });
        builder.setView(dialogLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void blackCardHandler(Player p) {
        String playerName = p.getName();
        String name = mCardName;
        String team = p.getCharacter().getTeam();

        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        final Intent blackCardIntent = new Intent(CardActivity.this, GameBoardActivity.class);
        blackCardIntent.putExtra("player", p);
        String dialogText = "";

        if (name.equals("Bloodthirsty Spider")) {
            blackCardIntent.putExtra("selfdamage", true);
            blackCardIntent.putExtra("damage", true);
            blackCardIntent.putExtra("amount", 2);
            dialogText += playerName + " took 2 damage";
        }
        else if (name.equals("Spiritual Doll")) {
            // roll 6 sided die
            int roll = Utils.rollDie(6);
            dialogText += "You rolled a [" + roll + "]\n\n";
            if (roll <= 4) {
                blackCardIntent.putExtra("damage", true);
                dialogText += playerName + " took 3 damage.";
            }
            if (roll > 4) {
                blackCardIntent.putExtra("selfdamage", true);
                dialogText += "You took 3 damage.";
            }
            blackCardIntent.putExtra("amount", 3);
        }
        else if (name.equals("Vampire Bat")) {
            blackCardIntent.putExtra("damage", true);
            blackCardIntent.putExtra("selfheal", true);
            blackCardIntent.putExtra("amount", 2);
            blackCardIntent.putExtra("healAmount", 1);
            dialogText += playerName + " took 2 damage.\n\nYou healed 1 damage.";
        }
        TextView view = new TextView(this);
        view.setText(dialogText);
        dialogLayout.addView(view, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Black Card Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK, blackCardIntent);
                finish();
                dialog.dismiss();
            }
        });
        builder.setView(dialogLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void whiteCardHandler(Player p) {
        String name = mCardName;
        String playerName = p.getName();

        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        final Intent whiteCardIntent = new Intent(CardActivity.this, GameBoardActivity.class);
        whiteCardIntent.putExtra("player", p);
        String dialogText = "";

        if(name.equals("Blessing")) {
            // roll 6-sided die and heal that amount
            int roll = Utils.rollDie(6);
            dialogText += "You rolled a [" + roll + "]";
            dialogText += playerName + " healed for " + roll + " damage.";
            whiteCardIntent.putExtra("heal", true);
            whiteCardIntent.putExtra("amount", roll);
        }
        else if(name.equals("First Aid")) {
            // set character's damage to 7
            dialogText += playerName + " damage set to 7.";
            whiteCardIntent.putExtra("setDamage", 7);
        }

        TextView view = new TextView(this);
        view.setText(dialogText);
        dialogLayout.addView(view, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("White Card Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK, whiteCardIntent);
                finish();
                dialog.dismiss();
            }
        });
        builder.setView(dialogLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CARD_PLAYER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Player p = null;
                if (data.hasExtra("playerSelected")) {
                    p = data.getParcelableExtra("playerSelected");
                }
                if (mCardColor.equals("GREEN")) {
                    greenCardHandler(p);
                }
                else if (mCardColor.equals("BLACK")) {
                    blackCardHandler(p);
                }
                else {
                    whiteCardHandler(p);
                }
            }
        }
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
