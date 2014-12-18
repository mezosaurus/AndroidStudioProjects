package cs4962.shadowhunters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ethan on 12/3/2014.
 */
public class PlayerList {
    static Map<Integer, Player> mPlayerList = new HashMap<Integer, Player>();
    static PlayerList mInstance = null;

    static File mPlayerListFile = null;
    static PlayerList getInstance() {
        if (mInstance == null) {
            mInstance = new PlayerList();
        }
        if (mPlayerListFile != null && mPlayerList.size() == 0)
            mInstance.loadPlayerList(mPlayerListFile);
        return mInstance;
    }

    private PlayerList() {}

    public interface OnPlayerListChanged {
        public void onPlayerListChanged();
    }

    OnPlayerListChanged mPlayerListChanged = null;

    public OnPlayerListChanged getPlayerListChanged() {
        return mPlayerListChanged;
    }

    public void setPlayerListChanged(OnPlayerListChanged mPlayerListChanged) {
        this.mPlayerListChanged = mPlayerListChanged;
    }

    public static File getPlayerListFile() {
        return mPlayerListFile;
    }

    public static void setPlayerListFile(File mPlayerListFile) {
        PlayerList.mPlayerListFile = mPlayerListFile;
    }

    public Set<Integer> getIdentifiers() {
        return mPlayerList.keySet();
    }

    public Player getPlayer(int color) {
        return mPlayerList.get(color);
    }

    public void addPlayer(Player p) {
        mPlayerList.put(p.getColor(), p);
        savePlayerList(mPlayerListFile);
        if (mPlayerListChanged != null) {
            mPlayerListChanged.onPlayerListChanged();
        }
    }

    public void removePlayer(UUID identifier) {
        mPlayerList.remove(identifier);
        savePlayerList(mPlayerListFile);
        if (mPlayerListChanged != null) {
            mPlayerListChanged.onPlayerListChanged();
        }
    }

    public void loadPlayerList(File playerListFile) {
        mPlayerList.clear();
        try {
            FileReader textReader = new FileReader(playerListFile);
            BufferedReader bufferedTextReader = new BufferedReader(textReader);
            String jsonPlayerList = null;
            jsonPlayerList = bufferedTextReader.readLine();

            Gson gson = new Gson();
            Type playerListType = new TypeToken<Map<Integer, Player>>(){}.getType();
            Map<Integer, Player> playerList = gson.fromJson(jsonPlayerList, playerListType);
            bufferedTextReader.close();
            Iterator it = playerList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                Player p = (Player)pairs.getValue();
                mInstance.addPlayer(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePlayerList(File playerListFile) {
        Gson gson = new Gson();
        String jsonPlayerList = gson.toJson(mPlayerList);
        try {
            FileWriter textWriter = new FileWriter(playerListFile);
            BufferedWriter bufferedTextWriter = new BufferedWriter(textWriter);
            bufferedTextWriter.write(jsonPlayerList);
            bufferedTextWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
