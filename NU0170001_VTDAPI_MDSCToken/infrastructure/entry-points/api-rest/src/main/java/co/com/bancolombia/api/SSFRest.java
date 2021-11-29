package co.com.bancolombia.api;

import co.com.bancolombia.model.api.RequestValidateFromFront;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.usecase.ssf.SSFUseCase;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Api(value = "apiSSF", consumes = "SSF API")
public class SSFRest {
    private final SSFUseCase ssfUseCase;

    @PostMapping(path = "/validateSoftToken")
    @ApiOperation(value = "Method to validate a soft token")
    @ApiResponses(value = {@ApiResponse(code = 200, responseHeaders = {
            @ResponseHeader(name = "sessionid", response = String.class)}, message = "Validate token successfully",
            response = RequestValidateFromFront.class),
            @ApiResponse(code = 500, responseHeaders = {
                    @ResponseHeader(name = "sessionid", response = String.class)}, message = "Error obtain token",
                    response = Error.class)})
    public ResponseEntity validateSoftToken(@RequestBody RequestValidateFromFront requestValidateFromFront,
                                            @RequestHeader("sessionid") String sessionId
    ) {
        return ssfUseCase.validateSoftToken(requestValidateFromFront.getToken(), sessionId).fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }
}
