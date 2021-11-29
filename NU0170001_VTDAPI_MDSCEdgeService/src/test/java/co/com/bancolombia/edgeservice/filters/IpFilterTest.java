package co.com.bancolombia.edgeservice.filters;

import co.com.bancolombia.edgeservice.helpers.LoggerApp;
import co.com.bancolombia.logging.technical.LoggerFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.InetSocketAddress;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest()
public class IpFilterTest {
    private final static String appName= "NU0104001_VTDAPI_ASOEdgeService";
    private LoggerApp logger = new LoggerApp(LoggerFactory.getLog(this.appName));
    private GatewayFilterChain filterChain;
    @Before
    public void setUp(){
        filterChain= mock(GatewayFilterChain.class);
    }
    @Test
    public void testShouldInsertIpWhenExecuteFilter(){
        MockServerHttpRequest request = MockServerHttpRequest.get("/asociacion-tarjeta-debito/edgeservice-api/validacionesApi/changeAuthCode")
                .remoteAddress(InetSocketAddress.createUnresolved("127.1.1.1",8080))
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.builder(request).build();

        when(filterChain.filter(any())).thenReturn(Mono.just(new Object()).then());

        IpFilter ipFilter = new IpFilter(logger);
        GatewayFilter config  = ipFilter.apply(new IpFilter.Config(ipFilter.newConfig().getName()));
        Mono<Void> serverResponseMono = config.filter(exchange,filterChain);

        StepVerifier.create(serverResponseMono)
                .expectComplete().verify();


        verify(filterChain,times(1)).filter(any());
    }
}