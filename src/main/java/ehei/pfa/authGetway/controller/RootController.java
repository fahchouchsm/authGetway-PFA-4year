package ehei.pfa.authGetway.controller;

import ehei.pfa.authGetway.database.entity.User;
import ehei.pfa.authGetway.database.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    private final UserRepository UserRepository;

    public RootController(UserRepository userRepository) {
        UserRepository = userRepository;
    };

    @GetMapping("/")
    public String Index() {
        User user = new User();
        UserRepository.save(user);
        return "simo created";
    };
}
