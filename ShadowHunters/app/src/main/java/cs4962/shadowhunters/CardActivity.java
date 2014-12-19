package cs4962.shadowhunters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class CardActivity extends Activity {
    private String mCardColor;
    private String mCardName;
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
        String cardText = i.getStringExtra("cardText");
        if (i.hasExtra("color")) {
            mCardColor = i.getStringExtra("color");
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
        }
        else {
            cardNameView.setBackgroundColor(Color.WHITE);
            cardNameView.setTextColor(Color.BLACK);
        }
        /*cardNameView.setBackgroundColor(Color.GRAY);
        cardNameView.setTextColor(Color.BLACK);
        cardNameView.setTextSize(50);
        cardNameView.setGravity(Gravity.CENTER);
        cardNameView.setPadding(10, 10, 10, 50);*/

        //TextView cardTextView = new TextView(this);
        TextView cardTextView = (TextView)findViewById(R.id.cardTextView);
        cardTextView.setText(cardText);
        /*cardTextView.setGravity(Gravity.CENTER);
        cardTextView.setTextSize(20);*/

        //Button closeBtn = new Button(this);
        Button closeBtn = (Button)findViewById(R.id.cardCloseBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameBoardIntent = new Intent(CardActivity.this, GameBoardActivity.class);
                setResult(RESULT_CANCELED, gameBoardIntent);
                finish();
            }
        });

        Button giveToPlayer = (Button)findViewById(R.id.cardGiveBtn);
        giveToPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open player list to select player to give card to
                Intent cardIntent = new Intent(CardActivity.this, PlayerListActivity.class);
                cardIntent.putExtra("cardrequest", true);
                startActivityForResult(cardIntent, CARD_PLAYER_REQUEST);
            }
        });
        /*layout.addView(cardNameView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(cardTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(closeBtn, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(layout);*/


    }

    private void greenCardHandler(Player p) {
        String name = mCardName;
        String team = p.getCharacter().getTeam();
        int health = p.getCharacter().getHealth();

        Intent greenCardIntent = new Intent(CardActivity.this, PlayerListActivity.class);
        String dialogText = "";
        if (name.equals("Aid")) {
            // hunter heal 1
            if (team.equals("HUNTER")) {
                dialogText += "";
            }
        }
        else if (name.equals("Anger")) {
            // hunter, shadow 1 damage

        }
        else if (name.equals("Blackmail")) {
            // hunter, neutral 1 damage

        }
        else if (name.equals("Bully")) {
            // less than or equal to 11 1 damage
            greenCardIntent.putExtra("healthLessThan", 11);
        }
        else if (name.equals("Exorcism")) {
            // shadow 2 damage

        }
        else if (name.equals("Greed")) {
            // neutral, shadow 1 damage

        }
        else if (name.equals("Huddle")) {
            // shadow heal 1

        }
        else if (name.equals("Nurturance")) {
            // neutral heak 1

        }
        else if (name.equals("Prediction")) {
            // show card
        }
        else if (name.equals("Slap")) {
            // hunter 1 damage

        }
        else if (name.equals("Spell")) {
            // shadow 1 damage

        }
        else if (name.equals("Tough Lesson")) {
            // greater than or equal to 12 2 damage

        }
    }

    private void blackCardHandler() {
        String name = mCardName;
    }

    private void whiteCardHandler() {
        String name = mCardName;
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
                    blackCardHandler();
                }
                else {
                    whiteCardHandler();
                }
            }
        }
    }
}
