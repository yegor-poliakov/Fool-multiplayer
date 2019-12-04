package fool.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usergame")
public class UserGame implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "stage")
    private String stage;

    @Lob
    @Column(name = "firstPlayer")
    private String firstPlayer;

    @Lob
    @Column(name = "secondPlayer")
    private String secondPlayer;

    @Lob
    @Column(name = "deckOfCards")
    private String deckOfCards;

    @Lob
    @Column(name = "\"table\"")
    private String table;

    @Lob
    @Column(name = "trump")
    private String trump;

    public UserGame() {
    }

    public long getId() {
        return id;
    }


    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(String secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public String getDeckOfCards() {
        return deckOfCards;
    }

    public void setDeckOfCards(String deckOfCards) {
        this.deckOfCards = deckOfCards;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTrump(String trump) {
        this.trump = trump;
    }

    public String getTrump() {
        return trump;
    }

}