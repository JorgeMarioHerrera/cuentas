package co.com.bancolombia.assignadvisor.service;


import bancolombia.dtd.vd.sucursales.proxy.mantenimientocuentadeposito.facade.impl.MantenimientoCuentasDepositoImpl;
import bancolombia.dtd.vd.sucursales.proxy.mantenimientocuentadeposito.util.ProxyException;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import com.grupobancolombia.ents.common.genericexception.v2.GenericException;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.enlace.v1.BusinessExceptionMsg;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.enlace.v1.SystemExceptionMsg;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.v1.AsignarAsesorResponse;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.v1.BusinessException;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.v1.SystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CodigoAsesorServiceTest {

    private final String urlTest = "http://urltest.com";
    private final static String ID_SESSION = "8fec29f9-fe82-4d00-a55c-30a5df6b1d66";
    private final static String JWT = "8fec29f9-fe82-4d00-a55c";

    @Mock
    private LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog("NU0170001_VTDAPI_MDSCCuentas"));
    @Mock
    private CodigoAsesorService codigoAsesorService;
    private static final String idSession = "IdSession-1234567890-asdfghjkl";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testShouldGetResponseFromCreate() throws Exception {
        MantenimientoCuentasDepositoImpl mantenimientoCuentasDepositoImplMock =
                Mockito.mock(MantenimientoCuentasDepositoImpl.class);
        when(mantenimientoCuentasDepositoImplMock.asignarAsesor(any(), any(), any())).
                thenReturn(new AsignarAsesorResponse());
        // Create class with injections for constructor and set @Value of Spring
        CodigoAsesorService service = spy(new CodigoAsesorService(loggerApp));
        doReturn(mantenimientoCuentasDepositoImplMock).when(service).getMantenimientoCuentasDepositoImpl();
        Either<ErrorExeption, AsignarAsesorResponse> result = service.asignarAsesor(new HashMap<>(),"1",
                "123",ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
        assertNotNull(result.getRight());
    }

    @Test
    void testShouldGetException1FromCreate() throws Exception {
        BusinessException ex = new BusinessException();
        ex.setGenericException(new GenericException());
        MantenimientoCuentasDepositoImpl mantenimientoCuentasDepositoImplMock =
                Mockito.mock(MantenimientoCuentasDepositoImpl.class);
        when(mantenimientoCuentasDepositoImplMock.asignarAsesor(any(), any(), any())).
                thenThrow(new BusinessExceptionMsg("BusinessException", ex));
        // Create class with injections for constructor and set @Value of Spring
        CodigoAsesorService service = spy(new CodigoAsesorService(loggerApp));
        doReturn(mantenimientoCuentasDepositoImplMock).when(service).getMantenimientoCuentasDepositoImpl();
        Either<ErrorExeption, AsignarAsesorResponse> result = service.asignarAsesor(new HashMap<>(),"1",
                "123",ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }

    @Test
    void testShouldGetException2FromCreate() throws Exception {
        MantenimientoCuentasDepositoImpl mantenimientoCuentasDepositoImplMock =
                Mockito.mock(MantenimientoCuentasDepositoImpl.class);
        when(mantenimientoCuentasDepositoImplMock.asignarAsesor(any(), any(), any())).
                thenThrow(new ProxyException("ProxyException"));
        // Create class with injections for constructor and set @Value of Spring
        CodigoAsesorService service = spy(new CodigoAsesorService(loggerApp));
        doReturn(mantenimientoCuentasDepositoImplMock).when(service).getMantenimientoCuentasDepositoImpl();
        Either<ErrorExeption, AsignarAsesorResponse> result = service.asignarAsesor(new HashMap<>(),"1",
                "123",ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }
    @Test
    void testShouldGetException3FromCreate() throws Exception {
        SystemException ex = new SystemException();
        ex.setGenericException(new GenericException());
        MantenimientoCuentasDepositoImpl mantenimientoCuentasDepositoImplMock =
                Mockito.mock(MantenimientoCuentasDepositoImpl.class);
        when(mantenimientoCuentasDepositoImplMock.asignarAsesor(any(), any(), any())).
                thenThrow(new SystemExceptionMsg("SystemException", ex));
        // Create class with injections for constructor and set @Value of Spring
        CodigoAsesorService service = spy(new CodigoAsesorService(loggerApp));
        doReturn(mantenimientoCuentasDepositoImplMock).when(service).getMantenimientoCuentasDepositoImpl();
        Either<ErrorExeption, AsignarAsesorResponse> result = service.asignarAsesor(new HashMap<>(),"1",
                "123",ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }



}