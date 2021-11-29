package co.com.bancolombia.kinesisfirehoseservice;

import java.util.HashMap;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import org.springframework.stereotype.Component;
import co.com.bancolombia.firehose.impl.FunctionalReportService;
import co.com.bancolombia.model.either.Either;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.firehose.model.InvalidArgumentException;
import software.amazon.awssdk.services.firehose.model.ResourceNotFoundException;
import software.amazon.awssdk.services.firehose.model.ServiceUnavailableException;

import static co.com.bancolombia.LoggerOptions.Actions.*;
import static co.com.bancolombia.LoggerOptions.Services.KFS_SERVICE_NAME;

@Component
@RequiredArgsConstructor
public class KinesisFirehoseService implements IFirehoseService {

    private final LoggerApp loggerApp;
    private final FunctionalReportService functionalReportService;

    /**
     * Envio del los Parametros para el reporte.
     * @param reportRow <String, String> reportFields (name_file, update_time, successful, error_code, error_description).
     * @param idSession
     */
    @Override
    public Either<ErrorExeption, Boolean> save(HashMap<String, String> reportRow, String idSession) {
        loggerApp.init(this.getClass().toString(), KFS_SERVICE_NAME, idSession);

        Boolean response = null;
        Either<ErrorExeption, Boolean> returnError = null;
        try {
            response = functionalReportService.sendStepFuncionalMDSC(idSession, reportRow);
            loggerApp.logger(KFS_SAVE_SUCCESS, response.toString(),  LoggerOptions.EnumLoggerLevel.INFO, null);
        } catch (InvalidArgumentException e) {
            loggerApp.logger(KFS_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), co.com.bancolombia.LoggerOptions
                    .EnumLoggerLevel.ERROR, e);
            returnError = Either.left(ErrorsEnum.KFS_ARGUMENT_INVALID.buildError());
        } catch (ResourceNotFoundException e) {
            loggerApp.logger(KFS_RESOURCE_NOT_FOUND, e.getMessage(), LoggerOptions.EnumLoggerLevel.ERROR, e);
            returnError = Either.left(ErrorsEnum.KFS_NOT_FOUND.buildError());
        } catch (ServiceUnavailableException e) {
            loggerApp.logger(KFS_SERVICE_UNAVAILABLE, e.getMessage(), LoggerOptions.EnumLoggerLevel.ERROR, e);
            returnError = Either.left(ErrorsEnum.KFS_SERVICE_UNVAILABLE.buildError());
        } catch (AwsServiceException e) {
            loggerApp.logger(KFS_AWS_SERVICE_EXCEPTION, e.getMessage(), LoggerOptions.EnumLoggerLevel.ERROR, e);
            returnError = Either.left(ErrorsEnum.KFS_AWS_SERVICE_EXCEPTION.buildError());
        } catch (SdkException e) {
            loggerApp.logger(KFS_SDK_SERVICE_EXCEPTION, e.getMessage(), LoggerOptions.EnumLoggerLevel.ERROR, e);
            returnError = Either.left(ErrorsEnum.KFS_SDK_SDKSERVICE_EXCEPTION.buildError());
        } if (null != returnError) {
            return returnError;
        } else {
            return (!response) ? Either.left(ErrorsEnum
                    .KFS_RETURN_NOT_OK.buildError()) : Either.right(Boolean.TRUE);
        }
    }

}
