package fool.dto;

import fool.gameLogic.Player;

public class PlayerDTO {
    private int numberOfCards;
    private String playerName;

    PlayerDTO (Player player){
        numberOfCards = player.getPlayerHand().size();
        playerName = player.getPlayerName();
    }
}