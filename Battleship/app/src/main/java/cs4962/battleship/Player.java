package cs4962.battleship;

import java.util.ArrayList;

/**
 * Created by Ethan on 10/27/2014.
 */
public class Player {
    // Positions selected by player
    private ArrayList<String> mActions;
    // Positions missed by player
    private ArrayList<String> mMisses;
    // Positions hit by player
    private ArrayList<String> mHits;
    // Board for the player
    private Board mBoard;

    public Player() {
        mActions = new ArrayList<String>();
        mMisses = new ArrayList<String>();
        mHits = new ArrayList<String>();
        mBoard = new Board();
    }

    public boolean launchMissile(String position, Player opponent) {
        // Get opponent information
        Board oppBoard = opponent.getBoard();
        ArrayList<Ship> ships = oppBoard.getShips();

        // Make sure player hasn't already performed action on this position.
        // If they have, return false.
        if (mActions.contains(position)) {
            return false;
        }
        // If they haven't, add the position to the actions list
        else {
            mActions.add(position);
        }

        boolean hitSuccess = false;
        for (int i = 0; i < ships.size(); i++) {
            Ship s = ships.get(i);
            hitSuccess = s.registerHit(position);
        }
        if (hitSuccess) {
            mHits.add(position);
        }
        else {
            mMisses.add(position);
        }
        return hitSuccess;
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
    }
}
