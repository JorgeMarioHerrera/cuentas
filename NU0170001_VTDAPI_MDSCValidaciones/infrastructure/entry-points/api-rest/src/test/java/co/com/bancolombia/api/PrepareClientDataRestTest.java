package co.com.bancolombia.api;

import co.com.bancolombia.model.api.ResponseToFrontPD;
import co.com.bancolombia.model.deviceanduser.DeviceInfo;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.usecase.preparedata.PrepareClientDataUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes={PrepareClientDataRest.class})
@EnableWebMvc
public class PrepareClientDataRestTest {
    @MockBean
    private PrepareClientDataUseCase prepareClientDataUseCase;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private  static final String ID_SESSION="999999000000000000000000000009";
    private  static final String JWT= "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE2Mjc1NzQ1NzkuNTg2LCJpYXQiOjE2Mjc0ODg1NzkuNTg2LCJkb2N1bWVudFR5cGUiOiJjYyIsImRvY3VtZW50TnVtYmVyIjoiMTIzNDU2Nzg5IiwicHJvZHVjdElkIjoiMTExIiwiY2xpZW50Q2F0ZWdvcnkiOiJDQSIsInNlc3Npb25JZCI6IjEyMzQ1Njc4OTAxMjM0NTY3ODkyMTIzNDU2Nzg5IiwiYXV0aENvZGUiOiIifQ.OmY3CsEiN48RVSJ_vtet1boE9LOzfAp2WO9qs_0gKeEXlIUXisTYx1DPC1r2wGumVu9LWQylunEJZSnE9_LSvm9f9NqTSgONeaKgI9s8tLsks_EAev34SEEoXuapEH8w2mygYMcBnJf2svXHc_xyPmr77sBgfy1y1Vy5Cwz8T3v33rk8CydbHLGGUV8CkUgttZvq_Ua2-hAUqUswcCSv6C-m5T1PBeXr2M0BMrLJY508O2DhOCVF1Lzu1wG9sLtnl7YfeyaQuG5dTUvOLhX3hVsK37hJSbGwdAu_x5SPtT1M1Uee6KL4uxucepf9mTUxr53Vqn8wH8awCjPaZBSgog";
    private  static final String IP = "0.0.0.0";
    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testShouldResponseHTTP200WhenPrepareClientDataSuccess() throws Exception {
        DeviceInfo info = DeviceInfo.builder().ipClient("0.0.0.0").deviceBrowser("Chrome").userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36").deviceOS("Mac").osVersion("mac-os-x-15").device("PC").build();
        ResponseToFrontPD responseToFrontPD = ResponseToFrontPD.builder().name("").build();
        doReturn(Either.right(responseToFrontPD)).when(prepareClientDataUseCase).prepareClientData(ID_SESSION, info);
        mockMvc.perform(MockMvcRequestBuilders.post("/prepareClientData")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .header("clientIp", IP)
                .header("sessionId",ID_SESSION)
                .content(asJsonString(info)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
