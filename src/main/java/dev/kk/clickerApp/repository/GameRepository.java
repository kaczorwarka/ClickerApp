package dev.kk.clickerApp.repository;

import dev.kk.clickerApp.model.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
}
