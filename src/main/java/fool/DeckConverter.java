package fool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import fool.domain.*;
import fool.gameLogic.*;
import fool.dto.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeckConverter {
    public UserGame deckToUserGame(Stage stage, Deck deck) {
        UserGame userGame = new UserGame();
        userGame.setStage(stage + "");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonMap = objectMapper.writeValueAsString(deck.talon);
            userGame.setTalon(jsonMap);
        } catch (Exception e) {
            return null;
        }

        try {
            String jsonMap = objectMapper.writeValueAsString(deck.table);
            userGame.setTable(jsonMap);
        } catch (Exception e) {
            return null;
        }

        try {
            String jsonMap = objectMapper.writeValueAsString(deck.players);
            userGame.setPlayers(jsonMap);
        } catch (Exception e) {
            return null;
        }

        try {
            String jsonMap = objectMapper.writeValueAsString(deck.trump);
            userGame.setTrump(jsonMap);
        } catch (Exception e) {
            return null;
        }

        try {
            String jsonMap = objectMapper.writeValueAsString(deck.statusString);
            userGame.setStatusString(jsonMap);
        } catch (Exception e) {
            return null;
        }

        try {
            String jsonMap = objectMapper.writeValueAsString(deck.deckSize);
            userGame.setDeckSize(jsonMap);
        } catch (Exception e) {
            return null;
        }

        return userGame;
    }

    public Deck userGameToDeck(UserGame userGame) throws IOException {
        Deck deck = new Deck();
        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType talonType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Card.class);
        deck.talon = objectMapper.readValue(userGame.getTalon(), talonType);
        deck.table = objectMapper.readValue(userGame.getTable(), talonType);
        CollectionType playerType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Player.class);
        deck.players = objectMapper.readValue(userGame.getPlayers(), playerType);
        deck.trump = objectMapper.readValue(userGame.getTrump(), String.class);
        deck.statusString = objectMapper.readValue(userGame.getStatusString(), String.class);
        deck.deckSize = objectMapper.readValue(userGame.getDeckSize(), int.class);
        return deck;
    }

    public GameState deckToGameState(long deckID, int playerID, Stage stage, Deck deck) throws Exception {
        GameStage gameStage = stageToGameStage(stage);
        return new GameState(deckID, playerID, gameStage, deck);
    }

    private GameStage stageToGameStage(Stage stage) throws Exception {
        switch (stage) {
            case Continue:
                return GameStage.Continue;
            case OnHold:
                return GameStage.OnHold;
            case End:
                return GameStage.End;
            default:
                throw new Exception("Invalid GameStage ENUM");
        }
    }

    public GameList userGamesToList (List<UserGame> userGames) throws IOException {
        GameList gameList = new GameList();
        for (UserGame userGame : userGames) {
            GameInfo gameInfo = new GameInfo();
            gameInfo.setDeckID(userGame.getDeckID());
            Deck deck = userGameToDeck(userGame);
            gameInfo.setCreatorName(deck.players[0].getPlayerName());
            for (int j = 0; j < deck.players.length; j++) {
                if (deck.players[j].getPlayerID() == 0) {
                    gameInfo.setCurrentPlayersNumber(j);
                    break;
                }
            }
            gameInfo.setTotalPlayersNumber(deck.players.length);
            gameList.games.add(gameInfo);
        }
        return gameList;
    }

}