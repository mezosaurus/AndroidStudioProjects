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
    private boolean mAttack = false;
    private boolean mWoodsHeal = false;
    private boolean mWoodsDmg = false;
    private int mWoodsAmount = 0;
    private boolean mCard = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        Intent i = getIntent();
        if (i.hasExtra("attack"))
            mAttack = i.getBooleanExtra("attack", false);
        if (i.hasExtra("weirdwoods")) {
            String woods = i.getStringExtra("weirdwoods");
            if (woods.equals("heal")) {
                mWoodsHeal = true;
            }
            else {
                mWoodsDmg = true;
            }
            mWoodsAmount = i.getIntExtra("amount", 0);
        }
        if (i.hasExtra("cardrequest")) {
            mCard = i.getBooleanExtra("cardrequest", false);
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
                else if (mWoodsHeal) {
                    resultIntent.putExtra("playerToAttack", p);
                    resultIntent.putExtra("dmgAmount", mWoodsAmount);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                else if (mWoodsDmg) {
                    resultIntent.putExtra("playerToHeal", p);
                    resultIntent.putExtra("healAmount", mWoodsAmount);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                else if (mCard) {
                    resultIntent.putExtra("playerSelected", p);
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
                if (mAttack || mWoodsHeal || mWoodsDmg || mCard) {
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
