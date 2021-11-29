package co.com.bancolombia.edgeservice.filters;

import co.com.bancolombia.edgeservice.helpers.LoggerApp;
import co.com.bancolombia.edgeservice.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Objects;

import static co.com.bancolombia.edgeservice.helpers.LoggerOptions.Services.*;
import static co.com.bancolombia.edgeservice.helpers.LoggerOptions.Actions.*;
import static co.com.bancolombia.edgeservice.helpers.LoggerOptions.EnumLoggerLevel.*;


@Component
@RequiredArgsConstructor
public class AuthFilter implements GatewayFilterFactory<AuthFilter.Config> {

    private final WebClient webClient;
    private final LoggerApp loggerApp;

    @Value("${paths.security.validate}")
    public String pathValidateSession;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (exchange.getRequest().getHeaders().get(AppConstants.AUTHORIZATION) != null &&
                    !Objects.requireNonNull(exchange.getRequest().getHeaders()
                            .get(AppConstants.AUTHORIZATION)).get(0).isEmpty()) {
                if(exchange.getRequest().getHeaders().get(AppConstants.IDSESSION) != null &&
                        !Objects.requireNonNull(exchange.getRequest().getHeaders()
                                .get(AppConstants.IDSESSION)).get(0).isEmpty()) {
                    loggerApp.init(this.getClass().toString(), EDGE_SERVICE_FILTERS, Objects.requireNonNull(
                            exchange.getRequest().getHeaders().get(AppConstants.IDSESSION)).get(0));
                    return this.webClient.post().uri(pathValidateSession)
                            .header(AppConstants.AUTHORIZATION,
                                    Objects.requireNonNull(exchange.getRequest().getHeaders()
                                            .get(AppConstants.AUTHORIZATION)).get(0))
                            .header(AppConstants.IDSESSION,
                                    Objects.requireNonNull(exchange.getRequest().getHeaders()
                                            .get(AppConstants.IDSESSION)).get(0)).exchange()
                            //validate jwt
                            .flatMap(clientResponse -> obtainJWT(clientResponse, exchange, chain))
                            .flatMap(responseObject -> {
                                ResponseEntity response = responseObject;
                                return this.onError((response.getBody() != null ? response.getBody().toString() : ""),
                                        response.getStatusCodeValue(), exchange);
                            });
                }
                return this.onError("", HttpStatus.BAD_REQUEST.value(), exchange);
            }
            return this.onError("", HttpStatus.BAD_REQUEST.value(), exchange);
        };
    }

    @Override
    public Config newConfig() {
        return new Config("AuthFilter");
    }

    public static class Config {
        private String name;
        public Config(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    public Mono<ResponseEntity<String>> obtainJWT(ClientResponse clientResponse,
                                                  ServerWebExchange exchange, GatewayFilterChain chain) {
        String[] idSession = {Objects.requireNonNull(exchange.getRequest()
                .getHeaders()
                .get(AppConstants.IDSESSION)).get(0)};
        if (clientResponse.statusCode().is2xxSuccessful()) {
            loggerApp.logger(EDGE_SERVICE_SUCCESS_AUTHORIZATION, EDGE_SERVICE_SUCCESS_ID_SESSION, INFO, null);
            // generate new jwt and return  in the header
            exchange.getResponse().getHeaders().add(AppConstants.AUTHORIZATION, clientResponse
                    .headers()
                    .asHttpHeaders().getFirst(AppConstants.AUTHORIZATION));
            exchange.getResponse().getHeaders()
                    .add(AppConstants.IDSESSION, Objects.requireNonNull(exchange.getRequest()
                            .getHeaders()
                            .get(AppConstants.IDSESSION)).get(0));
            loggerApp.logger(EDGE_SERVICE_REQUEST_SUCCESS, null, TRACE, null);
            loggerApp.logger(EDGE_SERVICE_RESPONSE_OBJECT, clientResponse.toString(), DEBUG, null);
            return  chain.filter(
                    exchange.mutate().request(exchange.getRequest().mutate()
                            .header(AppConstants.IDSESSION,idSession)
                            .build()).build())
                    .map(ignored -> ResponseEntity.ok(""));
        }
        else {
            return clientResponse.toEntity(String.class);
        }
    }

    public Mono<Void> onError(String body, int statusCode, ServerWebExchange exchange) {
        byte[] bytes = body.getBytes(Charset.defaultCharset());
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        exchange.getResponse().setStatusCode(HttpStatus.valueOf(statusCode));
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}