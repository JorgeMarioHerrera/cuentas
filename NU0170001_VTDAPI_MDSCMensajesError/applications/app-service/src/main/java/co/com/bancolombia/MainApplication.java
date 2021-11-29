package co.com.bancolombia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] argv) {
        SpringApplication.run(MainApplication.class);
    }
}
