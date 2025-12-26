package ehei.pfa.authGetway.controller;

import ehei.pfa.authGetway.DTO.UserLoginDTO;
import ehei.pfa.authGetway.DTO.UserRegisterDTO;
import ehei.pfa.authGetway.DTO.res.ApiResponse;
import ehei.pfa.authGetway.DTO.res.LoginResDTO;
import ehei.pfa.authGetway.service.AuthService;
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
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterDTO dto) {
        authService.register(dto);
        ApiResponse<Void> res = ApiResponse.success("User created.");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResDTO>> login(@RequestBody UserLoginDTO dto) {
        String token = authService.login(dto);
        LoginResDTO loginResDTO = new LoginResDTO(token);
        ApiResponse<LoginResDTO> res = ApiResponse.success("Login successful.", loginResDTO);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
