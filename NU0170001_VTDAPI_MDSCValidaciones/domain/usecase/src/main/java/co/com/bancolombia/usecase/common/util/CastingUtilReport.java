package co.com.bancolombia.usecase.common.util;

import co.com.bancolombia.model.customerdata.RetrieveBasic;
import co.com.bancolombia.model.firehose.FunctionalReportCards;
import co.com.bancolombia.model.firehose.FunctionalReportOauthFUA;
import co.com.bancolombia.model.firehose.RequestFeedback;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.reportfields.*;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.ERROR;


@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class CastingUtilReport {
    private LoggerAppUseCase loggerAppUseCase;

    @SuppressWarnings("java:S107")
    public ReportFieldsModel getReportFieldsModel(FunctionalReportOauthFUA functionalReportOauthFUA,
                                                  RequestFeedback requestFeedback,
                                                  UserTransactional userTransactional,
                                                  Error error, String functionalStep) {
        requestFeedback = requestFeedback != null ? requestFeedback : RequestFeedback.builder().build();
        functionalReportOauthFUA = functionalReportOauthFUA != null ? functionalReportOauthFUA :
                FunctionalReportOauthFUA.builder().retrieveBasic(RetrieveBasic.builder().build())
                        .accountModel(FunctionalReportCards.builder().build()).build();
        error = error != null ? error : Error.builder().errorDescription(ErrorDescription.builder().build()).build();
        if (error.getErrorDescription() == null) {
            error.setErrorDescription(ErrorDescription.builder()
                    .functionalCode(error.getErrorCode())
                    .build());
        }
        return beanModel(functionalReportOauthFUA, requestFeedback, userTransactional, error, functionalStep);
    }

    @SuppressWarnings({"java:S107", "java:S138"})
    private ReportFieldsModel beanModel(FunctionalReportOauthFUA functionalReportOauthFUA,
                                        RequestFeedback requestFeedback,
                                        UserTransactional userTransactional, Error error, String functionalStep) {
        return ReportFieldsModel.builder()
                .deviceAndUser(DeviceAndUser.builder().idSession(userTransactional.getSessionID())
                        .ipClient(userTransactional.getIpClient())
                        .dateAndHourTransaction(String.valueOf(LocalDateTime.now()))
                        .functionalStep(functionalStep)
                        .typeClient((functionalReportOauthFUA.getAccountModel().getClientType() == null) ? "" :
                                functionalReportOauthFUA.getAccountModel().getClientType())
                        .deviceBrowser(userTransactional.getDeviceBrowser())
                        .userAgent(userTransactional.getUserAgent())
                        .deviceOS(userTransactional.getDeviceOS())
//                        .versionOS(convertAgentToOsVersion(userTransactional))
                        .device(userTransactional.getDevice())
                        .typeDocument(userTransactional.getTypeDocument())
                        .docNumber(userTransactional.getDocNumber())
                        .build())
                .feedback(Feedback.builder()
                        .achievement(requestFeedback.getAchievement())
                        .comment(requestFeedback.getComment())
                        .ease(requestFeedback.getEase())
                        .satisfaction(requestFeedback.getSatisfaction())
                        .build())
                .oauthFUA(OAuthFUA.builder()
                        .bondingDate(functionalReportOauthFUA.getRetrieveBasic() == null ? "" :
                                functionalReportOauthFUA.getRetrieveBasic().getVinculationDate()).build())
                .token(Token.builder().requestToken(functionalReportOauthFUA.getAccountModel().getRequestToken()
                        == null ? "" : functionalReportOauthFUA.getAccountModel().getRequestToken()).build())
                .errorReport(ErrorReport.builder()
                        .typeError(error.getErrorDescription().getErrorType())
                        .operationError(error.getErrorDescription().getErrorOperation())
                        .serviceError(error.getErrorDescription().getErrorService())
                        .codeError(error.getErrorCode())
                        .technicalDescription(error.getErrorDescription().getTechnicalDescription())
                        .functionalDescription(error.getErrorDescription().getFunctionalDescription())
                        .isDefault(String.valueOf(error.getErrorDescription().isMsnIsDefault()))
                        .retrievedAccounts(functionalReportOauthFUA.getAccountModel().getAccounts() == null ? "" :
                                String.join(",", functionalReportOauthFUA.getAccountModel().getAccounts()))
                        .retrievedCards(functionalReportOauthFUA.getAccountModel().getCards() == null ? "" :
                                String.join(",", functionalReportOauthFUA.getAccountModel().getCards()))
                        .build())
                .build();
    }

//    private String convertAgentToOsVersion(UserTransactional userTransactional) {
//        try {
//            return userTransactional.getUserAgent()
//                    .substring(userTransactional.getUserAgent().indexOf('(') + 1, userTransactional
//                            .getUserAgent().indexOf(')'));
//        } catch (RuntimeException e) {
//            loggerAppUseCase.init(this.getClass().toString(), "casting to report", "");
//            loggerAppUseCase.logger("RuntimeException casting report", e, ERROR, e);
//            return null;
//        }
//
//    }
}
