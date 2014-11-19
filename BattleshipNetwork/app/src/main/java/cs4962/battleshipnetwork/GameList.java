package cs4962.battleshipnetwork;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ethan on 10/29/2014.
 */
public class GameList {
    //static Map<String, Game> mGameList = new HashMap<String, Game>();
    static ArrayList<Game> mGameList = new ArrayList<Game>();

    static GameList mInstance = null;

    //static File mGameListFile = null;
    static GameList getInstance() {
        if (mInstance == null)
            mInstance = new GameList();
        //if (mGameListFile != null && mGameList.size() == 0)
            //mInstance.loadGameList(mGameListFile);
        return mInstance;
    }

    public interface OnGameListChanged {
        public void onGameListChanged();
    }

    OnGameListChanged mGameListChanged = null;

    public OnGameListChanged getGameListChanged() {
        return mGameListChanged;
    }

    public void setGameListChange(OnGameListChanged listener) {
        this.mGameListChanged = listener;
    }

    private GameList() {
        AsyncTask<String, Integer, BattleshipServices.GameList[]> listGamesTask = new AsyncTask<String, Integer, BattleshipServices.GameList[]>() {
            @Override
            protected BattleshipServices.GameList[] doInBackground(String... params) {
                return BattleshipServices.listGames();
            }

            @Override
            protected void onPostExecute(BattleshipServices.GameList[] gameLists) {
                super.onPostExecute(gameLists);

                // Add the games in backwards order so the newest is on top
                for (int i = gameLists.length - 1; i >= 0; i--) {
                    BattleshipServices.GameList gameList = gameLists[i];
                    String id = gameList.id;
                    String name = gameList.name;
                    BattleshipServices.GameStatus status = gameList.status;
                    Game newGame = new Game(id, name, status);
                    addGame(newGame);
                }
                /*for (BattleshipServices.GameList gameList : gameLists) {
                    String id = gameList.id;
                    String name = gameList.name;
                    BattleshipServices.GameStatus status = gameList.status;
                    Game newGame = new Game(id, name, status);
                    mGameList.add(newGame);
                    if (mGameListChanged != null) {
                        mGameListChanged.onGameListChanged();
                    }
                    //addGame(newGame);
                }*/
            }
        };
        listGamesTask.execute();

    }

    /*public static File getGameListFile() {
        return mGameListFile;
    }

    public static void setGameListFile(File mGameListFile) {
        GameList.mGameListFile = mGameListFile;
    }*/

    public ArrayList<String> getIdentifiers() {
        ArrayList<String> identifiers = new ArrayList<String>();
        for (int i = 0; i < mGameList.size(); i++) {
            Game g = mGameList.get(i);
            identifiers.add(g.getIdentifier());
        }
        return identifiers;
    }

    public Game getGame(String identifier) {
        for (int i = 0; i < mGameList.size(); i++) {
            Game g = mGameList.get(i);
            if (identifier.equals(g.getIdentifier())) {
                return g;
            }
        }
        return null;
    }

    private void addGame(Game game) {
        mGameList.add(game);
        if (mGameListChanged != null) {
            mGameListChanged.onGameListChanged();
        }
    }

    public void refreshGameList() {
        //mInstance = new GameList();
        mGameList.clear();
        AsyncTask<String, Integer, BattleshipServices.GameList[]> listGamesTask = new AsyncTask<String, Integer, BattleshipServices.GameList[]>() {
            @Override
            protected BattleshipServices.GameList[] doInBackground(String... params) {
                return BattleshipServices.listGames();
            }

            @Override
            protected void onPostExecute(BattleshipServices.GameList[] gameLists) {
                super.onPostExecute(gameLists);

                // Add the games in backwards order so the newest is on top
                for (int i = gameLists.length - 1; i >= 0; i--) {
                    BattleshipServices.GameList gameList = gameLists[i];
                    String id = gameList.id;
                    String name = gameList.name;
                    BattleshipServices.GameStatus status = gameList.status;
                    Game newGame = new Game(id, name, status);
                    addGame(newGame);
                }
            }
        };
        listGamesTask.execute();
        //saveGameList(mGameListFile);
        /*if (mGameListChanged != null) {
            mGameListChanged.onGameListChanged();
        }*/
    }

    /*public void removeGame(UUID identifier) {
        mGameList.remove(identifier);
        //saveGameList(mGameListFile);
        if (mGameListChanged != null) {
            mGameListChanged.onGameListChanged();
        }
    }*/

    /*public void loadGameList(File gameListFile) {
        mGameList.clear();
        try {
            FileReader textReader = new FileReader(gameListFile);
            BufferedReader bufferedTextReader = new BufferedReader(textReader);
            String jsonGameList = null;
            jsonGameList = bufferedTextReader.readLine();

            Gson gson = new Gson();
            Type gameListType = new TypeToken<Map<UUID, Game>>(){}.getType();
            Map<UUID, Game> gameList = gson.fromJson(jsonGameList, gameListType);
            //mGameList = gameList;
            bufferedTextReader.close();
            Iterator it = gameList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                Game g = (Game)pairs.getValue();
                mInstance.addGame(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveGameList(File gameListFile) {
        Gson gson = new Gson();
        String jsonGameList = gson.toJson(mGameList);
        try {
            FileWriter textWriter = new FileWriter(gameListFile);
            BufferedWriter bufferedTextWriter = new BufferedWriter(textWriter);
            bufferedTextWriter.write(jsonGameList);
            bufferedTextWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
