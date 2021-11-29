package co.com.bancolombia.config;

import co.com.bancolombia.api.ApiRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class SwaggerConfig {


    public static final Contact DEFAULT_CONTACT =
            new Contact("Bancolombia-Jornada de Depositos","https://www.grupobancolombia.com/personas",
                    "maanjime@bancolombia.com.co");
    public static final ApiInfo DEFAULT_API_INFO =
            new ApiInfo("ATD", "Asociación tarjeta debito", "1.0",
                    "Costumize",DEFAULT_CONTACT,"MIT License",
                    "http://opensource.org/licenses/MIT", new ArrayList<>());
    private static final Set<String> DEFAULT_PRODUCES_CONSUMES_AND_CONSUMES =
            new HashSet<>(Collections.singletonList("application/json"));


    @Bean
    public Docket docketApi(){
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_CONSUMES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_CONSUMES_AND_CONSUMES)
                .useDefaultResponseMessages(false)
                .select().apis(RequestHandlerSelectors.basePackage("co.com.bancolombia.api"))
                .build();
    }
}
