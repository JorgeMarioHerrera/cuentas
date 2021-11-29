package co.com.bancolombia.jwt;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.jwtmodel.JwtModel;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.DEBUG;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.ERROR;
@Service
@RequiredArgsConstructor
class JWTService implements IJWTService {
    @Value("${security.jwt.key}")
    private String privateKey;
    @Value("${security.jwt.publicKey}")
    private String publicKey;
    @Value("${security.service.ibmClientId}")
    private String ibmClientId;
    private final JwtClaims claims = new JwtClaims();
    private JsonWebSignature jws = new JsonWebSignature();
    private final LoggerApp loggerApp;
    @Override
    public Either<ErrorExeption, JwtModel> generateJwt(String idSession) {
        Either<ErrorExeption, JwtModel> response;
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_JWT, idSession);
        try {
            JwtClaims claims = buildClaims(idSession);
            PrivateKey key = (PrivateKey) loadKey(true, null);
            String jwt = generateJwtSha256(claims, key);
            loggerApp.logger(LoggerOptions.Actions.JWT_GENARATED, jwt, DEBUG, null);
            response = Either.right(JwtModel.builder().idSession(idSession).jwt(jwt).build());
        } catch (GeneralSecurityException e) {
            loggerApp.logger(LoggerOptions.Actions.JWT_EXCEPTION_SECURITY, e.getMessage(), ERROR, e);
            response = Either.left(ErrorsEnum.JWT_EXCEPTION_SECURITY.buildError());
        } catch (JoseException e) {
            loggerApp.logger(LoggerOptions.Actions.JWT_EXCEPTION_DECRYP, e.getMessage(), ERROR, e);
            response = Either.left(ErrorsEnum.JWT_EXCEPTION_DECRYP.buildError());
        } catch (MalformedClaimException e) {
            loggerApp.logger(LoggerOptions.Actions.JWT_EXCEPTION_MALFORMED, e.getMessage(), ERROR, e);
            response = Either.left(ErrorsEnum.JWT_EXCEPTION_MALFORMED.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(LoggerOptions.Actions.JWT_ERR_VALIDATE_UNKNOWN, e.getMessage(), ERROR, e);
            response = Either.left(ErrorsEnum.JWT_ERR_VALIDATE_UNKNOWN.buildError());
        }
        return response;
    }
    @Override
    public Either<ErrorExeption, String> validate(@NonNull JwtModel jwtModel) {
        Either<ErrorExeption, String> response = null;
        if (null == jwtModel.getIdSession() || jwtModel.getIdSession().isEmpty() || jwtModel.getConsumerCertificate()
                .isEmpty() || null == jwtModel.getConsumerCertificate() || jwtModel.getJwt() == null ||
                jwtModel.getJwt().isEmpty()) {
            response = Either.left(ErrorsEnum.JWT_ERR_IDSSESION_NOT_EXIST.buildError());
        } else {
            loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_JWT,
                    jwtModel.getIdSession());
            try {
                loggerApp.logger(LoggerOptions.Actions.JWT_VALIDATE, jwtModel.getJwt(), DEBUG, null);
                Key publicKey = loadKey(false, jwtModel.getConsumerCertificate());
                JsonWebSignature jwsValidate = new JsonWebSignature();
                jwsValidate.setCompactSerialization(jwtModel.getJwt());
                jwsValidate.setKey(publicKey);
                if (!jwsValidate.verifySignature()) {
                    response = Either.left(ErrorsEnum.JWT_ERR_INVALID_SIGN.buildError());
                }
            } catch (GeneralSecurityException e) {
                loggerApp.logger(LoggerOptions.Actions.JWT_ERR_SIGNING, e.getMessage(), ERROR, e);
                response = Either.left(ErrorsEnum.JWT_EXCEPTION_SECURITY.buildError());
            } catch (JoseException e) {
                loggerApp.logger(LoggerOptions.Actions.JWT_ERR_VALIDATE_JOSE, e.getMessage(), ERROR, e);
                response = Either.left(ErrorsEnum.JWT_ERR_INVALID_SIGN.buildError());
            } catch (RuntimeException e) {
                loggerApp.logger(LoggerOptions.Actions.JWT_ERR_VALIDATE_UNKNOWN, e.getMessage(), ERROR, e);
                response = Either.left(ErrorsEnum.JWT_ERR_VALIDATE_UNKNOWN.buildError());
            }
        } return null == response ? this.decodePayload(jwtModel.getJwt()): response;
    }

    @Override
    public Either<ErrorExeption, Boolean> validateExperience(JwtModel jwtModel) {
        Either<ErrorExeption, Boolean> response = null;
        if (null == jwtModel || null == jwtModel.getIdSession() || jwtModel.getIdSession().isEmpty()) {
            response = Either.left(ErrorsEnum.JWT_ERR_IDSSESION_NOT_EXIST.buildError());
        } else {
            loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_JWT,
                    jwtModel.getIdSession());
            try {
                loggerApp.logger(LoggerOptions.Actions.JWT_VALIDATE, jwtModel.getJwt(), DEBUG, null);
                Key publicKey = loadKey(false,this.publicKey);
                JsonWebSignature jwsValidate = new JsonWebSignature();
                jwsValidate.setCompactSerialization(jwtModel.getJwt());
                jwsValidate.setKey(publicKey);
                if (!jwsValidate.verifySignature()) {
                    response = Either.left(ErrorsEnum.JWT_ERR_INVALID_SIGN.buildError());
                }
            } catch (GeneralSecurityException e) {
                loggerApp.logger(LoggerOptions.Actions.JWT_ERR_SIGNING, e.getMessage(), ERROR, e);
                response = Either.left(ErrorsEnum.JWT_EXCEPTION_SECURITY.buildError());
            } catch (JoseException e) {
                loggerApp.logger(LoggerOptions.Actions.JWT_ERR_VALIDATE_JOSE, e.getMessage(), ERROR, e);
                response = Either.left(ErrorsEnum.JWT_ERR_INVALID_SIGN.buildError());
            } catch (RuntimeException e) {
                loggerApp.logger(LoggerOptions.Actions.JWT_ERR_VALIDATE_UNKNOWN, e.getMessage(), ERROR, e);
                response = Either.left(ErrorsEnum.JWT_ERR_VALIDATE_UNKNOWN.buildError());
            }
        }
        return null == response ? Either.right(true) : response;
    }

    private Either<ErrorExeption, String> decodePayload(String jwt) {
        String[] chunks = jwt.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        return Either.right(payload);
    }

    private String generateJwtSha256(JwtClaims claims, PrivateKey key) throws JoseException {
        jws.setPayload(claims.toJson());
        jws.setKey(key);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        jws.setKeyIdHeaderValue(UUID.randomUUID().toString());
        jws.setHeader("typ", "jwt");
        return jws.getCompactSerialization();
    }
    private JwtClaims buildClaims(String idSession) throws MalformedClaimException {
        Instant instant = Instant.now();
        NumericDate now = NumericDate.fromMilliseconds(instant.toEpochMilli());
        claims.setExpirationTimeMinutesInTheFuture(Constants.EXPIRATION_TOKEN_IN_MINUTES);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setClaim("idSession", idSession);
        claims.setClaim("iss", Constants.APPLICATION_NU);
        claims.setClaim("sum", ibmClientId);
        claims.setClaim("aud", Constants.APPLICATION_AUD_API_C);
        claims.setClaim("exp", claims.getExpirationTime().getValue());
        claims.setIssuedAt(now);
        claims.setNotBefore(now);
        return claims;
    }
    private Key loadKey(boolean isPrivate, String certificate) throws GeneralSecurityException {
        EncodedKeySpec keySpec;
        KeyFactory fact;
        String key;
        if (isPrivate) {
            key = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
            key = key.replace("-----END PRIVATE KEY-----", "");
        } else {
            key = certificate.replace("-----BEGIN PUBLIC KEY-----", "");
            key = key.replace("-----END PUBLIC KEY-----", "");
        }
        byte[] clear = Base64.getDecoder().decode(key);
        keySpec = isPrivate ? new PKCS8EncodedKeySpec(clear) : new X509EncodedKeySpec(clear);
        fact = KeyFactory.getInstance("RSA");
        return isPrivate ? fact.generatePrivate(keySpec) : fact.generatePublic(keySpec);
    }
}