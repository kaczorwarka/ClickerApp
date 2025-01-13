package dev.kk.clickerApp.controller;
import dev.kk.clickerApp.auth.GlobalGame;
import dev.kk.clickerApp.model.Game;
import dev.kk.clickerApp.service.GameService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/games")
public class GameController {

    final private GameService gameService;
    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    @GetMapping("/user/{email}")
    List<GlobalGame> getAllUserGames(@PathVariable String email){
        try {
            return gameService.getAllUserGames(email);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            // Bo użytkownik po emailu jest szukany narazie i może go nie znaleźć
        }
    }

    @GetMapping("/userBest/{email}")
    List<Game> getBestUserGames(@PathVariable String email, @RequestBody Map<String, Integer> places){
        try {
            return gameService.getBestUserGames(email, places.get("places"));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            // Bo użytkownik po emailu jest szukany narazie i może go nie znaleźć
        }
    }

    @GetMapping("/bestGlobal/{places}")
    List<GlobalGame> getBestGlobalGames(@PathVariable int places){
        return gameService.getBestGlobalGames(places);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{email}")
    void createGame(@PathVariable String email, @RequestBody Game game){
        try {
            gameService.createGame(email, game);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping()
    void deleteGame(@RequestBody Map<String, String> game){
        try{
            gameService.deleteGame(new ObjectId(game.get("id")));
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
