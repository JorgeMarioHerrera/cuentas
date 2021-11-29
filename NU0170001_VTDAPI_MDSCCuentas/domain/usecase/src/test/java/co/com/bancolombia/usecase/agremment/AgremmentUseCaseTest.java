package co.com.bancolombia.usecase.agremment;


import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestGetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import co.com.bancolombia.factory.address.FactoryAddress;
import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.agremment.IAgremmentService;
import co.com.bancolombia.model.agremment.ICostService;
import co.com.bancolombia.model.api.cost.AgreementCost;
import co.com.bancolombia.model.api.cost.ResponseCost;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.account.AgremmentUseCase;

class AgremmentUseCaseTest {

    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private IRedis iRedis;
    @Mock
    LoggerAppUseCase loggerAppUseCase;
    @Mock
    IFirehoseService iFirehoseService;
    @Mock
    ICostService iCostService;
    @Mock
    IAgremmentService iAgremmentService;
    @Mock
    IRequestGetType iRequestGetType;



    LoggerAppUseCase logger = new LoggerAppUseCase(LoggerFactory.getLog("NU0104001_VTDAPI_MDSCCuentas"));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("test should get success when get aggrement")
    void testShouldGetSuccessWhenGetAgremment() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCostService.callService(any(),any()))
            .thenReturn(Either.right(FactoryAgremmentServices.getCostResponseSusses()));
        when(iAgremmentService.callService(any(),any()))
        	.thenReturn(Either.right(FactoryAgremmentServices.getResponseAgremmentNitSusses()));
        when(iRequestGetType.findCatalog(any()))
                .thenReturn(Either.right(Arrays.asList(Constants.DEFAULT_PAYROLL_PLANS)));
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest(),
                FactoryAddress.getIdSession());
        
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(1)).callService(any(),any());
        verify(iCostService, times(1)).callService(any(),any());
        
        List<AgreementCost> list = result.getRight().getAgreement();       
        assertEquals(13495, list.get(0).getAgreementCode());
        assertEquals(25, list.get(0).getPlanCode());
        assertEquals("1900.5", list.get(0).getShareCost());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("test should fail user Obtained")
    void testShouldGetFailuserObtainedisLeft() {
    	when(iRedis.getUser(any())).thenReturn(Either.left(new ErrorExeption()));
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest(),
                FactoryAddress.getIdSession());  
        verify(iFirehoseService, times(0)).save(any(), any());
        verify(iRedis, times(0)).saveUser(any());
        verify(iAgremmentService, times(0)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }
    
    @Test
    @DisplayName("test should fail user Obtained sesion")
    void testShouldGetFailuserObtainedisLeftsesion() {
    	when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalInvalidSession()));
    	AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest(),
                FactoryAddress.getIdSession());  
        verify(iFirehoseService, times(0)).save(any(), any());
        verify(iRedis, times(0)).saveUser(any());
        verify(iAgremmentService, times(0)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }
    
    @Test
    @DisplayName("test should fail user Obtained doc number")
    void testShouldGetFailuserObtainedisLeftDocNumber() {
    	when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValidForNullDocType()));
    	AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest(),
                FactoryAddress.getIdSession());  
        verify(iFirehoseService, times(0)).save(any(), any());
        verify(iRedis, times(0)).saveUser(any());
        verify(iAgremmentService, times(0)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }
    
    @Test
    @DisplayName("test should get Fail When Attemps Mayor")
    void testShouldGetFailWhenAttempsMayor() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalMaxAttemps()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.left(new ErrorExeption()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
        .thenReturn(Error.builder().errorDescription(ErrorDescription.builder().build()).build());
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest(),
                FactoryAddress.getIdSession());
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(0)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());  
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }
    
    @Test
    @DisplayName("test should get Fail save redis")
    void testShouldGetFailWhenSaveRedis() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalMaxAttemps()));
        when(iRedis.saveUser(any())).thenReturn(Either.left(new ErrorExeption()));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest(),
                FactoryAddress.getIdSession());
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(0)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());  
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }
    
    @Test
    @DisplayName("test should get Fail When Cresponse Agremment Nit isLeft And Not Reply")
    void testShouldGetFailWhenCresponseAgremmentNitisLeftAndNotReply() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iAgremmentService.callService(any(),any()))
        	.thenReturn(Either.left(new ErrorExeption()));
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest(),
                FactoryAddress.getIdSession());
        
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(1)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());
        
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }
    
    @Test
    @DisplayName("test should get Fail When NIT IS 890903938")
    void testShouldGetFailWhenNitIs890903938() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iAgremmentService.callService(any(),any()))
        	.thenReturn(Either.left(new ErrorExeption()));
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest890903938(),
                FactoryAddress.getIdSession());
        
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(0)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());
        
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }
    
    @Test
    @DisplayName("test should get Fail When NIT IS 8909039388")
    void testShouldGetFailWhenNitIs8909039388() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iAgremmentService.callService(any(),any()))
        	.thenReturn(Either.left(new ErrorExeption()));
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest8909039388(),
                FactoryAddress.getIdSession());
        
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(0)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());
        
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }
    
    @SuppressWarnings("unchecked")
	@Test
    @DisplayName("test should get success when get aggrement retry")
    void testShouldGetSuccessWhenGetAgremmentRetry() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCostService.callService(any(),any()))
            .thenReturn(Either.right(FactoryAgremmentServices.getCostResponseSusses()));
        when(iAgremmentService.callService(any(),any()))
        	.thenReturn(Either.left(ErrorExeption.builder().code("CUDAAGR-BP0319").build())
        	,Either.right(FactoryAgremmentServices.getResponseAgremmentNitSusses())		
        );
        when(iRequestGetType.findCatalog(any()))
                .thenReturn(Either.right(Arrays.asList(Constants.DEFAULT_PAYROLL_PLANS)));
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequestRetry(),
                FactoryAddress.getIdSession());
        
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(2)).callService(any(),any());
        verify(iCostService, times(1)).callService(any(),any());
        
        List<AgreementCost> list = result.getRight().getAgreement();       
        assertEquals(13495, list.get(0).getAgreementCode());
        assertEquals(25, list.get(0).getPlanCode());
        assertEquals("1900.5", list.get(0).getShareCost());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }
    
    @SuppressWarnings("unchecked")
	@Test
    @DisplayName("test should get fail when get aggrement retry not verity digit")
    void testShouldGetSuccessWhenGetAgremmentRetryNotDigitVerify() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCostService.callService(any(),any()))
            .thenReturn(Either.right(FactoryAgremmentServices.getCostResponseSusses()));
        when(iAgremmentService.callService(any(),any()))
        	.thenReturn(Either.left(ErrorExeption.builder().code("BP0319").build())
        	,Either.right(FactoryAgremmentServices.getResponseAgremmentNitSusses())		
        );
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest(),
                FactoryAddress.getIdSession());
        
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(1)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());
        
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }
    
    @SuppressWarnings("unchecked")
	@Test
    @DisplayName("test should get fail when get aggrement retry")
    void testShouldGetFailWhenGetAgremmentRetry() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCostService.callService(any(),any()))
            .thenReturn(Either.right(FactoryAgremmentServices.getCostResponseSusses()));
        when(iAgremmentService.callService(any(),any()))
        	.thenReturn(Either.left(ErrorExeption.builder().code("CUDAAGR-BP0319").build()));
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequestRetry(),
                FactoryAddress.getIdSession());
        
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(2)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());
        
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }   
    
    @SuppressWarnings("unchecked")
	@Test
    @DisplayName("test should get fail when get aggrement retry")
    void testShouldGetFailWhenGetAgremmentRetryNot() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCostService.callService(any(),any()))
            .thenReturn(Either.right(FactoryAgremmentServices.getCostResponseSusses()));
        when(iAgremmentService.callService(any(),any()))
        	.thenReturn(Either.left(ErrorExeption.builder().code("CUDAAGR-BP0319").build()));
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequestRetrynot(),
                FactoryAddress.getIdSession());
        
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(1)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());
        
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }  
    
    @Test
    @DisplayName("test should get fail when get share cost fail")
    void testShouldGetFailWhenSharecostFail() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCostService.callService(any(),any()))
            .thenReturn(Either.left(new ErrorExeption()));
        when(iAgremmentService.callService(any(),any()))
        	.thenReturn(Either.right(FactoryAgremmentServices.getResponseAgremmentNitSusses()));
        when(iRequestGetType.findCatalog(any()))
                .thenReturn(Either.left(new ErrorExeption()));
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest(),
                FactoryAddress.getIdSession());
        
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(1)).callService(any(),any());
        verify(iCostService, times(1)).callService(any(),any());
        
      
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }
    
    
    @Test
    @DisplayName("test should get fail when get aggrement is empty")
    void testShouldGetFailWhenGetAgremmentIsEmpty() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iAgremmentService.callService(any(),any()))
        	.thenReturn(Either.right(FactoryAgremmentServices.getResponseAgremmentNitFail()));
        when(iRequestGetType.findCatalog(any()))
                .thenReturn(Either.right(Arrays.asList(Constants.DEFAULT_PAYROLL_PLANS)));
        AgremmentUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iAgremmentService,iCostService);
        Either<Error, ResponseCost> result = useCase.agremmentService(FactoryAgremmentServices.getRequest(),
                FactoryAddress.getIdSession());
        
        verify(iFirehoseService, times(1)).save(any(), any());
        verify(iRedis, times(1)).saveUser(any());
        verify(iAgremmentService, times(1)).callService(any(),any());
        verify(iCostService, times(0)).callService(any(),any());
        

        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }
    

    private AgremmentUseCase getUseCase(IMessageErrorService messageErrorService, IRedis iRedis,
                                      LoggerAppUseCase logger, IFirehoseService iFirehoseService,
                                      IAgremmentService iAgremmentService,ICostService iCostService) {

    	AgremmentUseCase useCase = new AgremmentUseCase(messageErrorService,
                iRedis, iFirehoseService, logger,iAgremmentService,iCostService, iRequestGetType);
    	
    	ReflectionTestUtils.setField(useCase, "maxTry", 3);
        return useCase;
    }


}