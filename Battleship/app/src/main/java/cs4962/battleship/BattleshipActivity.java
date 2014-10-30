package cs4962.battleship;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Ethan on 10/29/2014.
 */
public class BattleshipActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add test info
        Game one = new Game();
        one.date = new Date();
        one.playerOne = new Player();
        one.playerTwo = new Player();
        GameList.getInstance().addGame(one);

        Game two = new Game();
        two.date = new Date();
        two.playerOne = new Player();
        two.playerTwo = new Player();
        GameList.getInstance().addGame(two);

        Game three = new Game();
        three.date = new Date();
        three.playerOne = new Player();
        three.playerTwo = new Player();
        GameList.getInstance().addGame(three);

        Game four = new Game();
        four.date = new Date();
        four.playerOne = new Player();
        four.playerTwo = new Player();
        GameList.getInstance().addGame(four);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        setContentView(rootLayout);

        FrameLayout gameListLayout = new FrameLayout(this);
        gameListLayout.setId(20);
        rootLayout.addView(gameListLayout, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 30
        ));

        LinearLayout gridLayout = new LinearLayout(this);
        gridLayout.setOrientation(LinearLayout.VERTICAL);
        //gridLayout.setId(21);

        FrameLayout gridOneLayout = new FrameLayout(this);
        gridOneLayout.setId(21);
        gridLayout.addView(gridOneLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 50
        ));

        FrameLayout gridTwoLayout = new FrameLayout(this);
        gridTwoLayout.setId(22);
        gridLayout.addView(gridTwoLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 50
        ));

        rootLayout.addView(gridLayout, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 70
        ));



        GameListFragment gameListFragment = new GameListFragment();
        GridFragment gridFragmentOne = new GridFragment();
        GridFragment gridFragmentTwo = new GridFragment();

        FragmentTransaction addTransaction = getFragmentManager().beginTransaction();
        addTransaction.add(20, gameListFragment);
        addTransaction.add(21, gridFragmentOne);
        addTransaction.add(22, gridFragmentTwo);
        addTransaction.commit();

        gameListFragment.setOnGameSelectedListener(new GameListFragment.OnGameSelectedListener() {
            @Override
            public void onGameSelected(GameListFragment gameListFragment, UUID identifier) {
                // Game selected from list

            }
        });


    }
}
