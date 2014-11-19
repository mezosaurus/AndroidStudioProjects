package cs4962.battleshipnetwork;

import java.util.ArrayList;

/**
 * Created by Ethan on 10/27/2014.
 */
public class Ship {

    public enum Type { CARRIER, BATTLESHIP, SUBMARINE, CRUISER, DESTROYER }
    private Type mType;
    private int mSize;
    // Ship position, format [A-J][1-10]
    private ArrayList<String> mPositions;
    // List of hits on the ship, format [A-J][1-10], max number of hits is mSize
    private ArrayList<String> mHits;
    // Vertical or Horizontal
    private String mOrientation;
    private boolean mSunk;

    public Ship (Type type) {
        mType = type;
        switch (type) {
            case CARRIER:
                mSize = 5;
                break;
            case BATTLESHIP:
                mSize = 4;
                break;
            case SUBMARINE:
                mSize = 3;
                break;
            case CRUISER:
                mSize = 3;
                break;
            case DESTROYER:
                mSize = 2;
                break;
        }
        mPositions = new ArrayList<String>();
        mHits = new ArrayList<String>();
        mSunk = false;
    }

    public void setOrientation (String orientation) {
        mOrientation = orientation;
    }

    public boolean isSunk() {
        return mSunk;
    }

    public Type getType() {
        return mType;
    }

    public int getSize() {
        return mSize;
    }

    public ArrayList<String> getPositions() {
        return mPositions;
    }

    public ArrayList<String> getHits() {
        return mHits;
    }

    public void setPositions (ArrayList<String> positions) {
        mPositions = positions;
    }

    public boolean registerHit (String position) {
        if (mPositions.contains(position)) {
            mHits.add(position);
            if (mHits.size() == mSize) {
                mSunk = true;
            }
            return true;
        }
        else {
            return false;
        }
        //return mPositions.contains(position);
        /*boolean retVal = false;
        // Determine if the position selected is a valid position for the ship
        if (!validatePosition(position)) {
            return false;
        }
        // Position was found on the ship, determine if it is a valid hit
        else {
            retVal = validateHit(position);
        }
        return retVal;*/
    }

    public boolean validatePosition (String position) {
        return mPositions.contains(position);
        /*boolean validPosition = false;

        for (int posIndex = 0; posIndex < mPositions.size(); posIndex++) {
            if (position == mPositions.get(posIndex)) {
                validPosition = true;
            }
        }
        return validPosition;*/
    }

    private boolean validateHit (String position) {
        boolean validHit = true;
        // Return false immediately if the ship already has maximum number of hits (ship is sunk)
        if (mSunk) {
            return false;
        }
        // Ship has hits but is not sunk, make sure the position isn't already a hit
        for (int hitIndex = 0; hitIndex < mHits.size(); hitIndex++) {
            String hit = mHits.get(hitIndex);
            if (position == hit) {
                validHit = false;
            }
        }
        // Given position is not already a hit, add the position to the list of hits
        if (validHit) {
            mHits.add(position);
            // Check to see if the hit registered sunk the ship
            if (mHits.size() == mSize) {
                mSunk = true;
            }
        }
        return validHit;
    }
}
