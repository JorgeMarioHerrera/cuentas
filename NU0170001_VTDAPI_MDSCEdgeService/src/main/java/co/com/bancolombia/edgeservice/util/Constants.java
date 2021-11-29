package co.com.bancolombia.edgeservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    @Value("${paths.cors.urls}")
    public String urlCors;

}
