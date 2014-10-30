package cs4962.battleship;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ethan on 10/29/2014.
 */
public class GameList {
    Map<UUID, Game> mGame = new HashMap<UUID, Game>();

    static GameList mInstance = null;
    static GameList getInstance() {
        if (mInstance == null)
            mInstance = new GameList();
        return mInstance;
    }

    private GameList() {}

    public Set<UUID> getIdentifiers() {
        return mGame.keySet();
    }

    public Game getGame(UUID identifier) {
        return mGame.get(identifier);
    }

    public void addGame(Game game) {
        UUID identifier = UUID.randomUUID();
        game.identifier = identifier;
        mGame.put(identifier, game);
    }

    public void removeGame(UUID identifier) {
        mGame.remove(identifier);
    }
}
