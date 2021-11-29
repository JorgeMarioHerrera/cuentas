package co.com.bancolombia.config;

import co.com.bancolombia.firehose.impl.FunctionalReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class FunctionalReportServiceConfig {

    @Value("${services.kinesis.firehose.lzReportHead}")
    private String firehouseHeader;
    @Bean
    public FunctionalReportService functionalReportBean(){
        byte[] decodedBytes = Base64.getDecoder().decode(firehouseHeader);
        String decodedString = new String(decodedBytes);
        return new  FunctionalReportService(decodedString);
    }
}
