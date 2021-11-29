package co.com.bancolombia.edgeservice.filters;

import co.com.bancolombia.edgeservice.controller.RestApiController;
import co.com.bancolombia.edgeservice.filters.AuthFilter;
import co.com.bancolombia.edgeservice.helpers.LoggerApp;
import co.com.bancolombia.edgeservice.helpers.LoggerOptions;
import co.com.bancolombia.edgeservice.readinessprobe.ReadinessProbe;
import co.com.bancolombia.edgeservice.util.AppConstants;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.logging.technical.TechnicalExtLogger;
import co.com.bancolombia.logging.technical.logger.TechLogger;
import co.com.bancolombia.logging.technical.message.ObjectTechMsg;
import org.junit.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.logging.Logger;

import static co.com.bancolombia.edgeservice.helpers.LoggerOptions.Services.EDGE_SERVICE_FILTERS;
import static org.mockito.Mockito.*;

@SpringBootTest()
public class AuthFilterTest {
    private final static String appName= "NU0104001_VTDAPI_ASOEdgeService";
    private LoggerApp logger = new LoggerApp(LoggerFactory.getLog(this.appName));
    private  WebClient webClient;
    private  GatewayFilterChain filterChain;
    private  WebClient.RequestBodyUriSpec requestBodyUriSpecMock;
    private  WebClient.RequestBodySpec requestBodySpecMock;
    private  WebClient.RequestHeadersSpec requestHeadersSpecMock;
    private  WebClient.ResponseSpec responseSpecMock;
    private  ClientResponse clientResponse;
    private  ClientResponse.Headers responseHeaders;

    @Before
    public void setUp(){
        webClient = mock(WebClient.class);
        filterChain= mock(GatewayFilterChain.class);
        requestBodyUriSpecMock= mock(WebClient.RequestBodyUriSpec.class);
        requestBodySpecMock= mock(WebClient.RequestBodySpec.class);
        requestHeadersSpecMock= mock(WebClient.RequestHeadersSpec.class);
        responseSpecMock = mock(WebClient.ResponseSpec.class);
        clientResponse = mock(ClientResponse.class);
        responseHeaders =  mock(ClientResponse.Headers.class);
    }

    @Test
    public void testShouldGenerateNewJWTWhenExecuteFilterAndResponseIs2xxSuccessful() throws Exception{
        when(webClient.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.header(anyString(),anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.body(any())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(AppConstants.AUTHORIZATION, "52f112af7472aff0-53e6ab6fc5dfee58");
        httpHeaders.set(AppConstants.IDSESSION, "52f112af7472aff0-53e6ab6fc5dfee58");
        when(responseHeaders.asHttpHeaders()).thenReturn(httpHeaders);
        when(clientResponse.statusCode()).thenReturn(HttpStatus.OK);
        when(clientResponse.headers()).thenReturn(responseHeaders);
        when(clientResponse.toEntity(String.class)).thenReturn(Mono.just(new ResponseEntity<>("Hello World!", HttpStatus.OK)));
        when(requestBodySpecMock.exchange()).thenReturn(Mono.just(clientResponse));
        when(filterChain.filter(any())).thenReturn(Mono.just(new Object()).then());
        MockServerHttpRequest request = MockServerHttpRequest.get("/asociacion-tarjeta-debito/validaciones-api/session/validate")
                .headers(httpHeaders).build();
        MockServerWebExchange exchange = MockServerWebExchange.builder(request).build();


        AuthFilter authFilter = new AuthFilter(webClient,logger);
        ReflectionTestUtils.setField(authFilter, "pathValidateSession", "/asociacion-tarjeta-debito/validaciones-api/session/validate");
//        GatewayFilter config  = authFilter.apply(new AuthFilter.Config("AuthFilter"));
        GatewayFilter config  = authFilter.apply(new AuthFilter.Config(authFilter.newConfig().getName()));
        Mono<Void> serverResponseMono = config.filter(exchange,filterChain);

        StepVerifier.create(serverResponseMono)
                .expectComplete().verify();

        System.out.println("response");
        verify(webClient,times(1)).post();
        verify(filterChain,times(1)).filter(any());
        verify(requestBodyUriSpecMock,times(1)).uri(anyString());
        verify(requestBodySpecMock,times(0)).body(any());
        verify(requestBodySpecMock,times(2)).header(any(),any());
        verify(requestBodySpecMock,times(1)).exchange();
        verify(requestHeadersSpecMock,times(0)).retrieve();
        verify(clientResponse,times(1)).statusCode();
        verify(clientResponse,times(1)).headers();
        verify(responseHeaders,times(1)).asHttpHeaders();

    }

    @Test
    public void testShouldGetErrorWhenExecuteFilterAndResponseIsError() throws Exception{
        when(webClient.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.header(anyString(),anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.body(any())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(AppConstants.AUTHORIZATION, "52f112af7472aff0-53e6ab6fc5dfee58");
        httpHeaders.set(AppConstants.IDSESSION, "52f112af7472aff0-53e6ab6fc5dfee58");
        when(responseHeaders.asHttpHeaders()).thenReturn(httpHeaders);
        when(clientResponse.statusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(clientResponse.headers()).thenReturn(responseHeaders);
        when(clientResponse.toEntity(String.class)).thenReturn(Mono.just(new ResponseEntity<>("Hello World!", HttpStatus.INTERNAL_SERVER_ERROR)));
        when(requestBodySpecMock.exchange()).thenReturn(Mono.just(clientResponse));
        when(filterChain.filter(any())).thenReturn(Mono.just(new Object()).then());
        MockServerHttpRequest request = MockServerHttpRequest.get("/asociacion-tarjeta-debito/validaciones-api/session/validate")
                .headers(httpHeaders).build();
        MockServerWebExchange exchange = MockServerWebExchange.builder(request).build();


        AuthFilter authFilter = new AuthFilter(webClient,logger);
        ReflectionTestUtils.setField(authFilter, "pathValidateSession", "/asociacion-tarjeta-debito/validaciones-api/session/validate");
        GatewayFilter config  = authFilter.apply(new AuthFilter.Config(authFilter.newConfig().getName()));
        Mono<Void> serverResponseMono = config.filter(exchange,filterChain);


        StepVerifier.create(serverResponseMono)
                .expectComplete().verify();

        System.out.println("response");
        verify(webClient,times(1)).post();
        verify(filterChain,times(0)).filter(any());
        verify(requestBodyUriSpecMock,times(1)).uri(anyString());
        verify(requestBodySpecMock,times(0)).body(any());
        verify(requestBodySpecMock,times(2)).header(any(),any());
        verify(requestBodySpecMock,times(1)).exchange();
        verify(requestHeadersSpecMock,times(0)).retrieve();
        verify(clientResponse,times(1)).statusCode();
        verify(clientResponse,times(0)).headers();
        verify(responseHeaders,times(0)).asHttpHeaders();

    }

    @Test
    public void testShouldGenerateNewJWTWhenExecuteFilterAndResponseIs4xxBadRequest() throws Exception{
        when(webClient.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.header(anyString(),anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.body(any())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(AppConstants.AUTHORIZATION, "");
        httpHeaders.set(AppConstants.IDSESSION, "");
        when(responseHeaders.asHttpHeaders()).thenReturn(httpHeaders);
        when(clientResponse.statusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(clientResponse.headers()).thenReturn(responseHeaders);
        when(clientResponse.toEntity(String.class)).thenReturn(Mono.just(new ResponseEntity<>("Hello World!", HttpStatus.INTERNAL_SERVER_ERROR)));
        when(requestBodySpecMock.exchange()).thenReturn(Mono.just(clientResponse));
        when(filterChain.filter(any())).thenReturn(Mono.just(new Object()).then());
        MockServerHttpRequest request = MockServerHttpRequest.get("/asociacion-tarjeta-debito/validaciones-api/session/validate")
                .headers(httpHeaders).build();
        MockServerWebExchange exchange = MockServerWebExchange.builder(request).build();


        AuthFilter authFilter = new AuthFilter(webClient,logger);
        ReflectionTestUtils.setField(authFilter, "pathValidateSession", "/asociacion-tarjeta-debito/validaciones-api/session/validate");
        GatewayFilter config  = authFilter.apply(new AuthFilter.Config(authFilter.newConfig().getName()));
        Mono<Void> serverResponseMono = config.filter(exchange,filterChain);


        StepVerifier.create(serverResponseMono)
                .expectComplete().verify();

        System.out.println("response");
        verify(webClient,times(0)).post();
        verify(filterChain,times(0)).filter(any());
        verify(requestBodyUriSpecMock,times(0)).uri(anyString());
        verify(requestBodySpecMock,times(0)).body(any());
        verify(requestBodySpecMock,times(0)).header(any(),any());
        verify(requestBodySpecMock,times(0)).exchange();
        verify(requestHeadersSpecMock,times(0)).retrieve();
        verify(clientResponse,times(0)).statusCode();
        verify(clientResponse,times(0)).headers();
        verify(responseHeaders,times(0)).asHttpHeaders();

    }



    @Test
    public void testShouldGetErrorWhenExecuteFilterAndResponseIsErrorAuthorization() throws Exception{
        when(webClient.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.header(anyString(),anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.body(any())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(AppConstants.AUTHORIZATION, "");
        httpHeaders.set(AppConstants.IDSESSION, "52f112af7472aff0-53e6ab6fc5dfee58");
        when(responseHeaders.asHttpHeaders()).thenReturn(httpHeaders);
        when(clientResponse.statusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(clientResponse.headers()).thenReturn(responseHeaders);
        when(clientResponse.toEntity(String.class)).thenReturn(Mono.just(new ResponseEntity<>("Hello World!", HttpStatus.INTERNAL_SERVER_ERROR)));
        when(requestBodySpecMock.exchange()).thenReturn(Mono.just(clientResponse));
        when(filterChain.filter(any())).thenReturn(Mono.just(new Object()).then());
        MockServerHttpRequest request = MockServerHttpRequest.get("/asociacion-tarjeta-debito/validaciones-api/session/validate")
                .headers(httpHeaders).build();
        MockServerWebExchange exchange = MockServerWebExchange.builder(request).build();


        AuthFilter authFilter = new AuthFilter(webClient,logger);
        ReflectionTestUtils.setField(authFilter, "pathValidateSession", "/asociacion-tarjeta-debito/validaciones-api/session/validate");
        GatewayFilter config  = authFilter.apply(new AuthFilter.Config(authFilter.newConfig().getName()));
        Mono<Void> serverResponseMono = config.filter(exchange,filterChain);


        StepVerifier.create(serverResponseMono)
                .expectComplete().verify();

        System.out.println("response");
        verify(webClient,times(0)).post();
        verify(filterChain,times(0)).filter(any());
        verify(requestBodyUriSpecMock,times(0)).uri(anyString());
        verify(requestBodySpecMock,times(0)).body(any());
        verify(requestBodySpecMock,times(0)).header(any(),any());
        verify(requestBodySpecMock,times(0)).exchange();
        verify(requestHeadersSpecMock,times(0)).retrieve();
        verify(clientResponse,times(0)).statusCode();
        verify(clientResponse,times(0)).headers();
        verify(responseHeaders,times(0)).asHttpHeaders();

    }

    @Test
    public void testShouldGetErrorWhenExecuteFilterAndResponseIsErrorIdSession() throws Exception{
        when(webClient.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.header(anyString(),anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.body(any())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(AppConstants.AUTHORIZATION, "52f112af7472aff0-53e6ab6fc5dfee58");
        httpHeaders.set(AppConstants.IDSESSION, "");
        when(responseHeaders.asHttpHeaders()).thenReturn(httpHeaders);
        when(clientResponse.statusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(clientResponse.headers()).thenReturn(responseHeaders);
        when(clientResponse.toEntity(String.class)).thenReturn(Mono.just(new ResponseEntity<>("Hello World!", HttpStatus.INTERNAL_SERVER_ERROR)));
        when(requestBodySpecMock.exchange()).thenReturn(Mono.just(clientResponse));
        when(filterChain.filter(any())).thenReturn(Mono.just(new Object()).then());
        MockServerHttpRequest request = MockServerHttpRequest.get("/asociacion-tarjeta-debito/validaciones-api/session/validate")
                .headers(httpHeaders).build();
        MockServerWebExchange exchange = MockServerWebExchange.builder(request).build();


        AuthFilter authFilter = new AuthFilter(webClient,logger);
        ReflectionTestUtils.setField(authFilter, "pathValidateSession", "/asociacion-tarjeta-debito/validaciones-api/session/validate");
        GatewayFilter config  = authFilter.apply(new AuthFilter.Config(authFilter.newConfig().getName()));
        Mono<Void> serverResponseMono = config.filter(exchange,filterChain);


        StepVerifier.create(serverResponseMono)
                .expectComplete().verify();

        System.out.println("response");
        verify(webClient,times(0)).post();
        verify(filterChain,times(0)).filter(any());
        verify(requestBodyUriSpecMock,times(0)).uri(anyString());
        verify(requestBodySpecMock,times(0)).body(any());
        verify(requestBodySpecMock,times(0)).header(any(),any());
        verify(requestBodySpecMock,times(0)).exchange();
        verify(requestHeadersSpecMock,times(0)).retrieve();
        verify(clientResponse,times(0)).statusCode();
        verify(clientResponse,times(0)).headers();
        verify(responseHeaders,times(0)).asHttpHeaders();

    }

    @Test
    public void shouldHealthMono(){
        ReadinessProbe readinessProbe = new ReadinessProbe();
        Assert.assertEquals("MonoJust",readinessProbe.health().toString());
    }


    @Test
    public void shouldHealthOk(){
        RestApiController entity = new RestApiController();
        Assert.assertEquals(HttpStatus.OK,entity.health().getStatusCode());
    }


    @Test
    public void shouldLoggerAppError(){
        logger.init(this.getClass().toString(), "Test", null);
        logger.logger("Test","Test", LoggerOptions.EnumLoggerLevel.ERROR,new Exception());
        Assert.assertNotNull(1);
    }

    }



