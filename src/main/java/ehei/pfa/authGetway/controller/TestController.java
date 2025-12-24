package ehei.pfa.authGetway.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestController {

    @GetMapping("/test")
    public String hello() {
        return "hello world";
    }
}