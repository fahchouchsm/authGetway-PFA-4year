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
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        authService.register(userRegisterDTO);
        ApiResponse<Void> res = ApiResponse.success("user created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping
    public String login(@RequestBody UserLoginDTO userLoginDTO) {
        // todo
        return "";
    }
}
