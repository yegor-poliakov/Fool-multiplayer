package fool.gameLogic;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {
    public int compare(Card card1, Card card2) {
        if ((card1.suit == card2.suit && card1.rank > card2.rank) ||
                (card1.isTrump && !card2.isTrump)) {
            return 1;
        } else if ((card1.isTrump == false && card2.isTrump) ||
                (card1.isTrump && card2.isTrump && card1.rank < card2.rank) ||
                (!card1.isTrump && card1.rank < card2.rank) ||
                (card1.suit == card2.suit && card1.rank < card2.rank)) {
            return -1;
        } else return 0;
    }
}