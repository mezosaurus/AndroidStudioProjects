package cs4962.shadowhunters;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Ethan on 12/13/2014.
 */
public abstract class Card {
    protected String name;
    protected int damageAmount = 0;
    protected int healAmount = 0;
    protected ArrayList<String> eligibleTargetTeams = new ArrayList<String>();
    protected String text;
    protected boolean singleUse = false;
    protected boolean equipment = false;
    protected boolean revealIdentity = false;
    protected boolean fullHeal = false;
    protected boolean extraTurn = false;
    protected boolean giveEquipment = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamageAmount() {
        return damageAmount;
    }

    public void setDamageAmount(int damageAmount) {
        this.damageAmount = damageAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(int healAmount) {
        this.healAmount = healAmount;
    }

    public ArrayList<String> getEligibleTargetTeams() {
        return eligibleTargetTeams;
    }

    public void setEligibleTargetTeams(ArrayList<String> eligibleTargetTeams) {
        this.eligibleTargetTeams = eligibleTargetTeams;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSingleUse() {
        return singleUse;
    }

    public void setSingleUse(boolean singleUse) {
        this.singleUse = singleUse;
    }

    public boolean isEquipment() {
        return equipment;
    }

    public void setEquipment(boolean equipment) {
        this.equipment = equipment;
    }

    public boolean isRevealIdentity() {
        return revealIdentity;
    }

    public void setRevealIdentity(boolean revealIdentity) {
        this.revealIdentity = revealIdentity;
    }

    public boolean isFullHeal() {
        return fullHeal;
    }

    public void setFullHeal(boolean fullHeal) {
        this.fullHeal = fullHeal;
    }

    public boolean isExtraTurn() {
        return extraTurn;
    }

    public void setExtraTurn(boolean extraTurn) {
        this.extraTurn = extraTurn;
    }
}
