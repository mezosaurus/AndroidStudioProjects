package cs4962.shadowhunters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private Player mCurrentPlayer;
    private boolean mViewOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        final Intent i = getIntent();
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

        if (i.hasExtra("player")) {
            mCurrentPlayer = i.getParcelableExtra("player");
        }

        if (i.hasExtra("viewlist")) {
            mViewOnly = true;
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
                if (mViewOnly)
                    return;
                Player p = (Player)parent.getItemAtPosition(position);
                Intent resultIntent = new Intent(PlayerListActivity.this, GameBoardActivity.class);
                if (mCurrentPlayer != null) {
                    if (!i.hasExtra("weirdwoods") && p.getName().equals(mCurrentPlayer.getName())) {
                        return;
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(PlayerListActivity.this);
                        builder.setTitle("Invalid Selection");
                        builder.setMessage("You cannot select yourself for this action.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();*/
                    }
                }
                if (mAttack) {
                    resultIntent.putExtra("playerToAttack", p);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else if (mWoodsHeal) {
                    resultIntent.putExtra("playerToHeal", p);
                    resultIntent.putExtra("healAmount", mWoodsAmount);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else if (mWoodsDmg) {
                    resultIntent.putExtra("playerToAttack", p);
                    resultIntent.putExtra("dmgAmount", mWoodsAmount);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else if (mCard) {
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
                if (mViewOnly) {
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}
