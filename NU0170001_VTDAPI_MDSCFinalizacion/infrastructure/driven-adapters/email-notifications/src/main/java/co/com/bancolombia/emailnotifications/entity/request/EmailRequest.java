package co.com.bancolombia.emailnotifications.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private String senderMail;
    private String subjectEmail;
    private String messageTemplateId;
    private String messageTemplateType;
    private List<SendEmail> sendEmail;
}
