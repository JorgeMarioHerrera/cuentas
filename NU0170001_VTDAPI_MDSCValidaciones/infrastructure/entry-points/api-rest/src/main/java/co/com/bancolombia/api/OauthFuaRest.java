package co.com.bancolombia.api;

import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.oauthfua.OAuthRequestFUA;
import co.com.bancolombia.usecase.oauthuser.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Api(value = "validaciones", consumes = "Validaciones API")
public class OauthFuaRest {
    private final OAuthUserUseCase oAuthUserUseCase;

    @PostMapping(path = "/changeAuthCode")
    @ApiOperation(value = "Method with generate JWT and sessionId")
    @ApiResponses(value = {@ApiResponse(code=200, message="sessionId Created Successfully",
            response= OAuthRequestFUA.class),
            @ApiResponse(code=500, message="Error creating sessionId", response= Error.class) } )
    public ResponseEntity oauthUser(@RequestBody OAuthRequestFUA oauthRequestFUA, @RequestHeader String sessionID) {
        return oAuthUserUseCase.authUserOnFUA(oauthRequestFUA, sessionID).fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }
}
