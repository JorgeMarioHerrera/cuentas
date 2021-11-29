package co.com.bancolombia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamonDBConf {
    @Bean
    public DynamoDbClient amazonDynamoDB() {
        return DynamoDbClient.builder()
                .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create()).build();

    }

    @Bean
    DynamoDbEnhancedClient amazonDynamoDBEnhancedClient() {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(amazonDynamoDB()).build();
    }
  
    

}