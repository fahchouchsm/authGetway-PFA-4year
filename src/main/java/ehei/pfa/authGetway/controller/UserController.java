package ehei.pfa.authGetway.controller;

import ehei.pfa.authGetway.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/user")
public class UserController {
    @Value("${app.frontend-url}")
    private String frontendUrl;
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/verify/email")
    public RedirectView verifyEmail(@RequestParam String token) {
        userService.verifyEmailLink(token);
        return new RedirectView(frontendUrl + "/verified");
    }

    @GetMapping("/hello")
    public String hello(Authentication auth) {
        return "Hello user " + auth.getName();
    }
}
