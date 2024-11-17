package dev.kk.clickerApp.service;

import dev.kk.clickerApp.model.User;
import dev.kk.clickerApp.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByEmail(String email){
        return(userRepository.findUserByEmail(email));
    }

    public List<User> getAllUsers(){
        return(userRepository.findAll());
    }

   public void createUser(User user){
       userRepository.insert(user);
   }

   public Optional<User> deleteUser(String email){
        return userRepository.deleteUserByEmail(email);
   }

   public Optional<User> updateLives(String email, Integer additionalLives){
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setAmountOfLives(user.getAmountOfLives() + additionalLives);

            optionalUser = Optional.of(userRepository.save(user));
        }
        return optionalUser;
   }

   public Optional<User> updateUserData(String email, Map<String, String> userData){
       Optional<User> optionalUser = userRepository.findUserByEmail(email);

       if (optionalUser.isPresent()){
           User user = optionalUser.get();

           if (!Objects.equals(userData.get("firstName"), "") && Objects.nonNull(userData.get("firstName"))){
               user.setFirstName(userData.get("firstName"));
           }
           if (!Objects.equals(userData.get("lastName"), "") && Objects.nonNull(userData.get("lastName"))){
               user.setLastName(userData.get("lastName"));
           }
           if (!Objects.equals(userData.get("password"), "") && Objects.nonNull(userData.get("password"))){
               user.setPassword(userData.get("password"));
           }
           if (!Objects.equals(userData.get("email"), "") && Objects.nonNull(userData.get("email"))){
               user.setEmail(userData.get("email"));
           }

           optionalUser = Optional.of(userRepository.save(user));
       }
       return optionalUser;
   }
}
