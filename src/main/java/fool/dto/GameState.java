package fool.dto;

import fool.gameLogic.*;
import java.util.ArrayList;

public class GameState {
    private final GameStage stage;
    private final long deckID;
    private final int firstPlayerNumber;
    private final int secondPlayerNumber;
    private final boolean firstPlayerActive;
    private final boolean secondPlayerActive;
    private final boolean firstPlayerOffender;
    private final boolean secondPlayerOffender;
    private final String[] firstPlayerCards;
    private final String[] secondPlayerCards;
    private final String[] table;
    private final String trumpCard;
    private final int remainingCards;
    private final String trump;

    public GameState(long deckID, GameStage gameStage, Deck deck){
        this.stage = gameStage;
        this.deckID = deckID;
        this.firstPlayerNumber = 0;
        this.secondPlayerNumber = 1;
        this.firstPlayerActive = deck.firstPlayer.isActivityStatus();
        this.secondPlayerActive = deck.secondPlayer.isActivityStatus();
        this.firstPlayerOffender = deck.firstPlayer.isOffence();
        this.secondPlayerOffender = deck.secondPlayer.isOffence();
        this.firstPlayerCards = handToString(deck.firstPlayer.getPlayerHand());
        this.secondPlayerCards = handToString(deck.secondPlayer.getPlayerHand());
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

    public int getFirstPlayerNumber() {
        return firstPlayerNumber;
    }

    public int getSecondPlayerNumber() {
        return secondPlayerNumber;
    }

    public String[] getFirstPlayerCards() {
        return firstPlayerCards;
    }

    public String[] getSecondPlayerCards() {
        return secondPlayerCards;
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

    public boolean isFirstPlayerActive() {
        return firstPlayerActive;
    }

    public boolean isSecondPlayerActive() {
        return secondPlayerActive;
    }

    public boolean isFirstPlayerOffender() {
        return firstPlayerOffender;
    }

    public boolean isSecondPlayerOffender() {
        return secondPlayerOffender;
    }

    public String getTrump() {
        return trump;
    }
}