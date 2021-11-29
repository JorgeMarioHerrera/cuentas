package co.com.bancolombia.api;

import co.com.bancolombia.model.reportfields.RequestFeedback;
import co.com.bancolombia.usecase.feedback.FeedbackUseCase;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes={FeedbackRest.class})
@EnableWebMvc
public class FeedbackRestTest {
    @MockBean
    private FeedbackUseCase feedbackUseCase;
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private  static final String ID_SESSION="9999999";
    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testShouldResponseHTTP200WhenOauthSuccess() throws Exception {
        RequestFeedback texto = RequestFeedback.builder().need("5").app("5").process("5").comments("5").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/feedback")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .header("sessionID",ID_SESSION)
                .content(asJsonString(texto)))
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
