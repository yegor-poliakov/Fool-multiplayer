package fool.gameLogic;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    public ArrayList<Card> deckOfCards;
    public ArrayList<Card> table = new ArrayList<Card>();
    public Player[] players;
    public String trump;

    /*  */
    public Deck(int numberOfPlayers, int numberOfCards) throws Exception {
        deckOfCards = createDeck(numberOfCards);
        trump = trumpToString(deckOfCards.get(numberOfCards - 1).suit);
        players = createPlayers(numberOfPlayers);

        ArrayList<Card> hand = new ArrayList<>();
        for (int i = 0; i < players.length; i++) {
            hand.clear();
            for (int j = 0; j < 6; j++) {
                Card card = deckOfCards.remove(0);
                hand.add(card);
            }
            players[i].setPlayerHand(hand);
        }

        readyPlayerOne(players);

        if (lowestTrump1 < lowestTrump2) {
            firstPlayer.activityStatus = true;
            firstPlayer.offence = true;
        } else if (highestRank1 > highestRank2) {
            firstPlayer.activityStatus = true;
            firstPlayer.offence = true;
        } else {
            secondPlayer.activityStatus = true;
            secondPlayer.offence = true;
        }

    }

    /* create a deck of cards, shuffle it, update trump cards*/
    public static ArrayList<Card> createDeck(int numberOfCards) throws Exception {
        ArrayList<Card> deckOfCards = new ArrayList<Card>();
        if (numberOfCards != 36 || numberOfCards != 52){
            throw new Exception("Number of cards is nor 36, nor 52");
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

        for (Card card : deckOfCards) {
            if (deckOfCards.get(numberOfCards - 1).suit == card.suit) {
                card.isTrump = true;
            }
        }

        return deckOfCards;
    }

    /* set the trump String to display if no trump card in place */
    public static String trumpToString(Suit suit){
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
        if (numberOfPlayers < 1){
            throw new Exception("Number of players is less than 1-");
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
            players[i] = player;
        }

        if (numberOfAis == 1) {
            Player player = new Player();
            player.setIsHuman(false);
            players[numberOfPlayers] = player;
        }

        return players;
    }

    /* Compare all player hands to find the lowest trump or the highest regular card*/
    public static void readyPlayerOne(Player[] players) throws Exception {
        int lowestRankTrump = -1;
        int highestRankRegular = -1;
        int playerIndex = 0;

        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < players[i].getPlayerHand().size(); j++) {
                Card card = players[i].getPlayerHand().get(j);
                if (card.isTrump && card.getRank() < lowestRankTrump) {
                    lowestRankTrump = card.getRank();
                    playerIndex = i;
                }
            }
        }

        if (lowestRankTrump >= 0) {
            players[playerIndex].setActivityStatus(true);
            players[playerIndex].setOffenceStatus(true);
            return;
        }

        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < players[i].getPlayerHand().size(); j++) {
                Card card = players[i].getPlayerHand().get(j);
                if (card.getRank() > highestRankRegular) {
                    highestRankRegular = card.getRank();
                    playerIndex = i;
                }
            }
        }

        if (highestRankRegular >= 0) {
            players[playerIndex].setActivityStatus(true);
            players[playerIndex].setOffenceStatus(true);
            return;
        } else {
            throw new Exception("Unable to figure out the first player");
        }
    }

    /* React to user input */
    public Stage move(int playerNumber, int cardNumber) throws Exception {
        Player player;
        switch (playerNumber) {
            case 0:
                player = firstPlayer;
                break;
            case 1:
                player = secondPlayer;
                break;
            default:
                throw new Exception("Someone has tried to feed false data instead of player number");
        }
        if (player.offence) {
            return attack(player, player.playerHand.get(cardNumber));
        } else {
            return defend(player, player.playerHand.get(cardNumber));
        }
    }

    public Stage move(int playerNumber) throws Exception {
        Player player;
        switch (playerNumber) {
            case 0:
                player = firstPlayer;
                break;
            case 1:
                player = secondPlayer;
                break;
            default:
                throw new Exception("Someone has tried to feed false data instead of player number");
        }
        if (player.offence) {
            return retreat(player);
        } else {
            return giveUp(player);
        }
    }

    public Stage attack(Player player, Card card) {
        boolean aiResponse = false;
        boolean attackSuccess = false;
        if (player == firstPlayer) {
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

    public Stage replenish() {
        Player offender = firstPlayer;
        Player defender = secondPlayer;
        if (firstPlayer.isOffence()) {
            offender = firstPlayer;
            defender = secondPlayer;
        } else if (secondPlayer.isOffence()) {
            offender = secondPlayer;
            defender = firstPlayer;
        }

        int missingCards = 0;
        if (6 - firstPlayer.playerHand.size() > 0) {
            missingCards += 6 - firstPlayer.playerHand.size();
        }
        if (6 - secondPlayer.playerHand.size() > 0) {
            missingCards += 6 - secondPlayer.playerHand.size();
        }

        for (int i = 0; i < missingCards; i++) {
            if (deckOfCards.size() > 0) {
                if (offender.playerHand.size() < 6) {
                    offender.playerHand.add(deckOfCards.remove(0));
                } else if (defender.playerHand.size() < 6) {
                    defender.playerHand.add(deckOfCards.remove(0));
                }
                Player interLude;
                interLude = defender;
                defender = offender;
                offender = interLude;
            }
        }

        return Stage.Continue;
    }

    public void switchPlayers() {
        firstPlayer.activityStatus = !(firstPlayer.activityStatus);
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

    public void sortHand(Player player) {
        Collections.sort(player.playerHand, new CardComparator());
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

}