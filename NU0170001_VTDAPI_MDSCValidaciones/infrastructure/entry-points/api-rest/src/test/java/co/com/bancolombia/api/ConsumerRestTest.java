package co.com.bancolombia.api;

import co.com.bancolombia.model.dynamo.Consumer;
import co.com.bancolombia.model.dynamo.gateways.IDynamoService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes={ConsumerRest.class})
@EnableWebMvc
public class ConsumerRestTest {
    @MockBean
    private IDynamoService dynamo;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private  static final String ID_SESSION="9999999";
    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testShouldResponseHTTP500WhenAddConsumerFailed() throws Exception {
        Consumer con = new Consumer("001", "VINCULACION", "---");
        doReturn(Either.left(ErrorExeption.builder().code("VALCO-001").build())).when(dynamo).addConsumer(con, "");

        mockMvc.perform(MockMvcRequestBuilders.post("/addConsumer")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .content(asJsonString(con)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect( jsonPath("$.code").value("VALCO-001"));
    }

    @Test
    public void testShouldResponseHTTP200WhenAddConsumerSuccess() throws Exception {
        Consumer con = new Consumer("001", "VINCULACION", "---");
        doReturn(Either.right(con)).when(dynamo).addConsumer(con, "");
        mockMvc.perform(MockMvcRequestBuilders.post("/addConsumer")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .content(asJsonString(con)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect( jsonPath("$.consumerCertificate").value(con.getConsumerCertificate()));
    }

    @Test
    public void testShouldResponseHTTP500WhenUpdateConsumerFailed() throws Exception {
        Consumer con = new Consumer("001", "VINCULACION", "---");
        doReturn(Either.left(ErrorExeption.builder().code("VALCO-001").build())).when(dynamo).updateConsumerInfo(con);

        mockMvc.perform(MockMvcRequestBuilders.post("/updateConsumer")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .content(asJsonString(con)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect( jsonPath("$.code").value("VALCO-001"));
    }

    @Test
    public void testShouldResponseHTTP200WhenUpdateConsumerSuccess() throws Exception {
        Consumer con = new Consumer("001", "VINCULACION", "---");
        doReturn(Either.right(con)).when(dynamo).updateConsumerInfo(con);
        mockMvc.perform(MockMvcRequestBuilders.post("/updateConsumer")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .content(asJsonString(con)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect( jsonPath("$.consumerCertificate").value(con.getConsumerCertificate()));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
