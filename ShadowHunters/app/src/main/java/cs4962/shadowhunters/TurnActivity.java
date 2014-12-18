package cs4962.shadowhunters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class TurnActivity  extends Activity{
    private AreaCard mCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);


        TextView turnText = (TextView)findViewById(R.id.turnTextView);
        //turnText.setText("Player " + p.getName() + "'s Turn\n\n1. Roll to move\n\n2. Decide to take action at the area card you move to\n\n3. Decide to attack another player within range.");
        final TextView resultText = (TextView)findViewById(R.id.turnResultTextView);

        Button moveBtn = (Button)findViewById(R.id.turnMoveBtn);
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sixRoll = Utils.rollDie(6);
                int fourRoll = Utils.rollDie(4);
                int movementPosition = sixRoll + fourRoll;
                resultText.setText("You rolled a [" + sixRoll + "] and a [" + fourRoll + "].\n\nMove to area card at position [" + movementPosition + "] - ");
            }
        });

        Button actionBtn = (Button)findViewById(R.id.turnActionBtn);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button attackBtn = (Button)findViewById(R.id.turnAttackBtn);
        attackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
