package co.com.bancolombia.usecase;

import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.error.RequestGetError;
import co.com.bancolombia.model.error.gateways.IErrorService;
import co.com.bancolombia.model.errorexception.ErrorStatus;
import co.com.bancolombia.usecase.dto.StatusError;
import co.com.bancolombia.usecase.factory.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@SpringBootTest(classes = ErrorUseCase.class)
public class ErrorUseCaseTest {
    private final static String appName= "NU0170001_VTDAPI_MDSCMensajesError";

    @Mock
    IErrorService errorService;

    LoggerAppUseCase logger = new LoggerAppUseCase(LoggerFactory.getLog(this.appName));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public  void testShouldAddErrorWhenError(){
        Error errorTest = Factory.getErrorObjectError();
        List<Error> errorList = new ArrayList<Error>();
        errorList.add(errorTest);

        when(errorService.addError(any(Error.class))).thenReturn(Either.right(errorTest));

        ErrorUseCase errorUseCase = new ErrorUseCase(errorService,logger);
        //Either<ErrorException, List<Error>>  response = errorUseCase.addError(errorList);
        StatusError response = errorUseCase.addError(errorList);

        //assertEquals("202020202022",response.getRight().get(0).getApplicationId());
        assertEquals("202020202022",response.getErrorSuccess().get(0).getApplicationId());
    }

    @Test
    public  void testShouldUpdateErrorWhenError(){
        Error errorTest = Factory.getErrorObjectError();
        List<Error> errorList = new ArrayList<Error>();
        errorList.add(errorTest);

        when(errorService.updateStatusBanner(any(Error.class))).thenReturn(Either.right(errorTest));

        ErrorUseCase errorUseCase = new ErrorUseCase(errorService,logger);
        //Either<ErrorException, List<Error>>  response = errorUseCase.addError(errorList);
        StatusError response = errorUseCase.updateStatusBanner(errorList);

        //assertEquals("202020202022",response.getRight().get(0).getApplicationId());
        assertEquals("202020202022",response.getErrorSuccess().get(0).getApplicationId());
    }

    @Test
    public  void testShouldUpdateErrorWhenErrorError(){
        Error errorTest = Factory.getErrorObjectError();
        List<Error> errorList = new ArrayList<Error>();
        errorList.add(errorTest);

        when(errorService.updateStatusBanner(any(Error.class))).thenReturn(Either.right(errorTest));

        ErrorUseCase errorUseCase = new ErrorUseCase(errorService,logger);
        //Either<ErrorException, List<Error>>  response = errorUseCase.addError(errorList);
        StatusError response = errorUseCase.updateStatusBanner(errorList);

        //assertEquals("202020202022",response.getRight().get(0).getApplicationId());
        assertEquals("202020202022",response.getErrorSuccess().get(0).getApplicationId());
    }

    @Test
    public  void testShouldResponseErrorObjectWhenErrorExist(){
        RequestGetError requestGetError = RequestGetError.builder()
                .appCode("202020202022")
                .codeError("CODE_01")
                .idSession("TEST")
                .build();
        Error errorTest = Factory.getErrorObjectError();
        when(errorService.obtainErrorByPartitionAndShortKey(any(RequestGetError.class))).thenReturn(Either.right(errorTest));

        ErrorUseCase errorUseCase = new ErrorUseCase(errorService,logger);
        Error  response = errorUseCase.obtainErrorByPartitionAndShortKey(requestGetError);

        assertEquals("202020202022",response.getApplicationId());
    }

    @Test
    public  void testShouldResponseErrorObjectDefaultErrorWhenErrorNoExist(){
        RequestGetError requestGetError = RequestGetError.builder()
                .appCode("202020202022")
                .codeError("CODE_01")
                .idSession("TEST")
                .build();

        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode(requestGetError.getAppCode()).codeError("DEFAULT")
                .build();

        Error errorTest = Factory.getErrorObjectErrorDefault();
        when(errorService.obtainErrorByPartitionAndShortKey(requestGetError)).thenReturn(Either.left(ErrorStatus.builder().build()));
        when(errorService.obtainErrorByPartitionAndShortKey(requestGetErrorDefault)).thenReturn(Either.right(errorTest));

        ErrorUseCase errorUseCase = new ErrorUseCase(errorService,logger);
        Error  response = errorUseCase.obtainErrorByPartitionAndShortKey(requestGetError);

        assertEquals("202020202022",response.getApplicationId());
        assertEquals("DF-".concat(requestGetError.getCodeError()),response.getErrorCode());
        assertTrue(response.getErrorDescription().isMsnIsDefault());
    }

    @Test
    public  void testShouldResponseErrorObjectFaultErrorWhenErrorNoExist(){
        RequestGetError requestGetError = RequestGetError.builder()
                .appCode("202020202022")
                .codeError("CODE_01")
                .idSession("TEST")
                .build();

        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode(requestGetError.getAppCode()).codeError("DEFAULT")
                .build();

        Error errorTest = Factory.getErrorObjectErrorDefault();
        when(errorService.obtainErrorByPartitionAndShortKey(requestGetError)).thenReturn(Either.left(ErrorStatus.builder().build()));
        when(errorService.obtainErrorByPartitionAndShortKey(requestGetErrorDefault)).thenReturn(Either.left(ErrorStatus.builder().build()));

        ErrorUseCase errorUseCase = new ErrorUseCase(errorService,logger);
        Error  response = errorUseCase.obtainErrorByPartitionAndShortKey(requestGetError);

        assertEquals("202020202022",response.getApplicationId());
        assertEquals("FAULT-".concat(requestGetError.getCodeError()),response.getErrorCode());
        assertTrue(response.getErrorDescription().isMsnIsDefault());
    }

    @Test
    public  void testShouldResponseSuccessWhenFindAll(){

        Error errorTest = Factory.getErrorObjectErrorDefault();
        when(errorService.findAll()).thenReturn(Either.right(Collections.singletonList(errorTest)));

        ErrorUseCase errorUseCase = new ErrorUseCase(errorService,logger);
        Either<ErrorStatus, List<Error>>  response = errorUseCase.findAll();

        assertTrue(response.isRight());
        assertFalse(response.isLeft());
    }

}
