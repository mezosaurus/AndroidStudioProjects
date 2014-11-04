package cs4962.battleship;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Ethan on 10/29/2014.
 */
public class Game {
    private UUID identifier;
    private Player playerOne;
    private Player winner;
    private Player playerTwo;
    private boolean inProgress;
    private Date date;

    public Game() {
        playerOne = new Player();
        playerOne.setTurn(true);
        playerTwo = new Player();
        playerTwo.setTurn(false);
    }

    public void setWinner(int player) {
        if (player == 1) {
            winner = playerOne;
        }
        else if (player == 2) {
            winner = playerTwo;
        }
    }

    public int getWinner() {
        if (winner.equals(playerOne)) {
            return 1;
        }
        else {
            return 2;
        }
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public boolean inProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
