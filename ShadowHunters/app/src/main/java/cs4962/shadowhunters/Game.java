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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * Created by Ethan on 12/3/2014.
 */
public class Game {
    private ArrayList<Character> mHunterCharacters = new ArrayList<Character>();
    private ArrayList<Character> mShadowCharacters = new ArrayList<Character>();
    private ArrayList<Character> mNeutralCharacters = new ArrayList<Character>();
    private ArrayList<AreaCard> mBoard = new ArrayList<AreaCard>();
    private Queue<GreenCard> mGreenCards = new LinkedList<GreenCard>();
    private Queue<BlackCard> mBlackCards = new LinkedList<BlackCard>();
    private Queue<WhiteCard> mWhiteCards = new LinkedList<WhiteCard>();
    private ArrayList<GreenCard> mGreenCardList = new ArrayList<GreenCard>();
    private ArrayList<BlackCard> mBlackCardList = new ArrayList<BlackCard>();
    private ArrayList<WhiteCard> mWhiteCardList = new ArrayList<WhiteCard>();
    private Player currentPlayer;
    private LinkedList<Player> mPlayers = new LinkedList<Player>();
    private File mGameFile;

    // GAME SETUP
    public Game(File file) {
        this.mGameFile = file;
        // Check to see if it's a new game, init the game if it is
        if (file.exists()) {
            // Load game from file
            loadGame(file);
        }
        else {
            // Create board and randomize area card positions
            initBoard();
            // Shuffle White, Green, Black cards
            initGreenCards();
            initBlackCards();
            initWhiteCards();
            // Assign players to random teams
            assignCharacters();

            // Select random player to start game
            pickRandomStartPlayer();
        }
    }

    public Queue<GreenCard> getGreenCards() {
        return mGreenCards;
    }

    public void setGreenCards(Queue<GreenCard> mGreenCards) {
        this.mGreenCards = mGreenCards;
    }

    public Queue<BlackCard> getBlackCards() {
        return mBlackCards;
    }

    public void setBlackCards(Queue<BlackCard> mBlackCards) {
        this.mBlackCards = mBlackCards;
    }

    public Queue<WhiteCard> getWhiteCards() {
        return mWhiteCards;
    }

    public void setWhiteCards(Queue<WhiteCard> mWhiteCards) {
        this.mWhiteCards = mWhiteCards;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public File getGameFile() {
        return mGameFile;
    }

    public void setGameFile(File mGameFile) {
        this.mGameFile = mGameFile;
    }

    public ArrayList<AreaCard> getBoard() {
        return mBoard;
    }

    public LinkedList<Player> getPlayers() {
        return mPlayers;
    }

    private void initBoard() {
        AreaCard hermit = new AreaCard("Hermit's Cabin");
        mBoard.add(hermit);
        AreaCard underworld = new AreaCard("Underworld Gate");
        mBoard.add(underworld);
        AreaCard church = new AreaCard("Church");
        mBoard.add(church);
        AreaCard cemetery = new AreaCard("Cemetery");
        mBoard.add(cemetery);
        AreaCard woods = new AreaCard("Weird Woods");
        mBoard.add(woods);
        AreaCard altar = new AreaCard("Erstwhile Altar");
        mBoard.add(altar);
        // Randomly assign area card positions on board
        Collections.shuffle(mBoard);
    }

    private void assignCharacters () {
        initCharacters();

        PlayerList pList = PlayerList.getInstance();
        Set<Integer> playerIds = pList.getIdentifiers();
        int numPlayers = playerIds.size();

        switch (numPlayers) {
            case 4:
                // 4 Players: 2 Hunter, 2 Shadow
                randomizeTeams(2, 2, 0);
                break;
            case 5:
                // 5 Players: 2 Hunter, 2 Shadow, 1 Neutral
                randomizeTeams(2, 2, 1);
                break;
            case 6:
                // 6 Players: 2 Hunter, 2 Shadow, 2 Neutral
                randomizeTeams(2, 2, 2);
                break;
            case 7:
                // 7 Players: 2 Hunter, 2 Shadow, 3 Neutral
                randomizeTeams(2, 2, 3);
                break;
            case 8:
                // 8 Players: 3 Hunter, 3 Shadow, 2 Neutral
                randomizeTeams(3, 3, 2);
                break;
        }
        saveGame(mGameFile);
        pList.savePlayerList(pList.getPlayerListFile());
    }

    private void randomizeTeams(int numHunters, int numShadows, int numNeutrals) {
        PlayerList pList = PlayerList.getInstance();
        Set<Integer> playerIds = pList.getIdentifiers();

        Random rand = new Random();

        ArrayList<Character> tempNeutrals = new ArrayList<Character>();
        tempNeutrals.addAll(mNeutralCharacters);
        Collections.shuffle(tempNeutrals);
        ArrayList<Character> tempHunters = new ArrayList<Character>();
        tempHunters.addAll(mHunterCharacters);
        Collections.shuffle(tempHunters);
        ArrayList<Character> tempShadows = new ArrayList<Character>();
        tempShadows.addAll(mShadowCharacters);
        Collections.shuffle(tempShadows);

        int randomHunterIndex = rand.nextInt(tempHunters.size());
        int randomShadowIndex = rand.nextInt(tempShadows.size());
        int randomNeutralIndex = rand.nextInt(tempNeutrals.size());

        ArrayList<Character> shuffledTeams = new ArrayList<Character>();
        int assignedHunters = 0;
        int assignedShadows = 0;
        int assignedNeutrals = 0;

        while (assignedHunters < numHunters) {
            randomHunterIndex = rand.nextInt(tempHunters.size());
            Character hunter = tempHunters.remove(randomHunterIndex);
            shuffledTeams.add(hunter);
            assignedHunters++;
        }
        while (assignedShadows < numShadows) {
            randomShadowIndex = rand.nextInt(tempShadows.size());
            Character shadow = tempShadows.remove(randomShadowIndex);
            shuffledTeams.add(shadow);
            assignedShadows++;
        }
        while (assignedNeutrals < numNeutrals) {
            randomNeutralIndex = rand.nextInt(tempNeutrals.size());
            Character neutral = tempNeutrals.remove(randomNeutralIndex);
            shuffledTeams.add(neutral);
            assignedNeutrals++;
        }
        // Assign characters to players
        for (Integer color : playerIds) {
            Player p = pList.getPlayer(color);
            int randomIndex = rand.nextInt(shuffledTeams.size());
            p.setCharacter(shuffledTeams.remove(randomIndex));
        }
        saveGame(mGameFile);
    }

    private void initCharacters() {
        // Create neutrals
        mNeutralCharacters.add(new Character("Allie"));
        mNeutralCharacters.add(new Character("Bob"));
        mNeutralCharacters.add(new Character("Charles"));
        mNeutralCharacters.add(new Character("Daniel"));
        // Create hunters
        mHunterCharacters.add(new Character("Emi"));
        mHunterCharacters.add(new Character("Franklin"));
        mHunterCharacters.add(new Character("George"));
        // Create shadows
        mShadowCharacters.add(new Character("Unknown"));
        mShadowCharacters.add(new Character("Vampire"));
        mShadowCharacters.add(new Character("Werewolf"));
        saveGame(mGameFile);
    }

    private void initGreenCards() {
        mGreenCardList.add(new GreenCard("Aid"));
        mGreenCardList.add(new GreenCard("Anger"));
        mGreenCardList.add(new GreenCard("Anger"));
        mGreenCardList.add(new GreenCard("Blackmail"));
        mGreenCardList.add(new GreenCard("Blackmail"));
        mGreenCardList.add(new GreenCard("Bully"));
        mGreenCardList.add(new GreenCard("Exorcism"));
        mGreenCardList.add(new GreenCard("Greed"));
        mGreenCardList.add(new GreenCard("Greed"));
        mGreenCardList.add(new GreenCard("Huddle"));
        mGreenCardList.add(new GreenCard("Nurturance"));
        mGreenCardList.add(new GreenCard("Prediction"));
        mGreenCardList.add(new GreenCard("Slap"));
        mGreenCardList.add(new GreenCard("Slap"));
        mGreenCardList.add(new GreenCard("Spell"));
        mGreenCardList.add(new GreenCard("Tough Lesson"));
        shuffleCards("GREEN");
        saveGame(mGameFile);
    }

    private void initBlackCards () {
        mBlackCardList.add(new BlackCard("Banana Peel"));
        mBlackCardList.add(new BlackCard("Bloodthirsty Spider"));
        mBlackCardList.add(new BlackCard("Butcher Knife"));
        mBlackCardList.add(new BlackCard("Chainsaw"));
        mBlackCardList.add(new BlackCard("Diabolic Ritual"));
        mBlackCardList.add(new BlackCard("Dynamite"));
        mBlackCardList.add(new BlackCard("Handgun"));
        mBlackCardList.add(new BlackCard("Machine Gun"));
        mBlackCardList.add(new BlackCard("Masamune"));
        mBlackCardList.add(new BlackCard("Moody Goblin"));
        mBlackCardList.add(new BlackCard("Moody Goblin"));
        mBlackCardList.add(new BlackCard("Rusted Broad Axe"));
        mBlackCardList.add(new BlackCard("Spiritual Doll"));
        mBlackCardList.add(new BlackCard("Vampire Bat"));
        mBlackCardList.add(new BlackCard("Vampire Bat"));
        mBlackCardList.add(new BlackCard("Vampire Bat"));
        shuffleCards("BLACK");
        saveGame(mGameFile);
    }

    private void initWhiteCards() {
        mWhiteCardList.add(new WhiteCard("Advent"));
        mWhiteCardList.add(new WhiteCard("Blessing"));
        mWhiteCardList.add(new WhiteCard("Chocolate"));
        mWhiteCardList.add(new WhiteCard("Concealed Knowledge"));
        mWhiteCardList.add(new WhiteCard("Disenchant Mirror"));
        mWhiteCardList.add(new WhiteCard("First Aid"));
        mWhiteCardList.add(new WhiteCard("Flare of Judgement"));
        mWhiteCardList.add(new WhiteCard("Fortune Brooch"));
        mWhiteCardList.add(new WhiteCard("Guardian Angel"));
        mWhiteCardList.add(new WhiteCard("Holy Robe"));
        mWhiteCardList.add(new WhiteCard("Holy Water of Healing"));
        mWhiteCardList.add(new WhiteCard("Holy Water of Healing"));
        mWhiteCardList.add(new WhiteCard("Mystic Compass"));
        mWhiteCardList.add(new WhiteCard("Silver Rosary"));
        mWhiteCardList.add(new WhiteCard("Spear of Longinus"));
        mWhiteCardList.add(new WhiteCard("Talisman"));
        shuffleCards("WHITE");
        saveGame(mGameFile);
    }

    public void shuffleCards(String color) {
        if (color.equals("GREEN")) {
            mGreenCards.clear();
            Collections.shuffle(mGreenCardList);
            for (int i = 0; i < mGreenCardList.size(); i++) {
                mGreenCards.add(mGreenCardList.get(i));
            }
        }
        else if(color.equals("BLACK")) {
            mBlackCards.clear();
            Collections.shuffle(mBlackCardList);
            for (int i = 0; i < mBlackCardList.size(); i++) {
                mBlackCards.add(mBlackCardList.get(i));
            }
        }
        else {
            mWhiteCards.clear();
            Collections.shuffle(mWhiteCardList);
            for (int i = 0; i < mWhiteCardList.size(); i++) {
                mWhiteCards.add(mWhiteCardList.get(i));
            }
        }
    }

    private void pickRandomStartPlayer() {
        PlayerList pList = PlayerList.getInstance();
        Set<Integer> players = pList.getIdentifiers();
        ArrayList<Player> temp = new ArrayList<Player>();
        Random rand = new Random();
        // Add all the players to a temporary list
        for (Integer color : players) {
            temp.add(pList.getPlayer(color));
        }
        // Shuffle the list
        Collections.shuffle(temp);
        // Pick a random player from the shuffled list
        int index = rand.nextInt(temp.size());
        currentPlayer = temp.get(index);
        currentPlayer.setTurn(true);
        // start player is at beginning of the list, push the list on backwards
        if (index == 0) {
            for (int i = temp.size(); i >= 0; i--) {
                mPlayers.push(temp.get(i));
            }
        }
        else {
            // Push on start player
            mPlayers.push(temp.get(index));
            // Push all players before start player into queue going backwards through list
            for (int i = index-1; i >= 0; i--) {
                mPlayers.push(temp.get(i));
            }
            // Push all players after start player into queue going backwards through list
            for (int i = temp.size()-1; i > index; i--) {
                Player p = temp.get(i);
                mPlayers.push(temp.get(i));
            }
        }

        saveGame(mGameFile);
        pList.savePlayerList(pList.getPlayerListFile());
    }

    public void saveGame(File gameFile) {
        Gson gson = new Gson();
        // Save current player
        String jsonPlayer = gson.toJson(currentPlayer);
        // Save player queue
        String jsonPlayerQueue = gson.toJson(mPlayers);
        // Save board
        String jsonBoard = gson.toJson(mBoard);
        // Save characters
        String jsonHunterList = gson.toJson(mHunterCharacters);
        String jsonShadowList = gson.toJson(mShadowCharacters);
        String jsonNeutralList = gson.toJson(mNeutralCharacters);
        // Save cards
        String jsonGreenCardList = gson.toJson(mGreenCardList);
        String jsonGreenCards = gson.toJson(mGreenCards);
        String jsonBlackCardList = gson.toJson(mBlackCardList);
        String jsonBlackCards = gson.toJson(mBlackCards);
        String jsonWhiteCardList = gson.toJson(mWhiteCardList);
        String jsonWhiteCards = gson.toJson(mWhiteCards);

        try {
            FileWriter textWriter = new FileWriter(gameFile);
            BufferedWriter bufferedTextWriter = new BufferedWriter(textWriter);
            bufferedTextWriter.write(jsonPlayer + "\n");
            bufferedTextWriter.write(jsonPlayerQueue + "\n");
            bufferedTextWriter.write(jsonBoard + "\n");
            bufferedTextWriter.write(jsonHunterList + "\n");
            bufferedTextWriter.write(jsonShadowList + "\n");
            bufferedTextWriter.write(jsonNeutralList + "\n");
            bufferedTextWriter.write(jsonGreenCardList + "\n");
            bufferedTextWriter.write(jsonGreenCards + "\n");
            bufferedTextWriter.write(jsonBlackCardList + "\n");
            bufferedTextWriter.write(jsonBlackCards + "\n");
            bufferedTextWriter.write(jsonWhiteCardList + "\n");
            bufferedTextWriter.write(jsonWhiteCards);
            bufferedTextWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGame(File gameFile) {
        try {
            FileReader textReader = new FileReader(gameFile);
            BufferedReader bufferedTextReader = new BufferedReader(textReader);

            String jsonPlayer = bufferedTextReader.readLine();
            String jsonPlayerQueue = bufferedTextReader.readLine();
            String jsonBoard = bufferedTextReader.readLine();
            String jsonHunterList = bufferedTextReader.readLine();
            String jsonShadowList = bufferedTextReader.readLine();
            String jsonNeutralList = bufferedTextReader.readLine();
            String jsonGreenCardList = bufferedTextReader.readLine();
            String jsonGreenCards = bufferedTextReader.readLine();
            String jsonBlackCardList = bufferedTextReader.readLine();
            String jsonBlackCards = bufferedTextReader.readLine();
            String jsonWhiteCardList = bufferedTextReader.readLine();
            String jsonWhiteCards = bufferedTextReader.readLine();

            Gson gson = new Gson();
            Type playerQueueType = new TypeToken<LinkedList<Player>>(){}.getType();
            Type boardType = new TypeToken<ArrayList<AreaCard>>(){}.getType();
            Type characterListType = new TypeToken<Collection<Character>>(){}.getType();
            Type greenCardListType = new TypeToken<Collection<Card>>(){}.getType();
            Type blackCardListType = new TypeToken<ArrayList<BlackCard>>(){}.getType();
            Type whiteCardListType = new TypeToken<ArrayList<WhiteCard>>(){}.getType();
            Type greenCardsType = new TypeToken<LinkedList<GreenCard>>(){}.getType();
            Type blackCardsType = new TypeToken<LinkedList<BlackCard>>(){}.getType();
            Type whiteCardsType = new TypeToken<LinkedList<WhiteCard>>(){}.getType();

            currentPlayer = gson.fromJson(jsonPlayer, Player.class);

            mPlayers = gson.fromJson(jsonPlayerQueue, playerQueueType);

            mBoard = gson.fromJson(jsonBoard, boardType);

            mHunterCharacters = gson.fromJson(jsonHunterList, characterListType);
            mShadowCharacters = gson.fromJson(jsonShadowList, characterListType);
            mNeutralCharacters = gson.fromJson(jsonNeutralList, characterListType);

            mGreenCardList = gson.fromJson(jsonGreenCardList, greenCardListType);
            mGreenCards = gson.fromJson(jsonGreenCards, greenCardsType);

            mBlackCardList = gson.fromJson(jsonBlackCardList, blackCardListType);
            mBlackCards = gson.fromJson(jsonBlackCards, blackCardsType);

            mWhiteCardList = gson.fromJson(jsonWhiteCardList, whiteCardListType);
            mWhiteCards = gson.fromJson(jsonWhiteCards, whiteCardsType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
