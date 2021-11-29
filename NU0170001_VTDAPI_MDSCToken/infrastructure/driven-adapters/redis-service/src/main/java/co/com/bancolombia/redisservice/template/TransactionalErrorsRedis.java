package co.com.bancolombia.redisservice.template;

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
@RedisHash("Error")
@SuppressWarnings("java:S1820")
public class TransactionalErrorsRedis implements Serializable {
    // General
    @Id
    private String sessionID;
    @Indexed
    private String dateAndHourTransaction;
    private String errorCode;

    @TimeToLive
    public long getTimeToLive() {
        LocalDateTime futureDay =  LocalDateTime.now();

        futureDay = futureDay.plusDays(1).minusHours( futureDay.getHour())
                .minusMinutes(futureDay.getMinute()).minusSeconds(futureDay.getSecond());

        futureDay = futureDay.plusHours(2);

        return SECONDS.between( LocalDateTime.now(), futureDay);
    }
}