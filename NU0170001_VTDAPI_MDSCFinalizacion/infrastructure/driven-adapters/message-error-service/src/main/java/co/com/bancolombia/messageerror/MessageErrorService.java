package co.com.bancolombia.messageerror;

import co.com.bancolombia.Constants;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.messageerror.RequestMessageError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageErrorService implements IMessageErrorService {

    @Value("${services.messageError.urlService}")
    private String urlService;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    //Logs
    private final LoggerApp loggerApp;

    @Override
    public Error obtainErrorMessageByAppIdCodeError(ErrorExeption error, String idSession) {
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.TM_DRIVEN_ADAPTER_MESSAGEERROR, idSession);
        try {
            loggerApp.logger(LoggerOptions.Actions.VAL_MESSAGE_ERROR_OBTAIN_ERROR, error.toString()
                    .concat(". (idSession = " + idSession + ")"), LoggerOptions.EnumLoggerLevel.DEBUG, null);
            // Se realiza la consulta incluso con los valores de code y description nulos
            RequestMessageError requestMessageError = getRequestMessageError(error, idSession);
            Response response = queryOKHttp(requestMessageError);
            loggerApp.logger(LoggerOptions.Actions.VAL_MESSAGE_ERROR_RESPONSE, response.toString(),
                    LoggerOptions.EnumLoggerLevel.DEBUG, null);
            // Mapper de la respuesta al objeto
            return objectMapper.readValue(Objects.requireNonNull(response.body()).string(), Error.class);
        } catch (JsonParseException e) {
            loggerApp.logger(LoggerOptions.Actions.ME_JSON_PARSE_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
        } catch (JsonMappingException e) {
            loggerApp.logger(LoggerOptions.Actions.ME_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
        }  catch (IOException e) {
            loggerApp.logger(LoggerOptions.Actions.ME_IO_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
        } catch (RuntimeException e) {
            loggerApp.logger(LoggerOptions.Actions.ME_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
        }
        return errorDefault(validateFieldsOfObjectErrorException(error, Constants.FIELD_NAME_ERROR_EXCEPTION_CODE),
                validateFieldsOfObjectErrorException(error, Constants.FIELD_NAME_ERROR_EXCEPTION_DESCRIPTION));
    }

    private RequestMessageError getRequestMessageError(ErrorExeption error, String idSession) {
        return RequestMessageError.builder()
                .idSession(idSession).codeError(error.getCode())
                .descriptionDefault(error.getDescription())
                .appCode(Constants.MSN_ERROR_APPLICATION_ID_QUERY)
                .build();
    }

    private static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @NotNull
    private Response queryOKHttp(RequestMessageError requestMessageError) throws IOException {
        // Creando request
        MediaType mediaType = MediaType.parse(Constants.COMMON_MEDIA_TYPE);
        String content = asJsonString(requestMessageError);
        loggerApp.logger(LoggerOptions.Actions.VAL_MESSAGE_ERROR_QUERY, content,
                LoggerOptions.EnumLoggerLevel.DEBUG, null);
        RequestBody body = RequestBody.create(content, mediaType);
        Request request = new Request.Builder().url(urlService).post(body).build();
        loggerApp.logger(LoggerOptions.Actions.VAL_MESSAGE_ERROR_REQUEST, request.toString(),
                LoggerOptions.EnumLoggerLevel.DEBUG, null);
        // Ejecutando request
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    private String validateFieldsOfObjectErrorException(ErrorExeption error, String nameField) {
        String strResultValidate = Constants.STRING_EMPTY;
        switch (nameField) {
            case Constants.FIELD_NAME_ERROR_EXCEPTION_CODE:
                // (true, true), (true, false) - (false, false)
                strResultValidate = (Objects.nonNull(error) && StringUtils.isNotBlank(error.getCode()))
                        ? error.getCode() : Constants.FIELD_CAME_EMPTY;
                break;
            case Constants.FIELD_NAME_ERROR_EXCEPTION_DESCRIPTION:
                strResultValidate = (Objects.nonNull(error) && StringUtils.isNotBlank(error.getDescription()))
                        ? error.getDescription() : Constants.FIELD_CAME_EMPTY;
                break;
            default:
                break;
        }
        return strResultValidate;
    }

    private Error errorDefault(String codeErrorDefault, String technicalDescriptionErrorDefault) {
        loggerApp.logger(LoggerOptions.Actions.VAL_MESSAGE_ERROR_DEFAULT, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);
        return Error.builder()
                .applicationId(Constants.MSN_ERROR_APPLICATION_ID_QUERY)
                .errorCode(Constants.MSN_ERROR_PREFIX_DEFAULT.concat(codeErrorDefault))
                .errorDescription(ErrorDescription.builder()
                        .errorOperation(Constants.MSN_ERROR_OPERATION_DEFAULT)
                        .errorService(Constants.MSN_ERROR_SERVICE_DEFAULT)
                        .errorType(Constants.MSN_ERROR_TYPE_DEFAULT)
                        .exceptionType(Constants.MSN_ERROR_EXCEPTION_TYPE_DEFAULT)
                        .functionalCode(Constants.MSN_ERROR_FUNCTIONAL_CODE_DEFAULT)
                        .msnIsDefault(Constants.MSN_ERROR_MARK_DEFAULT)
                        .functionalDescription(Constants.MSN_ERROR_FUNCTIONAL_DESCRIPTION_DEFAULT)
                        .technicalDescription(technicalDescriptionErrorDefault)
                        .build())
                .build();
    }
}
