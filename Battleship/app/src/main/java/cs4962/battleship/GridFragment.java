package cs4962.battleship;

import android.app.Fragment;
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
    private Board mBoard;
    private GridView gridView;
    private String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    private String[] mNumbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    private String[] mText = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

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
                squareView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.i("ONCREATE", ((GridSquareView) v).getPosition() + " CLICKED");
                    }
                });
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
        return gridView;
    }

    private void drawShips() {
    }

    public void setGridViewBoard(Board board) {
        if (gridView == null) {
            return;
        }

        mBoard = board;
    }

}
