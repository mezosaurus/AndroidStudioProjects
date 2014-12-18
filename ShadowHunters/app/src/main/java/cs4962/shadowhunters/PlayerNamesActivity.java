package cs4962.shadowhunters;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import java.util.Map;


public class PlayerNamesActivity extends Activity {
    private int mNumPlayers;
    //private String[] mColorList = new String[]{"Black", "Green", "Blue", "Orange", "Pink", "Red", "Yellow", "White"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_namecolor);
        Intent homeIntent = getIntent();
        // Get number of players from main screen
        mNumPlayers = Integer.parseInt(homeIntent.getStringExtra("numPlayers"));
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 1; i <= mNumPlayers; i++) {
            data.add(i+"");
        }
        PlayerList.getInstance().setPlayerListFile(new File(getFilesDir(), "PlayerList.txt"));
        PlayerNameColorAdapter adapter = new PlayerNameColorAdapter(this, R.layout.player_name_color_row, data);

        ListView playerNameColorListView = (ListView)findViewById(R.id.playerNameColorListView);
        playerNameColorListView.setAdapter(adapter);

        Button cancelBtn = (Button)findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cancel button clicked
                Intent cancelIntent = new Intent(PlayerNamesActivity.this, HomeActivity.class);
                startActivity(cancelIntent);
                finish();
            }
        });

        Button startGameBtn = (Button)findViewById(R.id.startGameBtn);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start game button clicked
                boolean emptyTextBox = false;
                boolean sameColorPicked = false;
                // Check to make sure all player name fields have content
                for (int i = 1; i <= mNumPlayers; i++) {
                    EditText textBox = (EditText)findViewById(i+10);
                    Spinner spinner = (Spinner)findViewById(i+20);
                    String nameVal = textBox.getText().toString();
                    String colorVal = spinner.getSelectedItem().toString();
                    if (nameVal.length() == 0) {
                        emptyTextBox = true;
                    }
                    // Check to make sure all player color selectors have unique values
                    for (int j = 1; j <= mNumPlayers; j++) {
                        if (j == i)
                            continue;
                        String colorVal2 =  ((Spinner)findViewById(j+20)).getSelectedItem().toString();
                        if (colorVal.equals(colorVal2)) {
                            sameColorPicked = true;
                        }
                    }
                }
                if (emptyTextBox) {
                    // Alert for empty name
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlayerNamesActivity.this);
                    builder.setTitle("Empty Field");
                    builder.setMessage("All Fields Require Content");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if (sameColorPicked) {
                    // Alert for same color selected
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlayerNamesActivity.this);
                    builder.setTitle("Multiple Color Selected");
                    builder.setMessage("All Player Colors Must Be Unique");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    // Instantiate player objects and add them to the player list
                    PlayerList pList = PlayerList.getInstance();
                    for (int i = 1; i <= mNumPlayers; i++) {
                        EditText textBox = (EditText)findViewById(i+10);
                        Spinner colorSelect = (Spinner)findViewById(i+20);
                        String nameVal = textBox.getText().toString();
                        String colorText = colorSelect.getSelectedItem().toString();
                        int color = convertStringToColor(colorText);
                        Player p = new Player(nameVal, color);
                        pList.addPlayer(p);
                    }
                    // Go to game board activity
                    Intent gameIntent = new Intent(PlayerNamesActivity.this, GameBoardActivity.class);
                    startActivity(gameIntent);
                    finish();
                }
            }
        });
    }

    private int convertStringToColor (String color) {
        // black
        if (color.equals("Black")) {
            return Color.BLACK;
        }
        // green
        else if (color.equals("Green")) {
            return Color.GREEN;
        }
        // blue
        else if (color.equals("Blue")) {
            return Color.BLUE;
        }
        // orange
        else if (color.equals("Orange")) {
            return Utils.mixColor(Color.RED, Color.YELLOW);
        }
        // pink
        else if (color.equals("Pink")) {
            return Color.MAGENTA;
        }
        // red
        else if (color.equals("Red")) {
            return Color.RED;
        }
        // yellow
        else if (color.equals("Yellow")) {
            return Color.YELLOW;
        }
        // white
        else {
            return Color.WHITE;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }
}
