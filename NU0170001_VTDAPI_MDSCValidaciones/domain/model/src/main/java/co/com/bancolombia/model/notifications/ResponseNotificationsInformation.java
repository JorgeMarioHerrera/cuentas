package co.com.bancolombia.model.notifications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseNotificationsInformation {
    /**
     * Indica si el usuario esta inscrito en clave dinamica
     */
    private boolean dynamicKeyIndicator;
    /**
     * Indica el mecanismo de clave dinamica del cliente.
     */
    private String dynamicKeyMechanism;
    /**
     * Fecha de inscripcion del cliente al sistema de autenticacion fuerte.
     */
    private String enrollmentDate;
    /**
     * ultima fecha de modificacion del mecanismo de autenticacion.
     */
    private String lastMechanismUpdateDate;
}
