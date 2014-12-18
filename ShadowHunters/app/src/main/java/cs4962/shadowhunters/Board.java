package cs4962.shadowhunters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Ethan on 12/8/2014.
 */
public class Board {

    private Map<Integer, AreaCard> mBoard = new HashMap<Integer, AreaCard>();
    private ArrayList<AreaCard> mCards = new ArrayList<AreaCard>();
    private AreaCard hermit;
    private AreaCard underworld;
    private AreaCard church;
    private AreaCard cemetery;
    private AreaCard woods;
    private AreaCard altar;
    public Board() {
        hermit = new AreaCard("HERMITCABIN");
        mCards.add(hermit);
        underworld = new AreaCard("UNDERWORLDGATE");
        mCards.add(underworld);
        church = new AreaCard("CHURCH");
        mCards.add(church);
        cemetery = new AreaCard("CEMETERY");
        mCards.add(cemetery);
        woods = new AreaCard("WEIRDWOODS");
        mCards.add(woods);
        altar = new AreaCard("ERSTWHILEALTAR");
        mCards.add(altar);
        // Randomly assign area card positions on board
        randomizeCards();
    }

    private void randomizeCards() {
        Random rand = new Random();

        while (mCards.size() > 0) {
            int boardIndex = rand.nextInt(6);
            int cardIndex = rand.nextInt(mCards.size());
            while (mBoard.containsKey(boardIndex)) {
                boardIndex = rand.nextInt(6);
            }
            mBoard.put(boardIndex, mCards.remove(cardIndex));
        }
        System.out.println("TEST");
    }

    public Map<Integer, AreaCard> getMap() {
        return mBoard;
    }
}
