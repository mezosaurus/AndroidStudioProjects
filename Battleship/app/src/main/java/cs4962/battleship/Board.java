package cs4962.battleship;

import android.util.Log;

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
    private String[][] mBoard;
    private ArrayList<String> takenPositions = new ArrayList<String>();
    private ArrayList<Ship> mShips;
    private ArrayList<Ship> shipsToPlace = new ArrayList<Ship>();
    private Ship mCarrier = new Ship(Ship.Type.CARRIER);
    private Ship mBattleship = new Ship(Ship.Type.BATTLESHIP);
    private Ship mSubmarine = new Ship(Ship.Type.SUBMARINE);
    private Ship mCruiser = new Ship(Ship.Type.CRUISER);
    private Ship mDestroyer = new Ship(Ship.Type.DESTROYER);

    public Board () {
        initBoard();
        createShips();
        shipsToPlace.addAll(mShips);
        placeShips(shipsToPlace);
    }

    public ArrayList<Ship> getShips () {
        return mShips;
    }

    private void initBoard() {
        mBoard = new String[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                mBoard[i][j] = mLetters[j]+mNumbers[i];
            }
        }
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
            Log.i("PLACESHIPS", "position: " + randomPosition + " is taken");
            letter = getRandomLetter();
            number = getRandomNumber();
            randomPosition = letter + number;
        }
        Log.i("PLACESHIPS", "random position: " + randomPosition);
        int size = shipToPlace.getSize();
        Log.i("PLACESHIPS", "size of ship: " + size);
        String orientation = getRandomOrientation();
        // Depending on random orientation, check to see if the ship can be placed
        boolean placed = false;
        while (!placed) {
            if (orientation == "VERTICAL") {
                placed = checkVertical(shipToPlace, size, letter, number);
                // if vertical didn't work, try horizontal
                if (!placed) {
                    placed = checkHorizontal(shipToPlace, size, letter, number);
                }
            }
            else if (orientation == "HORIZONTAL") {
                placed = checkHorizontal(shipToPlace, size, letter, number);
                // if horizontal didn't work, try vertical
                if (!placed) {
                    placed = checkVertical(shipToPlace, size, letter, number);
                }
            }
            letter = getRandomLetter();
            number = getRandomNumber();
            orientation = getRandomOrientation();
        }
        // If out of loop, ship has been placed, remove it from list
        shipsToPlace.remove(shipToPlace);
        // Place remaining ships
        placeShips(shipsToPlace);
    }

    private boolean checkVertical (Ship ship, int size, String letter, String number) {
        Log.i("CHECKVERTICAL", "position: " + letter + number);
        // Vertical means keep the letter the same, but increment number
        // get index of letter
        int letterIndex = 0;
        for (int i = 0; i < mLetters.length; i++) {
            if (mLetters[i] == letter) {
                letterIndex = i;
            }
        }
        // get index of number
        int numberIndex = 0;
        for (int i = 0; i < mNumbers.length; i++) {
            if (mNumbers[i] == number) {
                numberIndex = i;
            }
        }
        //Log.i("CHECKVERTCIAL", "number index: " + numberIndex);
        // Determine if numberIndex + size is greater than mNumbers length.
        // If it is, placement in vertical orientation is impossible
        if (numberIndex + size > mNumbers.length) {
            return false;
        }

        boolean roomForShip = true;
        ArrayList<String> shipPositions = new ArrayList<String>();
        // Add current position since it has been checked for availability
        //shipPositions.add(letter+number);
        // Now we are sure there is enough room for the ship in the vertical direction, see if any
        // spots are taken below
        for (int i = 0; i < size; i++) {
            String position = mBoard[letterIndex][i];
            if (takenPositions.contains(position)) {
                roomForShip = false;
            }
            else {
                shipPositions.add(position);
            }
        }
        /*for (int i = numberIndex; i < mNumbers.length; i++) {
            String position = mBoard[letterIndex][i];
            //Log.i("CHECKVERTICAL", "board position: " + position);
            if (takenPositions.contains(position)) {
                roomForShip = false;
            }
            else {
                shipPositions.add(position);
            }
        }*/
        if (roomForShip) {
            takenPositions.addAll(shipPositions);
            ship.setPositions(shipPositions);
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkHorizontal(Ship ship, int size, String letter, String number) {
        Log.i("CHECKHORIZONTAL", "position: " + letter + number);
        // Horizontal means keep number the same, increment letter
        // get index of letter
        int letterIndex = 0;
        for (int i = 0; i < mLetters.length; i++) {
            if (mLetters[i] == letter) {
                letterIndex = i;
            }
        }
        //Log.i("CHECKHORIZONTAL", "letter index: " + letterIndex);
        // get index of number
        int numberIndex = 0;
        for (int i = 0; i < mNumbers.length; i++) {
            if (mNumbers[i] == number) {
                numberIndex = i;
            }
        }
        // Determine if letterIndex + size is greater than mLetters length.
        // If it is, placement in horizontal orientation is impossible
        if (letterIndex + size > mLetters.length) {
            //Log.i("CHECKHORIZONTAL", "letterIndex+1+size = " + letterIndex+1+size);
            return false;
        }

        boolean roomForShip = true;
        ArrayList<String> shipPositions = new ArrayList<String>();
        // Add current position since it has been checked for availability
        //shipPositions.add(letter+number);
        // Now we are sure there is enough room for the ship in the horizontal direction, see if any
        // spots are taken to the right
        for (int i = letterIndex; i < mLetters.length; i++) {
            String position = mBoard[i][numberIndex];
            //Log.i("CHECKHORIZONTAL", "board position: " + position);
            if (takenPositions.contains(position)) {
                roomForShip = false;
            }
            else {
                shipPositions.add(position);
            }
        }
        if (roomForShip) {
            takenPositions.addAll(shipPositions);
            ship.setPositions(shipPositions);
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
