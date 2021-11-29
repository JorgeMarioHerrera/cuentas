package co.com.bancolombia.apirest;

import co.com.bancolombia.api.ApiRest;
import co.com.bancolombia.apirest.factory.Factory;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.error.RequestGetError;
import co.com.bancolombia.model.errorexception.ErrorStatus;
import co.com.bancolombia.usecase.dto.StatusError;
import co.com.bancolombia.usecase.ErrorUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes={ApiRest.class})
@EnableWebMvc
class ApiRestTest {

    @MockBean
    private ErrorUseCase errorUseCase;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    void test() {
        /**
         * Arrange RequestBody Add-Error
         * */
        Error errorTest = Factory.returnObjectErrorTest("objectErrorTestFailed.json");
        List<Error> errorList = new ArrayList<Error>();
        errorList.add(errorTest);
    }
    @Test
    void testShouldResponseHTTP400InPathAddError() throws Exception {
        Error errorTest = Factory.returnObjectErrorTest("objectErrorTestFailed.json");
        List<ErrorStatus> errorListException = new ArrayList<ErrorStatus>();
        ErrorStatus errorStatus = ErrorStatus.builder()
                .code("ERR005")
                .description("Ya existe un error con el mismo codigo en la misma aplicacion").build();
        errorListException.add(errorStatus);
        List<Error> errorList = new ArrayList<Error>();
        errorList.add(errorTest);
        doReturn(StatusError.builder().errorFailed(errorListException)
                .errorSuccess(new ArrayList<Error>()).build())
                .when(errorUseCase).addError(any());
        mockMvc.perform(MockMvcRequestBuilders.post("/addError")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .content(asJsonString(errorList)))
                .andDo(print())

                // Validate the response is badRequest
                .andExpect(status().isBadRequest())
                // Validate the returned fields
                .andExpect(jsonPath("$[0].code").value(errorListException.get(0).getCode()))
                .andExpect(jsonPath("$[0].description").value(errorListException.get(0).getDescription()));

    }

    @Test
    void testShouldResponseCREATEDInPathAddError() throws Exception {
        Error errorTest = Factory.returnObjectErrorTest("objectErrorTest.json");
        List<Error> errorList = new ArrayList<>();
        errorList.add(errorTest);
        doReturn(StatusError.builder().errorSuccess(errorList).build()).when(errorUseCase).addError(any());
        //doReturn(Either.right(errorList)).when(errorUseCase).addError(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/addError")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .content(asJsonString(errorList)))
                .andDo(print())

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.errorSuccess[0].errorCode").value(errorTest.getErrorCode()))
                .andExpect(jsonPath("$.errorSuccess[0].applicationId").value(errorTest.getApplicationId()))
                .andExpect(jsonPath("$.errorSuccess[0].errorDescription.errorService")
                        .value(errorTest.getErrorDescription().getErrorService()))
                .andExpect(jsonPath("$.errorSuccess[0].errorDescription.errorType")
                        .value(errorTest.getErrorDescription().getErrorType()))
                .andExpect(jsonPath("$.errorSuccess[0].errorDescription.errorOperation")
                        .value(errorTest.getErrorDescription().getErrorOperation()))
                .andExpect(jsonPath("$.errorSuccess[0].errorDescription.exceptionType")
                        .value(errorTest.getErrorDescription().getExceptionType()))
                .andExpect(jsonPath("$.errorSuccess[0].errorDescription.functionalCode")
                        .value(errorTest.getErrorDescription().getFunctionalCode()))
                .andExpect(jsonPath("$.errorSuccess[0].errorDescription.technicalDescription")
                        .value(errorTest.getErrorDescription().getTechnicalDescription()))
                .andExpect(jsonPath("$.errorSuccess[0].errorDescription.functionalDescription")
                        .value(errorTest.getErrorDescription().getFunctionalDescription())
                );
    }

    @Test
    void testShouldResponse200ObtainErrorByPartitionAndShortKey() throws Exception {
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();
        Error errorTest = Factory.returnObjectErrorTest("objectErrorTest.json");

        doReturn(errorTest).when(errorUseCase).obtainErrorByPartitionAndShortKey(requestGetErrorDefault);
        mockMvc.perform(MockMvcRequestBuilders.post("/obtainErrorByPartitionAndShortKey")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .content(asJsonString(requestGetErrorDefault))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.errorCode").value(errorTest.getErrorCode()))
                .andExpect(jsonPath("$.applicationId").value(errorTest.getApplicationId()))
                .andExpect(jsonPath("$.errorDescription.errorService")
                        .value(errorTest.getErrorDescription().getErrorService()))
                .andExpect(jsonPath("$.errorDescription.errorType")
                        .value(errorTest.getErrorDescription().getErrorType()))
                .andExpect(jsonPath("$.errorDescription.errorOperation")
                        .value(errorTest.getErrorDescription().getErrorOperation()))
                .andExpect(jsonPath("$.errorDescription.exceptionType")
                        .value(errorTest.getErrorDescription().getExceptionType()))
                .andExpect(jsonPath("$.errorDescription.functionalCode")
                        .value(errorTest.getErrorDescription().getFunctionalCode()))
                .andExpect(jsonPath("$.errorDescription.technicalDescription")
                        .value(errorTest.getErrorDescription().getTechnicalDescription()))
                .andExpect(jsonPath("$.errorDescription.functionalDescription")
                        .value(errorTest.getErrorDescription().getFunctionalDescription()));
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}