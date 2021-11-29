package co.com.bancolombia.api;

import co.com.bancolombia.model.jwtmodel.JwtModel;
import co.com.bancolombia.usecase.security.SecurityUseCase;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ResponseHeader;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Api(value = "validaciones", consumes = "Validaciones API")
public class SecurityRest {

    private final SecurityUseCase securityUseCase;

    @PostMapping(path = "/session/validate")
    @ApiOperation(value = "Method with validate JWT")
    @ApiResponses(value = {@ApiResponse(code=200, responseHeaders = {
            @ResponseHeader(name = "idSession", response = String.class),
            @ResponseHeader(name = "Authorization", response = String.class)},
            message="Validate JWT successfully"),
            @ApiResponse(code=500, responseHeaders = {
                    @ResponseHeader(name = "idSession", response = String.class),
                    @ResponseHeader(name = "Authorization", response = String.class)},
                    message="Error validate JWT") } )
    public ResponseEntity validate(@RequestHeader("Authorization") String authorization,
                                   @RequestHeader("sessionID") String idSession  ) {
        JwtModel jwtModel = JwtModel.builder().jwt(authorization).idSession(idSession).build();
        return securityUseCase.validate(jwtModel).fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                response -> ResponseEntity.status(HttpStatus.OK)
                        .header("Authorization",response.getJwt()).body(response));
    }

    @PostMapping(path = "/session/finish")
    @ApiOperation(value = "Method with finish session")
    @ApiResponses(value = {@ApiResponse(code=200, responseHeaders = {
            @ResponseHeader(name = "sessionID", response = String.class)},
            message="Finish sessionId successfully")})
    public ResponseEntity finish(@RequestHeader("sessionId") String sessionId) {
        return ResponseEntity.status(HttpStatus.OK).body(securityUseCase.finish(sessionId));
    }
}
