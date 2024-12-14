package dev.kk.clickerApp.repository;
import dev.kk.clickerApp.model.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface GameRepository extends MongoRepository<Game, ObjectId> {

    List<Game> findGamesByUserId(ObjectId userId);

    Game findGameById(ObjectId Id);

    void deleteByUserId(ObjectId userId);
}
