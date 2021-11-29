package co.com.bancolombia.notificationsService.util;

import co.com.bancolombia.model.notifications.ResponseNotificationsInformation;
import co.com.bancolombia.notificationsService.entity.responsesuccess.EntityResponseSuccessNotifications;

public class ConvertResponseNotifications {
    public static ResponseNotificationsInformation entityToModel(EntityResponseSuccessNotifications
                                                                         response){
        return ResponseNotificationsInformation.builder()
                .dynamicKeyIndicator("1".equals(response.getData().getDynamicKeyIndicator()))
                .dynamicKeyMechanism(response.getData().getDynamicKeyMechanism())
                .enrollmentDate(response.getData().getEnrollmentDate())
                .lastMechanismUpdateDate(response.getData().getLastMechanismUpdateDate())
                .build();
    }
    private ConvertResponseNotifications(){}
}
