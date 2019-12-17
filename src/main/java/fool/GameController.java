package fool;
import fool.domain.*;
import fool.dto.*;
import fool.gameLogic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GameController {

    @Autowired
    UserGameRepository gameRepository;
    DeckConverter deckConverter = new DeckConverter();

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/newgame", method = RequestMethod.POST)
    public GameState createNewGame(@RequestParam(value = "numberOfPlayers", defaultValue = "1") int numberOfPlayers,
                                   @RequestParam(value = "numberOfCards", defaultValue = "36") int numberOfCards,
                                   @RequestParam(value = "playerName", defaultValue = "player1") String playerName)
                                   throws Exception {
        Deck deck = new Deck(numberOfPlayers, numberOfCards, playerName);
        deck.players[0].setPlayerID(1);
        Stage stage = Stage.OnHold;
        if (numberOfPlayers == 1){
             stage = Stage.Continue;
        }
        UserGame deckForDB = deckConverter.deckToUserGame(stage, deck);
        deckForDB = gameRepository.save(deckForDB);
        GameState gameState = deckConverter.deckToGameState(deckForDB.getDeckID(), 1, stage, deck);
        return gameState;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/joingame", method = RequestMethod.POST)
    public GameState joinGame(@RequestParam(value = "deckID") long deckID) throws Exception {
        UserGame userGame = gameRepository.findById(deckID).get();
        Deck deck = deckConverter.userGameToDeck(userGame);
        Stage stage = Stage.OnHold;

        int j = -1;
        int playerID = 0;
        for (int i = 0; i < deck.players.length; i++){
          if ((deck.players[i].getPlayerID()) == 0){
              deck.players[i].setPlayerID(i+1);
              playerID = deck.players[i].getPlayerID();
              j = i;
              break;
          }
        }
        if (j == deck.players.length){
            stage = Stage.Continue;
        }
        if (j == -1){
            throw new Exception ("No place for a new player");
        }

        UserGame deckForDB = deckConverter.deckToUserGame(stage, deck);
        deckForDB = gameRepository.save(deckForDB);
        GameState gameState = deckConverter.deckToGameState(deckForDB.getDeckID(), playerID, stage, deck);
        return gameState;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/joinbyid", method = RequestMethod.POST)
    public GameState joinGameByID(@RequestParam(value = "deckID") long deckID,
                                  @RequestParam(value = "playerID") int playerID) throws Exception {
        UserGame userGame = gameRepository.findById(deckID).get();
        Deck deck = deckConverter.userGameToDeck(userGame);
        Stage stage = Stage.Continue;
        UserGame deckForDB = deckConverter.deckToUserGame(stage, deck);
        deckForDB = gameRepository.save(deckForDB);
        GameState gameState = deckConverter.deckToGameState(deckForDB.getDeckID(), playerID, stage, deck);
        return gameState;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public GameState move(@RequestBody MakeMoveRequest request) throws Exception {
        UserGame userGame = gameRepository.findById(request.getDeckID()).get();
        Deck deck = deckConverter.userGameToDeck(userGame);
        long deckID = request.getDeckID();
        Stage stage;
        if (request.getCardNumber() != -1){
            stage = deck.move(request.getPlayerNumber(), request.getCardNumber());
        } else {
            stage = deck.move(request.getPlayerNumber());
        }
        UserGame userGameToDB = deckConverter.deckToUserGame(stage, deck);
        userGameToDB.setDeckID(userGame.getDeckID());
        gameRepository.save(userGameToDB);
        GameState gameState = deckConverter.deckToGameState(request.getDeckID(), request.getPlayerID(),
                stage, deck);
        return gameState;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/lookGame", method = RequestMethod.GET)
    public GameList lookUpGames() throws Exception {
        List<UserGame> listOfGames = gameRepository.findByStage("OnHold");
        GameList result = deckConverter.userGamesToList(listOfGames);
        return result;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/getstate", method = RequestMethod.GET)
    public GameState getState(@RequestParam(value = "deckID") long deckID,
                              @RequestParam(value = "playerID") int playerID ) throws Exception {
        UserGame userGame = gameRepository.findById(deckID).get();
        Deck deck = deckConverter.userGameToDeck(userGame);
        Stage stage = Stage.valueOf(userGame.getStage());
        GameState gameState = deckConverter.deckToGameState(deckID, playerID,
                stage, deck);
        return gameState;
    }

}