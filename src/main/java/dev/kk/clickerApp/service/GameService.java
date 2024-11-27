package dev.kk.clickerApp.service;
import dev.kk.clickerApp.model.Game;
import dev.kk.clickerApp.repository.GameRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserService userService;

    public GameService(GameRepository gameRepository, UserService userService){
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    public List<Game> getAllUserGames(String email){
        return gameRepository.findGamesByUserId(userService.getUserByEmail(email).getId());
    }

    public List<Game> getBestUserGames(String email, int numberOfPlaces){
        return gameRepository.findGamesByUserId(userService.getUserByEmail(email).getId())
                .stream()
                .sorted(Comparator.comparingInt(Game::getScore).reversed())
                .limit(numberOfPlaces)
                .toList();
    }

    public List<Game> getBestGlobalGames(int numberOfPlaces){
        return gameRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Game::getScore).reversed())
                .limit(numberOfPlaces)
                .toList();
    }

    public void createGame(String email, Game game){
        game.setUserId(userService.getUserByEmail(email).getId());
        gameRepository.insert(game);
    }

    public void deleteGame(ObjectId gameId){
        gameRepository.deleteById(gameId);
    }
}
