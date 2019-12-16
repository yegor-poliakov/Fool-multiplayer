package fool;
import fool.domain.*;
import fool.dto.*;
import fool.gameLogic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    @Autowired
    UserGameRepository gameRepository;
    DeckConverter deckConverter = new DeckConverter();

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/deck", method = RequestMethod.GET)
    public GameState createNewGame(@RequestParam(value = "numberOfPlayers", defaultValue = "1") int numberOfPlayers,
                                   @RequestParam(value = "numberOfCards", defaultValue = "36") int numberOfCards,
                                   @RequestParam(value = "playerName", defaultValue = "player1") String playerName)
                                   throws Exception {
        Deck deck = new Deck(numberOfPlayers, numberOfCards, playerName);
        Stage stage = Stage.OnHold;
        if (numberOfPlayers == 1){
             stage = Stage.Continue;
        }
        UserGame deckForDB = deckConverter.deckToUserGame(stage, deck);
        deckForDB = gameRepository.save(deckForDB);
        GameState gameState = deckConverter.deckToGameState(deckForDB.getId(), stage, deck);
        return gameState;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/deck", method = RequestMethod.POST)
    public GameState joinGame(@RequestParam(value = "deckID") long deckID) throws Exception {
        UserGame userGame = gameRepository.findById(deckID).get();
        Deck deck = deckConverter.userGameToDeck(userGame);
        Stage stage = Stage.OnHold;

        int j = 0;
        for (int i = 0; i < deck.players.length; i++){
          if ((deck.players[i].getPlayerID()) == 0){
              deck.players[i].setPlayerID();
              j = i;
              break;
          }
        }
        if (j == deck.players.length){
            stage = Stage.Continue;
        }

        UserGame deckForDB = deckConverter.deckToUserGame(stage, deck);
        deckForDB = gameRepository.save(deckForDB);
        GameState gameState = deckConverter.deckToGameState(deckForDB.getId(), stage, deck);
        return gameState;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/deck", method = RequestMethod.POST)
    public GameState joinGameByID(@RequestParam(value = "deckID") long deckID,
                                  @RequestParam(value = "playerID") int playerID) throws Exception {
        UserGame userGame = gameRepository.findById(deckID).get();
        Deck deck = deckConverter.userGameToDeck(userGame);
        Stage stage = userGame.getStage();
        UserGame deckForDB = deckConverter.deckToUserGame(stage, deck);
        deckForDB = gameRepository.save(deckForDB);
        GameState gameState = deckConverter.deckToGameState(deckForDB.getId(), stage, deck);
        return gameState;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/deck", method = RequestMethod.POST)
    public GameState cardMove(@RequestBody MakeMoveRequest request) throws Exception {
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
        userGameToDB.setId(userGame.getId());
        gameRepository.save(userGameToDB);
        GameState gameState = deckConverter.deckToGameState(request.getDeckID(), stage, deck);
        return gameState;
    }
}