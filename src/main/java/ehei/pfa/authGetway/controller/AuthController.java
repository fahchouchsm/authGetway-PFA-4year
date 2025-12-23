package ehei.pfa.authGetway.controller;

import ehei.pfa.authGetway.DTO.UserRegisterDTO;
import ehei.pfa.authGetway.database.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRegisterDTO userRegisterDTO) {

        return "";
    }

    @PostMapping
    public String login(@RequestParam String username, @RequestParam String password) {
        // todo
        return "";
    }
}
