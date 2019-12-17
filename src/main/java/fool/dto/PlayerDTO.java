package fool.dto;

import fool.gameLogic.Player;

public class PlayerDTO {
    private int numberOfCards;
    private String playerName;

    PlayerDTO(Player player) {
        this.numberOfCards = player.getPlayerHand().size();
        this.playerName = player.getPlayerName();
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}