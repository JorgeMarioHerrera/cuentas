package co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_contact;

import co.com.bancolombia.customerdata.entity.response.common.EntityMetaCustomerData;
import co.com.bancolombia.customerdata.entity.response.common.LinksEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRetrieveContactEntity {

    @JsonProperty("meta")
    private EntityMetaCustomerData meta;

    @JsonProperty("data")
    private ResponseDataContactEntity data;

    @JsonProperty("links")
    private LinksEntity linksEntity;

}
