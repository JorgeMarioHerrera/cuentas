package co.com.bancolombia.redis.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("User")
@SuppressWarnings("java:S1820")
public class UserTransactionalRedis implements Serializable {
    // General
    @Id
    private String idSession;
    @Indexed
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

    @TimeToLive
    public long getTimeToLive() {
        LocalDateTime futureDay =  LocalDateTime.now();

        futureDay = futureDay.plusDays(1).minusHours( futureDay.getHour())
                .minusMinutes(futureDay.getMinute()).minusSeconds(futureDay.getSecond());

        futureDay = futureDay.plusHours(2);

        return SECONDS.between( LocalDateTime.now(), futureDay);
    }
}