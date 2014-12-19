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


public class CardActivity extends Activity {
    private AreaCard mCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String cardName = i.getStringExtra("cardName");
        String cardText = i.getStringExtra("cardText");
        String color = i.getStringExtra("color");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        if (color.equals("GREEN")) {
            greenCardHandler(layout, cardName);
        }
        else if (color.equals("BLACK")) {
            blackCardHandler(layout, cardName);
        }
        else {
            whiteCardHandler(layout, cardName);
        }

        TextView cardNameView = new TextView(this);
        cardNameView.setBackgroundColor(Color.GRAY);
        cardNameView.setTextColor(Color.BLACK);
        cardNameView.setTextSize(50);
        cardNameView.setText(cardName);
        cardNameView.setGravity(Gravity.CENTER);
        cardNameView.setPadding(10, 10, 10, 50);

        TextView cardTextView = new TextView(this);
        cardTextView.setText(cardText);
        cardTextView.setGravity(Gravity.CENTER);
        cardTextView.setTextSize(20);

        Button closeBtn = new Button(this);
        closeBtn.setText("Close");
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameBoardIntent = new Intent(CardActivity.this, GameBoardActivity.class);
                setResult(RESULT_OK, gameBoardIntent);
                finish();
            }
        });
        layout.addView(cardNameView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(cardTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(closeBtn, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(layout);
    }

    private void greenCardHandler(LinearLayout layout, String name) {
        if (name.equals("Aid")) {

        }
        else if (name.equals("Anger")) {

        }
        else if (name.equals("Blackmail")) {

        }
        else if (name.equals("Bully")) {

        }
        else if (name.equals("Exorcism")) {

        }
        else if (name.equals("Greed")) {

        }
        else if (name.equals("Huddle")) {

        }
        else if (name.equals("Nurturance")) {

        }
        else if (name.equals("Prediction")) {

        }
        else if (name.equals("Slap")) {

        }
        else if (name.equals("Spell")) {

        }
        else if (name.equals("Tough Lesson")) {

        }
    }

    private void blackCardHandler(LinearLayout layout, String name) {

    }

    private void whiteCardHandler(LinearLayout layout, String name) {

    }
}
