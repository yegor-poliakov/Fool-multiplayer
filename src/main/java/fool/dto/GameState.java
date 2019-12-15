package fool.dto;

import fool.gameLogic.*;
import java.util.ArrayList;

public class GameState {
    private final GameStage stage;
    private final long deckID;
    private final long playerID;
    private final PlayerDTO[] players;
    private final int playerNumber;
    private final String[] playerCards;
    private final String[] table;
    private final String trumpCard;
    private final int remainingCards;
    private final String trump;

    public GameState(long deckID, int playerID, GameStage gameStage, Deck deck) throws Exception {
        this.stage = gameStage;
        this.deckID = deckID;
        this.playerID = playerID;
        this.players = new PlayerDTO[deck.players.length];
        for(int i = 0; i < players.length; i++){
           players[i] = new PlayerDTO(deck.players[i]);
        }
        this.playerNumber = findPlayer(playerID, deck.players);
        this.playerCards = handToString(deck.players[playerNumber].getPlayerHand());
        if (deck.deckOfCards.size() > 0){
            this.trumpCard = cardToString(deck.deckOfCards.get(deck.deckOfCards.size() - 1));
        } else {
            this.trumpCard = null;
        }
        this.trump = deck.trump;
        this.remainingCards = deck.deckOfCards.size();
        this.table = handToString(deck.table);
    }

    public String[] handToString(ArrayList<Card> hand){
        String[] stringHand = new String[hand.size()];
        for(int i = 0; i < hand.size(); i++){
            stringHand[i] = cardToString(hand.get(i));
        }
        return stringHand;
    }

    public String cardToString(Card card){
        String cardString;
        switch (card.getSuit()) {
                case Spades:
                    cardString = "S";
                    break;
                case Diamonds:
                    cardString = "D";
                    break;
                case Hearts:
                    cardString = "H";
                    break;
                default:
                    cardString = "C";
                    break;
            }
            if (card.getRank() < 5){
                cardString = cardString + (card.getRank() + 6);
            } else {
                switch (card.getRank()){
                    case 5:
                        cardString = cardString + "J";
                        break;
                    case 6:
                        cardString = cardString + "Q";
                        break;
                    case 7:
                        cardString = cardString + "K";
                        break;
                    default:
                        cardString = cardString + "A";
                        break;
                }
            }
        return cardString;
    }

    public long getDeckID() {
        return deckID;
    }

    public GameStage getStage() {
        return stage;
    }

    public int findPlayer(int playerID, Player[] players) throws Exception {
        for (int i = 0; i < players.length; i++){
            if (playerID == players[i].getPlayerID(players[i])){
                return i;
            }
        }
        throw new Exception("Player ID not found");
    }

    public String[] getTable() {
        return table;
    }

    public String getTrumpCard() {
        return trumpCard;
    }

    public int getRemainingCards() {
        return remainingCards;
    }

    public String getTrump() {
        return trump;
    }

    public long getPlayerID() {
        return playerID;
    }

    public String[] getPlayerCards() {
        return playerCards;
    }
}