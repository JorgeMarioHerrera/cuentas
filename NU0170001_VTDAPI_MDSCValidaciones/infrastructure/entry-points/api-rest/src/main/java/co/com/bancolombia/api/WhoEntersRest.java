package co.com.bancolombia.api;

import co.com.bancolombia.usecase.whoenters.WhoEntersUseCase;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Api(value = "validaciones", consumes = "Validaciones API")
public class WhoEntersRest {
    private final WhoEntersUseCase whoEnters;

    @PostMapping(path = "/whoEnters")
    @ApiOperation(value = "Method that validate the who enters")
    @ApiResponses(value = {@ApiResponse(code=200, responseHeaders = {
            @ResponseHeader(name = "idSession", response = String.class)},
            message="entrance data successfully"),
            @ApiResponse(code=500, responseHeaders = {
                    @ResponseHeader(name = "idSession", response = String.class)},
                    message="Error in the who enters") } )
    public ResponseEntity whoEnters(@RequestHeader String sessionID) {
        return whoEnters.whoEnters(sessionID).fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }
}
