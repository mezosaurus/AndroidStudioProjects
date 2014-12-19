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

        TextView cardNameView = new TextView(this);
        cardNameView.setBackgroundColor(Color.GRAY);
        cardNameView.setTextColor(Color.BLACK);
        cardNameView.setTextSize(50);
        cardNameView.setText(cardName);
        cardNameView.setGravity(Gravity.CENTER);

        TextView cardTextView = new TextView(this);
        cardTextView.setText(cardText);
        cardTextView.setGravity(Gravity.CENTER);

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

    private void greenCardHandler() {

    }

    private void blackCardHandler() {

    }

    private void whiteCardHandler() {

    }
}
