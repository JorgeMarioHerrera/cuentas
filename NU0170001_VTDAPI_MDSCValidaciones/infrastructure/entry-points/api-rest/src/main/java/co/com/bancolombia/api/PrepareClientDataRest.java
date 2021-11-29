package co.com.bancolombia.api;

import co.com.bancolombia.model.deviceanduser.DeviceInfo;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.oauthfua.OAuthRequestFUA;
import co.com.bancolombia.usecase.preparedata.PrepareClientDataUseCase;
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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Api(value = "validaciones", consumes = "Validaciones API")
public class PrepareClientDataRest {
    private final static int THREAD_NUMBER = 2;
    private final PrepareClientDataUseCase prepareClientDataUseCase;

    @PostMapping(path = "/prepareClientData")
    @ApiOperation(value = "Method with generate JWT and sessionId")
    @ApiResponses(value = {@ApiResponse(code=200, message="sessionId Created Successfully",
            response= OAuthRequestFUA.class),
            @ApiResponse(code=500, message="Error creating sessionId", response= Error.class) } )
    public ResponseEntity prepareClientData(@RequestHeader String clientIp, @RequestHeader String sessionId,
                                            @RequestBody DeviceInfo info) {
        info.setIpClient(clientIp);
        Executor executor = Executors.newFixedThreadPool(THREAD_NUMBER);
        CompletableFuture.runAsync( () -> prepareClientDataUseCase.prepareClientData(sessionId, info), executor);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
