package co.com.bancolombia.redis.util;


import co.com.bancolombia.redis.model.redis.UserTransactional;
import co.com.bancolombia.redis.template.UserTransactionalRedis;

public class Converter {

    public static UserTransactional entityToModel(UserTransactionalRedis userTransactionalRedis) {
        return UserTransactional.builder()
                .idSession(userTransactionalRedis.getIdSession())
                .numberDocument(userTransactionalRedis.getNumberDocument())
                .ipClient(userTransactionalRedis.getIpClient())
                .dateAndHourTransaction(userTransactionalRedis.getDateAndHourTransaction())
                .functionalStep(userTransactionalRedis.getFunctionalStep())
                .deviceBrowser(userTransactionalRedis.getDeviceBrowser())
                .userAgent(userTransactionalRedis.getUserAgent())
                .deviceOS(userTransactionalRedis.getDeviceOS())
                .device(userTransactionalRedis.getDevice())
                .typeDocument(userTransactionalRedis.getTypeDocument())
                .typeClient(userTransactionalRedis.getTypeClient())
                .validSession(userTransactionalRedis.getValidSession())
                .jwt(userTransactionalRedis.getJwt())
                .concurrentSessions(userTransactionalRedis.getConcurrentSessions())
                .greaterThanAmount(userTransactionalRedis.getGreaterThanAmount())
                .officeCode(userTransactionalRedis.getOfficeCode())
                .account(userTransactionalRedis.getAccount())
                .typeAccount(userTransactionalRedis.getTypeAccount())
                .attemptsGenerate(userTransactionalRedis.getAttemptsGenerate())
                .attemptsValidate(userTransactionalRedis.getAttemptsValidate())
                .mobilePhone(userTransactionalRedis.getMobilePhone())
                .email(userTransactionalRedis.getEmail())
                .validateToken(userTransactionalRedis.getValidateToken())
                .attemptsManagement(userTransactionalRedis.getAttemptsManagement())
                .cardResponseModel(userTransactionalRedis.getCardResponseModel())
                .build();
    }

    public static UserTransactionalRedis modelToEntity(UserTransactional userTransactional) {
        return UserTransactionalRedis.builder()
                .idSession(userTransactional.getIdSession())
                .numberDocument(userTransactional.getNumberDocument())
                .ipClient(userTransactional.getIpClient())
                .dateAndHourTransaction(userTransactional.getDateAndHourTransaction())
                .functionalStep(userTransactional.getFunctionalStep())
                .deviceBrowser(userTransactional.getDeviceBrowser())
                .userAgent(userTransactional.getUserAgent())
                .deviceOS(userTransactional.getDeviceOS())
                .device(userTransactional.getDevice())
                .typeDocument(userTransactional.getTypeDocument())
                .typeClient(userTransactional.getTypeClient())
                .validSession(userTransactional.getValidSession())
                .jwt(userTransactional.getJwt())
                .concurrentSessions(userTransactional.getConcurrentSessions())
                .greaterThanAmount(userTransactional.getGreaterThanAmount())
                .officeCode(userTransactional.getOfficeCode())
                .account(userTransactional.getAccount())
                .typeAccount(userTransactional.getTypeAccount())
                .attemptsGenerate(userTransactional.getAttemptsGenerate())
                .attemptsValidate(userTransactional.getAttemptsValidate())
                .mobilePhone(userTransactional.getMobilePhone())
                .email(userTransactional.getEmail())
                .validateToken(userTransactional.getValidateToken())
                .attemptsManagement(userTransactional.getAttemptsManagement())
                .cardResponseModel(userTransactional.getCardResponseModel())
                .build();
    }

    private Converter() {}
}
