package co.com.bancolombia.model.assignadviser.gateways;

import co.com.bancolombia.model.assignadviser.AssignAdvisorRequest;
import co.com.bancolombia.model.assignadviser.AssignAdvisorResponse;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;

public interface IAssignAdviserService {
    Either<ErrorExeption, AssignAdvisorResponse> assignAdviser(AssignAdvisorRequest assignAdvisorRequest,
                                                               String idSession);
}
