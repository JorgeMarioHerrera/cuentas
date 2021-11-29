package co.com.bancolombia.api;


import co.com.bancolombia.model.api.ResponseToFrontFin;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.finalization.FinalizationRequest;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.usecase.finalization.FinalizationUseCase;
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

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes={FinalizationRest.class})
@EnableWebMvc
public class FinalizationRestTest {
    @MockBean
    private FinalizationUseCase finalizationUseCase;
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private  static final String ID_SESSION="9999999";
    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testShouldResponseHTTP500WhenOauthFailed() throws Exception {
        FinalizationRequest texto = FinalizationRequest.builder().deliveryMessage("texto").build();
        doReturn(Either.left(Error.builder().errorCode("FINFIN-002").build())).when(finalizationUseCase).finalization(ID_SESSION, texto);

        mockMvc.perform(MockMvcRequestBuilders.post("/finalization")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .header("sessionID",ID_SESSION)
                .content(asJsonString(texto)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect( jsonPath("$.errorCode").value("FINFIN-002"));
    }

    @Test
    public void testShouldResponseHTTP200WhenOauthSuccess() throws Exception {
        FinalizationRequest texto = FinalizationRequest.builder().deliveryMessage("texto").build();
        ResponseToFrontFin responseToFrontFin = ResponseToFrontFin.builder().emailSent(true).build();
        doReturn(Either.right(responseToFrontFin)).when(finalizationUseCase).finalization(ID_SESSION, texto);

        mockMvc.perform(MockMvcRequestBuilders.post("/finalization")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .header("sessionID",ID_SESSION)
                .content(asJsonString(texto)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect( jsonPath("$.emailSent").value(responseToFrontFin.getEmailSent()));
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
