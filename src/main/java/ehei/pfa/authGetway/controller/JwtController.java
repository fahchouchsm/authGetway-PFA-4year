package ehei.pfa.authGetway.controller;

import ehei.pfa.authGetway.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.Base64;

@RestController
@RequestMapping("/jwt")
public class JwtController {

    @PostMapping("/public_key")
    public ResponseEntity<String> getPublicKey() {
        PublicKey publicKey = JwtUtil.getPublicKey();

        String encodedKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return ResponseEntity.ok().body(encodedKey);
    }
}
