package fool.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usergame")
public class UserGame implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long deckID;

    @Column(name = "stage")
    private String stage;

    @Lob
    @Column(name = "talon")
    private String talon;

    @Lob
    @Column(name = "players")
    private String players;

    @Lob
    @Column(name = "statusString")
    private String statusString;

    @Lob
    @Column(name = "deckSize")
    private String deckSize;

    @Lob
    @Column(name = "\"table\"")
    private String table;

    @Lob
    @Column(name = "trump")
    private String trump;

    public UserGame() {
    }

    public long getDeckID() {
        return deckID;
    }

    public void setDeckID(long deckID) {
        this.deckID = deckID;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStage() {
        return stage;
    }

    public void setTalon(String talon) {
        this.talon = talon;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public void setDeckSize(String deckSize) {
        this.deckSize = deckSize;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setTrump(String trump) {
        this.trump = trump;
    }

    public String getTalon() {
        return talon;
    }

    public String getTable() {
        return table;
    }

    public String getPlayers() {
        return players;
    }

    public String getTrump() {
        return trump;
    }

    public String getStatusString() {
        return statusString;
    }

    public String getDeckSize() {
        return deckSize;
    }

}