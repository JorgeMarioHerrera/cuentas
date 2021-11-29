package co.com.bancolombia.usecase.common.util;

import co.com.bancolombia.business.Constants;

import java.math.BigDecimal;
import java.security.SecureRandom;

public class IdSessionUtil {
    public static String generateSessionId() {
        SecureRandom secureRandom = new SecureRandom();
        String sessionId = "";

        do {
            BigDecimal random1 = BigDecimal.valueOf(Math.abs((double)((long)
                    secureRandom.nextInt() * Constants.RANDOM_ID_SESSION + 1L)));
            BigDecimal random2 = BigDecimal.valueOf(Math.abs((double)((long)
                    secureRandom.nextInt() * Constants.RANDOM_ID_SESSION + 1L)));
            BigDecimal random3 = BigDecimal.valueOf(Math.abs((double)((long)
                    secureRandom.nextInt() * Constants.RANDOM_ID_SESSION + 1L)));
            sessionId = random1.toPlainString() + "-" +random3.toPlainString()+"-"+ random2.toPlainString();
        } while(sessionId.length() <= Constants.LONG_ID_SESSION);

        return sessionId;
    }
}
