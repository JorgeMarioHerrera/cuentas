package co.com.bancolombia.model.emailnotifications.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponseDataModel {

    private HeaderDataResponseModel header;
    private String responseMessage;
    private List<ResponseIdModel> responseMessageIds;
}

