package co.com.bancolombia.api;

import co.com.bancolombia.model.api.ResponseToFrontID;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.usecase.inputdata.InputDataUseCase;
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

@SpringBootTest(classes={InputDataRest.class})
@EnableWebMvc
public class InputDataRestTest {
    @MockBean
    private InputDataUseCase inputDataUseCase;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private  static final String ID_SESSION="9999999";
    private  static final String CONSUMER ="001";
    private  static final String JWT= "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE2Mjc1NzQ1NzkuNTg2LCJpYXQiOjE2Mjc0ODg1NzkuNTg2LCJkb2N1bWVudFR5cGUiOiJjYyIsImRvY3VtZW50TnVtYmVyIjoiMTIzNDU2Nzg5IiwicHJvZHVjdElkIjoiMTExIiwiY2xpZW50Q2F0ZWdvcnkiOiJDQSIsInNlc3Npb25JZCI6IjEyMzQ1Njc4OTAxMjM0NTY3ODkyMTIzNDU2Nzg5IiwiYXV0aENvZGUiOiIifQ.OmY3CsEiN48RVSJ_vtet1boE9LOzfAp2WO9qs_0gKeEXlIUXisTYx1DPC1r2wGumVu9LWQylunEJZSnE9_LSvm9f9NqTSgONeaKgI9s8tLsks_EAev34SEEoXuapEH8w2mygYMcBnJf2svXHc_xyPmr77sBgfy1y1Vy5Cwz8T3v33rk8CydbHLGGUV8CkUgttZvq_Ua2-hAUqUswcCSv6C-m5T1PBeXr2M0BMrLJY508O2DhOCVF1Lzu1wG9sLtnl7YfeyaQuG5dTUvOLhX3hVsK37hJSbGwdAu_x5SPtT1M1Uee6KL4uxucepf9mTUxr53Vqn8wH8awCjPaZBSgog";
    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testShouldResponseHTTP500WhenInputDataFailed() throws Exception {
        doReturn(Either.left(Error.builder().errorCode("VALWE-007").build())).when(inputDataUseCase).inputData(JWT, CONSUMER, ID_SESSION);
        mockMvc.perform(MockMvcRequestBuilders.post("/inputData")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .header("jwt",JWT)
                .header("consumer",CONSUMER)
                .header("sessionID",ID_SESSION)
                .content(asJsonString("")))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect( jsonPath("$.errorCode").value("VALWE-007"));
    }

    @Test
    public void testShouldResponseHTTP200WhenInputDataSuccess() throws Exception {
        ResponseToFrontID responseToFrontID = new ResponseToFrontID(true, "true","JWT");
        doReturn(Either.right(responseToFrontID)).when(inputDataUseCase).inputData(JWT, CONSUMER, ID_SESSION);
        mockMvc.perform(MockMvcRequestBuilders.post("/inputData")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .header("jwt",JWT)
                .header("consumer",CONSUMER)
                .header("sessionID",ID_SESSION)
                .content(asJsonString("")))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect( jsonPath("$.redirectToFua").value(responseToFrontID.getRedirectToFua()));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
