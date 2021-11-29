package co.com.bancolombia.api;

import co.com.bancolombia.api.OauthFuaRest;
import co.com.bancolombia.api.factory.FactoryObjects;
import co.com.bancolombia.model.api.ResponseToFrontChangeCode;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.oauthfua.OAuthRequestFUA;
import co.com.bancolombia.usecase.oauthuser.OAuthUserUseCase;
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

@SpringBootTest(classes={OauthFuaRest.class})
@EnableWebMvc
public class OauthFuaRestTest {
    @MockBean
    private  OAuthUserUseCase oAuthUserUseCase;
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
        OAuthRequestFUA requestOauth = OAuthRequestFUA.builder().code("123456").build();
        doReturn(Either.left(Error.builder().errorCode("VALDAFS-002").build())).when(oAuthUserUseCase).authUserOnFUA(requestOauth, ID_SESSION);

        mockMvc.perform(MockMvcRequestBuilders.post("/changeAuthCode")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .header("sessionID",ID_SESSION)
                .content(asJsonString(requestOauth)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect( jsonPath("$.errorCode").value("VALDAFS-002"));
    }

    @Test
    public void testShouldResponseHTTP200WhenOauthSuccess() throws Exception {
        OAuthRequestFUA requestOauth = FactoryObjects.returnObjectOAuthRequestFUA("objectRequestOauth.json");
        ResponseToFrontChangeCode responseToFrontChangeCode = ResponseToFrontChangeCode.builder()
                .entryDate("fecha").lastEntryHour("lasthour").idSession(ID_SESSION).build();
        doReturn(Either.right(responseToFrontChangeCode)).when(oAuthUserUseCase).authUserOnFUA(requestOauth,ID_SESSION);

        mockMvc.perform(MockMvcRequestBuilders.post("/changeAuthCode")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .header("sessionID",ID_SESSION)
                .content(asJsonString(requestOauth)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect( jsonPath("$.idSession").value(responseToFrontChangeCode.getIdSession()));
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
