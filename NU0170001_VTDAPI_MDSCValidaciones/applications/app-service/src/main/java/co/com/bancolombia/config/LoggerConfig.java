package co.com.bancolombia.config;

import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.logging.technical.logger.TechLogger;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Configuration
public class LoggerConfig {
    @Value("${spring.application.name}")
    private String appName;
    @Value("#{'${spring.application.tagList}'.replace(' ', '').split(',')}")
    private List<String> tagList;

    @Bean
    public TechLogger getTechnicalLogger() {
        return LoggerFactory.getLog(this.appName);
    }

    @Bean
    public List<String> getTagList() {
        return tagList;
    }

}
