package co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDataContactEntity {
    @JsonProperty("contact")
    private List<DataContactListEntity> contact;
}
