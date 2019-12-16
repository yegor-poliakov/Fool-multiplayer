package fool.gameLogic;

public class Card {
    private Suit suit;
    private int rank;
    private boolean isTrump;
    private boolean inPlay;

    public Card(){

    }

    public Card(int rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.isTrump = false;
        this.inPlay = false;
    }

    public void setSuit(Suit suit){
        this.suit = suit;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

    public void setTrump(boolean isTrump){
        this.isTrump = isTrump;
    }

    public void setInPlay(boolean inPlay){
        this.inPlay = inPlay;
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean isTrump() {
        return isTrump;
    }

}