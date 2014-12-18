package cs4962.shadowhunters;

import android.graphics.Color;

/**
 * Created by Ethan on 12/3/2014.
 */
public class AreaCard {
    private int mColor;
    private String mName;
    private String mText;
    private int mRollOne;
    private int mRollTwo;

    public AreaCard(String name) {
        this.mName = name;
        if (name.equals("Hermit's Cabin")) {
            // green
            mColor = Color.GREEN;
            mRollOne = 2;
            mRollTwo = 3;
            mText = "You may draw a Green Card.";
        }
        else if (name.equals("Underworld Gate")) {
            // purple
            mColor = Utils.mixColor(Color.BLUE, Color.RED);
            mRollOne = 4;
            mRollTwo = 5;
            mText = "You may draw a card from the stack of your choice.";
        }
        else if (name.equals("Church")) {
            // white
            mColor = Color.WHITE;
            mRollOne = 6;
            mRollTwo = 0;
            mText = "You may draw a White card.";
        }
        else if (name.equals("Cemetery")) {
            // black
            mColor = Color.BLACK;
            mRollOne = 8;
            mRollTwo = 0;
            mText = "You may draw a Black card.";
        }
        else if (name.equals("Weird Woods")) {
            // grey
            mColor = Color.GRAY;
            mRollOne = 9;
            mRollTwo = 0;
            mText = "You may either give 2 damage to any player or heal 1 damage of any player.";
        }
        else if (name.equals("Erstwhile Altar")) {
            // brown
            int orange = Utils.mixColor(Color.RED, Color.YELLOW);
            mColor = Utils.mixColor(Color.BLUE, orange);
            mRollOne = 10;
            mRollTwo = 0;
            mText = "You may steal an Equipment Card from any player.";
        }
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public int getRollOne() {
        return mRollOne;
    }

    public void setRollOne(int mRollOne) {
        this.mRollOne = mRollOne;
    }

    public int getRollTwo() {
        return mRollTwo;
    }

    public void setRollTwo(int mRollTwo) {
        this.mRollTwo = mRollTwo;
    }


}
