package co.com.bancolombia.usecase.customcatalog;

import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.core.Response.Status;

import co.com.bancolombia.model.cost.plans.Account;
import co.com.bancolombia.model.cost.plans.Card;
import co.com.bancolombia.model.cost.plans.DataCuota;
import co.com.bancolombia.model.cost.plans.DataRequest;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.idtransactional.IDTransactional;
import co.com.bancolombia.model.plans.Plan;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Actions;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.EnumLoggerLevel;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Services;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Constants;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestCustomCatalogueUpdate;
import co.com.bancolombia.model.requestclientjersey.gateways.IClientJersey;
import co.com.bancolombia.model.responseaccountoffersplans.ResponseAccountOffersPlans;
import co.com.bancolombia.usecase.enums.Errors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCatalogUseCase {


    private final IRequestCustomCatalogueUpdate iRequestCustomCatalogue;
    private final IClientJersey iClientJersey;
    private final IPropertyOfLoggerRepository loggerApp;
    private final IFirehoseService iFirehoseService;
    public static final long RANDOM_ID_SESSION = 100_000L;


    @SuppressWarnings({"unchecked", "java:S138"})
    public Either<ErrorEx, String> updateCatalog() {
        IDTransactional instanciaId = new IDTransactional(generateSession("SHA1PRNG"));
        loggerApp.init(this.getClass().toString(), Services.UC_DOMAIN_USECASE_CUSTOMCATALOG,
                instanciaId.getIdSession());
        try {
            loggerApp.logger(Actions.MU_CUSTOMCATALOG_RECEIVE, null, EnumLoggerLevel.INFO, null);
            List<RequestCatalogue> listCatalog = (List<RequestCatalogue>) iClientJersey
                    .sendRequest(ResponseAccountOffersPlans.class, null, instanciaId.getIdSession(),
                            Constants.NUMBER_1);
            List<RequestCatalogue> catalogDelete = new ArrayList<>();
            IntStream.range(0, listCatalog.size()).boxed().forEach(in -> {
                try {
                    listCatalog.get(in).setData(dataWhithShareCost(listCatalog, in, instanciaId.getIdSession()));
                } catch (KeyManagementException | NoSuchAlgorithmException | RuntimeException e) {
                    iFirehoseService.save(dataReportHashBuild(listCatalog.get(in) != null ?
                                            listCatalog.get(in).getTypeCatalogue() : null,
                                    Constants.NOT, Errors.MUESC001.buildError().getService(),
                                    Errors.MUESC001.buildError().getOperation(), Errors.MUESC001.buildError().getCode(),
                                    Errors.MUESC001.buildError().getDescription() + Constants.DOTS +
											e.getMessage()), instanciaId.getIdSession());
                    catalogDelete.add(listCatalog.get(in));
                }
            });
            IntStream.range(0, catalogDelete.size()).boxed().forEach(in -> listCatalog.remove(catalogDelete.get(in)));
            List<Either<ErrorEx, RequestCatalogue>> listResult = listCatalog.stream()
                    .map(in -> iRequestCustomCatalogue.updateCatalog(in, instanciaId.getIdSession()))
                    .collect(Collectors.toList());
            List<RequestCatalogue> success = listResult.stream().filter(Either::isRight).map(Either::getRight)
                    .collect(Collectors.toList());
            List<ErrorEx> fail = listResult.stream().filter(Either::isLeft).map(Either::getLeft)
					.collect(Collectors.toList());
            success.forEach(in ->
                iFirehoseService.save(dataReportHashBuild(in.getTypeCatalogue(), Constants.YES, Constants.BLANK,
                        Constants.BLANK, Constants.BLANK, Constants.BLANK), instanciaId.getIdSession()));
            fail.forEach(in ->
                iFirehoseService.save(dataReportHashBuild(in.getName(), Constants.NOT, in.getService(),
                        in.getOperation(), in.getCode(), in.getDescription()), instanciaId.getIdSession()));
            if (!success.isEmpty() && fail.isEmpty()) {
                loggerApp.logger(Actions.MU_CUSTOMCATALOG_SUCCESS, Status.OK.toString(), EnumLoggerLevel.INFO, null);
                return Either.right(Status.ACCEPTED.toString());
            } else {
                loggerApp.logger(Actions.MU_CUSTOMCATALOG_EMPTY, Status.NOT_ACCEPTABLE.toString(), EnumLoggerLevel.INFO,
                        null);
                return Either.left(Errors.MUECC001.buildError());
            }
        } catch (KeyManagementException | NoSuchAlgorithmException | RuntimeException e) {
            iFirehoseService.save(dataReportHashBuild("PLANS",
                    Constants.NOT, Errors.MUESC001.buildError().getService(),
                    Errors.MUECC002.buildError().getOperation(), Errors.MUECC002.buildError().getCode(),
                    Errors.MUECC002.buildError().getDescription() + Constants.DOTS + e.getMessage()),
					instanciaId.getIdSession());
            loggerApp.logger(Actions.MU_CUSTOMCATALOG_ANY_ERROR, e.getMessage(), EnumLoggerLevel.ERROR, e);
            return Either.left(Errors.MUECC002.buildError());
        }
    }


	@SuppressWarnings("java:S107")
    private HashMap<String, String> dataReportHashBuild(String name, String succesful, String service, String operation,
														String code, String description) {
        HashMap<String, String> datosReporte = new HashMap<>();
        datosReporte.put(Constants.NAME_FILE, name);
        datosReporte.put(Constants.UPDATE_TIME, LocalDateTime.now().toString());
        datosReporte.put(Constants.SUCCESSFUL, succesful);
        datosReporte.put(Constants.SERVICE, service);
        datosReporte.put(Constants.OPERATION, operation);
        datosReporte.put(Constants.ERROR_CODE, code);
        datosReporte.put(Constants.ERROR_DESCRIPTION, description);
        return datosReporte;
    }

    private Object dataWhithShareCost(List<RequestCatalogue> catalog, Integer in, String idSession)
            throws KeyManagementException, NoSuchAlgorithmException {
        Plan plan = (Plan) catalog.get(in).getData();
        plan.setManagementFee(shareCostService(plan.getTypePlan(), plan.getCodePlan(), idSession));
        return plan;
    }

    private String shareCostService(String typePlan, int codePlan, String idSession)
            throws KeyManagementException, NoSuchAlgorithmException {
        DataCuota dataCuota = DataCuota.builder().account(Account.builder().type(typePlan).build())
                .card(Card.builder().classe(Actions.CLASS_CARD).build()).plan(co.com.bancolombia.model.cost.plans.Plan
                        .builder().id(Actions.FILTER_PLAN).code(String.valueOf(codePlan)).build())
                .build();
        DataRequest data = DataRequest.builder().data(dataCuota).build();
        double cost = iClientJersey.sendRequestCost(idSession, data, DataRequest.class);
        return format(cost);
    }

    public String format(double number) {
        String str = String.valueOf(number);
        int intNumber = Integer.parseInt(str.substring(0, str.indexOf('.')));
        int decNumberInt = Integer.parseInt(str.substring(str.indexOf('.') + 1));
        int suma = decNumberInt > 0 ? 1 : 0;
        intNumber = intNumber + suma;
        DecimalFormat df = new DecimalFormat("$#,###.##");
        String s = df.format(intNumber);
        return s.replace(',', '.');
    }

    public String generateSession(String algoritmo) {

        SecureRandom randon;
        try {
            randon = SecureRandom.getInstance(algoritmo);
            byte[] bytes = new byte[Constants.NUMBER_12];
            randon.nextBytes(bytes);
            Encoder encoder = Base64.getUrlEncoder().withoutPadding();
            return encoder.encodeToString(bytes).replace(Constants.PIPE, Constants.STRING);
        } catch (NoSuchAlgorithmException e) {
            BigDecimal random = BigDecimal
                    .valueOf(Math.abs((double) ((long) new SecureRandom().nextInt() * RANDOM_ID_SESSION + 1L)));
            return random.toPlainString().replace(Constants.PIPE, Constants.STRING);
        }
    }

}
