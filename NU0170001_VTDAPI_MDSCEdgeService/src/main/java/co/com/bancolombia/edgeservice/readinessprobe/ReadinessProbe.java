package co.com.bancolombia.edgeservice.readinessprobe;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ReadinessProbe {

    @GetMapping("/health")
    public Mono<String> health(){
        return Mono.just("Health");
    }
}
