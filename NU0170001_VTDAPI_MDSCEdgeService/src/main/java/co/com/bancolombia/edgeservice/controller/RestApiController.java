package co.com.bancolombia.edgeservice.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestApiController {

    @GetMapping("/modulo-solucion-cuentas/edgeservice-api/actuator/health")
    public ResponseEntity health(){
        return ResponseEntity.status(HttpStatus.OK).body("{\"status\":\"OK\"}");
    }
}
