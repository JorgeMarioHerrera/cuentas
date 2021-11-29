package co.com.bancolombia.delivery.service.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataRequest {
    private User user;
    private Transaction transaction;
    private Customer customer;
    private Office office;
    private Card card;
    private Account account;

}
