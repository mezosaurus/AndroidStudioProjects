package cs4962.shadowhunters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.Parse;

import java.io.File;


public class HomeActivity extends Activity {

    private static final String[] mNumPlayersList = new String[]{"4", "5", "6", "7", "8"};
    private String mNumPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Parse.initialize(this, "m9zybGhREf8v7B6rMz7FcIpTKDLiE0fAPuyXcliF", "8vVSUXMFrpKc83qwpHyb3gig1IKsh7twxiTlRfWl");
        setContentView(R.layout.activity_home);
        PlayerList pList = PlayerList.getInstance();
        File f = new File(getFilesDir(), "PlayerList.txt");
        pList.setPlayerListFile(f);
        if (f.exists()) {
            // Player list read from file, skip game creation
            Intent gameBoardIntent = new Intent(this, GameBoardActivity.class);
            startActivity(gameBoardIntent);
            finish();
        }
        else {
            setupNumPlayersDropdown();
            setupCreateGameBtn();
        }
    }

    private void setupNumPlayersDropdown() {
        Spinner numPlayerSelect = (Spinner) findViewById(R.id.playerDropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mNumPlayersList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numPlayerSelect.setAdapter(adapter);
        numPlayerSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mNumPlayers = (String)parent.getItemAtPosition(position);
                TextView teamDescription = (TextView)findViewById(R.id.teamDescriptionTextView);
                switch(position) {
                    case 0:
                        // 4 players
                        teamDescription.setText("4 Players: 2 Hunter, 2 Shadow");
                        break;
                    case 1:
                        // 5 players
                        teamDescription.setText("5 Players: 2 Hunter, 2 Shadow, 1 Neutral");
                        break;
                    case 2:
                        // 6 players
                        teamDescription.setText("6 Players: 2 Hunter, 2 Shadow, 2 Neutral");
                        break;
                    case 3:
                        // 7 players
                        teamDescription.setText("7 Players: 2 Hunter, 2 Shadow, 3 Neutral");
                        break;
                    case 4:
                        // 8 players
                        teamDescription.setText("8 Players: 3 Hunter, 3 Shadow, 2 Neutral");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupCreateGameBtn() {
        Button createGameBtn = (Button)findViewById(R.id.createGameBtn);
        createGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create game button clicked
                Intent playerNamesIntent = new Intent(HomeActivity.this, PlayerNamesActivity.class);
                playerNamesIntent.putExtra("numPlayers", mNumPlayers);
                startActivity(playerNamesIntent);
                finish();
            }
        });
    }
}
