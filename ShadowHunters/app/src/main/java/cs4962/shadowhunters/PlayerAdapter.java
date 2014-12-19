package cs4962.shadowhunters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ethan on 12/10/2014.
 */
public class PlayerAdapter extends ArrayAdapter<Player> {
    Context context;
    int layoutResourceId;
    ArrayList<Player> data = null;
    public PlayerAdapter(Context context, int textViewResourceId, ArrayList<Player> objects) {
        super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.context = context;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlayerHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PlayerHolder();
            holder.txtContent = (TextView)row.findViewById(R.id.txtContent);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else {
            holder = (PlayerHolder)row.getTag();
        }

        Player player = data.get(position);

        String title = player.getName();
        String content = "DAMAGE : " + player.getDamage();

        holder.txtTitle.setBackgroundColor(player.getColor());
        if (player.getColor() == Color.BLACK) {
            holder.txtTitle.setTextColor(Color.WHITE);
        }
        else {
            holder.txtTitle.setTextColor(Color.BLACK);
        }

        if (player.isRevealed()) {
            title += " REVEALED AS : " + player.getCharacter().getName() + " (" + player.getCharacter().getTeam() + ")";
            content += "\nHEALTH: " + player.getCharacter().getHealth();
        }
        int equipmentCount = player.getEquipment().size();
        content += "\nEQUIPMENT COUNT : " + equipmentCount;

        AreaCard boardPos = player.getBoardPosition();
        if (boardPos != null) {
            content += "\nBOARD POSITION: " + boardPos.getName();
        }

        holder.txtTitle.setText(title);
        holder.txtContent.setText(content);

        return row;
    }

    static class PlayerHolder
    {
        TextView txtContent;
        TextView txtTitle;
    }
}
