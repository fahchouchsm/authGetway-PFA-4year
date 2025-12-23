package ehei.pfa.authGetway.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping
    public String login(@RequestParam String username, @RequestParam String password) {

        return "";
    }
}
