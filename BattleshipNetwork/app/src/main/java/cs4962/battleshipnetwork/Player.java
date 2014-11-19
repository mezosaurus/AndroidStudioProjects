package cs4962.battleshipnetwork;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Ethan on 10/27/2014.
 */
public class Player {
    // Player name
    private String name;
    // Player id
    private String id;
    // Positions selected by player
    private ArrayList<String> mActions;
    // Positions missed by player
    private ArrayList<String> mMisses;
    // Positions hit by player
    private ArrayList<String> mHits;
    // Board for the player
    private Board mBoard;
    // Boolean var to indicate if it is the player's turn or not
    private boolean mTurn;
    // Counter to determine number of sunk ships
    //private int mSunkShips = 0;

    public Player(String name) {
        this.name = name;
        mBoard = new Board();
        /*mActions = new ArrayList<String>();
        mMisses = new ArrayList<String>();
        mHits = new ArrayList<String>();
        mBoard = new Board();*/
    }

    public Board getBoard() {
        return mBoard;
    }

    public void setBoard(Board mBoard) {
        this.mBoard = mBoard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTurn () {
        return mTurn;
    }

    public void setTurn(boolean turn) {
        mTurn = turn;
    }

    /*public void addMiss(String position) {
        mMisses.add(position);
        mActions.add(position);
    }

    public void addHit(String position) {
        mHits.add(position);
        mActions.add(position);
    }*/

    /*public int getSunkShips() {
        return mSunkShips;
    }*/



    /*public String launchMissile(String position) {
        ArrayList<Ship> ships = mBoard.getShips();

        for (int i = 0; i < ships.size(); i++) {
            Ship s = ships.get(i);
            if (s.registerHit(position)) {
                if (s.isSunk()) {
                    mSunkShips++;
                    return "SUNK" + s.getType().toString();
                }
                else {
                    return "HIT";
                }
            }
        }
        return "MISS";
    }

    public ArrayList<String> getActions() {
        return mActions;
    }

    public ArrayList<String> getMisses() {
        return mMisses;
    }

    public ArrayList<String> getHits() {
        return mHits;
    }

    public Board getBoard() {
        return mBoard;
    }*/
}
