package dev.kk.clickerApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "Users")
public class User {
    @Id
    ObjectId id;
    String firstName;
    String lastName;
    String password;
    @Indexed(unique = true)
    String email;
    Integer amountOfLives;
}
