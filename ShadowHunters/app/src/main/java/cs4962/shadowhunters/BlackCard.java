package cs4962.shadowhunters;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ethan on 12/8/2014.
 */
public class BlackCard extends Card {

    public BlackCard (String name) {
        this.name = name;
        if (name.equals("Banana Peel")) {
            this.singleUse = true;
            this.text = "Give one of your equipment cards to another character. If you have no equipment cards, you receive 1 damage.";
            this.damageAmount = 1;
            this.giveEquipment = true;
        }
        else if (name.equals("Bloodthirsty Spider")) {
            this.singleUse = true;
            this.text = "You give 2 damage to any character and receive 2 damage to yourself.";
            this.damageAmount = 2;
        }
        else if (name.equals("Butcher Knife") || name.equals("Chainsaw") || name.equals("Rusted Broad Axe")) {
            this.equipment = true;
            this.text = "On a successful attack, you give 1 point of extra damage.";
            this.damageAmount = 1;
        }
        else if (name.equals("Diabolic Ritual")) {
            this.singleUse = true;
            this.text = "If you are a SHADOW, you may reveal your identity. If you do, your damage points are reset to 0.";
            this.revealIdentity = true;
            this.eligibleTargetTeams.add("SHADOW");
            this.fullHeal = true;
        }
        else if (name.equals("Dynamite")) {
            this.singleUse = true;
            this.text = "Roll both the dice and give 3 points of damage to all characters in the location designated by the total number rolled. (Nothing happens if a 7 is rolled)";
        }
        else if (name.equals("Handgun")) {
            this.equipment = true;
            this.text = "Your attack range becomes every Area but your own.";
        }
        else if (name.equals("Machine Gun")) {
            this.equipment = true;
            this.text = "Your attacks target all characters within your attack range (roll the dice once and apply the same damage to all affected characters).";
        }
        else if (name.equals("Masamune")) {
            this.equipment = true;
            this.text = "You MUST attack if possible. Your attacks are executed by a single roll of the 4-sided die (you cannot fail).";
        }
        else if (name.equals("Moody Goblin")) {
            this.singleUse = true;
            this.text = "You steal an equipment card from any character.";
        }
        else if (name.equals("Spiritual Doll")) {
            this.singleUse = true;
            this.text = "Pick any character and roll a 6-sided die. If the die number is 1 to 4, you give 3 points of damage to that character. If the die number is 5 or 6, you get 3 points of damage.";
            this.damageAmount = 3;
        }
        else if (name.equals("Vampire Bat")) {
            this.singleUse = true;
            this.text = "You give 2 points of damage to any character and heal 1 point of your own damage.";
            this.damageAmount = 2;
            this.healAmount = 1;
        }
    }
}
