package co.com.bancolombia.jwt;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import co.com.bancolombia.firehose.impl.FunctionalReportService;
import co.com.bancolombia.kinesisfirehoseservice.KinesisFirehoseService;
import co.com.bancolombia.model.either.Either;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.firehose.model.InvalidArgumentException;
import software.amazon.awssdk.services.firehose.model.ResourceNotFoundException;
import software.amazon.awssdk.services.firehose.model.ServiceUnavailableException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KinesisFireHouseTest {
    @Mock
    private FunctionalReportService functionalReportService;

    @Mock
    LoggerApp loggerApp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testShouldSimulateSussesWhenExecute() throws Exception {
        when(functionalReportService.sendStepFuncionalMDSC(any(),any())).thenReturn(true );
        KinesisFirehoseService functionalReportUseCase = new KinesisFirehoseService(loggerApp,functionalReportService);
        Either<ErrorExeption, Boolean>  result = functionalReportUseCase.save(new HashMap<String, String>(),"12345");
        assertTrue(result.isRight());
    }

    @Test
    void testShouldSimulateSussesWhenExecuteFalse() throws Exception {
        when(functionalReportService.sendStepFuncionalMDSC(any(),any())).thenReturn(false );
        KinesisFirehoseService functionalReportUseCase = new KinesisFirehoseService(loggerApp,functionalReportService);
        Either<ErrorExeption, Boolean>  result = functionalReportUseCase.save(new HashMap<String, String>(),"12345");
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateExceptionWhenExecuteInvalidArgumentException() throws Exception {
        when(functionalReportService.sendStepFuncionalMDSC(any(),any())).thenThrow(InvalidArgumentException.builder().build());
        KinesisFirehoseService functionalReportUseCase = new KinesisFirehoseService(loggerApp,functionalReportService);
        Either<ErrorExeption, Boolean>  result = functionalReportUseCase.save(new HashMap<String, String>(),"12345");
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateExceptionWhenExecuteResourceNotFoundException() throws Exception {
        when(functionalReportService.sendStepFuncionalMDSC(any(),any())).thenThrow(ResourceNotFoundException.builder().build());
        KinesisFirehoseService functionalReportUseCase = new KinesisFirehoseService(loggerApp,functionalReportService);
        Either<ErrorExeption, Boolean>  result = functionalReportUseCase.save(new HashMap<String, String>(),"12345");
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateExceptionWhenExecuteServiceUnavailableException() throws Exception {
        when(functionalReportService.sendStepFuncionalMDSC(any(),any())).thenThrow(ServiceUnavailableException.builder().build());
        KinesisFirehoseService functionalReportUseCase = new KinesisFirehoseService(loggerApp,functionalReportService);
        Either<ErrorExeption, Boolean>  result = functionalReportUseCase.save(new HashMap<String, String>(),"12345");
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateExceptionWhenExecuteAwsServiceException() throws Exception {
        when(functionalReportService.sendStepFuncionalMDSC(any(),any())).thenThrow(AwsServiceException.builder().build());
        KinesisFirehoseService functionalReportUseCase = new KinesisFirehoseService(loggerApp,functionalReportService);
        Either<ErrorExeption, Boolean>  result = functionalReportUseCase.save(new HashMap<String, String>(),"12345");
        assertTrue(result.isLeft());
    }
    @Test
    void testShouldSimulateExceptionWhenExecuteSdkException() throws Exception {
        when(functionalReportService.sendStepFuncionalMDSC(any(),any())).thenThrow(SdkException.builder().build());
        KinesisFirehoseService functionalReportUseCase = new KinesisFirehoseService(loggerApp,functionalReportService);
        Either<ErrorExeption, Boolean>  result = functionalReportUseCase.save(new HashMap<String, String>(),"12345");
        assertTrue(result.isLeft());
    }

}

