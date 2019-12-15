package fool.gameLogic;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    ArrayList<Card> playerHand;
    private int playerID;
    private String playerName;
    private boolean isHuman;
    private boolean activityStatus;
    private boolean offenceStatus;

    public Player() {
        this.activityStatus = false;
        this.playerHand = new ArrayList<>();
        this.offenceStatus = false;
        playerName = "NPC";
        this.isHuman = false;
        this.playerID = 0;
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(ArrayList<Card> playerHand) {
        this.playerHand = playerHand;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isHuman() {
        return this.isHuman;
    }

    public void setIsHuman(boolean human) {
        this.isHuman = human;
    }

    public boolean isActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(boolean activityStatus) {
        this.activityStatus = activityStatus;
    }

    public boolean isOffenceStatus() {
        return offenceStatus;
    }

    public void setOffenceStatus(boolean offence) {
        this.offenceStatus = offence;
    }

    public void setPlayerID(){
        Random random = new Random();
        playerID = ((random.nextInt() * 10000) % 1000);
    }

    public int getPlayerID(Player player){
        return player.playerID;
    }
}