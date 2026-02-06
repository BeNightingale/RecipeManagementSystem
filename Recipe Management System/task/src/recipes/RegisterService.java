package recipes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class RegisterService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    boolean registerUser(String email, String password) {
        final String encodedPassword = passwordEncoder.encode(password);
        final Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isPresent()) {
            log.info("Email {} is already used! You can't create new account!", email);
            return false;
        }
        final User newUser = new User(null, email, encodedPassword);
        final User savedUser = userRepository.save(newUser);
        log.info("Registered user with email {} (id = {}).", email, savedUser.getId());
        return true;
    }
}