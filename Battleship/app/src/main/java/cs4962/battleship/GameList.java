package cs4962.battleship;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ethan on 10/29/2014.
 */
public class GameList {
    static Map<UUID, Game> mGameList = new HashMap<UUID, Game>();

    static GameList mInstance = null;

    static File mGameListFile = null;
    static GameList getInstance() {
        if (mInstance == null)
            mInstance = new GameList();
        if (mGameListFile != null && mGameList.size() == 0)
            mInstance.loadGameList(mGameListFile);
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

    private GameList() {}

    public static File getGameListFile() {
        return mGameListFile;
    }

    public static void setGameListFile(File mGameListFile) {
        GameList.mGameListFile = mGameListFile;
    }

    public Set<UUID> getIdentifiers() {
        return mGameList.keySet();
    }

    public Game getGame(UUID identifier) {
        return mGameList.get(identifier);
    }

    public void addGame(Game game) {
        UUID identifier = UUID.randomUUID();
        game.setIdentifier(identifier);
        mGameList.put(identifier, game);
        saveGameList(mGameListFile);
        if (mGameListChanged != null) {
            mGameListChanged.onGameListChanged();
        }
    }

    public void removeGame(UUID identifier) {
        mGameList.remove(identifier);
        saveGameList(mGameListFile);
        if (mGameListChanged != null) {
            mGameListChanged.onGameListChanged();
        }
    }

    public void loadGameList(File gameListFile) {
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
    }
}
