package co.com.bancolombia.edgeservice.filters;

        import co.com.bancolombia.edgeservice.helpers.LoggerApp;
        import co.com.bancolombia.edgeservice.util.AppConstants;
        import lombok.RequiredArgsConstructor;
        import org.springframework.cloud.gateway.filter.GatewayFilter;
        import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
        import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
        import org.springframework.http.server.reactive.ServerHttpRequest;
        import org.springframework.stereotype.Component;

        import java.net.InetSocketAddress;
        import java.util.Objects;

        import static co.com.bancolombia.edgeservice.helpers.LoggerOptions.Actions.*;
        import static co.com.bancolombia.edgeservice.helpers.LoggerOptions.EnumLoggerLevel.INFO;
        import static co.com.bancolombia.edgeservice.helpers.LoggerOptions.Services.EDGE_SERVICE_FILTERS;

@Component
@RequiredArgsConstructor
public class IpFilter implements GatewayFilterFactory<IpFilter.Config> {
    private final LoggerApp loggerApp;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            loggerApp.init(this.getClass().toString(), EDGE_SERVICE_FILTERS,EDGE_SERVICE_ID_SESSION_MSN );
            loggerApp.logger(EDGE_SERVICE_SUCCESS_AUTHORIZATION, EDGE_SERVICE_INIT_IP_CLIENT, INFO, null);
            InetSocketAddress inetSocketAddress = XForwardedRemoteAddressResolver.trustAll().resolve(exchange);
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header(AppConstants.IP_CLIENT, Objects.requireNonNull(
                            getIp(inetSocketAddress)
                    ), "")
                    .build();
            loggerApp.logger(EDGE_SERVICE_SUCCESS_AUTHORIZATION, EDGE_SERVICE_FINISH_IP_CLIENT, INFO, null);
            return chain.filter(exchange.mutate().request(request).build());
        };
    }
    public String getIp(InetSocketAddress inetSocketAddress) {
        String ipCliente = inetSocketAddress.getHostName();
        if ("".equals(ipCliente) || null == ipCliente) {
            ipCliente = "1.1.1.1";
            return ipCliente;
        }
        ipCliente = ipCliente.contains(",") ? ipCliente.split(",")[0] : ipCliente;
        loggerApp.logger(EDGE_SERVICE_SUCCESS_AUTHORIZATION, "Client Ip obtained", INFO, null);
        return ipCliente;
    }

    @Override
    public Config newConfig() {
        return new Config("IpFilter");
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

}
