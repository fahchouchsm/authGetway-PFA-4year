package ehei.pfa.authGetway.controller;

import ehei.pfa.authGetway.DTO.res.ApiResponse;
import ehei.pfa.authGetway.DTO.res.MeDTO;
import ehei.pfa.authGetway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeDTO>> me(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success("User info.", userService.getMe(userId)));
    }
}