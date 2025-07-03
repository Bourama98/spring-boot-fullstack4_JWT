package com.amigoscode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    record PingPong(String result) {
    }

    @GetMapping("/ping")
    private PingPong getPingPong() {
        return new PingPong("pong: Ping Mangara Bourama07032025");
    }
}
