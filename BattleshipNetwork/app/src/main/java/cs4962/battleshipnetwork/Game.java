package cs4962.battleshipnetwork;

import android.os.AsyncTask;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Ethan on 10/29/2014.
 */
public class Game {
    private String identifier;
    private String name;
    private BattleshipServices.GameStatus status;
    private Player playerOne;
    private Player winner;
    private Player playerTwo;
    private int missilesLaunched;
    //private boolean inProgress;
    //private Date date;

    public Game(String id, String name, BattleshipServices.GameStatus status) {
        identifier = id;
        this.status = status;
        this.name = name;

        /*playerOne = new Player();
        playerOne.setTurn(true);
        playerTwo = new Player();
        playerTwo.setTurn(false);*/
    }

    /*public void setWinner(int player) {
        if (player == 1) {
            winner = playerOne;
        }
        else if (player == 2) {
            winner = playerTwo;
        }
    }*/

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner (Player winner) {
        this.winner = winner;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player p1) {
       playerOne = p1;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player p2) {
        playerTwo = p2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BattleshipServices.GameStatus getStatus() {
        return status;
    }

    public void setStatus(BattleshipServices.GameStatus status) {
        this.status = status;
    }

    public int getMissilesLaunched() {
        return missilesLaunched;
    }

    public void setMissilesLaunched(int missilesLaunched) {
        this.missilesLaunched = missilesLaunched;
    }

    /* public boolean inProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }*/

    /*public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }*/
}
