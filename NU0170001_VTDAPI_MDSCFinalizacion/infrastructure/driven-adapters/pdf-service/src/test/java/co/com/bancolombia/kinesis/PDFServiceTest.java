package co.com.bancolombia.kinesis;

import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.kinesis.factory.Factory;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.pdfservice.PDFService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PDFServiceTest {

    private final LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog(String.valueOf(this.getClass())));


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testShouldSimulateIllegalStateExceptionWhenExecuteOkhttpClient() throws IOException {
        PDFService pdfService = getService();
        String result = pdfService.buildPdfWelcome(Factory.getUser(),"La tarjeta débito será entregada entre 1 y 3 días hábiles, recuerda activarla una vez la recibas.");
        Assert.notNull(result,"not compliance");
    }

    private PDFService getService() {
        String  resources = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator;

        PDFService pdfService = new PDFService();
        ReflectionTestUtils.setField(pdfService, "dirTemplateWelcome", resources);
        ReflectionTestUtils.setField(pdfService, "templateFileName", "CARTA_BIENVENIDA_CUENTA_AHORROS.pdf");
        ReflectionTestUtils.setField(pdfService, "urlGooglePlay", "https://play.google.com/store/apps/details?id=com.todo1.mobile&hl=es_CO");
        ReflectionTestUtils.setField(pdfService, "urlAppStore", "https://apps.apple.com/co/app/bancolombia-app-personas/id565101003");
        ReflectionTestUtils.setField(pdfService, "urlRegulation", "https://www.grupobancolombia.com/wcm/connect/7cf88e07-1376-47a0-8ca3-daa38213f0cc/F-1238-8003002ReglamentoCuentaAhorros.pdf?MOD=AJPERES");
        return  pdfService;
    }


}

