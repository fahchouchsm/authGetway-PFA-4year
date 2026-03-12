package ehei.pfa.authGetway.controller;

import ehei.pfa.authGetway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Value("${app.frontend-url}")
    private String frontendUrl;
    private final UserService userService;

    @GetMapping("/verify/email")
    public RedirectView verifyEmail(@RequestParam String token) {
        userService.verifyEmailLink(token);
        return new RedirectView(frontendUrl + "/verified");
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }
}