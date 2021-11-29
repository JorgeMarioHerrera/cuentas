package co.com.bancolombia.assignadvisor.impl.factory;




import co.com.bancolombia.model.assignadviser.AssignAdvisorRequest;

import java.io.IOException;


public class FactoryAssignAdvisor {
    public static AssignAdvisorRequest getRequest() throws IOException {
        return AssignAdvisorRequest.builder().advisorCode("1").accountNumber("123").build();
    }
}
