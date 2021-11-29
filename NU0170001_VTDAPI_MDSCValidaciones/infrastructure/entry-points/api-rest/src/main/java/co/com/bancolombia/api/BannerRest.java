package co.com.bancolombia.api;

import co.com.bancolombia.usecase.banner.BannerUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Api(value = "validaciones", consumes = "Validaciones API")
public class BannerRest {
    private final BannerUseCase bannerUseCase;

    @PostMapping(path = "/banner")
    @ApiOperation(value = "Method that validate the entrance data")
    @ApiResponses(value = {@ApiResponse(code=200, responseHeaders = {
            @ResponseHeader(name = "idSession", response = String.class)},
            message="entrance data successfully"),
            @ApiResponse(code=500, responseHeaders = {
                    @ResponseHeader(name = "idSession", response = String.class)},
                    message="Error in the banner") } )
    public ResponseEntity banner() {
        return bannerUseCase.banner().fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }
}
