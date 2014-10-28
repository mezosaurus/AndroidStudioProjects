package cs4962.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ethan on 10/27/2014.
 */
public class Board {

    private String[] mLetters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    private String[] mNumbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    private String[] mOrientations = { "VERTICAL", "HORIZONTAL" };
    private String[][] mBoard = {mLetters, mNumbers};
    private ArrayList<String> takenPositions = new ArrayList<String>();
    private ArrayList<Ship> mShips;
    private ArrayList<Ship> shipsToPlace = new ArrayList<Ship>();
    private Ship mCarrier = new Ship(Ship.Type.CARRIER);
    private Ship mBattleship = new Ship(Ship.Type.BATTLESHIP);
    private Ship mSubmarine = new Ship(Ship.Type.SUBMARINE);
    private Ship mCruiser = new Ship(Ship.Type.CRUISER);
    private Ship mDestroyer = new Ship(Ship.Type.DESTROYER);

    public Board () {
        createShips();
        shipsToPlace.addAll(mShips);
        placeShips(shipsToPlace);
    }

    public ArrayList<Ship> getShips () {
        return mShips;
    }

    private void createShips() {
        mShips = new ArrayList<Ship>();
        mShips.add(mCarrier);
        mShips.add(mBattleship);
        mShips.add(mSubmarine);
        mShips.add(mCruiser);
        mShips.add(mDestroyer);
    }

    private void placeShips(List<Ship> ships) {
        if (ships.size() == 0)
            return;
        Ship shipToPlace = ships.get(0);

        String letter = getRandomLetter();
        String number = getRandomNumber();
        String randomPosition = letter + number;
        // Keep looping until the randomPosition isn't taken
        while (takenPositions.contains(randomPosition)) {
            letter = getRandomLetter();
            number = getRandomNumber();
            randomPosition = letter + number;
        }
        int size = shipToPlace.getSize();
        String orientation = getRandomOrientation();
        // Depending on random orientation, check to see if the ship can be placed
        boolean placed = false;
        while (true) {
            if (orientation == "VERTICAL") {
                placed = checkVertical(shipToPlace, size, letter, number);
                // if veritcal didn't work, try horizontal
                if (!placed) {
                    orientation = "HORIZONTAL";
                    continue;
                }
                else {
                    break;
                }
            }
            if (orientation == "HORIZONTAL") {
                placed = checkHorizontal(shipToPlace, size, letter, number);
                // if horizontal didn't work, try vertical
                if (!placed) {
                    orientation = "VERTICAL";
                    continue;
                }
                else {
                    break;
                }
            }
            // If neither orientation worked, reset variables
            if (!placed) {
                letter = getRandomLetter();
                number = getRandomNumber();
                orientation = getRandomOrientation();
            }
        }
        // If out of loop, ship has been placed, remove it from list
        shipsToPlace.remove(shipToPlace);
        // Place remaining ships
        placeShips(shipsToPlace);
    }

    private boolean checkVertical (Ship ship, int size, String letter, String number) {
        // Vertical means keep the letter the same, but increment number
        // get index of letter
        int letterIndex = 0;
        for (int i = 0; i < mLetters.length - 1; i++) {
            if (mLetters[i] == letter) {
                letterIndex = i;
            }
        }
        // get index of number
        int numberIndex = 0;
        for (int i = 0; i < mNumbers.length - 1; i++) {
            if (mNumbers[i] == number) {
                numberIndex = i;
            }
        }
        // Determine if numberIndex + size is greater than mNumbers length.
        // If it is, placement in vertical orientation is impossible
        if (numberIndex + size > mNumbers.length) {
            return false;
        }

        boolean roomForShip = true;
        ArrayList<String> shipPositions = new ArrayList<String>();
        // Add current position since it has been checked for availability
        shipPositions.add(mBoard[letterIndex][numberIndex]);
        // Now we are sure there is enough room for the ship in the vertical direction, see if any
        // spots are taken below
        for (int i = numberIndex; i < mNumbers.length - 1;) {
            String position = mBoard[letterIndex][i+1];
            if (takenPositions.contains(position)) {
                roomForShip = false;
            }
            else {
                shipPositions.add(position);
            }
        }
        if (roomForShip) {
            takenPositions.addAll(shipPositions);
            ship.setPositions((String[])shipPositions.toArray());
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkHorizontal(Ship ship, int size, String letter, String number) {
        // Horizontal means keep number the same, increment letter
        // get index of letter
        int letterIndex = 0;
        for (int i = 0; i < mLetters.length - 1; i++) {
            if (mLetters[i] == letter) {
                letterIndex = i;
            }
        }
        // get index of number
        int numberIndex = 0;
        for (int i = 0; i < mNumbers.length - 1; i++) {
            if (mNumbers[i] == number) {
                numberIndex = i;
            }
        }
        // Determine if letterIndex + size is greater than mLetters length.
        // If it is, placement in horizontal orientation is impossible
        if (letterIndex + size > mLetters.length) {
            return false;
        }

        boolean roomForShip = true;
        ArrayList<String> shipPositions = new ArrayList<String>();
        // Add current position since it has been checked for availability
        shipPositions.add(mBoard[letterIndex][numberIndex]);
        // Now we are sure there is enough room for the ship in the horizontal direction, see if any
        // spots are taken to the right
        for (int i = letterIndex; i < mLetters.length - 1;) {
            String position = mBoard[i+1][numberIndex];
            if (takenPositions.contains(position)) {
                roomForShip = false;
            }
            else {
                shipPositions.add(position);
            }
        }
        if (roomForShip) {
            takenPositions.addAll(shipPositions);
            ship.setPositions((String[])shipPositions.toArray());
            return true;
        }
        else {
            return false;
        }
    }

    private String getRandomLetter() {
        Random rand = new Random();
        int randomNum = rand.nextInt(mLetters.length);
        return mLetters[randomNum];
    }

    private String getRandomNumber() {
        Random rand = new Random();
        int randomNum = rand.nextInt(mNumbers.length);
        return mNumbers[randomNum];
    }

    private String getRandomOrientation() {
        Random rand = new Random();
        int randomNum = rand.nextInt(mOrientations.length);
        return mOrientations[randomNum];
    }
}
