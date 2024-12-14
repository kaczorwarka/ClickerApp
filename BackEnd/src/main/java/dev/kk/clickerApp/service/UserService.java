package dev.kk.clickerApp.service;
import dev.kk.clickerApp.model.User;
import dev.kk.clickerApp.repository.GameRepository;
import dev.kk.clickerApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public UserService(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public User getUser(ObjectId id) {
        return userRepository.findUserById(id).orElseThrow();
    }

    public User getUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow();
    }

    public List<User> getAllUsers(){
        return(userRepository.findAll());
    }

   public void createUser(User user){
       userRepository.insert(user);
   }

   public void deleteUser(String email){
        gameRepository.deleteByUserId(getUserByEmail(email).getId());
        userRepository.deleteUserByEmail(email).orElseThrow();
   }

   public User updateLives(String email, Integer additionalLives){
        User user = userRepository.findUserByEmail(email).orElseThrow();
        user.setAmountOfLives(user.getAmountOfLives() + additionalLives);
        return user;
   }

   public User updateUserData(String email, Map<String, String> userData){
       User user = userRepository.findUserByEmail(email).orElseThrow();

       if (!Objects.equals(userData.get("firstName"), "") && Objects.nonNull(userData.get("firstName"))){
           user.setFirstName(userData.get("firstName"));
       }
       if (!Objects.equals(userData.get("lastName"), "") && Objects.nonNull(userData.get("lastName"))) {
           user.setLastName(userData.get("lastName"));
       }
       if (!Objects.equals(userData.get("email"), "") && Objects.nonNull(userData.get("email"))){
           user.setEmail(userData.get("email"));
       }

       return userRepository.save(user);
   }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username).orElseThrow();
    }
}
