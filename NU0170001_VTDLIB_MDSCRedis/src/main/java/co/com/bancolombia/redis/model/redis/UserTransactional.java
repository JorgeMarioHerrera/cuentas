package co.com.bancolombia.redis.model.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1820")
public class UserTransactional {
    // General
    private String idSession;
    private String numberDocument;
    private String ipClient;
    private String dateAndHourTransaction;
    private String functionalStep;
    private String deviceBrowser;
    private String userAgent;
    private String deviceOS;
    private String device;
    private String typeDocument;
    private String typeClient;
    // Error Stopper
    private Boolean validSession;
    // JWT
    private String jwt;
    private Boolean concurrentSessions;
    // Validate
    private Boolean greaterThanAmount;
    private String officeCode;
    // Who enters
    private String account;
    private String typeAccount;
    // Token
    private int attemptsGenerate;
    private int attemptsValidate;
    private long mobilePhone;
    private String email;
    private Boolean validateToken;
    // Card
    private int attemptsManagement;
    private String cardResponseModel;
}
