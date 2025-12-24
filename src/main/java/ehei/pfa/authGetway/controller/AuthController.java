package ehei.pfa.authGetway.controller;

import ehei.pfa.authGetway.DTO.UserLoginDTO;
import ehei.pfa.authGetway.DTO.UserRegisterDTO;
import ehei.pfa.authGetway.Utils.ApiResponse;
import ehei.pfa.authGetway.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterDTO dto) {
        authService.register(dto);
        ApiResponse<Void> res = ApiResponse.success("User created.");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserLoginDTO dto) {
        String token = authService.login(dto);
        ApiResponse<String> res = ApiResponse.success("Login successful.", token);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
