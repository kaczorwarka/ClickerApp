package dev.kk.clickerApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collation = "Games")
public record Game(
        @Id
        Integer id,
        @DocumentReference
        User user,
        Integer Score
) {}
