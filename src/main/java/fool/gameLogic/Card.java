package fool.gameLogic;

public class Card {
    private Suit suit;
    private int rank;
    private boolean isTrump;

    public Card() {
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isTrump() {
        return isTrump;
    }

    public void setTrump(boolean isTrump) {
        this.isTrump = isTrump;
    }

}