package co.com.bancolombia.model.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1820")
public class UserTransactional {
    // Input Data
    private String sessionID;
    private String docNumber;
    private String dateAndHourTransaction;
    private String typeDocument;
    private String typeClient;
    private String productId;
    private String clientCategory;
    private String authCode;
    // OAUTH
    private String ipClient;
    private String functionalStep;
    private String deviceBrowser;
    private String userAgent;
    private String deviceOS;
    private String device;
    private String os;
    private String osVersion;
    // CUSTOMER DATA
    private boolean customerSuccess;
    private String firstName;
    private String secondName;
    private String firstSurName;
    private String secondSurName;
    private String clientTypeMDM;
    private String mobilPhone;
    private String email;
    private String cityCode;
    private String departmentCode;
    private String address;
    // NOTIFICATIONS
    private boolean notificationsSuccess;
    private boolean hasSoftToken;
    private String softTokenLastUpdate;
    private String softTokenEnrollmentDate;
    private String dynamicMechanism;
    // ACCOUNT LIST
    private boolean accountListSuccess;
    private String accounts;
    private Double savingBalance;
    private Double currentBalance;
    private Double fundsBalance;
    private Double availableOverdraft;
    //WHO ENTERS
    private int attemptsReadyData;
    private boolean homeDelivery;
    private boolean balanceValidation;
    // FUNDS
    private boolean fundsSuccess;
    private boolean fundsWasConsumed;
    // CUSTOMER ACCOUNT
    private boolean customerAccountSuccess;
    private boolean customerAccountWasConsumed;
    //ACCOUNT
    private String vendorCode;
    private boolean vendorInput;
    private boolean termsLinkClicked;
    private String planName;
    private String acceptanceTimestamp;
    private String atmCost;
    private String officeCost;
    private String managementFee;
    private String accountNumber;
    private String gmf;
    private String gmfBank;
    private String city;
    //OAUTH
    private String authTime;
    // FINALIZATION
    private boolean emailSent;
    // Error Stopper
    private boolean validSession;
    // JWT
    private String jwt;
    private boolean concurrentSessions;
    // Validate
    private boolean greaterThanAmount;
    private String officeCode;
    // Token
    private int attemptsGenerate;
    private int attemptsValidate;
    private boolean validateToken;
    // Card
    private int attemptsManagement;
    // Agremment
    private int attemptsAgremment;
}
