package dev.kk.clickerApp.repository;

import dev.kk.clickerApp.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {

   Optional<User> findUserByEmail(String email);

   Optional<User> deleteUserByEmail(String email);
}
