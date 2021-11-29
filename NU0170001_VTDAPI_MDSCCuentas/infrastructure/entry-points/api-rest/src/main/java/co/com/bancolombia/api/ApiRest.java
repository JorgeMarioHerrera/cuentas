package co.com.bancolombia.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.bancolombia.model.api.RequestCreateFromFront;
import co.com.bancolombia.model.api.ResponseCreateToFront;
import co.com.bancolombia.model.api.agremment.RequestAgremmentFromFront;
import co.com.bancolombia.model.api.direction.RequestConfirmDirectionFromFront;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.usecase.account.AccountUseCase;
import co.com.bancolombia.usecase.account.AddressUseCase;
import co.com.bancolombia.usecase.account.AgremmentUseCase;
import co.com.bancolombia.usecase.account.DeliveryUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Api(value = "apiCuentas", consumes = "Cuentas API")
public class ApiRest {
    private final AccountUseCase accountUseCase;
    private final AddressUseCase addressUseCase;
    private final DeliveryUseCase deliveryUseCase;
    private final AgremmentUseCase agremmentUseCase;

    @PostMapping(path = "/create")
    @ApiOperation(value = "Method to create an account")
    @ApiResponses(value = {@ApiResponse(code=200, responseHeaders = {
            @ResponseHeader(name = "sessionid", response = String.class)},
            message="Account created successfully", response = ResponseCreateToFront.class),
            @ApiResponse(code=500, responseHeaders = {
                    @ResponseHeader(name = "sessionid", response = String.class)},
                    message="Error creating account", response = Error.class) })

    public ResponseEntity createAccount(@RequestBody RequestCreateFromFront requestCreateFromFront,
                                        @RequestHeader("sessionid") String sessionId) {
        return accountUseCase.createAccount(requestCreateFromFront,sessionId).fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }

    @PostMapping(path = "/saveInformationDelivery")
    @ApiOperation(value = "Method to save information of delivery")
    @ApiResponses(value = {@ApiResponse(code=200, responseHeaders = {
            @ResponseHeader(name = "sessionid", response = String.class)},
            message="Confirm address successfully", response = ResponseCreateToFront.class),
            @ApiResponse(code=500, responseHeaders = {
                    @ResponseHeader(name = "sessionid", response = String.class)},
                    message="Error confirm address", response = Error.class) })
    public ResponseEntity<Object> saveInformationDelivery(@RequestBody RequestConfirmDirectionFromFront request,
                                                          @RequestHeader("sessionid") String idSession) {
        return addressUseCase.saveInformationDelivery(request,idSession).fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }

    @PostMapping(path = "/callServiceDelivery")
    @ApiOperation(value = "Method to Call Service delivery")
    @ApiResponses(value = {@ApiResponse(code=200, responseHeaders = {
            @ResponseHeader(name = "sessionid", response = String.class)},
            message="Confirm call delivery successfully", response = ResponseCreateToFront.class),
            @ApiResponse(code=500, responseHeaders = {
                    @ResponseHeader(name = "sessionid", response = String.class)},
                    message="Error confirm delivery", response = Error.class) })
    public ResponseEntity<Object> callServiceDelivery(@RequestBody RequestConfirmDirectionFromFront request,
                                                          @RequestHeader("sessionid") String idSession) {
        return deliveryUseCase.callServiceDelivery(request,idSession).fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }
    
    @PostMapping(path = "/agremmentService")
    @ApiOperation(value = "Method to Call Service Agremment")
    @ApiResponses(value = {@ApiResponse(code=200, responseHeaders = {
            @ResponseHeader(name = "sessionid", response = String.class)},
            message="Confirm call Agremment successfully", response = ResponseCreateToFront.class),
            @ApiResponse(code=500, responseHeaders = {
                    @ResponseHeader(name = "sessionid", response = String.class)},
                    message="Error confirm agremment", response = Error.class) })
    public ResponseEntity<Object> agremmentService(@RequestBody RequestAgremmentFromFront requestAgremmentFromFront,
                                                          @RequestHeader("sessionid") String idSession) {
        return agremmentUseCase.agremmentService(requestAgremmentFromFront,idSession).fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }
}
