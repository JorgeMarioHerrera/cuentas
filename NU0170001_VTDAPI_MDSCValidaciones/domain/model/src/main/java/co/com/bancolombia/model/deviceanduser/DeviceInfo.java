package co.com.bancolombia.model.deviceanduser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DeviceInfo {
    String ipClient;
    String deviceBrowser;
    String userAgent;
    String deviceOS;
    String device;
    String osVersion;
}