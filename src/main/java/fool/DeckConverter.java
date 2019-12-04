package fool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import fool.domain.*;
import fool.gameLogic.*;
import fool.dto.*;


import java.io.IOException;
import java.util.ArrayList;

public class DeckConverter {
    public UserGame deckToUserGame(Stage stage, Deck deck) {
        UserGame userGame = new UserGame();
        userGame.setStage(stage + "");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonMap = objectMapper.writeValueAsString(deck.deckOfCards);
            userGame.setDeckOfCards(jsonMap);
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
            String jsonMap = objectMapper.writeValueAsString(deck.firstPlayer);
            userGame.setFirstPlayer(jsonMap);
        } catch (Exception e) {
            return null;
        }

        try {
            String jsonMap = objectMapper.writeValueAsString(deck.secondPlayer);
            userGame.setSecondPlayer(jsonMap);
        } catch (Exception e) {
            return null;
        }

        try {
            String jsonMap = objectMapper.writeValueAsString(deck.trump);
            userGame.setTrump(jsonMap);
        } catch (Exception e) {
            return null;
        }

        return userGame;
    }

    public Deck userGameToDeck(UserGame userGame) throws IOException {
        Deck deck = new Deck();
        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType deckOfCardsType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Card.class);
        deck.deckOfCards = objectMapper.readValue(userGame.getDeckOfCards(), deckOfCardsType);
        deck.table = objectMapper.readValue(userGame.getTable(), deckOfCardsType);
        deck.firstPlayer = objectMapper.readValue(userGame.getFirstPlayer(), Player.class);
        deck.secondPlayer = objectMapper.readValue(userGame.getSecondPlayer(), Player.class);
        deck.trump = objectMapper.readValue(userGame.getTrump(), String.class);
        return deck;
    }

    public GameState deckToGameState(long deckID, Stage stage, Deck deck) throws Exception {
        GameStage gameStage = stageToGameStage(stage);
        return new GameState(deckID, gameStage, deck);
    }


    private GameStage stageToGameStage(Stage stage) throws Exception {
        switch (stage) {
            case Continue:
                return GameStage.Continue;
            case Victory:
                return GameStage.Victory;
            case Loss:
                return GameStage.Loss;
            case Draw:
                return GameStage.Draw;
            default:
                throw new Exception("Someone has messed with the code! Check GameStage ENUM");
        }
    }
}