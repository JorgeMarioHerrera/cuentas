package co.com.bancolombia.accountmanagementservice.impl;

import bancolombia.dtd.b1c.api.proxy.vinculacion.gestionDeCuentas.GestionDeCuentasHelper;

import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.accountmanagementservice.impl.factory.FactoryCreateAccount;
import co.com.bancolombia.logging.technical.LoggerFactory;

import co.com.bancolombia.model.activateaccount.CreateAccountRequest;
import co.com.bancolombia.model.activateaccount.CreateAccountResponse;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;


import com.grupobancolombia.ents.common.genericexception.v2.GenericException;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.vinculacion.gestiondecuentas.enlace.v1.*;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.vinculacion.gestiondecuentas.v1.*;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountServiceTest {

    private final String urlTest = "http://urltest.com";
    private final static String ID_SESSION = "8fec29f9-fe82-4d00-a55c-30a5df6b1d66";
    private final static String JWT = "8fec29f9-fe82-4d00-a55c";

    @Mock
    private LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog("NU0170001_VTDAPI_MDSCCuentas"));
    private static final String idSession = "IdSession-1234567890-asdfghjkl";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testShouldGetResponseFromCreate() throws Exception {

        GestionDeCuentasHelper gestionDeCuentasMock = Mockito.mock(GestionDeCuentasHelper.class);
        Mockito.when(gestionDeCuentasMock
                .crearCuenta(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                        anyInt(), any(), any())).thenReturn("00");
        // Create class with injections for constructor and set @Value of Spring
        AccountService service = spy(new AccountService(loggerApp));
        addProperties(service, loggerApp);
        doReturn(gestionDeCuentasMock).when(service).getGestionDeCuentasHelperInstance(Mockito.eq(ID_SESSION),
                Mockito.anyMap());
        Either<ErrorExeption, CreateAccountResponse> result = service.createAccount(FactoryCreateAccount.getRequest(),
                ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
        assertNotNull(result.getRight());
    }

    @Test
    void testShouldGetException1FromCreate() throws Exception {

        GestionDeCuentasHelper gestionDeCuentasMock = Mockito.mock(GestionDeCuentasHelper.class);
        Mockito.when(gestionDeCuentasMock
                .crearCuenta(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                        anyInt(), any(), any())).thenReturn("1");
        // Create class with injections for constructor and set @Value of Spring
        AccountService service = spy(new AccountService(loggerApp));
        addProperties(service, loggerApp);
        doReturn(gestionDeCuentasMock).when(service).getGestionDeCuentasHelperInstance(Mockito.eq(ID_SESSION),
                Mockito.anyMap());
        Either<ErrorExeption, CreateAccountResponse> result = service.createAccount(FactoryCreateAccount.getRequest(),
                ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }

    @Test
    void testShouldGetException2FromCreate() throws Exception {
        SystemException ex = new SystemException();
        ex.setGenericException(new GenericException());
        GestionDeCuentasHelper gestionDeCuentasMock = Mockito.mock(GestionDeCuentasHelper.class);
        Mockito.when(gestionDeCuentasMock
                .crearCuenta(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                        anyInt(), any(), any())).thenThrow(new SystemExceptionMsg("SystemException", ex));
        // Create class with injections for constructor and set @Value of Spring
        AccountService service = spy(new AccountService(loggerApp));
        addProperties(service, loggerApp);
        doReturn(gestionDeCuentasMock).when(service).getGestionDeCuentasHelperInstance(Mockito.eq(ID_SESSION),
                Mockito.anyMap());
        Either<ErrorExeption, CreateAccountResponse> result = service.createAccount(FactoryCreateAccount.getRequest(),
                ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }

    @Test
    void testShouldGetException3FromCreate() throws Exception {
        ConsultarPlanesNominaBusinessException ex = new ConsultarPlanesNominaBusinessException();
        ex.setGenericException(new GenericException());
        GestionDeCuentasHelper gestionDeCuentasMock = Mockito.mock(GestionDeCuentasHelper.class);
        Mockito.when(gestionDeCuentasMock
                .crearCuenta(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                        anyInt(), any(), any())).thenThrow(new ConsultarPlanesNominaBusinessExceptionMsg(
                                "ConsultarPlanesNominaBusinessException", ex));
        // Create class with injections for constructor and set @Value of Spring
        AccountService service = spy(new AccountService(loggerApp));
        addProperties(service, loggerApp);
        doReturn(gestionDeCuentasMock).when(service).getGestionDeCuentasHelperInstance(Mockito.eq(ID_SESSION),
                Mockito.anyMap());
        Either<ErrorExeption, CreateAccountResponse> result = service.createAccount(FactoryCreateAccount.getRequest(),
                ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }


    @Test
    void testShouldGetException4FromCreate() throws Exception {
        ConsultarCuentaAhorroALaManoBusinessException ex = new ConsultarCuentaAhorroALaManoBusinessException();
        ex.setGenericException(new GenericException());
        GestionDeCuentasHelper gestionDeCuentasMock = Mockito.mock(GestionDeCuentasHelper.class);
        Mockito.when(gestionDeCuentasMock
                .crearCuenta(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                        anyInt(), any(), any())).thenThrow(new ConsultarCuentaAhorroALaManoBusinessExceptionMsg(
                "ConsultarCuentaAhorroALaManoBusinessException", ex));
        // Create class with injections for constructor and set @Value of Spring
        AccountService service = spy(new AccountService(loggerApp));
        addProperties(service, loggerApp);
        doReturn(gestionDeCuentasMock).when(service).getGestionDeCuentasHelperInstance(Mockito.eq(ID_SESSION),
                Mockito.anyMap());
        Either<ErrorExeption, CreateAccountResponse> result = service.createAccount(FactoryCreateAccount.getRequest(),
                ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }

    @Test
    void testShouldGetException5FromCreate() throws Exception {
        AtarCuentaATarjetaBusinessException ex = new AtarCuentaATarjetaBusinessException();
        ex.setGenericException(new GenericException());
        GestionDeCuentasHelper gestionDeCuentasMock = Mockito.mock(GestionDeCuentasHelper.class);
        Mockito.when(gestionDeCuentasMock
                .crearCuenta(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                        anyInt(), any(), any())).thenThrow(new AtarCuentaATarjetaBusinessExceptionMsg(
                "AtarCuentaATarjetaBusinessException", ex));
        // Create class with injections for constructor and set @Value of Spring
        AccountService service = spy(new AccountService(loggerApp));
        addProperties(service, loggerApp);
        doReturn(gestionDeCuentasMock).when(service).getGestionDeCuentasHelperInstance(Mockito.eq(ID_SESSION),
                Mockito.anyMap());
        Either<ErrorExeption, CreateAccountResponse> result = service.createAccount(FactoryCreateAccount.getRequest(),
                ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }

    @Test
    void testShouldGetException6FromCreate() throws Exception {
        CrearCuentaBusinessException ex = new CrearCuentaBusinessException();
        ex.setGenericException(new GenericException());
        GestionDeCuentasHelper gestionDeCuentasMock = Mockito.mock(GestionDeCuentasHelper.class);
        Mockito.when(gestionDeCuentasMock
                .crearCuenta(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                        anyInt(), any(), any())).thenThrow(new CrearCuentaBusinessExceptionMsg(
                "CrearCuentaBusinessException", ex));
        // Create class with injections for constructor and set @Value of Spring
        AccountService service = spy(new AccountService(loggerApp));
        addProperties(service, loggerApp);
        doReturn(gestionDeCuentasMock).when(service).getGestionDeCuentasHelperInstance(Mockito.eq(ID_SESSION),
                Mockito.anyMap());
        Either<ErrorExeption, CreateAccountResponse> result = service.createAccount(FactoryCreateAccount.getRequest(),
                ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }


    @Test
    void testShouldGetException7FromCreate() throws Exception {
        GestionDeCuentasHelper gestionDeCuentasMock = Mockito.mock(GestionDeCuentasHelper.class);
        Mockito.when(gestionDeCuentasMock
                .crearCuenta(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                        anyInt(), any(), any())).thenThrow(new Exception("Exception"));
        // Create class with injections for constructor and set @Value of Spring
        AccountService service = spy(new AccountService(loggerApp));
        addProperties(service, loggerApp);
        doReturn(gestionDeCuentasMock).when(service).getGestionDeCuentasHelperInstance(Mockito.eq(ID_SESSION),
                Mockito.anyMap());
        Either<ErrorExeption, CreateAccountResponse> result = service.createAccount(FactoryCreateAccount.getRequest(),
                ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }



    private AccountService addProperties(AccountService accountService, LoggerApp loggerApp) {

        ReflectionTestUtils.setField(accountService, "requestTimeout", "500");
        ReflectionTestUtils.setField(accountService, "connectionTimeout", "500");
        ReflectionTestUtils.setField(accountService, "endpointAccountsManagement", "link");
        ReflectionTestUtils.setField(accountService, "fwkShowXmlSoapBody", "true");

        return accountService;
    }


}