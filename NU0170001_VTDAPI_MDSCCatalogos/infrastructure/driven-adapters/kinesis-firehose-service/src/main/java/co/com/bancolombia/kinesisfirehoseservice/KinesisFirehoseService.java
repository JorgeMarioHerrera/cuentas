package co.com.bancolombia.kinesisfirehoseservice;


import static co.com.bancolombia.kinesisfirehoseservice.LoggerOptions.Actions.KFS_ILLEGAL_ARGUMENT_EXCEPTION;
import static co.com.bancolombia.kinesisfirehoseservice.LoggerOptions.Actions.KFS_RESOURCE_NOT_FOUND;
import static co.com.bancolombia.kinesisfirehoseservice.LoggerOptions.Actions.KFS_SAVE_SUCCESS;
import static co.com.bancolombia.kinesisfirehoseservice.LoggerOptions.Actions.KFS_SDK_SERVICE_EXCEPTION;
import static co.com.bancolombia.kinesisfirehoseservice.LoggerOptions.Actions.KFS_SERVICE_UNAVAILABLE;
import static co.com.bancolombia.kinesisfirehoseservice.LoggerOptions.Services.KFS_SERVICE_NAME;

import java.util.HashMap;

import static co.com.bancolombia.kinesisfirehoseservice.LoggerOptions.Actions.KFS_AWS_SERVICE_EXCEPTION;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import co.com.bancolombia.firehose.impl.FunctionalReportService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.EnumLoggerLevel;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.firehose.model.InvalidArgumentException;
import software.amazon.awssdk.services.firehose.model.ResourceNotFoundException;
import software.amazon.awssdk.services.firehose.model.ServiceUnavailableException;

@Component
@RequiredArgsConstructor
public class KinesisFirehoseService implements IFirehoseService {

	private final IPropertyOfLoggerRepository loggerApp;
    private final FunctionalReportService functionalReportService;

    @Override
    public Either<ErrorEx, Boolean> save(HashMap<String, String> reportRow, String idSession) {
        loggerApp.init(this.getClass().toString(), KFS_SERVICE_NAME, idSession);
        
        Boolean response = null;
        Either<ErrorEx, Boolean> returnError = null;
        try {
            response = functionalReportService.sendStepFuncionalCatalogos(idSession, reportRow);
            loggerApp.logger(KFS_SAVE_SUCCESS, response.toString(),  EnumLoggerLevel.INFO, null);
        } catch (InvalidArgumentException e) {
            loggerApp.logger(KFS_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), EnumLoggerLevel.ERROR, e);
            returnError = Either.left(ErrorsEnum.KFS_ARGUMENT_INVALID.buildError());
        } catch (ResourceNotFoundException e) {
            loggerApp.logger(KFS_RESOURCE_NOT_FOUND, e.getMessage(), EnumLoggerLevel.ERROR, e);
            returnError = Either.left(ErrorsEnum.KFS_NOT_FOUND.buildError());
        } catch (ServiceUnavailableException e) {
            loggerApp.logger(KFS_SERVICE_UNAVAILABLE, e.getMessage(), EnumLoggerLevel.ERROR, e);
            returnError = Either.left(ErrorsEnum.KFS_SERVICE_UNVAILABLE.buildError());
        } catch (AwsServiceException e) {
            loggerApp.logger(KFS_AWS_SERVICE_EXCEPTION, e.getMessage(), EnumLoggerLevel.ERROR, e);
            returnError = Either.left(ErrorsEnum.KFS_AWS_SERVICE_EXCEPTION.buildError());
        } catch (SdkException e) {
            loggerApp.logger(KFS_SDK_SERVICE_EXCEPTION, e.getMessage(), EnumLoggerLevel.ERROR, e);
            returnError = Either.left(ErrorsEnum.KFS_SDK_SDKSERVICE_EXCEPTION.buildError());
        } if (null != returnError) {
            return returnError;
        } else {
            return (!response) ? Either.left(ErrorsEnum
                    .KFS_RETURN_NOT_OK.buildError()) : Either.right(Boolean.TRUE);
        }
    }

}
