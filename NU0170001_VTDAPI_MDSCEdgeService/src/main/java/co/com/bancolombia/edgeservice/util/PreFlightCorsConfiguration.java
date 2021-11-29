package co.com.bancolombia.edgeservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class PreFlightCorsConfiguration {

    private Constants constants;

    @Autowired
    PreFlightCorsConfiguration(Constants constants){
        this.constants = constants;
    }

    @Bean
    public CorsWebFilter corsFilter() {
        return new CorsWebFilter(corsConfigurationSource());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.setAllowedMethods(Arrays.asList(HttpMethod.PUT.toString(),
                HttpMethod.GET.toString(), HttpMethod.POST.toString(), HttpMethod.DELETE.toString()));
        config.addAllowedHeader("*");
        config.setAllowedOrigins(Arrays.asList(constants.urlCors.split(",")));
        config.setExposedHeaders(Arrays.asList(AppConstants.AUTHORIZATION,AppConstants.IDSESSION));
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
