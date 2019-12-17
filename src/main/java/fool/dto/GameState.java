package fool.dto;

import java.util.ArrayList;

import fool.gameLogic.*;

public class GameState {
    private final long deckID;
    private final GameStage stage;
    private final String statusText;
    private final String trumpCard;
    private final String trump;
    private final int remainingCards;
    private final int playerID;
    private final int playerNumber;
    private final String playerStatus;
    private final String[] playerCards;
    private final PlayerDTO[] players;
    private final String[] table;
    private final int deckSize;

    public GameState(long deckID, int playerID, GameStage gameStage, Deck deck) throws Exception {
        this.deckID = deckID;
        this.stage = gameStage;
        this.statusText = deck.statusString;
        this.trumpCard = calculateTrumpCard(deck);
        this.trump = deck.trump;
        this.remainingCards = deck.talon.size();
        this.playerID = playerID;
        this.playerNumber = findPlayer(playerID, deck.players);
        this.playerStatus = calculatePlayerStatus(playerNumber, deck);
        this.playerCards = handToString(deck.players[playerNumber].getPlayerHand());
        this.players = calculatePlayersDTO(deck);
        this.table = handToString(deck.table);
        this.deckSize = deck.deckSize;
    }

    public String calculateTrumpCard(Deck deck) {
        String trumpCard;
        if (deck.talon.size() > 0) {
            trumpCard = cardToString(deck.talon.get(deck.talon.size() - 1));
        } else {
            trumpCard = null;
        }
        return trumpCard;
    }

    public int findPlayer(int playerID, Player[] players) throws Exception {
        for (int i = 0; i < players.length; i++) {
            if (playerID == players[i].getPlayerID()) {
                return i;
            }
        }
        throw new Exception("Player ID not found");
    }

    public String calculatePlayerStatus(int playerNumber, Deck deck) {
        String status = "";
        if (deck.players[playerNumber].isActivityStatus()) {
            status += "Active";
        } else {
            status += "Passive";
        }
        if (deck.players[playerNumber].isOffenceStatus()) {
            status += "Attacker";
        } else {
            status += "Defendant";
        }
        return status;
    }

    public String[] handToString(ArrayList<Card> hand) {
        String[] stringHand = new String[hand.size()];
        for (int i = 0; i < hand.size(); i++) {
            stringHand[i] = cardToString(hand.get(i));
        }
        return stringHand;
    }

    public String cardToString(Card card) {
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

        if (deckSize == 36) {
            if (card.getRank() < 5) {
                cardString = cardString + (card.getRank() + 6);
            } else {
                switch (card.getRank()) {
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
        }

        if (deckSize == 52) {
            if (card.getRank() < 9) {
                cardString = cardString + (card.getRank() + 2);
            } else {
                switch (card.getRank()) {
                    case 9:
                        cardString = cardString + "J";
                        break;
                    case 10:
                        cardString = cardString + "Q";
                        break;
                    case 11:
                        cardString = cardString + "K";
                        break;
                    default:
                        cardString = cardString + "A";
                        break;
                }
            }
        }
        return cardString;
    }

    public PlayerDTO[] calculatePlayersDTO(Deck deck) {
        PlayerDTO[] players = new PlayerDTO[deck.players.length];
        for (int i = 0; i < players.length; i++) {
            players[i] = new PlayerDTO(deck.players[i]);
        }
        return players;
    }

    public long getDeckID() {
        return deckID;
    }

    public GameStage getStage() {
        return stage;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getTrumpCard() {
        return trumpCard;
    }

    public String getTrump() {
        return trump;
    }

    public int getRemainingCards() {
        return remainingCards;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getPlayerNumber() { return playerNumber; }

    public String getPlayerStatus() {
        return playerStatus;
    }

    public String[] getPlayerCards() {
        return playerCards;
    }

    public PlayerDTO[] getPlayers() {
        return players;
    }

    public String[] getTable() {
        return table;
    }

}