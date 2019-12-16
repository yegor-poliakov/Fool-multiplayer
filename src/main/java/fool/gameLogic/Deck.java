package fool.gameLogic;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    public ArrayList<Card> talon;
    public ArrayList<Card> table = new ArrayList<Card>();
    public Player[] players;
    public String trump;
    public String statusString = "Waiting for players to join";

    public void setStatusString(String status) {
        this.statusString = status;
    }

    /* set the initial state of the game, initialize AI attack in case the first player is AI */
    public Deck(int numberOfPlayers, int numberOfCards, String playerName) throws Exception {
        talon = createDeck(numberOfCards);
        trump = trumpToString(talon.get(numberOfCards - 1).getSuit());
        players = createPlayers(numberOfPlayers);
        distributeCards();
        players[0].setPlayerID();
        players[0].setPlayerName(playerName);
        readyPlayerOne();
        if (!players[findActiveAttacker()].isHuman()) {
            aiAttack();
        }
        if (numberOfPlayers == 1) {
            statusUpdate();
        }
    }

    /* create a deck of cards, shuffle it, update trump cards*/
    public static ArrayList<Card> createDeck(int numberOfCards) throws Exception {
        ArrayList<Card> deckOfCards = new ArrayList<Card>();
        if (numberOfCards != 36 || numberOfCards != 52) {
            throw new Exception("Number of cards is neither 36, nor 52");
        }

        for (int i = 0; i < numberOfCards; i++) {
            Card card = new Card();
            switch (i % 4) {
                case 0:
                    card.setRank(i / 4);
                    card.setSuit(Suit.Spades);
                    deckOfCards.add(card);
                    break;
                case 1:
                    card.setRank(i / 4);
                    card.setSuit(Suit.Diamonds);
                    deckOfCards.add(card);
                    break;
                case 2:
                    card.setRank(i / 4);
                    card.setSuit(Suit.Hearts);
                    deckOfCards.add(card);
                    break;
                case 3:
                    card.setRank(i / 4);
                    card.setSuit(Suit.Clubs);
                    deckOfCards.add(card);
                    break;
            }
        }
        Collections.shuffle(deckOfCards);

        /* assign trump status to trump cards */
        for (Card card : deckOfCards) {
            if (deckOfCards.get(numberOfCards - 1).getSuit() == card.getSuit()) {
                card.setTrump(true);
            }
        }

        return deckOfCards;
    }

    /* give the trump String to display when there is no trump card to display */
    public static String trumpToString(Suit suit) {
        switch (suit) {
            case Spades:
                return "Spades";
            case Diamonds:
                return "Diamonds";
            case Hearts:
                return "Hearts";
            default:
                return "Clubs";
        }
    }

    /* create players with empty hands, if player wants to play alone, include AI */
    public static Player[] createPlayers(int numberOfPlayers) {
        int numberOfAis = 0;
        if (numberOfPlayers == 1) {
            numberOfAis = 1;
        }

        Player[] players = new Player[numberOfPlayers + numberOfAis];

        for (int i = 0; i < numberOfPlayers; i++) {
            String name = players[i].getPlayerName() + i;
            Player player = new Player();
            player.setPlayerName(name);
            players[i] = player;
        }

        if (numberOfAis == 1) {
            Player player = new Player();
            player.setIsHuman(false);
            players[numberOfPlayers] = player;
            player.setPlayerName("Skynet");
        }

        return players;
    }

    /* distribute cards from deck to players */
    public void distributeCards() {
        ArrayList<Card> hand = new ArrayList<>();
        for (int i = 0; i < players.length; i++) {
            hand.clear();
            for (int j = 0; j < 6; j++) {
                Card card = talon.remove(0);
                hand.add(card);
            }
            players[i].setPlayerHand(hand);
        }
    }

    /* compare all player hands to find the lowest trump or the highest plain card */
    public void readyPlayerOne() throws Exception {
        int lowestRankTrump = -1;
        int highestRankPlain = -1;
        int playerTrumpIndex = 0;
        int playerPlainIndex = 0;

        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < 6; j++) {
                Card card = players[i].getPlayerHand().get(j);
                if (card.isTrump() && card.getRank() < lowestRankTrump) {
                    lowestRankTrump = card.getRank();
                    playerTrumpIndex = i;
                }
                if (!card.isTrump() && card.getRank() > highestRankPlain) {
                    highestRankPlain = card.getRank();
                    playerPlainIndex = i;
                }
            }
        }

        if (lowestRankTrump >= 0) {
            players[playerTrumpIndex].setActivityStatus(true);
            players[(playerTrumpIndex + 1) % players.length].setOffenceStatus(false);
        } else if (highestRankPlain >= 0) {
            players[playerPlainIndex].setActivityStatus(true);
            players[(playerPlainIndex + 1) % players.length].setOffenceStatus(false);
        } else {
            throw new Exception("Unable to figure out the first player");
        }
    }

    /* pick a card move */
    public Stage move(int playerNumber, int cardNumber) throws Exception {
        if (players[playerNumber].isOffenceStatus()) {
            return attack(playerNumber, cardNumber);
        } else {
            return defend(playerNumber, cardNumber);
        }
    }

    /* pick a status change move */
    public Stage move(int playerNumber) throws Exception {
        if (players[playerNumber].isOffenceStatus()) {
            return pass(playerNumber);
        } else {
            return takeAll(playerNumber);
        }
    }

    /* attack if feasible */
    public Stage attack(int playerNumber, int cardNumber) throws Exception {
        boolean attackSuccess = false;
        if (table.size() == 0) {
            attackSuccess = true;
        } else {
            for (int i = 0; i < table.size(); i++) {
                if (players[playerNumber].getPlayerHand().get(cardNumber).getRank() == table.get(i).getRank()) {
                    attackSuccess = true;
                    break;
                }
            }
        }

        if (attackSuccess) {
            table.add(players[playerNumber].removePlayerCard(cardNumber));
            players[playerNumber].setActivityStatus(false);
            players[findDefender()].setActivityStatus(true);
            statusUpdate();
            if (endGame() == Stage.End) {
                return Stage.End;
            }
            if (!players[findDefender()].isHuman()) {
                aiDefence();
            }
        }

        return Stage.Continue;
    }

    /* defend if feasible */
    public Stage defend(int playerNumber, int cardNumber) throws Exception {
        boolean defenceSuccess = false;
        Card cardAttacking = table.get(table.size() - 1);
        Card cardDefending = players[playerNumber].getPlayerHand().get(cardNumber);
        if ((cardAttacking.getSuit() == cardDefending.getSuit()
                && cardAttacking.getRank() < cardDefending.getRank()) ||
                (cardAttacking.getSuit() != cardDefending.getSuit() && cardDefending.isTrump())) {
            defenceSuccess = true;
        }

        if (defenceSuccess) {
            table.add(players[playerNumber].removePlayerCard(cardNumber));
            if (table.size() == 12 || players[playerNumber].getPlayerHand().size() == 0) {
                nextRound();
            } else {
                nextTurn();
            }
        }

        return Stage.Continue;
    }

    /* switch to the next attacker or end the round if no one wants or is able to attack */
    public Stage pass(int playerNumber) throws Exception {
        int checkRange = ((findActiveAttacker() + players.length - playerNumber) % 4);
        for (int i = playerNumber + 1; i < playerNumber + checkRange; i++) {
            if (i != findDefender() && i != (findDefender() + players.length)
                    && pickAttackCard(players[i % players.length].getPlayerHand()) != -1) {
                players[findActiveAttacker()].setActivityStatus(false);
                players[i % players.length].setActivityStatus(true);
                statusUpdate();
                if (!players[findActiveAttacker()].isHuman()) {
                    aiAttack();
                }
                return Stage.Continue;
            }
        }
        nextRound();
        return Stage.Continue;
    }

    /* move all cards from the table to defender's hand and switch to the next offender */
    public Stage takeAll(int playerNumber) throws Exception {
        for (int i = 0; i < table.size(); i++) {
            players[playerNumber].givePlayerCard(table.get(i));
        }
        activateNextOffender();
        nextRound();
        return Stage.Continue;
    }

    /* continue round with the next attack */
    public void nextTurn() throws Exception {
        int primaryOffender = (players.length - 1 - findDefender()) % players.length;
        int fullTurn = primaryOffender + players.length - 1;
        for (int i = primaryOffender; i < fullTurn; i++) {
            if (i != findDefender() && i != (findDefender() + players.length)
                    && pickAttackCard(players[i % players.length].getPlayerHand()) != -1) {
                players[findActiveAttacker()].setActivityStatus(false);
                players[i % players.length].setActivityStatus(true);
                statusUpdate();
                if (!players[findActiveAttacker()].isHuman()) {
                    aiAttack();
                }
                return;
            }
        }
        nextRound();
    }

    /* clear the table, replenish hands, switch to the next primary attacker */
    public void nextRound() throws Exception {
        table.clear();
        replenish();
        activateNextOffender();
        statusUpdate();
        if (!players[findActiveAttacker()].isHuman()) {
            aiAttack();
        }
    }

    /* produce AI defence move */
    public Stage aiDefence() throws Exception {
        boolean effectiveDefence = false;
        delay();
        Card attackingCard = table.get(table.size() - 1);
        if (pickDefenceCard(players[findDefender()].getPlayerHand(), attackingCard) != -1) {
            effectiveDefence = true;
            int defCardIndex = pickDefenceCard(players[findDefender()].getPlayerHand(), attackingCard);
            defend(findDefender(), defCardIndex);
        }

        if (!effectiveDefence) {
            takeAll(findDefender());
        }

        return Stage.Continue;
    }

    /* produce a response if AI has to attack */
    public Stage aiAttack() throws Exception {
        boolean effectiveAttack = false;
        delay();
        if (pickAttackCard(players[findActiveAttacker()].getPlayerHand()) != -1) {
            effectiveAttack = true;
            int attCardIndex = pickAttackCard(players[findDefender()].getPlayerHand());
            attack(findActiveAttacker(), attCardIndex);
        }

        if (!effectiveAttack) {
            pass(findActiveAttacker());
        }

        return Stage.Continue;
    }

    /* add necessary cards to players' hands after the round if the deck is not empty */
    public void replenish() throws Exception {
        int playerNumber = (players.length - 1 - findDefender()) % players.length;
        for (int i = playerNumber; i < players.length - 1; i++) {
            if (i != findDefender() && i != findDefender() + players.length) {
                int cardsToReplenish = 6 - players[i].getPlayerHand().size();
                for (int j = 0; j <= cardsToReplenish; j++) {
                    if (talon.size() != 0) {
                        players[i].getPlayerHand().add(talon.remove(0));
                    } else {
                        break;
                    }
                }
            }
        }

        int cardsToReplenish = 6 - players[findDefender()].getPlayerHand().size();
        for (int j = 0; j < cardsToReplenish; j++) {
            if (talon.size() != 0) {
                players[findDefender()].getPlayerHand().add(talon.remove(0));
            } else {
                break;
            }
        }
    }

    public void activateNextOffender() throws Exception {
        int currentDefender = findDefender();
        players[(currentDefender + 1) % players.length].setOffenceStatus(false);
        players[currentDefender].setOffenceStatus(true);
        players[currentDefender].setActivityStatus(true);
    }

    /* pick the lowest card from a hand to beat a given card (for AI defend move)
       returns -1 if no card present, returns the card index if found */
    public int pickDefenceCard(ArrayList<Card> hand, Card actionCard) {
        int lowestDefenceCard = -1;
        CardComparator cardComparator = new CardComparator();
        for (int i = 0; i < hand.size(); i++) {
            if ((cardComparator.compare(hand.get(i), actionCard) == 1)) {
                lowestDefenceCard = i;
                break;
            }
        }

        if (lowestDefenceCard != -1) {
            for (int i = 0; i < hand.size(); i++) {
                if ((cardComparator.compare(hand.get(i), actionCard) == 1)) {
                    if (cardComparator.compare(hand.get(i), hand.get(lowestDefenceCard)) == -1) {
                        lowestDefenceCard = i;
                    }
                }
            }
        }

        return lowestDefenceCard;
    }

    /* pick the lowest card from a hand to attack to check if attack move is possible for a player
       returns -1 if no attack possible, returns the card index if there is at least one attack card */
    public int pickAttackCard(ArrayList<Card> hand) {
        int lowestAttackCard = -1;
        CardComparator cardComparator = new CardComparator();

        if (table.size() == 0) {
            lowestAttackCard = 0;
            for (int i = 0; i < hand.size(); i++) {
                if (cardComparator.compare(hand.get(i), hand.get(lowestAttackCard)) == -1) {
                    lowestAttackCard = i;
                }
            }
        } else {
            for (int i = 0; i < hand.size(); i++) {
                for (int j = 0; j < table.size(); j++) {
                    if (hand.get(i).getRank() == table.get(j).getRank()) {
                        lowestAttackCard = i;
                        break;
                    }
                }
            }

            if (lowestAttackCard != -1) {
                for (int i = 0; i < hand.size(); i++) {
                    for (int j = 0; j < table.size(); j++) {
                        if (hand.get(i).getRank() == table.get(j).getRank()) {
                            if (cardComparator.compare(hand.get(i), hand.get(lowestAttackCard)) == -1) {
                                lowestAttackCard = i;
                            }
                        }
                    }
                }
            }
        }

        return lowestAttackCard;
    }

    /* find index of a defending player */
    public int findDefender() throws Exception {
        for (int i = 0; i < players.length; i++) {
            if (!players[i].isOffenceStatus()) {
                return i;
            }
        }
        throw new Exception("Missing defender");
    }

    /* find index of an attacking player */
    public int findActiveAttacker() throws Exception {
        for (int i = 0; i < players.length; i++) {
            if (players[i].isOffenceStatus() && players[i].isActivityStatus()) {
                return i;
            }
        }
        throw new Exception("Missing active attacker");
    }

    /* check if the game continues */
    public Stage endGame() {
        if (talon.size() == 0) {
            int j = players.length;
            for (int i = 0; i < players.length; i++) {
                if (players[i].getPlayerHand().size() == 0) {
                    j -= 1;
                }
            }
            if (j <= 1) {
                return Stage.End;
            }
        }
        return Stage.Continue;
    }

    /* update status string to be displayed */
    public void statusUpdate() throws Exception {
        for (int i = 0; i < players.length; i++) {
            if (players[i].isActivityStatus()) {
                if (players[i].isOffenceStatus()) {
                    statusString = players[i].getPlayerName() + "attacks"
                            + players[findDefender()].getPlayerName();
                } else {
                    statusString = players[i].getPlayerName() + "defends";
                }
            }
        }
    }

    /* delay for AI moves*/
    public void delay() throws InterruptedException {
        Thread.sleep(2000);
    }

}