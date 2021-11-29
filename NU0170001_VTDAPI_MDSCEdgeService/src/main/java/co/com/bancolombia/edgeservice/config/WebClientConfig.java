package co.com.bancolombia.edgeservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${paths.validacionesApi.baseUri}")
    public String baseUrlSecurity;


    @Bean
    public WebClient getWebClient(){
        return  WebClient.builder().baseUrl(baseUrlSecurity).build();
    }
}
