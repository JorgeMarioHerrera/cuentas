package co.com.bancolombia.delivery.service.util;


import co.com.bancolombia.business.Constants;
import co.com.bancolombia.delivery.service.entity.request.Card;
import co.com.bancolombia.delivery.service.entity.request.ContactInformation;
import co.com.bancolombia.delivery.service.entity.request.Customer;
import co.com.bancolombia.delivery.service.entity.request.DataRequest;
import co.com.bancolombia.delivery.service.entity.request.DeliveryRequest;
import co.com.bancolombia.delivery.service.entity.request.Identification;
import co.com.bancolombia.delivery.service.entity.request.Office;
import co.com.bancolombia.delivery.service.entity.request.Transaction;
import co.com.bancolombia.delivery.service.entity.request.Account;
import co.com.bancolombia.delivery.service.entity.request.User;
import co.com.bancolombia.model.api.direction.RequestConfirmDirectionFromFront;
import co.com.bancolombia.model.redis.UserTransactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DeliveryConverter {

    @SuppressWarnings("java:S138")
    public static DeliveryRequest modelToEntity(UserTransactional user, RequestConfirmDirectionFromFront frontRequest,
                                                int office) {
        List<String> cardListType = new ArrayList<>();
        cardListType.add(frontRequest.getCardType());
        return DeliveryRequest.builder().data(
                DataRequest.builder()
                .user(User.builder().id(Constants.IDPRODUCT).build())
                .transaction(
                        Transaction.builder()
                                .id(user.getSessionID().substring(0, Constants.TWELVE))
                                .date(user.getDateAndHourTransaction())
                        .build())
                .customer(
                        Customer.builder()
                                .identification(
                                        Identification.builder()
                                                .documentNumber(user.getDocNumber())
                                                .documentType(Constants.TIPDOC +user.getTypeDocument())
                                        .build()
                                )
                                .name(formatName(user))
                                .contactInformation(
                                        ContactInformation.builder()
                                                .address(frontRequest.getDirectionAddress()+("".equals(frontRequest
                                                        .getAddressComplement()) ? "":
                                                        (" "+frontRequest.getAddressComplement())))
                                                .city(String.format(Constants.FORMAT5D , Integer.parseInt(frontRequest
                                                        .getCityCodeAddress().substring(0, frontRequest
                                                                .getCityCodeAddress().length() - Constants.THREE))))
                                                .cellPhone(user.getMobilPhone())
                                                .contactPhone(user.getMobilPhone())
                                                .email(user.getEmail())
                                        .build()
                                )
                        .build()
                )
                .office(Office.builder().code(String.valueOf(office)).build())
                .card(Card.builder().types(cardListType).build())
                .account(Account.builder().type(Constants.CUENTA_DE_AHORRO).number(user.getAccountNumber()).build())
                .build())
                .build();
    }
    
    private static String formatName(UserTransactional user) {
        return Stream.of(user.getFirstName(),
                user.getSecondName(),
                user.getFirstSurName(),
                user.getSecondSurName())
                .filter(string -> string != null && !string.isEmpty())
                .collect(Collectors.joining(Constants.SPACE));
    }
    
    private DeliveryConverter() {
    }
}
