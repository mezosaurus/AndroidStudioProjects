package cs4962.battleshipnetwork;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Ethan on 11/15/2014.
 */
public class BattleshipServices {

    public static final String BASE_URL = "http://battleship.pixio.com";
    public enum GameStatus { DONE, PLAYING, WAITING }
    public enum GridSquareStatus { HIT, MISS, SHIP, NONE }

    public class GameList {
        public String id;
        public String name;
        public GameStatus status;
    }

    public class GameModel {
        public String id;
        public String name;
        public String player1;
        public String player2;
        public String winner;
        public int missilesLaunched;
    }

    public class Boards {
        public PlayerBoard[] playerBoard;
        public OpponentBoard[] opponentBoard;
    }

    public class PlayerBoard {
        public int xPos;
        public int yPos;
        public GridSquareStatus status;
    }

    public class OpponentBoard {
        public int xPos;
        public int yPos;
        public GridSquareStatus status;
    }

    public class Guess {
        public boolean hit;
        public int shipSunk;
    }

    public class Turn {
        public boolean isYourTurn;
        public String winner;
    }

    public class CreateGame {
        public String playerId;
        public String gameId;
    }

    public class JoinGame {
        public String playerId;
    }

    public static GameList[] listGames() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(BASE_URL + "/api/games");
            HttpResponse response = client.execute(request);

            InputStream responseContent = response.getEntity().getContent();
            Scanner responseScanner = new Scanner(responseContent).useDelimiter("\\A");
            String responseString = responseScanner.hasNext() ? responseScanner.next() : null;

            if (responseString == null) {
                return null;
            }

            Gson gson = new Gson();
            GameList[] gameList = gson.fromJson(responseString, GameList[].class);
            return gameList;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GameModel getGameDetail(String identifier) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(BASE_URL + "/api/games/" + identifier);
            HttpResponse response = client.execute(request);

            InputStream responseContent = response.getEntity().getContent();
            Scanner responseScanner = new Scanner(responseContent).useDelimiter("\\A");
            String responseString = responseScanner.hasNext() ? responseScanner.next() : null;

            if (responseString == null) {
                return null;
            }

            Gson gson = new Gson();
            GameModel game = gson.fromJson(responseString, GameModel.class);
            return game;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JoinGame joinGame(String gameId, String playerName) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(BASE_URL + "/api/games/" + gameId + "/join");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("playerName", playerName));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(request);

            InputStream responseContent = response.getEntity().getContent();
            Scanner responseScanner = new Scanner(responseContent).useDelimiter("\\A");
            String responseString = responseScanner.hasNext() ? responseScanner.next() : null;

            if (responseString == null) {
                return null;
            }

            Gson gson = new Gson();
            JoinGame joinGame = gson.fromJson(responseString, JoinGame.class);
            return joinGame;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CreateGame createGame(String gameName, String playerName) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(BASE_URL + "/api/games");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("gameName", gameName));
            nameValuePairs.add(new BasicNameValuePair("playerName", playerName));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(request);

            InputStream responseContent = response.getEntity().getContent();
            Scanner responseScanner = new Scanner(responseContent).useDelimiter("\\A");
            String responseString = responseScanner.hasNext() ? responseScanner.next() : null;

            if (responseString == null) {
                return null;
            }

            Gson gson = new Gson();
            CreateGame createGame = gson.fromJson(responseString, CreateGame.class);
            return createGame;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Guess makeGuess(String gameId, String playerId, int xPos, int yPos) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(BASE_URL + "/api/games/" + gameId + "/guess");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("playerId", playerId));
            nameValuePairs.add(new BasicNameValuePair("xPos", xPos+""));
            nameValuePairs.add(new BasicNameValuePair("yPos", yPos+""));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(request);

            InputStream responseContent = response.getEntity().getContent();
            Scanner responseScanner = new Scanner(responseContent).useDelimiter("\\A");
            String responseString = responseScanner.hasNext() ? responseScanner.next() : null;

            if (responseString == null) {
                return null;
            }

            Gson gson = new Gson();
            Guess guess = gson.fromJson(responseString, Guess.class);
            return guess;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Turn getTurn(String gameId, String playerId) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(BASE_URL + "/api/games/" + gameId + "/status");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("playerId", playerId));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(request);

            InputStream responseContent = response.getEntity().getContent();
            Scanner responseScanner = new Scanner(responseContent).useDelimiter("\\A");
            String responseString = responseScanner.hasNext() ? responseScanner.next() : null;

            if (responseString == null) {
                return null;
            }

            Gson gson = new Gson();
            Turn turn = gson.fromJson(responseString, Turn.class);
            return turn;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boards getBoards(String gameId, String playerId) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(BASE_URL + "/api/games/" + gameId + "/board");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("playerId", playerId));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(request);

            InputStream responseContent = response.getEntity().getContent();
            Scanner responseScanner = new Scanner(responseContent).useDelimiter("\\A");
            String responseString = responseScanner.hasNext() ? responseScanner.next() : null;

            if (responseString == null) {
                return null;
            }
            else if (responseString.contains("Game is not in play.")) {
                return null;
            }

            Gson gson = new Gson();
            Boards boards = gson.fromJson(responseString, Boards.class);
            return boards;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
