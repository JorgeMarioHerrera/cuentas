package co.com.bancolombia.jwt;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.jwtmodel.JwtModel;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class JWTServiceTest {
    private static final String ID_SESSION = "33399-00023-23023093-232";
    private static final String JWT = "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE2Mjc1NzQ1NzkuNTg2LCJpYXQiOjE2Mjc0ODg1NzkuNTg2LCJkb2N1bWVudFR5cGUiOiJjYyIsImRvY3VtZW50TnVtYmVyIjoiMTIzNDU2Nzg5IiwicHJvZHVjdElkIjoiMTExIiwiY2xpZW50Q2F0ZWdvcnkiOiJDQSIsInNlc3Npb25JZCI6IjEyMzQ1Njc4OTAxMjM0NTY3ODkyMTIzNDU2Nzg5IiwiYXV0aENvZGUiOiIifQ.OmY3CsEiN48RVSJ_vtet1boE9LOzfAp2WO9qs_0gKeEXlIUXisTYx1DPC1r2wGumVu9LWQylunEJZSnE9_LSvm9f9NqTSgONeaKgI9s8tLsks_EAev34SEEoXuapEH8w2mygYMcBnJf2svXHc_xyPmr77sBgfy1y1Vy5Cwz8T3v33rk8CydbHLGGUV8CkUgttZvq_Ua2-hAUqUswcCSv6C-m5T1PBeXr2M0BMrLJY508O2DhOCVF1Lzu1wG9sLtnl7YfeyaQuG5dTUvOLhX3hVsK37hJSbGwdAu_x5SPtT1M1Uee6KL4uxucepf9mTUxr53Vqn8wH8awCjPaZBSgog";
    private static final String JWT_INVALID = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImJkYTYyYjM2LTFlNTEtNGY5ZS05YjY3LWIzZjQ5YjAyM2JjZiIsInR5cCI6Imp3dCJ9.eyJleHAiOjE2MjUxNTQ1NjYsImp0aSI6ImRCSU1YeUJmRDYwQ0tYb1M1UFhXSEEiLCJpYXQiOjE2MjUxNTI3NjYsImlkU2Vzc2lvbiI6IjE1OTY0NjEzMjY5OTk5OS0xMDk4OTEwNjgxOTk5OTktMTQ0MTMwNTkwOTk5OTk5IiwiaXNzIjoiTlUwMTA0MDAxIiwic3VtIjoiYTk3ZjhjNWQtNmVlMC00ZTg0LTk0YjctMGY1ZTc0OTAxN2I5IiwiYXVkIjoiQVBJR2F0ZXdheV9MQU4iLCJuYmYiOjE2MjUxNTI3NjZ9.LoOzO8u0O5iznqEVDRk8-J1hLOrM7LJLyAF9KFxHM8D06WTCseM6tDmTUjN03rmt7E4xxLYeR8UHbeyGUjz-5n8TCEFQR_qgaRbrvjjqxdq1lyi09H1JZDYogcm6AiDvhYEhvEOBf6Y4LdApwBwj18YZ_IW1gq5a-6leVUnJ8bb1vAuFUpdxyMzpF3YSZLKcJqRMAYVYJrjR5mhwH1PJXu7ZqqlE_78_5wkn_J0-BW1e3SMJ8KTdUk8KOHUjg8ru1s4cXXcHYF8oqaIsS8kmTF4FIicEAIR_ipBrI9dB1UGPlJ0-eI16jI7qWd1owsEdtoQzforQ9dabb6gwq2zA";
    private final LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog(String.valueOf(this.getClass())));

    @Test
    void testShouldSimulateGeneralSecurityExceptionWhenExecuteGenerateJwt() {
        JWTService jwtService = addProperties();
        ReflectionTestUtils.setField(jwtService, "privateKey",
                "-----BEGIN PRIVATE KEY-----MIIEvg-----END PRIVATE KEY-----");
        Either<ErrorExeption, JwtModel> result = jwtService.generateJwt(ID_SESSION);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(ErrorsEnum.JWT_EXCEPTION_SECURITY.buildError().getCode(),result.getLeft().getCode());
    }
    @Test
    void testShouldSimulateJoseExceptionWhenExecuteGenerateJwt() throws Exception {
        JWTService jwtService = addProperties();
        JsonWebSignature jwsMock = mock(JsonWebSignature.class);
        when(jwsMock.getCompactSerialization()).thenThrow(new JoseException(""));
        ReflectionTestUtils.setField(jwtService, "jws", jwsMock);
        Either<ErrorExeption, JwtModel> result = jwtService.generateJwt(ID_SESSION);
        verify(jwsMock,times(1)).getCompactSerialization();
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(ErrorsEnum.JWT_EXCEPTION_DECRYP.buildError().getCode(),result.getLeft().getCode());
    }
    @Test
    void testShouldSimulateMalformedClaimExceptionWhenExecuteGenerateJwt() throws Exception {
        JWTService jwtService = addProperties();
        JwtClaims  claimsMock = mock(JwtClaims.class);
        claimsMock.setExpirationTimeMinutesInTheFuture(Constants.EXPIRATION_TOKEN_IN_MINUTES);
        when(claimsMock.getExpirationTime()).thenThrow(new MalformedClaimException(""));
        ReflectionTestUtils.setField(jwtService, "claims", claimsMock);
        Either<ErrorExeption, JwtModel> result = jwtService.generateJwt(ID_SESSION);
        verify(claimsMock,times(1)).getExpirationTime();
        assertTrue(result.isLeft());
    }
    @Test
    void testShouldSimulateRuntimeExceptionWhenExecuteGenerateJwt() throws Exception {
        JWTService jwtService = addProperties();
        JwtClaims  claimsMock = mock(JwtClaims.class);
        claimsMock.setExpirationTimeMinutesInTheFuture(Constants.EXPIRATION_TOKEN_IN_MINUTES);
        when(claimsMock.getExpirationTime()).thenThrow(new RuntimeException(""));
        ReflectionTestUtils.setField(jwtService, "claims", claimsMock);
        Either<ErrorExeption, JwtModel> result = jwtService.generateJwt(ID_SESSION);
        verify(claimsMock,times(1)).getExpirationTime();
        assertTrue(result.isLeft());
    }
    @Test
    void testShouldGetSuccessWhenExecuteGenerateJwt() {
        JWTService jwtService = addProperties();
        Either<ErrorExeption, JwtModel> result = jwtService.generateJwt(ID_SESSION);
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
        assertNotNull(result.getRight().getIdSession());
        assertNotNull(result.getRight().getJwt());
    }
    @Test
    void testShouldGetErrorWhenExecuteValidateAndJwtObjectIsNull() {
        JWTService jwtService = addProperties();
        Either<ErrorExeption, String> result = jwtService.validate(JwtModel.builder().build());
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.JWT_ERR_IDSSESION_NOT_EXIST.buildError(),result.getLeft());
    }
    @Test
    void testShouldGetErrorWhenExecuteValidateAndIdSessionIsNull()  {
        JWTService jwtService = addProperties();
        Either<ErrorExeption, String> result = jwtService.validate(
                JwtModel.builder().build()
        );
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.JWT_ERR_IDSSESION_NOT_EXIST.buildError(),result.getLeft());
    }
    @Test
    void testShouldGetErrorWhenExecuteValidateAndIdSessionIsEmpty() {
        JWTService jwtService = addProperties();
        Either<ErrorExeption, String> result = jwtService.validate(
                JwtModel.builder().idSession("").build()
        );
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.JWT_ERR_IDSSESION_NOT_EXIST.buildError(),result.getLeft());
    }
    @Test
    void testShouldSimulateGeneralSecurityExceptionWhenExecuteValidateExperience()  {
        JWTService jwtService = addProperties();
        ReflectionTestUtils.setField(jwtService, "publicKey",
                "-----BEGIN PUBLIC KEY-----MIIEvg-----END PUBLIC KEY-----");
        Either<ErrorExeption, Boolean> result = jwtService.validateExperience(JwtModel.builder().idSession(ID_SESSION)
                .jwt(JWT).build());
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.JWT_EXCEPTION_SECURITY.buildError(),result.getLeft());
    }
    @Test
    void testShouldSimulateInvalidJWTWhenExecuteValidateExperience() {
        JWTService jwtService = addProperties();
        ReflectionTestUtils.setField(jwtService, "publicKey", "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0iXpieRgI8GOAyfFeK7f0Eead9XPIWlQMUpmgxmMOR3RqzHfp3tiRF6oqO3jBo+fnVzpWZC+LvkX9hpIQ2ysjytVGG5s6MaULDysPAhrpV+kpHWxeWb5DLnakiACkntc5OSXhOPXjsarAjwX1tQkeqq3LOF0D5h11c35QKdxQbtp9Ca0dn3W1YCHnJtIOiCTbPRpdFxG5XMq2YotFbdMPoYnm5irPVEUB50dsE7fPBIEeenTS5CLYs14HbGwpT+k+ulVcF9CGvGP0b9GHi7bMlhtjRfenZIYTo2nb/m0xge7mZdzKsEFH4Vhm/MTWv8V6/m/Od8ELLKRVutgG23F+wIDAQAB-----END PUBLIC KEY-----");
        Either<ErrorExeption, Boolean> result = jwtService.validateExperience(
                JwtModel.builder().idSession(ID_SESSION)
                        .jwt(JWT_INVALID).build()
        );
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.JWT_ERR_INVALID_SIGN.buildError(),result.getLeft());
    }
    @Test
    void testShouldRuntimeExceptionWhenExecuteValidateExperience() throws Exception {
        JWTService jwtService = addProperties();
        Either<ErrorExeption, Boolean> result = jwtService.validateExperience(
                JwtModel.builder().idSession(ID_SESSION)
                        .jwt(JWT).build()
        );
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.JWT_ERR_VALIDATE_UNKNOWN.buildError(),result.getLeft());
    }
    @Test
    void testShouldGetErrorWhenExecuteVerifySignatureExperience() {
        JWTService jwtService = addProperties();
        ReflectionTestUtils.setField(jwtService, "publicKey", "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0iXpieRgI8GOAyfFeK7f0Eead9XPIWlQMUpmgxmMOR3RqzHfp3tiRF6oqO3jBo+fnVzpWZC+LvkX9hpIQ2ysjytVGG5s6MaULDysPAhrpV+kpHWxeWb5DLnakiACkntc5OSXhOPXjsarAjwX1tQkeqq3LOF0D5h11c35QKdxQbtp9Ca0dn3W1YCHnJtIOiCTbPRpdFxG5XMq2YotFbdMPoYnm5irPVEUB50dsE7fPBIEeenTS5CLYs14HbGwpT+k+ulVcF9CGvGP0b9GHi7bMlhtjRfenZIYTo2nb/m0xge7mZdzKsEFH4Vhm/MTWv8V6/m/Od8ELLKRVutgG23F+wIDAQAB-----END PUBLIC KEY-----");
        Either<ErrorExeption, Boolean> result = jwtService.validateExperience(
                JwtModel.builder().idSession(ID_SESSION)
                        .jwt(JWT_INVALID).build()
        );
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.JWT_ERR_INVALID_SIGN.buildError(),result.getLeft());
    }
    @Test
    void testShouldGetSuccessWhenExecuteValidateExperience() {
        JWTService jwtService = addProperties();
        ReflectionTestUtils.setField(jwtService, "publicKey", "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0iXpieRgI8GOAyfFeK7f0Eead9XPIWlQMUpmgxmMOR3RqzHfp3tiRF6oqO3jBo+fnVzpWZC+LvkX9hpIQ2ysjytVGG5s6MaULDysPAhrpV+kpHWxeWb5DLnakiACkntc5OSXhOPXjsarAjwX1tQkeqq3LOF0D5h11c35QKdxQbtp9Ca0dn3W1YCHnJtIOiCTbPRpdFxG5XMq2YotFbdMPoYnm5irPVEUB50dsE7fPBIEeenTS5CLYs14HbGwpT+k+ulVcF9CGvGP0b9GHi7bMlhtjRfenZIYTo2nb/m0xge7mZdzKsEFH4Vhm/MTWv8V6/m/Od8ELLKRVutgG23F+wIDAQAB-----END PUBLIC KEY-----");
        Either<ErrorExeption, JwtModel> jwt = jwtService.generateJwt(ID_SESSION);
        Either<ErrorExeption, Boolean> result = jwtService.validateExperience(
                JwtModel.builder().idSession(ID_SESSION)
                        .jwt(jwt.getRight().getJwt()).build()
        );
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }
    @Test
    void testShouldSimulateGeneralSecurityExceptionWhenExecuteValidate()  {
        JWTService jwtService = addProperties();
        ReflectionTestUtils.setField(jwtService, "publicKey",
                "-----BEGIN PUBLIC KEY-----MIIEvg-----END PUBLIC KEY-----");
        Either<ErrorExeption, String> result = jwtService.validate(JwtModel.builder().idSession(ID_SESSION)
                .jwt(JWT).consumerCertificate("-----BEGIN PUBLIC KEY-----MIIEvg-----END PUBLIC KEY-----").build());
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.JWT_EXCEPTION_SECURITY.buildError(),result.getLeft());
    }

    @Test
    void testShouldSimulateInvalidJWTWhenExecuteValidate() {
        JWTService jwtService = addProperties();
        ReflectionTestUtils.setField(jwtService, "publicKey", "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0iXpieRgI8GOAyfFeK7f0Eead9XPIWlQMUpmgxmMOR3RqzHfp3tiRF6oqO3jBo+fnVzpWZC+LvkX9hpIQ2ysjytVGG5s6MaULDysPAhrpV+kpHWxeWb5DLnakiACkntc5OSXhOPXjsarAjwX1tQkeqq3LOF0D5h11c35QKdxQbtp9Ca0dn3W1YCHnJtIOiCTbPRpdFxG5XMq2YotFbdMPoYnm5irPVEUB50dsE7fPBIEeenTS5CLYs14HbGwpT+k+ulVcF9CGvGP0b9GHi7bMlhtjRfenZIYTo2nb/m0xge7mZdzKsEFH4Vhm/MTWv8V6/m/Od8ELLKRVutgG23F+wIDAQAB-----END PUBLIC KEY-----");
        Either<ErrorExeption, String> result = jwtService.validate(
                JwtModel.builder().idSession(ID_SESSION)
                        .jwt(JWT_INVALID).consumerCertificate("-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0iXpieRgI8GOAyfFeK7f0Eead9XPIWlQMUpmgxmMOR3RqzHfp3tiRF6oqO3jBo+fnVzpWZC+LvkX9hpIQ2ysjytVGG5s6MaULDysPAhrpV+kpHWxeWb5DLnakiACkntc5OSXhOPXjsarAjwX1tQkeqq3LOF0D5h11c35QKdxQbtp9Ca0dn3W1YCHnJtIOiCTbPRpdFxG5XMq2YotFbdMPoYnm5irPVEUB50dsE7fPBIEeenTS5CLYs14HbGwpT+k+ulVcF9CGvGP0b9GHi7bMlhtjRfenZIYTo2nb/m0xge7mZdzKsEFH4Vhm/MTWv8V6/m/Od8ELLKRVutgG23F+wIDAQAB-----END PUBLIC KEY-----").build()
        );
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.JWT_ERR_INVALID_SIGN.buildError(),result.getLeft());
    }

    @Test
    void testShouldRuntimeExceptionWhenExecuteValidate() throws Exception {
        JWTService jwtService = addProperties();
        Either<ErrorExeption, String> result = jwtService.validate(
                JwtModel.builder().idSession(ID_SESSION)
                        .jwt(JWT).consumerCertificate("-----BEGIN CERTIFICATE-----MIIEezCCA2OgAwIBAgIUSiN3sjO7jbn28Ehx0NSTyGS/gXUwDQYJKoZIhvcNAQELBQAwgcwxCzAJBgNVBAYTAkNPMRIwEAYDVQQIDAlBTlRJT1FVSUExETAPBgNVBAcMCE1FREVMTElOMRkwFwYDVQQKDBBCQU5DT0xPTUJJQSBTLkEuMSYwJAYDVQQLDB1WSUNFUFJFU0lERU5DSUEgREUgVEVDTk9MT0dJQTEnMCUGA1UEAwwebm9tYnJlYXBwLmFwcHMuYW1iaWVudGVzYmMuY29tMSowKAYJKoZIhvcNAQkBFhtjcmFsb3BlckBiYW5jb2xvbWJpYS5jb20uY28wHhcNMjEwNjE4MTg1NjM1WhcNMjIwNjE4MTg1NjM1WjCBzDELMAkGA1UEBhMCQ08xEjAQBgNVBAgMCUFOVElPUVVJQTERMA8GA1UEBwwITUVERUxMSU4xGTAXBgNVBAoMEEJBTkNPTE9NQklBIFMuQS4xJjAkBgNVBAsMHVZJQ0VQUkVTSURFTkNJQSBERSBURUNOT0xPR0lBMScwJQYDVQQDDB5ub21icmVhcHAuYXBwcy5hbWJpZW50ZXNiYy5jb20xKjAoBgkqhkiG9w0BCQEWG2NyYWxvcGVyQGJhbmNvbG9tYmlhLmNvbS5jbzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANIl6YnkYCPBjgMnxXiu39BHmnfVzyFpUDFKZoMZjDkd0asx36d7YkReqKjt4waPn51c6VmQvi75F/YaSENsrI8rVRhubOjGlCw8rDwIa6VfpKR1sXlm+Qy52pIgApJ7XOTkl4Tj147GqwI8F9bUJHqqtyzhdA+YddXN+UCncUG7afQmtHZ91tWAh5ybSDogk2z0aXRcRuVzKtmKLRW3TD6GJ5uYqz1RFAedHbBO3zwSBHnp00uQi2LNeB2xsKU/pPrpVXBfQhrxj9G/Rh4u2zJYbY0X3p2SGE6Np2/5tMYHu5mXcyrBBR+FYZvzE1r/Fev5vznfBCyykVbrYBttxfsCAwEAAaNTMFEwHQYDVR0OBBYEFOytRSqMCepT9RuhfhwQcXYCwijVMB8GA1UdIwQYMBaAFOytRSqMCepT9RuhfhwQcXYCwijVMA8GA1UdEwEB/wQFMAMBAf8wDQYJKoZIhvcNAQELBQADggEBAKUgR2f3+WheYe+STgAPrAbZ3Mtev8IgaYA1du8rfFqnl6AxlrVcze6TvGRjvuriVk38kQowG/Jjp28o4tZCXFPGD8sz65jR8JdBxw/0s8oX+Ywvi9aqbWOQEdu6gQ+hqN1kGVXji6HJsmgUxef5/eKyBMtt3Xni1m0vhpB/g/qkrOdcj3n0/33GxT7PCEMGeYBot6cMN5yeDc+PKM6EzlZ/yEkV/lqR9D+4Pzx/33MrMcMCAEjVO3aPB9IKzziJ4KpxZVMVPA/zC8bpYqgZ1ccVyxw5heauijqDOuT+tDaPnJ+s3Blpea9sId8QGGdrjE9Pi4Bw71Qb3DOc6Wt7SGw=-----END CERTIFICATE-----").build()
        );
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.JWT_ERR_VALIDATE_UNKNOWN.buildError(),result.getLeft());
    }

    @Test
    void testShouldGetSuccessWhenExecuteValidate() {
        JWTService jwtService = addProperties();
        ReflectionTestUtils.setField(jwtService, "publicKey", "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0iXpieRgI8GOAyfFeK7f0Eead9XPIWlQMUpmgxmMOR3RqzHfp3tiRF6oqO3jBo+fnVzpWZC+LvkX9hpIQ2ysjytVGG5s6MaULDysPAhrpV+kpHWxeWb5DLnakiACkntc5OSXhOPXjsarAjwX1tQkeqq3LOF0D5h11c35QKdxQbtp9Ca0dn3W1YCHnJtIOiCTbPRpdFxG5XMq2YotFbdMPoYnm5irPVEUB50dsE7fPBIEeenTS5CLYs14HbGwpT+k+ulVcF9CGvGP0b9GHi7bMlhtjRfenZIYTo2nb/m0xge7mZdzKsEFH4Vhm/MTWv8V6/m/Od8ELLKRVutgG23F+wIDAQAB-----END PUBLIC KEY-----");
        Either<ErrorExeption, JwtModel> jwt = jwtService.generateJwt(ID_SESSION);
        Either<ErrorExeption, String> result = jwtService.validate(
                JwtModel.builder().idSession(ID_SESSION)
                        .jwt(jwt.getRight().getJwt()).consumerCertificate("-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0iXpieRgI8GOAyfFeK7f0Eead9XPIWlQMUpmgxmMOR3RqzHfp3tiRF6oqO3jBo+fnVzpWZC+LvkX9hpIQ2ysjytVGG5s6MaULDysPAhrpV+kpHWxeWb5DLnakiACkntc5OSXhOPXjsarAjwX1tQkeqq3LOF0D5h11c35QKdxQbtp9Ca0dn3W1YCHnJtIOiCTbPRpdFxG5XMq2YotFbdMPoYnm5irPVEUB50dsE7fPBIEeenTS5CLYs14HbGwpT+k+ulVcF9CGvGP0b9GHi7bMlhtjRfenZIYTo2nb/m0xge7mZdzKsEFH4Vhm/MTWv8V6/m/Od8ELLKRVutgG23F+wIDAQAB-----END PUBLIC KEY-----").build()
        );
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    private JWTService addProperties() {
        JWTService service= new JWTService(loggerApp);
        ReflectionTestUtils.setField(service, "privateKey", "-----BEGIN PRIVATE KEY-----MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDSJemJ5GAjwY4DJ8V4rt/QR5p31c8haVAxSmaDGYw5HdGrMd+ne2JEXqio7eMGj5+dXOlZkL4u+Rf2GkhDbKyPK1UYbmzoxpQsPKw8CGulX6SkdbF5ZvkMudqSIAKSe1zk5JeE49eOxqsCPBfW1CR6qrcs4XQPmHXVzflAp3FBu2n0JrR2fdbVgIecm0g6IJNs9Gl0XEblcyrZii0Vt0w+hiebmKs9URQHnR2wTt88EgR56dNLkItizXgdsbClP6T66VVwX0Ia8Y/Rv0YeLtsyWG2NF96dkhhOjadv+bTGB7uZl3MqwQUfhWGb8xNa/xXr+b853wQsspFW62AbbcX7AgMBAAECggEBAKltk5pvG+v4E20L1bP1GdGsyScwpKzFYbg7yeaM5HbwSakCrtUzLwj8YHLCSLVzeja/QAvMHOl2PxFWI0jpBz7uqs7lyLzrzW7VA+/qwg80y2Mk2I+Oq+DMvyHs2OYMu9p3SooDwDkGh71ngbjclQSpOsHei1o/i1FznS+5UCCzaRQpnBxfvIgP6ocgTm4qOl/KAV7JKJNJ5pAytgPHQFwnVZTw5TR4RqEashfMxPzVuNQvooQy7TPEs+bjoMrVKcqg5KKsXR340sP50O87RJXztd6O3dEJjchGGj/IRcDaTIPLhGT3MJiswRtShuFwJ57CJ8k88EVOWEIGsEcH92ECgYEA7oWhELbbIu/MeuRgt2fzYpKCOeTbu6/AvK7/T2FiMpIL4thgBkAyiM/gDkVQ3KIO6BXhytIRGT7iTTVzGIBFpzP7CbCj4+lqgRcruG0qQZucpqw8LHAhyGem8OtVXximgJtUnNB9UOeeDeE3YKcBzzv15qx9N8cLUXzjexjD/kkCgYEA4YwGVMZyyAPCb7yV0YxCkHlerJiviJ7Pz6WlzZ3daKZsP5suDXgn3DqmFtGV8gsXSX+FB9h2Zfdfyc5ElZMJJLjghTF/T3QPMsqF9rTiH7O+cUaPkdA5WLOI/SEzXvbqvAtLQcr9fg1Mw1QoG9b3GTU+bdHlf17ARWb6GDQ58iMCgYBx+m8mAosi3b4zt3A0FDtsjiAmtCMARBLw1xrvftSi38FITCdOtVywnc0uZJcoZ1SCOPqlpc4/5xnS1UjDm4AtTxyV28/lOAQJ3d2pipjXsxmfV8yFW6v5DPbsRziLcccWy6MaMFLB13xim42P5R5rPfbanPBVOAtDjE9T4li96QKBgHVdvwGoTpxEUI6CbNehDBZQAfMAdP873Nmp0hCgIFHPoC6tGpbJurxADKN9JnDQUqrdX6VIf7vpsP+7hTbsT2q7haHpstzugOYmOvW5BJ4Ik4YweI8g8mZ07kkCWNAG3vlBS0UOU0CfvR07kq+B+g01Bqpw+eKAtL6oqmWj769PAoGBALnhcZju/kHTd7xOaXWIG70h9cBRrwFNTNJLLbveoEfK8O3L1B4PLF/1+zqwAg801fFoqmQ239tvaTLxlL6Q4McyDkN39u25OVgGWEy/i3zldcqEQH25ywyLve6sMWFPGbKfbVrxOcWXoxDKDY2JrfPMVMaruGlSEE1KyHi7c8fn-----END PRIVATE KEY-----");
        ReflectionTestUtils.setField(service, "ibmClientId", "ibmClientId");
        return service;
    }
}