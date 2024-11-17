package dev.kk.clickerApp.controller;

import dev.kk.clickerApp.model.User;
import dev.kk.clickerApp.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    User getUserByEmail(@PathVariable String email){
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return user.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createUser(@RequestBody User user){
        try{
            userService.createUser(user);
        } catch (DuplicateKeyException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{email}")
    void deleteUser(@PathVariable String email){
        Optional<User> user = userService.deleteUser(email);

        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("lives/{email}")
    User updateLives(@PathVariable String email, @RequestBody Map<String, Integer> lives){
        Optional<User> user = userService.updateLives(email, lives.get("additionalLives"));

        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return user.get();
    }

    @PutMapping("/{email}")
    User updateUserData(@PathVariable String email, @RequestBody Map<String, String> userData){
        try{
            Optional<User> user = userService.updateUserData(email, userData);

            if (user.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return user.get();
        }catch (DuplicateKeyException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
