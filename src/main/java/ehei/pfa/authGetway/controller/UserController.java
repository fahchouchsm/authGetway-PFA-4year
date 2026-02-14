package ehei.pfa.authGetway.controller;

import ehei.pfa.authGetway.DTO.res.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class UserController {

    @GetMapping("/verify/email")
    public ResponseEntity<ApiResponse<Void>>
}
