package co.com.bancolombia.api;

import co.com.bancolombia.model.api.ResponseToFrontBanner;
import co.com.bancolombia.model.api.ResponseToFrontWE;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.usecase.banner.BannerUseCase;
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

@SpringBootTest(classes={BannerRest.class})
@EnableWebMvc
public class BannerRestTest {
    @MockBean
    private BannerUseCase bannerUseCase;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testShouldResponseHTTP500WhenInputDataFailed() throws Exception {
        doReturn(Either.left(Error.builder().errorCode("VALWE-007").build())).when(bannerUseCase).banner();
        mockMvc.perform(MockMvcRequestBuilders.post("/banner")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .content(asJsonString("")))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect( jsonPath("$.errorCode").value("VALWE-007"));
    }

    @Test
    public void testShouldResponseHTTP200WhenInputDataSuccess() throws Exception {
        ResponseToFrontBanner response = new ResponseToFrontBanner(true);
        doReturn(Either.right(response)).when(bannerUseCase).banner();
        mockMvc.perform(MockMvcRequestBuilders.post("/banner")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .content(asJsonString("")))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect( jsonPath("$.output").value(response.getOutput()));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
