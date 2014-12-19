package cs4962.shadowhunters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;


public class PlayerListActivity extends Activity {
    private boolean mAttack;
    private boolean mWoods;
    private int mWoodsDmg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        Intent i = getIntent();
        mAttack = i.getBooleanExtra("attack", false);
        mWoods = i.getBooleanExtra("weirdwoods", false);
        if (mWoods) {
            mWoodsDmg = i.getIntExtra("amount", 0);
        }

        PlayerList pList = PlayerList.getInstance();
        Set<Integer> playerIds = pList.getIdentifiers();
        int numPlayers = playerIds.size();
        ArrayList<Player> players = new ArrayList<Player>();
        for (Integer color : playerIds) {
            Player p = pList.getPlayer(color);
            players.add(p);
        }
        PlayerAdapter adapter = new PlayerAdapter(this, R.layout.player_list_row, players);

        ListView playerListView = (ListView)findViewById(R.id.playerListView);
        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player p = (Player)parent.getItemAtPosition(position);
                Intent resultIntent = new Intent(PlayerListActivity.this, GameBoardActivity.class);
                if (mAttack) {
                    resultIntent.putExtra("playerToAttack", p);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                if (mWoods) {
                    resultIntent.putExtra("playerToAttack", p);
                    resultIntent.putExtra("dmgAmount", mWoodsDmg);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
        playerListView.setAdapter(adapter);

        Button boardBtn = (Button)findViewById(R.id.playerListBoardBtn);
        boardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(PlayerListActivity.this, GameBoardActivity.class);
                if (mAttack) {
                    setResult(RESULT_CANCELED, resultIntent);
                    finish();
                }
                else if (mWoods) {
                    setResult(RESULT_CANCELED, resultIntent);
                    finish();
                }
                else {
                    Intent boardIntent = new Intent(PlayerListActivity.this, GameBoardActivity.class);
                    startActivity(boardIntent);
                    finish();
                }
            }
        });
    }
}
