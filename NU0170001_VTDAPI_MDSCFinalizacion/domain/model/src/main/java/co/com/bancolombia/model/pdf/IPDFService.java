package co.com.bancolombia.model.pdf;

import co.com.bancolombia.model.redis.UserTransactional;

public interface IPDFService {
    String buildPdfWelcome(UserTransactional user, String deliveryMessage);
}
