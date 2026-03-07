package ehei.pfa.authGetway.controller;

import ehei.pfa.authGetway.DTO.RegisterDTO;
import ehei.pfa.authGetway.DTO.UserLoginDTO;
import ehei.pfa.authGetway.DTO.res.ApiResponse;
import ehei.pfa.authGetway.DTO.res.LoginResDTO;
import ehei.pfa.authGetway.DTO.res.RefreshResDTO;
import ehei.pfa.authGetway.DTO.res.RegisterResDTO;
import ehei.pfa.authGetway.constant.COOKIE;
import ehei.pfa.authGetway.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResDTO>> register(@Valid @RequestBody RegisterDTO dto, HttpServletResponse response) {
        RegisterResDTO createdUser = authService.register(dto, response);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User created.", createdUser));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResDTO>> login(@RequestBody UserLoginDTO dto, HttpServletResponse response) {
        String accessToken = authService.login(dto, response);
        return ResponseEntity.ok(ApiResponse.success("Login successful.", new LoginResDTO(accessToken)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshResDTO>> refresh(
            @CookieValue(name = COOKIE.REFRESHTOKEN, required = false) String refreshToken,
            HttpServletResponse response
            ) {

        if (refreshToken == null)
            return ResponseEntity.status(401).body(ApiResponse.error("Refresh token missing."));

        String newAccessToken = authService.refreshToken(refreshToken, response);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed.", new RefreshResDTO(newAccessToken)));
    }
}
