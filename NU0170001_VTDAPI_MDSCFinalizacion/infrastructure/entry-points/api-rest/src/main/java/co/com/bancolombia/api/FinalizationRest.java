package co.com.bancolombia.api;

import co.com.bancolombia.model.api.ResponseToFrontFin;
import co.com.bancolombia.model.finalization.FinalizationRequest;
import co.com.bancolombia.usecase.finalization.FinalizationUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Api(value = "validaciones", consumes = "Validaciones API")
public class FinalizationRest {
    private final FinalizationUseCase finalizationUseCase ;

    @PostMapping(path = "/finalization")
    @ApiOperation(value = "Method with generate JWT and sessionId")
    @ApiResponses(value = {@ApiResponse(code=200, message="sessionId Created Successfully",
            response= ResponseToFrontFin.class),
            @ApiResponse(code=500, message="Error creating sessionId", response= Error.class) } )
    public ResponseEntity finalization( @RequestHeader String sessionId, @RequestBody FinalizationRequest request) {
        return finalizationUseCase.finalization(sessionId, request).fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }

}
