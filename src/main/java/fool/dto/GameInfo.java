package fool.dto;

public class GameInfo {
    private long deckID;
    private String creatorName;
    private int currentPlayersNumber;
    private int totalPlayersNumber;

    public long getDeckID() {
        return deckID;
    }

    public void setDeckID(long deckID) {
        this.deckID = deckID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getCurrentPlayersNumber() {
        return currentPlayersNumber;
    }

    public void setCurrentPlayersNumber(int currentPlayersNumber) {
        this.currentPlayersNumber = currentPlayersNumber;
    }

    public int getTotalPlayersNumber() {
        return totalPlayersNumber;
    }

    public void setTotalPlayersNumber(int totalPlayersNumber) {
        this.totalPlayersNumber = totalPlayersNumber;
    }
}
