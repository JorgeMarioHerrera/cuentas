package co.com.bancolombia.assignadvisor.impl;


import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.assignadvisor.impl.factory.FactoryAssignAdvisor;
import co.com.bancolombia.assignadvisor.service.CodigoAsesorService;
import co.com.bancolombia.logging.technical.LoggerFactory;


import co.com.bancolombia.model.assignadviser.AssignAdvisorResponse;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;


import com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.v1.AsignarAsesorResponse;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AssignAdviserServiceTest {

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
        when(codigoAsesorService.asignarAsesor(any(), any(), any(), any())).
                thenReturn(Either.right(new AsignarAsesorResponse()));
        // Create class with injections for constructor and set @Value of Spring
        AssignAdvicerService service = new AssignAdvicerService(codigoAsesorService);
        addProperties(service, loggerApp);
        Either<ErrorExeption, AssignAdvisorResponse> result = service.assignAdviser(FactoryAssignAdvisor.getRequest(),
                ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
        assertNotNull(result.getRight());
    }

    @Test
    void testShouldGetException1FromCreate() throws Exception {

        when(codigoAsesorService.asignarAsesor(any(), any(), any(), any())).
                thenReturn(Either.left(new ErrorExeption()));
        // Create class with injections for constructor and set @Value of Spring
        AssignAdvicerService service = new AssignAdvicerService(codigoAsesorService);
        addProperties(service, loggerApp);
        Either<ErrorExeption, AssignAdvisorResponse> result = service.assignAdviser(FactoryAssignAdvisor.getRequest(),
                ID_SESSION);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }


    private void addProperties(AssignAdvicerService assignAdvicerService, LoggerApp loggerApp) {
        ReflectionTestUtils.setField(assignAdvicerService, "requestTimeout", "500");
        ReflectionTestUtils.setField(assignAdvicerService, "connectionTimeout", "500");
        ReflectionTestUtils.setField(assignAdvicerService, "endpointAccountsManagement", "link");
        ReflectionTestUtils.setField(assignAdvicerService, "fwkShowXmlSoapBody", "true");
    }


}