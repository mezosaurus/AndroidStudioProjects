package cs4962.shadowhunters;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ethan on 12/10/2014.
 */
public class Character implements Parcelable {
    private String team;
    private String name;
    private int health;
    private String winCondition;
    private String ability;
    private String abilityName;
    private boolean abilityUsed;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(team);
        dest.writeString(name);
        dest.writeInt(health);
        dest.writeString(winCondition);
        dest.writeString(ability);
        dest.writeString(abilityName);
        dest.writeByte((byte) (abilityUsed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Character(Parcel in) {
        this.team = in.readString();
        this.name = in.readString();
        this.health = in.readInt();
        this.winCondition = in.readString();
        this.ability = in.readString();
        this.abilityName = in.readString();
        this.abilityUsed = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Character> CREATOR
            = new Parcelable.Creator<Character>() {
        public Character createFromParcel(Parcel in) {
            return new Character(in);
        }

        public Character[] newArray(int size) {
            return new Character[size];
        }
    };

    public Character(String name) {
        this.name = name;
        this.abilityUsed = false;
        if (name.equals("Allie")) {
            this.team = "NEUTRAL";
            this.health = 8;
            this.winCondition = "You're not dead when the game is over.";
            this.abilityName = "Mother's Love";
            this.ability = "Full heal your damage. (Only once per game)";
        }
        else if (name.equals("Bob")) {
            this.team = "NEUTRAL";
            this.health = 10;
            this.winCondition = "You have 5 or more equipment cards.";
            this.abilityName = "Robbery";
            this.ability = "If you inflict 2 or more damage to a character you may take an Equipment card of your choice from that character instead of giving them damage.";
        }
        else if (name.equals("Charles")) {
            this.team = "NEUTRAL";
            this.health = 11;
            this.winCondition = "At the time you kill another character, the total number of dead characters is 3 or more.";
            this.abilityName = "Bloody Feast";
            this.ability = "After you attack, you may give yourself 2 points of damage to attack the same character again.";
        }
        else if (name.equals("Daniel")) {
            this.team = "NEUTRAL";
            this.health = 13;
            this.winCondition = "You are the first character to die OR all the Shadow characters are dead and you are not.";
            this.abilityName = "Scream";
            this.ability = "As soon as another player dies you must reveal your identity.";
        }
        else if (name.equals("Emi")) {
            this.team = "HUNTER";
            this.health = 10;
            this.winCondition = "All the Shadow characters are dead.";
            this.abilityName = "Teleport";
            this.ability = "When you move, you can roll dice as normal or move to an adjacent Area Card.";

        }
        else if (name.equals("Franklin")) {
            this.team = "HUNTER";
            this.health = 12;
            this.winCondition = "All the Shadow characters are dead.";
            this.abilityName = "Lightning";
            this.ability = "At the start of your turn, you can pick any player and give him/her damage equal to the roll of a 6-sided die. (Only once per game)";
        }
        else if (name.equals("George")) {
            this.team = "HUNTER";
            this.health = 14;
            this.winCondition = "All the Shadow characters are dead.";
            this.abilityName = "Demolish";
            this.ability = "At the start of your turn, you can pick any player and give him/her damage equal to the roll of a 4-sided die. (Only once per game)";
        }
        else if (name.equals("Unknown")) {
            this.team = "SHADOW";
            this.health = 11;
            this.winCondition = "All the Hunter characters are dead OR 3 Neutral characters are dead.";
            this.abilityName = "Deceit";
            this.ability = "You may lie when given a Hermit card. You don't have to reveal your identity to do this.";
        }
        else if (name.equals("Vampire")) {
            this.team = "SHADOW";
            this.health = 13;
            this.winCondition = "All the Hunter characters are dead OR 3 Neutral characters are dead.";
            this.abilityName = "Suck Blood";
            this.ability = "If you attack a player and inflict damage, you heal 2 points of your own damage.";
        }
        else if (name.equals("Werewolf")) {
            this.team = "SHADOW";
            this.health = 14;
            this.winCondition = "All the Hunter characters are dead OR 3 Neutral characters are dead.";
            this.abilityName = "Counterattack";
            this.ability = "After you are attacked, you can attack that character immediately.";
        }
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getWinCondition() {
        return winCondition;
    }

    public void setWinCondition(String winCondition) {
        this.winCondition = winCondition;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public void setAbilityName(String abilityName) {
        this.abilityName = abilityName;
    }

    public boolean isAbilityUsed() {
        return abilityUsed;
    }

    public void setAbilityUsed(boolean abilityUsed) {
        this.abilityUsed = abilityUsed;
    }
}
