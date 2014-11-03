package cs4962.battleship;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Ethan on 10/29/2014.
 */
public class GridFragment extends Fragment {
    private Player mPlayer;
    private boolean mActionFragment;
    private GridView gridView;
    private String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    private String[] mNumbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    private String[] mText = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    OnSquareClickedListener mCallback;

    // Container Activity must implement this interface
    public interface OnSquareClickedListener {
        public void onSquareClicked(GridSquareView actionView);
    }

    public void setActionFragment (boolean actionFragment) {
        mActionFragment = actionFragment;
    }

    public GridView getGridView() {
        return gridView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSquareClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSquareSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gridView = new GridView(getActivity());
        int rowCount = 0;
        int columnCount = 0;
        int letterNumberIndex = 0;
        for (int i = 0; i < 121; i++) {
            GridSquareView squareView = new GridSquareView(getActivity(), gridView.getWidth()/11, gridView.getHeight()/11);

            // Ignore gridSquares that are used for Letter/Number indications
            if ((i >=0 && i <= 11) || i == 22 || i == 33 || i == 44 || i == 55 || i == 66 || i == 77 || i == 88 || i == 99 || i == 110) {
                squareView.setPosition("");
                squareView.setLetterNumber(mText[letterNumberIndex]);
                letterNumberIndex++;
            }
            else {
                String letter = mLetters[columnCount];
                String number = mNumbers[rowCount];
                squareView.setPosition(letter + number);
                // Make sure this fragment is the one that should register clicks
                if (mActionFragment) {
                    squareView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            GridSquareView view = (GridSquareView) v;
                            mCallback.onSquareClicked(view);
                        }
                    });
                }
                if (columnCount == 9) {
                    columnCount = 0;
                    rowCount++;
                    // Reached end of row, reset childCenters
                }
                else {
                    columnCount++;
                }
            }
            gridView.addView(squareView);
        }
        gridView.setId(10);
        //drawShips();
        return gridView;
    }

    public GridSquareView getSquareByPos(String position) {
        GridSquareView retVal = null;
        for (int i = 0; i < gridView.getChildCount(); i++) {
            GridSquareView child = (GridSquareView)gridView.getChildAt(i);
            if (child.getPosition() == position) {
                retVal = child;
            }
        }
        return retVal;
    }

    public void setGridViewPlayer(Player player) {
        if (gridView == null) {
            return;
        }
        mPlayer = player;
    }

    public void drawShips() {
        ArrayList<String> shipPositions = mPlayer.getBoard().getShipPositions();
        for (int i = 0; i < shipPositions.size(); i++) {
            String shipPos = shipPositions.get(i);
            for (int j = 0; j < gridView.getChildCount(); j++) {
                GridSquareView child = (GridSquareView)gridView.getChildAt(j);
                // reset grid square to blue
                //child.setColor(Color.BLUE);
                //child.invalidate();
                String boardPos = child.getPosition();
                if (shipPos.equals(boardPos)) {
                    child.setColor(Color.GRAY);
                    child.invalidate();
                    //gridView.invalidate();
                }
            }
        }
    }

    public void drawHitsMisses() {
        ArrayList<String> hits = mPlayer.getHits();
        ArrayList<String> misses = mPlayer.getMisses();

        // Draw hits
        for (int i = 0; i < hits.size(); i++) {
            String hitPos = hits.get(i);
            for (int j = 0; j < gridView.getChildCount(); j++) {
                GridSquareView child = (GridSquareView)gridView.getChildAt(j);
                // reset grid square to blue
                //child.setColor(Color.BLUE);
                //child.invalidate();
                String boardPos = child.getPosition();
                if (hitPos.equals(boardPos)) {
                    child.setColor(Color.RED);
                    child.invalidate();
                    //gridView.invalidate();
                }
            }
        }

        // Draw misses
        for (int i = 0; i < misses.size(); i++) {
            String missPos = misses.get(i);
            for (int j = 0; j < gridView.getChildCount(); j++) {
                GridSquareView child = (GridSquareView)gridView.getChildAt(j);
                // reset grid square to blue
                //child.setColor(Color.BLUE);
                //child.invalidate();
                String boardPos = child.getPosition();
                if (missPos.equals(boardPos)) {
                    child.setColor(Color.WHITE);
                    child.invalidate();
                    //gridView.invalidate();
                }
            }
        }
    }

    public void resetFragment() {
        for (int j = 0; j < gridView.getChildCount(); j++) {
            GridSquareView child = (GridSquareView) gridView.getChildAt(j);
            child.setColor(Color.BLUE);
            child.invalidate();
        }
    }
}
