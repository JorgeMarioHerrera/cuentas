package co.com.bancolombia.api;

import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.error.RequestGetError;
import co.com.bancolombia.usecase.ErrorUseCase;
import co.com.bancolombia.usecase.dto.StatusError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Api(value = "messageError", consumes = "Message Error API")
public class ApiRest {
    @Value("${banner.value}")
    private String valueBanner;

    private final ErrorUseCase errorUseCase;


    @PostMapping(path = "/addError",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Method with creates a new Message Error")
    @ApiResponses(value = {@ApiResponse(code=201, message="Message Error Created Successfully",
            response=StatusError.class),
            @ApiResponse(code=400, message="Error creating Message Error",
                    response= Error.class) } )
    public ResponseEntity addError(@RequestBody List<Error> errors) {
        StatusError status = errorUseCase.addError(errors);
        if (status.getErrorSuccess().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status.getErrorFailed());
        }else {
            return ResponseEntity.status(HttpStatus.CREATED).body(status);
        }
    }


    @PostMapping(path = "/obtainErrorByPartitionAndShortKey")
    @ApiOperation(value = "Method return retrieves given MessageError")
    @ApiResponses(value = {@ApiResponse(code=200, message="Obtain successfull message", response=Error.class)})
    public ResponseEntity obtainErrorByPartitionAndShortKey(@RequestBody RequestGetError requestGetError) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(errorUseCase.obtainErrorByPartitionAndShortKey(requestGetError));

    }

    @PostMapping(path = "/getAll")
    @ApiOperation(value = "Method return retrieves given MessageError")
    @ApiResponses(value = {@ApiResponse(code=200, message="Obtain successfull message", response=Error.class)})
    public ResponseEntity getAll(@RequestHeader("value") String value) {
        if(!value.equals(valueBanner)) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ERROR");
        }else {
            return errorUseCase.findAll().fold(
                    responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                    response -> ResponseEntity.status(HttpStatus.OK).body(response));

        }
    }

    @PostMapping(path = "/update")
    public ResponseEntity changeStatusBanner(@RequestBody List<Error> error, @RequestHeader("value") String value) {
        if (!value.equals(valueBanner)) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR");
        } else {
            StatusError status = errorUseCase.updateStatusBanner(error);
            if (status.getErrorSuccess().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status.getErrorFailed());
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(status);
            }
        }
    }

}
