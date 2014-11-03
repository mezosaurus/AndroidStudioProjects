package cs4962.battleship;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Ethan on 10/29/2014.
 */
public class BattleshipActivity extends Activity implements GridFragment.OnSquareClickedListener {
    private Game mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameList.getInstance().setGameListFile(new File(getFilesDir(), "GameList.txt"));

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        setContentView(rootLayout);

        LinearLayout leftLayout = new LinearLayout(this);
        leftLayout.setOrientation(LinearLayout.VERTICAL);

        FrameLayout gameListLayout = new FrameLayout(this);
        gameListLayout.setId(20);

        leftLayout.addView(gameListLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 90
        ));

        Button newGameBtn = new Button(this);
        newGameBtn.setText("New Game");
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // New game button clicked
                Game newGame = new Game();
                newGame.setInProgress(true);
                newGame.setDate(new Date());
                newGame.setPlayerOne(new Player());
                newGame.getPlayerOne().setTurn(true);
                newGame.setPlayerTwo(new Player());
                newGame.getPlayerTwo().setTurn(false);
                /*newGame.inProgress = true;
                newGame.date = new Date();
                newGame.playerOne = new Player();
                newGame.playerOne.setTurn(true);
                newGame.playerTwo = new Player();
                newGame.playerTwo.setTurn(false);*/
                GameList.getInstance().addGame(newGame);
                //GameListFragment listFragment = (GameListFragment)getFragmentManager().findFragmentById(20);
                //int count = listFragment.getCount();
                //listFragment.refresh();
            }
        });

        leftLayout.addView(newGameBtn, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 10
        ));

        rootLayout.addView(leftLayout, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 30
        ));

        LinearLayout gridLayout = new LinearLayout(this);
        gridLayout.setOrientation(LinearLayout.VERTICAL);
        //gridLayout.setId(21);

        FrameLayout gridOneLayout = new FrameLayout(this);
        gridOneLayout.setId(22);
        gridLayout.addView(gridOneLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 50
        ));

        FrameLayout gridTwoLayout = new FrameLayout(this);
        gridTwoLayout.setId(21);
        gridLayout.addView(gridTwoLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 50
        ));

        rootLayout.addView(gridLayout, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 70
        ));



        GameListFragment gameListFragment = new GameListFragment();
        GridFragment gridFragmentOne = new GridFragment();
        gridFragmentOne.setActionFragment(false);
        GridFragment gridFragmentTwo = new GridFragment();
        // This fragment is the one that the user should click to fire missiles
        gridFragmentTwo.setActionFragment(true);

        FragmentTransaction addTransaction = getFragmentManager().beginTransaction();
        addTransaction.add(20, gameListFragment);
        addTransaction.add(21, gridFragmentOne);
        addTransaction.add(22, gridFragmentTwo);
        addTransaction.commit();


        gameListFragment.setOnGameSelectedListener(new GameListFragment.OnGameSelectedListener() {
            @Override
            public void onGameSelected(GameListFragment gameListFragment, UUID identifier) {
                Log.i("ONGAMESELECTED", "identifier: " + identifier);
                // Game selected from list
                // Update game fragments
                mGame = GameList.getInstance().getGame(identifier);
                Player p1 = mGame.getPlayerOne();
                Player p2 = mGame.getPlayerTwo();
                //Player p1 = mGame.playerOne;
                //Player p2 = mGame.playerTwo;

                GridFragment fragmentOne = (GridFragment)getFragmentManager().findFragmentById(21);
                GridFragment fragmentTwo = (GridFragment)getFragmentManager().findFragmentById(22);

                if (p1.isTurn()) {
                    fragmentOne.setGridViewPlayer(p1);
                    fragmentTwo.setGridViewPlayer(p1);
                }
                else {
                    fragmentOne.setGridViewPlayer(p2);
                    fragmentTwo.setGridViewPlayer(p2);
                }
                fragmentOne.drawShips();
                fragmentTwo.drawHitsMisses();
            }
        });
    }

    @Override
    public void onSquareClicked(GridSquareView actionView) {
        if (mGame == null)
            return;
        // User selected a grid square
        GameListFragment listFragment = (GameListFragment)getFragmentManager().findFragmentById(20);
        GridFragment fragmentOne = (GridFragment)getFragmentManager().findFragmentById(21);
        GridFragment fragmentTwo = (GridFragment)getFragmentManager().findFragmentById(22);
        Player p1 = mGame.getPlayerOne();
        Player p2 = mGame.getPlayerTwo();
        //Player p1 = mGame.playerOne;
        //Player p2 = mGame.playerTwo;
        String position = actionView.getPosition();
        String actionMsg;
        //boolean hitSuccess;
        String alertMsg = "";

        // Check to see which player's board was selected
        if (p1.isTurn()) {
            if (p1.getActions().contains(position)) {
                return;
            }
            // player 1's turn
            // Launch the missile to player 2's board
            actionMsg = p2.launchMissile(position);
            if (actionMsg.contains("SUNK")) {
                // Update the clicked board with a hit
                actionView.setColor(Color.RED);
                // Add the hit to player 1
                p1.addHit(position);
                alertMsg += "PLAYER 1's turn resulted in a HIT at " + position + "\n\n";
                alertMsg += "PLAYER 1 SUNK PLAYER 2's " + actionMsg.substring(4) + "\n\n";
            }
            else if (actionMsg == "HIT") {
                // Update the clicked board with a hit
                actionView.setColor(Color.RED);
                // Add the hit to player 1
                p1.addHit(position);
                alertMsg += "PLAYER 1's turn resulted in a HIT at " + position + "\n\n";
            }
            else {
                // Update the clicked board with a miss
                actionView.setColor(Color.WHITE);
                // Add the miss to player 1
                p1.addMiss(position);
                alertMsg += "PLAYER 1's turn resulted in a MISS at " + position + "\n\n";
            }
            actionView.invalidate();
            fragmentOne.resetFragment();
            listFragment.refresh();
            alertMsg += "Please pass to PLAYER 2 for their turn";
        }
        else {
            if (p2.getActions().contains(position)) {
                return;
            }
            // player 2's turn
            // Launch the missile to player 1's board
            actionMsg = p1.launchMissile(position);
            if (actionMsg.contains("SUNK")) {
                // Update the clicked board with a hit
                actionView.setColor(Color.RED);
                // Add the hit to player 1
                p1.addHit(position);
                alertMsg += "PLAYER 2's turn resulted in a HIT at " + position + "\n\n";
                alertMsg += "PLAYER 2 SUNK PLAYER 1's " + actionMsg.substring(4) + "\n\n";
            }
            else if (actionMsg == "HIT") {
                // Update the clicked board with a hit
                actionView.setColor(Color.RED);
                // Add the hit to player 2
                p2.addHit(position);
                alertMsg += "PLAYER 2's turn resulted in a HIT at " + position + "\n\n";
            }
            else {
                // Update the clicked board with a miss
                actionView.setColor(Color.WHITE);
                // Add the miss to player 2
                p2.addMiss(position);
                alertMsg += "PLAYER 2's turn resulted in a MISS at " + position + "\n\n";
            }
            actionView.invalidate();
            listFragment.refresh();
            fragmentOne.resetFragment();
            alertMsg += "Please pass to PLAYER 1 for their turn";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                GridFragment fragmentOne = (GridFragment)getFragmentManager().findFragmentById(21);
                GridFragment fragmentTwo = (GridFragment)getFragmentManager().findFragmentById(22);
                Player p1 = mGame.getPlayerOne();
                Player p2 = mGame.getPlayerTwo();
                //Player p1 = mGame.playerOne;
                //Player p2 = mGame.playerTwo;
                if (p1.isTurn()) {
                    p1.setTurn(false);
                    p2.setTurn(true);
                    // Set grids to new player
                    fragmentOne.setGridViewPlayer(p2);
                    fragmentOne.resetFragment();
                    fragmentTwo.setGridViewPlayer(p2);
                    fragmentTwo.resetFragment();
                    // Draw ships
                    fragmentOne.drawShips();
                    // Draw hits and misses
                    fragmentTwo.drawHitsMisses();
                }
                else {
                    p2.setTurn(false);
                    p1.setTurn(true);
                    // Set grids to new player
                    fragmentOne.setGridViewPlayer(p1);
                    fragmentOne.resetFragment();
                    fragmentTwo.setGridViewPlayer(p1);
                    fragmentTwo.resetFragment();
                    // Draw ships
                    fragmentOne.drawShips();
                    // Draw hits and misses
                    fragmentTwo.drawHitsMisses();
                }
                dialog.dismiss();
            }
        });

        builder.setMessage(alertMsg)
                .setTitle("Results");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
