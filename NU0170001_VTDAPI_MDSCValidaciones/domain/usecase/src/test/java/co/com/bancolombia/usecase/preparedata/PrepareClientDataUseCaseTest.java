package co.com.bancolombia.usecase.preparedata;

import co.com.bancolombia.factory.Factory;
import co.com.bancolombia.factory.FactoryPrepareClientDataServices;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.accountbalance.gateways.IAccountsBalancesService;
import co.com.bancolombia.model.accountlist.gateways.IAccountListService;
import co.com.bancolombia.model.api.ResponseToFrontPD;
import co.com.bancolombia.model.consolidatedbalance.gateways.IConsolidatedBalanceService;
import co.com.bancolombia.model.customerdata.gateways.ICustomerDataService;
import co.com.bancolombia.model.deviceanduser.DeviceInfo;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.notifications.gateways.INotificationsService;
import co.com.bancolombia.model.redis.Account;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

// @SpringBootTest(classes = WhoEntersUseCase.class)
public class PrepareClientDataUseCaseTest {
    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private ICustomerDataService customerDataService;
    @Mock
    private IAccountListService accountListService;
    @Mock
    private IRedis iRedis;
    @Mock
    private INotificationsService notificationsService;
    @Mock
    private IJWTService jwtService;
    @Mock
    private IConsolidatedBalanceService funds;
    @Mock
    private IAccountsBalancesService currentAccountBalances;
    @Mock
    ObjectMapper mapper;

    private final static String appName = "NU0170001_VTDAPI_MDSCValidaciones";
    LoggerAppUseCase logger = new LoggerAppUseCase(LoggerFactory.getLog(this.appName));

    @BeforeEach
    void setUp() {
        mapper =  mock(ObjectMapper.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testShouldGetErrorWhenRedisObtainUser() {
        when(iRedis.getUser(anyString())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAR-001").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALDAR-001"));
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);
        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), FactoryPrepareClientDataServices.getDeviceInfo());
        assertTrue(result.isLeft());

        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isLeft());
        Assertions.assertFalse(result.isRight());
        assertEquals("VALDAR-001", result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetErrorWhenRedisObtainUserDocumentNumberIsNull() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalNullDocumentNumber()));
        when(iRedis.saveError(any())).thenReturn(Either.right(Boolean.TRUE));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALPD-001"));
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);
        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isLeft());
        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isLeft());
        Assertions.assertFalse(result.isRight());
        assertEquals("VALPD-001", result.getLeft().getErrorCode());
    }

    @Test
    @DisplayName("Testing success when all the validations fit and the services answer success")
    void testShouldGetSuccessWhenPassingThoughtPrepareClientData() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getSavingAccountsListLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministrationNoBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getSavingAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveSavingAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
        assertNull(result.getRight().getName());
    }

    @Test
    @DisplayName("Testing Error when client doesn't have stk as dynamic mechanism")
    void testShouldGetErrorWhenPassingThoughtPrepareClientDataNOSTK() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getSavingAccountsListLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrueNoSTK()));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministrationNoBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getSavingAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveSavingAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
        assertNull(result.getRight().getName());
    }

    @Test
    @DisplayName("Testing success when all the validations fit and the services answer success")
    void testShouldGetSuccessWhenPassingThoughtPrepareClientDataSeveralContactData() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContactMoreThanOne()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getSavingAccountsListLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministrationNoBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getSavingAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveSavingAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
        assertNull(result.getRight().getName());
    }

    @Test
    @DisplayName("Testing success when all the validations fit and the services answer success")
    void testShouldGetSuccessWhenPassingThoughtPrepareClientDataSeveralContactDataAndOneIsNull() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContactMoreThanOneNull()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getSavingAccountsListLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministrationNoBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getSavingAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveSavingAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
        assertNull(result.getRight().getName());
    }

    @Test
    @DisplayName("Testing success when all the validations fit and the services answer success")
    void testShouldGetSuccessWhenPassingThoughtPrepareClientDataSeveralContactDataAndOneIsNullFirstPosition() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContactMoreThanOneNullFirstPosition()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getSavingAccountsListLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministrationNoBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getSavingAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveSavingAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
        assertNull(result.getRight().getName());
    }

    @Test
    @DisplayName("Testing Error when the customer detailed data service throws an error")
    void testShouldGetErrorWhenPrepareClientDataCustomerDetailedInformationError() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.left(ErrorExeption.builder().code("VALPD-002").build()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getSavingAccountsListLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALPD-002"));
        when(iRedis.saveError(any())).thenReturn(Either.right(Boolean.TRUE));
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isLeft());
        Assertions.assertFalse(result.isRight());
        assertEquals("VALPD-002", result.getLeft().getErrorCode());
    }

    @Test
    @DisplayName("Testing Error when the account list service throws an error")
    void testShouldGetErrorWhenPrepareClientDataAccountListError() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAAL-001").build()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALDAAL-001"));
        when(iRedis.saveError(any())).thenReturn(Either.right(Boolean.TRUE));
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isLeft());
        Assertions.assertFalse(result.isRight());
        assertEquals("VALDAAL-001", result.getLeft().getErrorCode());
    }

    @Test
    @DisplayName("Testing Error when the soft token service throws an error")
    void testShouldGetErrorWhenPrepareClientDataSoftTokenError() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getSavingAccountsListLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAAN-003").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALDAAN-003"));
        when(iRedis.saveError(any())).thenReturn(Either.right(Boolean.TRUE));
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isLeft());
        Assertions.assertFalse(result.isRight());
        assertEquals("VALDAAN-003", result.getLeft().getErrorCode());
    }

    @Test
    @DisplayName("Testing success when the Account List service retrieve an empty list")
    void testShouldGetErrorWhenPrepareClientDataAccountListEmpty() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getAccountsListEmpty()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministrationNoBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("Testing when funds returns more than 5M in balance")
    void testShouldGetErrorWhenPrepareClientDataAccountListEmptyAndFundsMoreThan5M() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getAccountsListEmpty()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministration5MInBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("Testing when account list returns more than 5M in balance")
    void testShouldGetErrorWhenPrepareClientDataAccountListMoreThan5M() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getSavingAccountsListMoreThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministration5MInBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("Testing when account balance returns more than 5M in balance")
    void testShouldGetErrorWhenPrepareClientDataAccountBalanceMoreThan5M() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getAccountsListCurrentAccountMoreThan3M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministration5MInBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValid3MBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getCurrentAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveCurrentAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);

        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("Testing success when the Account List service retrieve an Current Account with less than 5M and the validations fit")
    void testShouldGetErrorWhenPrepareClientDataAccountListCurrentAccount() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getAccountsListCurrentAccountLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
//        when(iRedis.saveError(any())).thenReturn(Either.right(Boolean.TRUE));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministrationNoBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getCurrentAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveCurrentAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);
        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("Testing success when the Account List service retrieve an Saving Account with more than 5M and the validations fit")
    void testShouldGetErrorWhenPrepareClientDataAccountListSavingAccountMoreThan5M() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getSavingAccountsListMoreThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
//        when(iRedis.saveError(any())).thenReturn(Either.right(Boolean.TRUE));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministrationNoBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getSavingAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveSavingAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);
        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("Testing success when the Notifications service retrieve false and the other validations fit, it means no home delivery for this guy")
    void testShouldGetErrorWhenPrepareClientDataNotificationsFalse() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getSavingAccountsListLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentFalse()));
//        when(iRedis.saveError(any())).thenReturn(Either.right(Boolean.TRUE));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministrationNoBalance()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getSavingAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveSavingAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);
        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("Testing Error when the funds administration service retrieve an error")
    void testShouldGetErrorWhenPrepareClientDataFundsError() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getSavingAccountsListLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
//        when(iRedis.saveError(any())).thenReturn(Either.right(Boolean.TRUE));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAFA-004").build()));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALDAFA-004"));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getSavingAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveSavingAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);
        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isLeft());
        Assertions.assertFalse(result.isRight());
        assertEquals("VALDAFA-004", result.getLeft().getErrorCode());
    }

    @Test
    @DisplayName("Testing Error when the customer balance service retrieve an error")
    void testShouldGetErrorWhenPrepareClientDataCustomerBalanceError() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getAccountsListCurrentAccountLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
//        when(iRedis.saveError(any())).thenReturn(Either.right(Boolean.TRUE));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAAB-003").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALDAAB-003"));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getCurrentAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveCurrentAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);
        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isLeft());
        Assertions.assertFalse(result.isRight());
        assertEquals("VALDAAB-003", result.getLeft().getErrorCode());
    }

    @Test
    @DisplayName("Testing Error when saving on redis")
    void testShouldGetErrorWhenSavingOnRedis() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getAccountsListCurrentAccountLessThan5M()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
//        when(iRedis.saveError(any())).thenReturn(Either.right(Boolean.TRUE));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidNoBalance()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("VALPD-003"));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministrationNoBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.left(ErrorExeption.builder().code("VALPD-003").build()));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getCurrentAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveCurrentAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);
        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isLeft());
        Assertions.assertFalse(result.isRight());
        assertEquals("VALPD-003", result.getLeft().getErrorCode());
    }

    @Test
    @DisplayName("Testing when the sum of saving, current, overdraft and funds gives more than 5M")
    void testShouldGetSuccessButFalseToDeliveryHomeWhenBalanceItsMoreThan5M() throws JsonProcessingException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getUserTransactionalValidForPrepareDataClient()));
        when(customerDataService.retrieveDetailed(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveDetailed()));
        when(customerDataService.retrieveContact(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getRetrieveContact()));
        when(accountListService.retrieveList(anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCurrentAccountsListWith1MBalance()));
        when(notificationsService.getEnrolmentInformation(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getNotificationsEnrollmentTrue()));
//        when(iRedis.saveError(any())).thenReturn(Either.right(Boolean.TRUE));
        when(currentAccountBalances.getAccountBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getCustomerAccountValidWith2MBalance()));
        when(funds.getCustomerConsolidatedBalance(anyString(), anyString(), anyString())).thenReturn(Either.right(FactoryPrepareClientDataServices.getFundsAdministrationWith2MBalance()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(mapper.writeValueAsString(any())).thenReturn(FactoryPrepareClientDataServices.getCurrentAccountAsString());
        when(mapper.readValue(anyString(),eq(Account[].class))).thenReturn( FactoryPrepareClientDataServices.getRetrieveCurrentAccountModel());
        PrepareClientDataUseCase prepareClientDataUseCase = new PrepareClientDataUseCase(messageErrorService, iRedis, logger, mapper, notificationsService, customerDataService, accountListService, funds, currentAccountBalances);
        Either<Error, ResponseToFrontPD> result = prepareClientDataUseCase.prepareClientData(Factory.getJWTModel().getIdSession(), DeviceInfo.builder().build());
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
    }

}