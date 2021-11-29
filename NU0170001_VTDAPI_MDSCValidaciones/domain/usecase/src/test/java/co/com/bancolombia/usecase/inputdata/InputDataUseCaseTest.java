package co.com.bancolombia.usecase.inputdata;


import co.com.bancolombia.factory.Factory;
import co.com.bancolombia.factory.FactoryInputDataServices;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.api.ResponseToFrontID;
import co.com.bancolombia.model.dynamo.gateways.IDynamoService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.inputdata.InputDataModel;
import co.com.bancolombia.model.jwtmodel.JwtModel;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

// @SpringBootTest(classes = InputDataUseCase.class)
public class InputDataUseCaseTest {
    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private IRedis iRedis;
    @Mock
    IDynamoService dynamo;
    @Mock
    IJWTService jwtService;
    @Mock
    ObjectMapper mapper;
    @Mock
    IFirehoseService iFirehoseService;

    private final static String appName = "NU0104001_VTDAPI_MDSCValidaciones";
    LoggerAppUseCase logger = new LoggerAppUseCase(LoggerFactory.getLog(this.appName));

    @BeforeEach
    void setUp() {
        mapper =  mock(ObjectMapper.class);
        MockitoAnnotations.initMocks(this);
    }
    private InputDataUseCase getUseCase(IMessageErrorService messageErrorService, IRedis iRedis, LoggerAppUseCase loggerAppUseCase, IDynamoService dynamo, IJWTService jwtService, ObjectMapper mapper, IFirehoseService iFirehoseService) {
        InputDataUseCase useCase = new InputDataUseCase(messageErrorService, iRedis,  logger, dynamo, jwtService, mapper,iFirehoseService);
        ReflectionTestUtils.setField(useCase,"validPlans","10,12,31,18,13,NOMINA");
        return  useCase;
    }

    @Test
    @DisplayName("test should get success when a valid consumer calls the app")
    @SneakyThrows
    void testShouldGetSuccessWhenSaveSession() {
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(dynamo.getConsumer(any(), any())).thenReturn(Either.right(FactoryInputDataServices.getValidConsumer()));
        when(jwtService.validate(any())).thenReturn(Either.right(FactoryInputDataServices.getValidJWTPayload()));
        when(jwtService.generateJwt(any())).thenReturn(Either.right(JwtModel.builder().jwt("JWTEST").build()));
        when(mapper.readValue(anyString(), eq(InputDataModel.class))).thenReturn(FactoryInputDataServices.getValidInputDataModel());
        InputDataUseCase inputDataUseCase = getUseCase(messageErrorService, iRedis, logger, dynamo,jwtService, mapper,iFirehoseService);
        Either<Error, ResponseToFrontID> result = inputDataUseCase.inputData(Factory.getJWT(), Factory.getConsumer() ,Factory.getJWTModel().getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
        assertEquals(true, result.getRight().getRedirectToFua());
    }

    @Test
    @DisplayName("test should get success when a valid consumer calls the app")
    @SneakyThrows
    void testShouldGetErrorWhenWronDocType() {
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(dynamo.getConsumer(any(), any())).thenReturn(Either.right(FactoryInputDataServices.getValidConsumer()));
        when(jwtService.validate(any())).thenReturn(Either.right(FactoryInputDataServices.getValidJWTPayload()));
        when(jwtService.generateJwt(any())).thenReturn(Either.right(JwtModel.builder().jwt("JWTEST").build()));
        when(mapper.readValue(anyString(), eq(InputDataModel.class))).thenReturn(FactoryInputDataServices.getValidInputDataModelDocTypeCC());
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALID-003"));
        InputDataUseCase inputDataUseCase = getUseCase(messageErrorService, iRedis, logger, dynamo,jwtService, mapper,iFirehoseService);
        Either<Error, ResponseToFrontID> result = inputDataUseCase.inputData(Factory.getJWT(), Factory.getConsumer() ,Factory.getJWTModel().getIdSession());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("VALID-003", result.getLeft().getErrorCode());
    }

    @Test
    @DisplayName("test should get success when a valid consumer calls the app")
    @SneakyThrows
    void testShouldGetErrorWhenWrongPlan() {
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(dynamo.getConsumer(any(), any())).thenReturn(Either.right(FactoryInputDataServices.getValidConsumer()));
        when(jwtService.validate(any())).thenReturn(Either.right(FactoryInputDataServices.getValidJWTPayload()));
        when(jwtService.generateJwt(any())).thenReturn(Either.right(JwtModel.builder().jwt("JWTEST").build()));
        when(mapper.readValue(anyString(), eq(InputDataModel.class))).thenReturn(FactoryInputDataServices.getValidInputDataModelNonExistanPlan());
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALID-004"));
        InputDataUseCase inputDataUseCase = getUseCase(messageErrorService, iRedis, logger, dynamo,jwtService, mapper,iFirehoseService);
        Either<Error, ResponseToFrontID> result = inputDataUseCase.inputData(Factory.getJWT(), Factory.getConsumer() ,Factory.getJWTModel().getIdSession());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("VALID-004", result.getLeft().getErrorCode());
    }

    @Test
    @DisplayName("test should get error when an invalid consumer calls the app")
    @SneakyThrows
    void testShouldGetErrorWhenInvalidConsumerCalls() {
        when(dynamo.getConsumer(any(), any())).thenReturn(Either.left(ErrorExeption.builder().build()));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALDCAP-002"));
        InputDataUseCase inputDataUseCase = new InputDataUseCase(messageErrorService, iRedis, logger, dynamo,jwtService, mapper,iFirehoseService);
        Either<Error, ResponseToFrontID> result = inputDataUseCase.inputData(Factory.getJWT(), Factory.getConsumer() ,Factory.getJWTModel().getIdSession());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get error while trying to save on redis")
    @SneakyThrows
    void testShouldGetErrorWhenSaveSessionOnRedis() {
        when(iRedis.saveUser(any())).thenReturn(Either.left(ErrorExeption.builder().build()));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(dynamo.getConsumer(any(), any())).thenReturn(Either.right(FactoryInputDataServices.getValidConsumer()));
        when(jwtService.validate(any())).thenReturn(Either.right(FactoryInputDataServices.getValidJWTPayload()));
        when(jwtService.generateJwt(any())).thenReturn(Either.right(JwtModel.builder().jwt("JWTEST").build()));
        when(mapper.readValue(anyString(), eq(InputDataModel.class))).thenReturn(FactoryInputDataServices.getValidInputDataModel());
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALID-002"));
        InputDataUseCase inputDataUseCase = getUseCase(messageErrorService, iRedis, logger, dynamo,jwtService, mapper,iFirehoseService);
        Either<Error, ResponseToFrontID> result = inputDataUseCase.inputData(Factory.getJWT(), Factory.getConsumer() ,Factory.getJWTModel().getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("VALID-002", result.getLeft().getErrorCode());
    }

    @Test
    @DisplayName("test should get success when a valid consumer calls the app")
    @SneakyThrows
    void testShouldGetErrorValidatingJWT() {
        when(dynamo.getConsumer(any(), any())).thenReturn(Either.right(FactoryInputDataServices.getValidConsumer()));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALID-001"));
        when(jwtService.validate(any())).thenReturn(Either.left(ErrorExeption.builder().build()));
        InputDataUseCase inputDataUseCase = new InputDataUseCase(messageErrorService, iRedis, logger, dynamo,jwtService, mapper,iFirehoseService);
        Either<Error, ResponseToFrontID> result = inputDataUseCase.inputData(Factory.getJWT(), Factory.getConsumer() ,Factory.getJWTModel().getIdSession());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("VALID-001", result.getLeft().getErrorCode());
    }


    @Test
    @DisplayName("test should get error when the jwt is error")
    @SneakyThrows
    void testShouldGetErrorWhenNotGenerateJWT() {
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(dynamo.getConsumer(any(), any())).thenReturn(Either.right(FactoryInputDataServices.getValidConsumer()));
        when(jwtService.validate(any())).thenReturn(Either.right(FactoryInputDataServices.getValidJWTPayload()));
        when(jwtService.generateJwt(any())).thenReturn(Either.left(ErrorExeption.builder().code("TESTERROR").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("TESTERROR"));
        when(mapper.readValue(anyString(), eq(InputDataModel.class))).thenReturn(FactoryInputDataServices.getValidInputDataModel());
        InputDataUseCase inputDataUseCase = getUseCase(messageErrorService, iRedis, logger, dynamo,jwtService, mapper,iFirehoseService);
        Either<Error, ResponseToFrontID> result = inputDataUseCase.inputData(Factory.getJWT(), Factory.getConsumer() ,Factory.getJWTModel().getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("TESTERROR", result.getLeft().getErrorCode());
    }
}