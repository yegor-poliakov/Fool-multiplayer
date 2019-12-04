package fool;
import fool.domain.*;
import fool.dto.*;
import fool.gameLogic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    @Autowired
    UserGameRepository gameRepository;
    DeckConverter deckConverter = new DeckConverter();

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/deck", method = RequestMethod.GET)
    public GameState create() throws Exception {
        Deck deck = new Deck();
        UserGame deckForDB = deckConverter.deckToUserGame(Stage.Continue, deck);
        deckForDB = gameRepository.save(deckForDB);
        GameState gameState = deckConverter.deckToGameState(deckForDB.getId(), Stage.Continue, deck);
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