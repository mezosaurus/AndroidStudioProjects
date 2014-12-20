package cs4962.shadowhunters;

/**
 * Created by Ethan on 12/8/2014.
 */
public class WhiteCard extends Card{

    public WhiteCard(String name) {
        this.name = name;
        if (name.equals("Advent")) {
            this.singleUse = true;
            this.text = "If you are a HUNTER, you may reveal your identity. If you do, your damage points are reset to 0.";
            this.revealIdentity = true;
            this.fullHeal = true;
        }
        else if(name.equals("Blessing")) {
            this.singleUse = true;
            this.text = "Pick a character other than yourself and roll a 6-sided die. That character heals the amount of damage shown on the die.";
        }
        else if(name.equals("Chocolate")) {
            this.singleUse = true;
            this.text = "If your character's name begins with A, E, or U, you may reveal your identity. If you do, your damage points are reset to 0.";
            this.revealIdentity = true;
            this.fullHeal = true;
        }
        /*else if(name.equals("Concealed Knowledge")) {
            this.singleUse = true;
            this.text = "Once this turn is over, you will take another turn.";
            this.extraTurn = true;
        }*/
        else if(name.equals("Disenchant Mirror")) {
            this.singleUse = true;
            this.text = "If your character's name begins with V or W, you must reveal your identity.";
            this.revealIdentity = true;
        }
        else if(name.equals("First Aid")) {
            this.singleUse = true;
            this.text = "Set a character's damage at 7 (you may choose yourself).";
        }
        else if(name.equals("Flare of Judgement")) {
            this.singleUse = true;
            this.text = "All characters except yourself receive 2 points of damage.";
            this.damageAmount = 2;
        }
        /*else if(name.equals("Fortune Brooch")) {
            this.equipment = true;
            this.text = "You receive no damage from the Weird Woods.";
        }
        else if(name.equals("Guardian Angel")) {
            this.singleUse = true;
            this.text = "You get no damage from another character attack until your next turn.";
        }
        else if(name.equals("Holy Robe")) {
            this.equipment = true;
            this.text = "Your attacks do 1 less point of damage and the amount of damage you receive from attacks is reduced by 1.";
        }*/
        else if(name.equals("Holy Water of Healing")) {
            this.singleUse = true;
            this.text = "Heal 2 points of your damage.";
            this.healAmount = 2;
        }
        /*else if(name.equals("Mystic Compass")) {
            this.equipment = true;
            this.text = "You may roll twice to move and choose which result to use.";
        }
        else if(name.equals("Silver Rosary")) {
            this.equipment = true;
            this.text = "If you attack and kill another character, you steal all their equipment.";
        }
        else if(name.equals("Spear of Longinus")) {
            this.equipment = true;
            this.text = "If you are a HUNTER and your attack is successful, you may reveal your identity. If you do, your attack does 2 extra points of damage.";
            this.damageAmount = 2;
            this.revealIdentity = true;
        }
        else if(name.equals("Talisman")) {
            this.equipment = true;
            this.text = "You receive no damage from the BLACK cards 'Bloodthirsty Spider', 'Vampire Bat' and 'Dynamite'.";
        }*/
    }
}
