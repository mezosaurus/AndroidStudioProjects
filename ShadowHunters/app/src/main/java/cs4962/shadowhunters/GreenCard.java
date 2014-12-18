package cs4962.shadowhunters;

import java.util.ArrayList;

/**
 * Created by Ethan on 12/8/2014.
 */
public class GreenCard extends Card {

    private boolean showCharacter = false;
    private int healthThreshold = 0;

    public GreenCard(String name) {
        this.name = name;
        eligibleTargetTeams = new ArrayList<String>();
        if (name.equals("Aid")) {
            eligibleTargetTeams.add("HUNTER");
            healAmount = 1;
            text = "If you are a HUNTER, heal 1 damage. If you have no damage, take 1 damage.";
        }
        else if (name.equals("Anger")) {
            eligibleTargetTeams.add("HUNTER");
            eligibleTargetTeams.add("SHADOW");
            giveEquipment = true;
            damageAmount = 1;
            text = "If you are a HUNTER or SHADOW, give 1 equipment card to current player or take 1 damage.";
        }
        else if (name.equals("Blackmail")) {
            eligibleTargetTeams.add("HUNTER");
            eligibleTargetTeams.add("NEUTRAL");
            giveEquipment = true;
            damageAmount = 1;
            text = "If you are a HUNTER or NEUTRAL, give 1 equipment card to current player or take 1 damage.";
        }
        else if (name.equals("Bully")) {
            eligibleTargetTeams.add("NEUTRAL");
            eligibleTargetTeams.add("SHADOW");
            eligibleTargetTeams.add("HUNTER");
            damageAmount = 1;
            healthThreshold = 11;
            text = "If your HP is less than or equal to 11 (A, B, C, E, U), take 1 damage.";
        }
        else if (name.equals("Exorcism")) {
            eligibleTargetTeams.add("SHADOW");
            damageAmount = 2;
            text = "If you are a SHADOW, take 2 damage.";
        }
        else if (name.equals("Greed")) {
            eligibleTargetTeams.add("SHADOW");
            eligibleTargetTeams.add("NEUTRAL");
            giveEquipment = true;
            damageAmount = 1;
            text = "If you are a NEUTRAL or SHADOW, give 1 equipment card to current player or take 1 damage.";
        }
        else if (name.equals("Huddle")) {
            eligibleTargetTeams.add("SHADOW");
            healAmount = 1;
            text = "If you are SHADOW, heal 1 damage. If you have no damage, take 1 damage.";
        }
        else if (name.equals("Nurturance")) {
            eligibleTargetTeams.add("NEUTRAL");
            healAmount = 1;
            text = "If you are a NEUTRAL, heal 1 damage. If you have no damage, take 1 damage.";
        }
        else if (name.equals("Prediction")) {
            eligibleTargetTeams.add("NEUTRAL");
            eligibleTargetTeams.add("SHADOW");
            eligibleTargetTeams.add("HUNTER");
            showCharacter = true;
            text = "Show your character card to current player.";
        }
        else if (name.equals("Slap")) {
            eligibleTargetTeams.add("HUNTER");
            damageAmount = 1;
            text = "If you are a HUNTER, take 1 damage.";
        }
        else if (name.equals("Spell")) {
            eligibleTargetTeams.add("SHADOW");
            damageAmount = 1;
            text = "If you are a SHADOW, take 1 damage.";
        }
        else if (name.equals("Tough Lesson")) {
            eligibleTargetTeams.add("NEUTRAL");
            eligibleTargetTeams.add("SHADOW");
            eligibleTargetTeams.add("HUNTER");
            healthThreshold = 12;
            damageAmount = 2;
            text = "If your HP is greater than or equal to 12 (D, F, G, V, W) take 2 damage.";
        }
    }
}
