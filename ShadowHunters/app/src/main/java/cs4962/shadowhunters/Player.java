package cs4962.shadowhunters;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Ethan on 12/3/2014.
 */
public class Player implements Parcelable{
    private String name;
    private Character character;
    private ArrayList<Card> equipment = new ArrayList<Card>();
    private boolean turn;
    private int color;
    private AreaCard boardPosition;
    private boolean revealed;
    private int damage;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(character, 0);
        dest.writeByte((byte) (turn ? 1 : 0));
        dest.writeInt(color);
        dest.writeParcelable(boardPosition, 0);
        dest.writeByte((byte) (revealed ? 1 : 0));
        dest.writeInt(damage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Player(Parcel in) {
        this.name = in.readString();
        this.character = in.readParcelable(Character.class.getClassLoader());
        this.turn = in.readByte() != 0;
        this.color = in.readInt();
        this.boardPosition = in.readParcelable(AreaCard.class.getClassLoader());
        this.revealed = in.readByte() != 0;
        this.damage = in.readInt();
    }

    public static final Parcelable.Creator<Player> CREATOR
            = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public Player(String name, int color) {
        this.name = name;
        this.color = color;
        this.turn = false;
        this.damage = 0;
        this.revealed = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public AreaCard getBoardPosition() {
        return boardPosition;
    }

    public void setBoardPosition(AreaCard boardPosition) {
        this.boardPosition = boardPosition;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public ArrayList<Card> getEquipment() {
        return equipment;
    }

    public void setEquipment(ArrayList<Card> equipment) {
        this.equipment = equipment;
    }

    public int getColor() {
        return color;
    }

    public void setColor(String color) {
        // black
        if (color.equals("Black")) {
            this.color = Color.BLACK;
        }
        // green
        else if (color.equals("Green")) {
            this.color = Color.GREEN;
        }
        // blue
        else if (color.equals("Blue")) {
            this.color = Color.BLUE;
        }
        // orange
        else if (color.equals("Orange")) {
            this.color = Utils.mixColor(Color.RED, Color.YELLOW);
        }
        // pink
        else if (color.equals("Pink")) {
            this.color = Color.MAGENTA;
        }
        // red
        else if (color.equals("Red")) {
            this.color = Color.RED;
        }
        // yellow
        else if (color.equals("Yellow")) {
            this.color = Color.YELLOW;
        }
        // white
        else if (color.equals("White")) {
            this.color = Color.WHITE;
        }
    }
}
