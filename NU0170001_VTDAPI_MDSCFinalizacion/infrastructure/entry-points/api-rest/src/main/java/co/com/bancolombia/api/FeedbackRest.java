package co.com.bancolombia.api;

import co.com.bancolombia.model.messageerror.Error;
import lombok.RequiredArgsConstructor;
import co.com.bancolombia.usecase.feedback.FeedbackUseCase;
import co.com.bancolombia.model.reportfields.RequestFeedback;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ResponseHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Api(value = "validaciones", consumes = "Validaciones API")
public class FeedbackRest {

    private final FeedbackUseCase feedbackUseCase;

    @PostMapping(path = "/feedback")
    @ApiOperation(value = "Method with send report")
    @ApiResponses(value = {@ApiResponse(code=200, responseHeaders = {
            @ResponseHeader(name = "idSession", response = String.class)}, message="Send report successfully",
            response=RequestFeedback.class),
            @ApiResponse(code=500, responseHeaders = {
                    @ResponseHeader(name = "idSession", response = String.class)}, message="Error send report",
                    response= Error.class) } )
    public ResponseEntity sendReport(@RequestBody RequestFeedback requestFeedback,
                                     @RequestHeader("sessionId") String sessionId) {
        feedbackUseCase.feedBackFinish(requestFeedback, sessionId);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
