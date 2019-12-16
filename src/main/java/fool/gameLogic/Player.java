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
        this.activityStatus = false;
        this.playerHand = new ArrayList<>(6);
        this.offenceStatus = true;
        this.playerName = "player";
        this.isHuman = false;
        this.playerID = 0;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID() {
        Random random = new Random();
        playerID = ((random.nextInt() * 10000) % 1000);
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

    public Card removePlayerCard(int index){
        return playerHand.remove(index);
    }

    public void givePlayerCard(Card card){
        playerHand.add(card);
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