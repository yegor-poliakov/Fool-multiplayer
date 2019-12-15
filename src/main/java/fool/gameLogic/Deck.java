package fool.gameLogic;

import java.util.ArrayList;

public class Deck {
    public ArrayList<Card> deckOfCards;
    public ArrayList<Card> table = new ArrayList<Card>();
    public Player[] players;
    public String trump;

    /* set the initial state of the game */
    public Deck(int numberOfPlayers, int numberOfCards) throws Exception {
        deckOfCards = createDeck(numberOfCards);
        trump = trumpToString(deckOfCards.get(numberOfCards - 1).suit);
        players = createPlayers(numberOfPlayers);
        distributeCards();
        readyPlayerOne(players);
    }

    /* create a deck of cards, shuffle it, update trump cards*/
    public static ArrayList<Card> createDeck(int numberOfCards) throws Exception {
        ArrayList<Card> deckOfCards = new ArrayList<Card>();
        if (numberOfCards != 36 || numberOfCards != 52) {
            throw new Exception("Number of cards is neither 36, nor 52");
        }

        for (int i = 0; i < numberOfCards; i++) {
            Card card = new Card(-1, Suit.Spades);
            switch (i % 4) {
                case 0:
                    card.rank = i / 4;
                    card.suit = Suit.Spades;
                    deckOfCards.add(card);
                    break;
                case 1:
                    card.rank = i / 4;
                    card.suit = Suit.Diamonds;
                    deckOfCards.add(card);
                    break;
                case 2:
                    card.rank = i / 4;
                    card.suit = Suit.Hearts;
                    deckOfCards.add(card);
                    break;
                case 3:
                    card.rank = i / 4;
                    card.suit = Suit.Clubs;
                    deckOfCards.add(card);
                    break;
            }
        }
        Collections.shuffle(deckOfCards);

        /* make trump cards' status "trump"*/
        for (Card card : deckOfCards) {
            if (deckOfCards.get(numberOfCards - 1).suit == card.suit) {
                card.isTrump = true;
            }
        }

        return deckOfCards;
    }

    /* set the trump String to display if there is no trump card in place */
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

    /* create players with empty hands, include AI if there is only one player*/
    public static Player[] createPlayers(int numberOfPlayers) throws Exception {
        if (numberOfPlayers < 1) {
            throw new Exception("Number of players is less than 1");
        }

        int numberOfAis = 0;
        if (numberOfPlayers == 1) {
            numberOfAis = 1;
        }

        Player[] players = new Player[numberOfPlayers + numberOfAis];

        for (int i = 0; i < numberOfPlayers; i++) {
            String name = "player" + i;
            Player player = new Player();
            player.setPlayerName(name);
            player.setIsHuman(true);
            player.setPlayerID();
            players[i] = player;
        }

        if (numberOfAis == 1) {
            Player player = new Player();
            player.setIsHuman(false);
            players[numberOfPlayers] = player;
        }

        return players;
    }

    /* distribute cards from deck between players */
    public void distributeCards() {
        ArrayList<Card> hand = new ArrayList<>();
        for (int i = 0; i < players.length; i++) {
            hand.clear();
            for (int j = 0; j < 6; j++) {
                Card card = deckOfCards.remove(0);
                hand.add(card);
            }
            players[i].setPlayerHand(hand);
        }
    }

    /* compare all player hands to find the lowest trump or the highest regular card*/
    public void readyPlayerOne(Player[] players) throws Exception {
        int lowestRankTrump = -1;
        int highestRankRegular = -1;
        int playerTrumpIndex = 0;
        int playerRegularIndex = 0;

        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < players[i].getPlayerHand().size(); j++) {
                Card card = players[i].getPlayerHand().get(j);
                if (card.isTrump && card.getRank() < lowestRankTrump) {
                    lowestRankTrump = card.getRank();
                    playerTrumpIndex = i;
                }
                if (!card.isTrump && card.getRank() > highestRankRegular) {
                    highestRankRegular = card.getRank();
                    playerRegularIndex = i;
                }
            }
        }

        if (lowestRankTrump >= 0) {
            players[playerTrumpIndex].setActivityStatus(true);
            players[playerTrumpIndex].setOffenceStatus(true);
        } else if (highestRankRegular >= 0) {
            players[playerRegularIndex].setActivityStatus(true);
            players[playerRegularIndex].setOffenceStatus(true);
        } else {
            throw new Exception("Unable to figure out the first player");
        }

        /* initialize AI attack in case the first player is AI */
        if (!players[findActiveOffender(players)].isHuman()){
            int iniCardIndex = pickAttackCard(players[findActiveOffender(players)].getPlayerHand());
            attack(players[findActiveOffender(players)], players[findActiveOffender(players)].playerHand.get(iniCardIndex));
            return;
        }
    }

    /* react to client input */
    public Stage move(int playerNumber, int cardNumber) {
        if (players[playerNumber].isOffenceStatus()) {
            return attack(players[playerNumber], players[playerNumber].playerHand.get(cardNumber));
        } else {
            return defend(players[playerNumber], players[playerNumber].playerHand.get(cardNumber));
        }
    }

    public Stage move(int playerNumber) {
        if (players[playerNumber].isOffenceStatus()) {
            return retreat(players[playerNumber]);
        } else {
            return giveUp(players[playerNumber]);
        }
    }

    public Stage attack(Player player, Card card) {
        boolean aiResponse = false;
        boolean attackSuccess = false;
        if () {
            aiResponse = true;
        }

        ArrayList<Card> hand1 = firstPlayer.playerHand;
        ArrayList<Card> hand2 = secondPlayer.playerHand;

        if ((hand1.size() == 0) || (hand2.size() == 0)) {
            return Stage.Continue;
        } else {
            if (table.size() == 0) {
                table.add(card);
                player.playerHand.remove(card);
                switchPlayers();
                attackSuccess = true;
            } else {
                if ((table.size() % 2 == 0)) {
                    for (int i = 0; i < table.size(); i++) {
                        if (card.rank == table.get(i).rank) {
                            table.add(card);
                            player.playerHand.remove(card);
                            switchPlayers();
                            attackSuccess = true;
                            break;
                        }
                    }
                } else {
                    return Stage.Continue;
                }
            }
        }

        if (aiResponse && attackSuccess) {
            boolean effectiveDefence = false;
            if (secondPlayer.playerHand.size() > 0) {
                if (pickDefenceCard(secondPlayer.playerHand, card) != -1) {
                    effectiveDefence = true;
                    int defCardIndex = pickDefenceCard(secondPlayer.playerHand, card);
                    defend(secondPlayer, secondPlayer.playerHand.get(defCardIndex));
                }
            }

            if (!effectiveDefence) {
                giveUp(secondPlayer);
            }
        }

        return Stage.Continue;
    }

    public Stage defend(Player player, Card card) {
        boolean aiResponse = false;
        if (player == firstPlayer) {
            aiResponse = true;
        }

        Card cardAttacking = new Card();
        if (table.size() % 2 == 1) {
            cardAttacking = table.get(table.size() - 1);
        }

        if ((cardAttacking.suit == card.suit && cardAttacking.rank < card.rank) ||
                (cardAttacking.suit != card.suit && card.isTrump)) {
            table.add(card);
            player.playerHand.remove(card);
            switchPlayers();
        }

        if (aiResponse) {
            boolean effectiveAttack = false;
            if (secondPlayer.playerHand.size() > 0) {
                if (pickAttackCard(secondPlayer.playerHand) != -1) {
                    effectiveAttack = true;
                    int attCardIndex = pickAttackCard(secondPlayer.playerHand);
                    attack(secondPlayer, secondPlayer.playerHand.get(attCardIndex));
                }
            }

            if (!effectiveAttack) {
                retreat(secondPlayer);
            }
        }

        return Stage.Continue;
    }

    public Stage retreat(Player player) {
        boolean aiResponse = false;
        if (player == firstPlayer) {
            aiResponse = true;
        }

        if (table.size() % 2 == 0) {
            table.clear();
            switchOffender();
            switchPlayers();
            replenish();
        }

        if (endGame() != Stage.Continue) {
            return endGame();
        }

        if (aiResponse) {
            if (secondPlayer.playerHand.size() > 0) {
                int attCardIndex = pickAttackCard(secondPlayer.playerHand);
                attack(secondPlayer, secondPlayer.playerHand.get(attCardIndex));
            }
        }

        return endGame();
    }

    public Stage giveUp(Player player) {
        boolean aiResponse = false;
        if (player == firstPlayer) {
            aiResponse = true;
        }

        if (table.size() % 2 == 1) {
            player.playerHand.addAll(table);
            table.clear();
            switchPlayers();
            replenish();
        }

        if (endGame() != Stage.Continue) {
            return endGame();
        }

        if (aiResponse) {
            if (secondPlayer.playerHand.size() > 0) {
                int attCardIndex = pickAttackCard(secondPlayer.playerHand);
                attack(secondPlayer, secondPlayer.playerHand.get(attCardIndex));
            }
        }

        return endGame();
    }

    /* add necessary cards to player hands after the round if the deck is not empty
        the parameter playerNumber has to be an index of either primary offender or defender
    */
    public Stage replenish(int playerNumber) {
        if (!players[playerNumber].isOffenceStatus()) {
            playerNumber = playerNumber % (playerNumber + players.length);
        }

        for (int i = playerNumber; i < deckOfCards.size() + playerNumber; i++) {
            if (deckOfCards.size() != 0) {
                if (players[i].getPlayerHand().size() < 6) {
                    players[i % players.length].getPlayerHand().add(deckOfCards.remove(0));
                }
            } else {
                break;
            }
        }

        return Stage.Continue;
    }

    public void switchPlayers(int playerNumber) {
        playerNumber = playerNumber % (playerNumber + players.length);
        secondPlayer.activityStatus = !(secondPlayer.activityStatus);
    }

    public Stage endGame() {
        if (deckOfCards.size() == 0) {
            if (firstPlayer.playerHand.size() == 0 && secondPlayer.playerHand.size() == 0) {
                return Stage.Draw;
            }

            if (firstPlayer.playerHand.size() == 0 && secondPlayer.playerHand.size() > 0) {
                return Stage.Victory;
            }

            if (firstPlayer.playerHand.size() > 0 && secondPlayer.playerHand.size() == 0) {
                return Stage.Loss;
            }
        }
        return Stage.Continue;
    }

    public void switchOffender() {
        firstPlayer.offence = !(firstPlayer.offence);
        secondPlayer.offence = !(secondPlayer.offence);

    }

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
                    if (hand.get(i).rank == table.get(j).rank) {
                        lowestAttackCard = i;
                        break;
                    }
                }
            }

            if (lowestAttackCard != -1) {
                for (int i = 0; i < hand.size(); i++) {
                    for (int j = 0; j < table.size(); j++) {
                        if (hand.get(i).rank == table.get(j).rank) {
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

    public static int findDefender(Player[] players) throws Exception {
        for (int i = 0; i < players.length; i++) {
            if (!players[i].isOffenceStatus()) {
                return i;
            }
        }
        throw new Exception("Missing defender");
    }

    public static int findActiveOffender(Player[] players) throws Exception {
        for (int i = 0; i < players.length; i++) {
            if (players[i].isOffenceStatus() && players[i].isActivityStatus()) {
                return i;
            }
        }
        throw new Exception("Missing active offender");
    }
}