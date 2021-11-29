package co.com.bancolombia.usecase.common.util;

import co.com.bancolombia.model.reportfields.*;

public class ValidateObject {
    public static ReportFieldsModel checkNotNull(ReportFieldsModel reportFields) {
        if (null == reportFields) {
            reportFields = ReportFieldsModel.builder().build();
        }
        if (null == reportFields.getDeviceAndUser()) {
            reportFields.setDeviceAndUser(DeviceAndUser.builder().build());
        }
        if (null == reportFields.getOauthFUA()) {
            reportFields.setOauthFUA(OAuthFUA.builder().build());
        }
        if (null == reportFields.getToken()) {
            reportFields.setToken(Token.builder().build());
        }
        if (null == reportFields.getAssociation()) {
            reportFields.setAssociation(Association.builder().build());
        }
        if (null == reportFields.getFeedback()) {
            reportFields.setFeedback(Feedback.builder().build());
        }
        if (null == reportFields.getErrorReport()) {
            reportFields.setErrorReport(ErrorReport.builder().build());
        }
        return reportFields;
    }
}
