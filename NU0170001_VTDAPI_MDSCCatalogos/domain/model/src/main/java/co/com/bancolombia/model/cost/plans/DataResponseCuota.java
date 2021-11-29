package co.com.bancolombia.model.cost.plans;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponseCuota {
	@SerializedName("meta")
    public EntityMeta meta;

    @SerializedName("data")
    public DataResponse data;

}

