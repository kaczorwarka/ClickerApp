package dev.kk.clickerApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collation = "Users")
public record User (
        @Id
        Integer id,
        String firstName,
        String lastName,
        Date dateOfBirth,
        String email,
        Integer amountOfLives
){}
