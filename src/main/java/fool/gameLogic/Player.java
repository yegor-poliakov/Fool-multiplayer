package fool.gameLogic;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private int playerID;
    private String playerName;
    private ArrayList<Card> playerHand;
    private boolean isHuman;
    private boolean activityStatus;
    private boolean offenceStatus;

    public Player() {
        this.playerID = 0;
        this.playerName = "player";
        this.playerHand = new ArrayList<>();
        this.isHuman = true;
        this.activityStatus = false;
        this.offenceStatus = true;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(ArrayList<Card> playerHand) {
        this.playerHand = playerHand;
    }

    public Card removePlayerCard(int index) {
        return this.playerHand.remove(index);
    }

    public void givePlayerCard(Card card) {
        this.playerHand.add(card);
    }

    public boolean isHuman() {
        return this.isHuman;
    }

    public void setIsHuman(boolean human) {
        this.isHuman = human;
    }

    public boolean isOffenceStatus() {
        return offenceStatus;
    }

    public void setOffenceStatus(boolean offence) {
        this.offenceStatus = offence;
    }

    public boolean isActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(boolean activityStatus) {
        this.activityStatus = activityStatus;
    }

}