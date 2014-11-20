package cs4962.battleshipnetwork;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Ethan on 10/29/2014.
 */
public class BattleshipActivity extends Activity implements GridFragment.OnSquareClickedListener {
    private Game mGame;
    private String mPlayerName;
    private String mPlayerId;
    private String mGameName;
    private String[] mLetters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    private String[] mNumbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    private int mLetterIndex = 0;
    private int mNumberIndex = 0;
    private boolean mIsTurn;
    private Handler handler = new Handler();
    private AlertDialog waitingForPlayerDialog;
    private AlertDialog.Builder waitingForPlayerBuilder;
    private String dialogMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        waitingForPlayerBuilder = new AlertDialog.Builder(this);
        waitingForPlayerDialog = waitingForPlayerBuilder.create();

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        setContentView(rootLayout);

        LinearLayout leftLayout = new LinearLayout(this);
        leftLayout.setOrientation(LinearLayout.VERTICAL);

        FrameLayout gameListLayout = new FrameLayout(this);
        gameListLayout.setId(20);

        leftLayout.addView(gameListLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 80
        ));

        Button newGameBtn = new Button(this);
        newGameBtn.setText("New Game");
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new game button clicked
                newGame();
            }
        });

        Button refreshBtn = new Button(this);
        refreshBtn.setText("Refresh");
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // refresh button clicked
                GameList.getInstance().refreshGameList();
            }
        });

        leftLayout.addView(newGameBtn, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 10
        ));

        leftLayout.addView(refreshBtn, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 10
        ));

        rootLayout.addView(leftLayout, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 30
        ));

        LinearLayout gridLayout = new LinearLayout(this);
        gridLayout.setOrientation(LinearLayout.VERTICAL);

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
            public void onGameSelected(GameListFragment gameListFragment, String identifier) {
                // Game selected from list
                Game selectedGame = GameList.getInstance().getGame(identifier);
                // Make sure that the game selected is joinable
                if (selectedGame.getStatus() != BattleshipServices.GameStatus.WAITING) {
                    return;
                }
                mGame = GameList.getInstance().getGame(identifier);
                // Prompt user for player name
                getPlayerName();
            }
        });
    }

    @Override
    public void onSquareClicked(GridSquareView actionView) {
        // User selected a grid square
        if (mGame == null) {
            return;
        }
        if (mGame.getStatus() != BattleshipServices.GameStatus.PLAYING) {
            return;
        }

        String position = actionView.getPosition();
        String letter = position.substring(0, 1);
        String number = position.substring(1);
        // get index of letter
        for (int i = 0; i < mLetters.length; i++) {
            if (mLetters[i].equals(letter)) {
                mLetterIndex = i;
                break;
            }
        }
        // get index of number
        int numberIndex = 0;
        for (int i = 0; i < mNumbers.length; i++) {
            if (mNumbers[i].equals(number)) {
                mNumberIndex = i;
                break;
            }
        }
        //determineTurn();
        makeGuess();
    }

    private void getPlayerName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Join Game");

        final EditText input = new EditText(this);
        input.setHint("Player name");

        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Editable name = input.getText();
                mPlayerName = name.toString();
                if (mPlayerName.length() > 0) {
                    // Game is waiting for player, call join game service
                    dialogMessage = "Waiting for opponent to take their turn.";
                    joinGame();

                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                mPlayerName = null;
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getBoard() {
        AsyncTask<String, Integer, BattleshipServices.Boards> boardTask = new AsyncTask<String, Integer, BattleshipServices.Boards>() {
            @Override
            protected BattleshipServices.Boards doInBackground(String... params) {
                return BattleshipServices.getBoards(mGame.getIdentifier(), mPlayerId);
            }

            @Override
            protected void onPostExecute(BattleshipServices.Boards boards) {
                if (boards == null)
                    return;
                super.onPostExecute(boards);

                GridFragment fragmentOne = (GridFragment)getFragmentManager().findFragmentById(21);
                fragmentOne.setActionFragment(false);
                fragmentOne.resetFragment();


                GridFragment fragmentTwo = (GridFragment)getFragmentManager().findFragmentById(22);
                fragmentTwo.setActionFragment(true);
                fragmentTwo.resetFragment();

                BattleshipServices.PlayerBoard[] playerBoard = boards.playerBoard;
                BattleshipServices.OpponentBoard[] oppBoard = boards.opponentBoard;
                Player p1 = mGame.getPlayerOne();
                Player p2 = mGame.getPlayerTwo();
                // Determine which player the user is
                if (mPlayerName == null)
                    return;
                if (p1.getName().equals(mPlayerName)) {
                    p1.getBoard().initBoard(playerBoard);
                    p2.getBoard().initBoard(oppBoard);
                    fragmentOne.setGridViewPlayer(p1);
                    fragmentTwo.setGridViewPlayer(p2);
                    fragmentOne.drawShips();
                    fragmentOne.drawHitsMisses(p1);
                    fragmentTwo.drawHitsMisses(p2);
                }
                else {
                    p1.getBoard().initBoard(oppBoard);
                    p2.getBoard().initBoard(playerBoard);
                    fragmentOne.setGridViewPlayer(p2);
                    fragmentTwo.setGridViewPlayer(p1);
                    fragmentOne.drawShips();
                    fragmentOne.drawHitsMisses(p2);
                    fragmentTwo.drawHitsMisses(p1);
                }
            }
        };
        boardTask.execute();
    }

    private void newGame() {
        // Prompt user for player name and game name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Game");
        LinearLayout inputLayout = new LinearLayout(this);
        inputLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText playerNameInput = new EditText(this);
        playerNameInput.setHint("Player name");
        playerNameInput.setFocusable(true);
        playerNameInput.setFocusableInTouchMode(true);
        inputLayout.addView(playerNameInput);

        final EditText gameNameInput = new EditText(this);
        gameNameInput.setHint("Game name");
        inputLayout.addView(gameNameInput);

        builder.setView(inputLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Editable playerName = playerNameInput.getText();
                Editable gameName = gameNameInput.getText();
                mPlayerName = playerName.toString();
                mGameName = gameName.toString();
                //mGame.setName(gameName.toString());
                if (mPlayerName.length() > 0 && mGameName.length() > 0) {
                    // call new game service
                    AsyncTask<String, Integer, BattleshipServices.CreateGame> newGameTask = new AsyncTask<String, Integer, BattleshipServices.CreateGame>() {
                        @Override
                        protected BattleshipServices.CreateGame doInBackground(String... params) {
                            return BattleshipServices.createGame(mGameName, mPlayerName);
                        }

                        @Override
                        protected void onPostExecute(BattleshipServices.CreateGame createGame) {
                            super.onPostExecute(createGame);

                            mGame = new Game(createGame.gameId, mGameName, BattleshipServices.GameStatus.WAITING);
                            //mGame.setIdentifier(createGame.gameId);
                            mPlayerId = createGame.playerId;
                            GameList.getInstance().refreshGameList();
                        }
                    };
                    newGameTask.execute();

                    // Spin off thread to continually call the isTurn service to know when another player has joined the created game
                    dialogMessage = "Waiting for another player to join the game.";
                    callIsTurnService();
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                mPlayerName = null;
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getGameDetail() {
        // Call service to get the game detail
        AsyncTask<String, Integer, BattleshipServices.GameModel> gameDetailTask =
            new AsyncTask<String, Integer, BattleshipServices.GameModel>() {
                @Override
                protected BattleshipServices.GameModel doInBackground(String... params) {
                    return BattleshipServices.getGameDetail(mGame.getIdentifier());
                }

                @Override
                protected void onPostExecute(BattleshipServices.GameModel gameModel) {
                    if (gameModel == null)
                        return;
                    super.onPostExecute(gameModel);
                    Player p1 = new Player(gameModel.player1);
                    Player p2 = new Player(gameModel.player2);
                    //Player winner = new Player(gameModel.winner);
                    mGame.setPlayerOne(p1);
                    mGame.setPlayerTwo(p2);
                    //mGame.setWinner(winner);
                    mGame.setMissilesLaunched(gameModel.missilesLaunched);

                    // Get the boards
                    getBoard();
                }
        };
        gameDetailTask.execute();
    }

    private void joinGame() {
        AsyncTask<String, Integer, BattleshipServices.JoinGame> joinGameTask =
            new AsyncTask<String, Integer, BattleshipServices.JoinGame>() {
                @Override
                protected BattleshipServices.JoinGame doInBackground(String... params) {
                    return BattleshipServices.joinGame(mGame.getIdentifier(), mPlayerName);
                }

                @Override
                protected void onPostExecute(BattleshipServices.JoinGame joinGame) {
                    if (joinGame == null)
                        return;
                    super.onPostExecute(joinGame);

                    mPlayerId = joinGame.playerId;
                    getGameDetail();
                    GameList.getInstance().refreshGameList();
                    callIsTurnService();
                }
        };
        joinGameTask.execute();
        mGame.setStatus(BattleshipServices.GameStatus.PLAYING);
    }

    private void makeGuess() {
        AsyncTask<String, Integer, BattleshipServices.Guess> makeGuessTask = new AsyncTask<String, Integer, BattleshipServices.Guess>() {
            @Override
            protected BattleshipServices.Guess doInBackground(String... params) {
                return BattleshipServices.makeGuess(mGame.getIdentifier(), mPlayerId, mLetterIndex, mNumberIndex);
            }

            @Override
            protected void onPostExecute(BattleshipServices.Guess guess) {
                if (guess == null)
                    return;
                super.onPostExecute(guess);

                determineTurn();

                String alertMsg = "";
                boolean hitSuccess = guess.hit;
                if (hitSuccess) {
                    // SUCCESS
                    alertMsg += "GUESS AT " + mLetters[mLetterIndex]+mNumbers[mNumberIndex] + " RESULTED IN A HIT\n\n";
                }
                else {
                    // FAIL
                    alertMsg += "GUESS AT " + mLetters[mLetterIndex]+mNumbers[mNumberIndex] + " RESULTED IN A MISS\n\n";
                }
                int shipSunk = guess.shipSunk;
                switch (shipSunk) {
                    case 0:
                        // No ship sunk
                        break;
                    case 2:
                        // Ship of size 2 sunk
                        alertMsg += "YOU SUNK A SHIP OF SIZE 2";
                        break;
                    case 3:
                        // Ship of size 3 sunk
                        alertMsg += "YOU SUNK A SHIP OF SIZE 3";
                        break;
                    case 4:
                        // Ship of size 4 sunk
                        alertMsg += "YOU SUNK A SHIP OF SIZE 4";
                        break;
                    case 5:
                        // Ship of size 5 sunk
                        alertMsg += "YOU SUNK A SHIP OF SIZE 5";
                        break;
                }

                // Create alert dialog for guess results
                AlertDialog.Builder builder = new AlertDialog.Builder(BattleshipActivity.this);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GameList.getInstance().refreshGameList();
                        getBoard();
                        //mIsTurn = false;
                        dialogMessage = "Waiting for opponent to take their turn.";
                        callIsTurnService();
                        dialog.dismiss();
                    }
                });
                builder.setMessage(alertMsg)
                        .setTitle("Turn Results");
                AlertDialog dialog = builder.create();
                System.out.println(alertMsg);
                dialog.show();
            }
        };
        makeGuessTask.execute();
    }

    private void determineTurn() {
        // Make sure it is the user's turn
        AsyncTask<String, Integer, BattleshipServices.Turn> turnTask = new AsyncTask<String, Integer, BattleshipServices.Turn>() {
            @Override
            protected BattleshipServices.Turn doInBackground(String... params) {
                System.out.println("DETERMINE TURN");
                return BattleshipServices.getTurn(mGame.getIdentifier(), mPlayerId);
            }

            @Override
            protected void onPostExecute(BattleshipServices.Turn turn) {
                if (turn == null)
                    return;
                super.onPostExecute(turn);

                boolean isTurn = turn.isYourTurn;
                String winner = turn.winner;
                if (!winner.equals("IN PROGRESS")) {
                    mGame.setWinner(new Player(winner));
                    mGame.setStatus(BattleshipServices.GameStatus.DONE);
                    String endMsg = "";
                    if (winner.equals(mPlayerName)) {
                        // PLAYER WON
                        endMsg += "YOU WON";
                    }
                    else {
                        // OPPONENT WON
                        endMsg += "YOU LOST";
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(BattleshipActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GameList.getInstance().refreshGameList();
                            dialog.dismiss();
                        }
                    });
                    builder.setMessage(endMsg)
                            .setTitle("GAME OVER");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                if (isTurn) {
                    // It is the player's turn
                    mIsTurn = true;
                }
                else {
                    mIsTurn = false;
                    // Create alert dialog to inform player it is not their turn
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(BattleshipActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GameList.getInstance().refreshGameList();
                            dialog.dismiss();
                        }
                    });
                    builder.setMessage("It is not your turn. Please wait.")
                            .setTitle("OPPONENT TURN");
                    AlertDialog dialog = builder.create();
                    dialog.show();*/
                }
            }
        };
        turnTask.execute();
    }

    private Runnable checkForNewPlayer = new Runnable() {

        public void run() {
            if (!mIsTurn) {
                // Inform player that the new game is waiting for a player to join
                waitingForPlayerDialog.setMessage(dialogMessage);
                if (!waitingForPlayerDialog.isShowing())
                    waitingForPlayerDialog.show();
                determineTurn();
                //GameList.getInstance().refreshGameList();
                handler.postDelayed(this, 1000);
            }
            else {
                if (dialogMessage.equals("Waiting for another player to join the game.")) {
                    mGame.setStatus(BattleshipServices.GameStatus.PLAYING);
                }
                waitingForPlayerDialog.dismiss();
                getGameDetail();
                getBoard();
            }
        }
    };

    private void callIsTurnService() {
        handler.postDelayed(checkForNewPlayer, 1000);
    }

    /*private Runnable updateBoard = new Runnable() {
        public void run() {
            getBoard();
            handler.postDelayed(this, 1000);
        }
    };*/

    //private void refreshBoard() { handler.postDelayed(updateBoard, 1000); }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
