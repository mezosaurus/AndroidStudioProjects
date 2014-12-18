package cs4962.shadowhunters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ethan on 12/14/2014.
 */
public class PlayerNameColorAdapter extends ArrayAdapter<String> {
    private String[] mColorList = new String[]{"Black", "Green", "Blue", "Orange", "Pink", "Red", "Yellow", "White"};
    Context context;
    int layoutResourceId;
    ArrayList<String> data = null;
    public PlayerNameColorAdapter(Context context, int textViewResourceId, ArrayList<String> data) {
        super(context, textViewResourceId, data);
        this.layoutResourceId = textViewResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        String playerNum = data.get(position);
        LinearLayout main = (LinearLayout)row.findViewById(R.id.playerColorNameListRowMain);
        LinearLayout spinnerLayout = new LinearLayout(context);
        spinnerLayout.setOrientation(LinearLayout.VERTICAL);

        EditText playerName = new EditText(context);
        playerName.setMaxLines(1);
        playerName.setHint("Player " + playerNum + " Name");
        playerName.setText("player " + playerNum);
        playerName.setId(position+11);
        main.addView(playerName, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2));

        TextView spinnerLabel = new TextView(context);
        spinnerLabel.setText("Player " + playerNum + " Color");
        spinnerLayout.addView(spinnerLabel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1));

        Spinner colorSelect = new Spinner(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mColorList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSelect.setAdapter(adapter);
        colorSelect.setId(position+21);
        spinnerLayout.addView(colorSelect, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1));

        main.addView(spinnerLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        return row;
    }
}
