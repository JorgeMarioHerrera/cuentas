package co.com.bancolombia.agremment.service.util;


import java.util.ArrayList;
import java.util.List;

import co.com.bancolombia.Constants;
import co.com.bancolombia.agremment.service.entity.request.Agreement;
import co.com.bancolombia.agremment.service.entity.request.AgremmentRequest;
import co.com.bancolombia.agremment.service.entity.request.Employer;
import co.com.bancolombia.agremment.service.entity.request.DataRequest;
import co.com.bancolombia.agremment.service.entity.request.Identification;
import co.com.bancolombia.agremment.service.entity.request.Pagination;
import co.com.bancolombia.agremment.service.entity.response.ResponseEntity;
import co.com.bancolombia.model.api.agremment.RequestAgremmentFromFront;
import co.com.bancolombia.model.api.agremment.ResponseAgremmentNit;
import co.com.bancolombia.model.redis.UserTransactional;


public class Converter {

    public static AgremmentRequest modelToEntity(RequestAgremmentFromFront frontRequest,
												 int key, int size) {
        return AgremmentRequest.builder()
                .data(DataRequest.builder()
                        .pagination(Pagination.builder()
                                .key(key)
                                .size(size).build())
                        .agreement(Agreement.builder()
                                .employer(Employer.builder()
                                        .identification(Identification.builder()
                                                .type(Constants.COMMON_TYPE_NIT)
                                                .number(frontRequest.getNit())
                                                .build()).build()).build())
                        .build())
                .build();
    }

    public static ResponseAgremmentNit entityToModel(ResponseEntity responseEntity) {
        List<co.com.bancolombia.model.api.agremment.ModelAgreement> agreements =
                new ArrayList<>();
        responseEntity.getData().getAgreement().forEach((v) -> {
            co.com.bancolombia.model.api.agremment.ModelAgreement conve =
                    co.com.bancolombia.model.api.agremment.ModelAgreement
                            .builder()
                            .agreementCode(String.valueOf(v.getAgreementCode()))
                            .planCode(String.valueOf(v.getPlanCode()))
                            .chargesGroup(String.valueOf(v.getChargesGroup()))
                            .percentageCharges(String.valueOf(v.getPercentageCharges()))
                            .status(String.valueOf(v.getStatus()))
                            .build();
            agreements.add(conve);
        });

        return ResponseAgremmentNit.builder().agreement(agreements).build();
    }
}
