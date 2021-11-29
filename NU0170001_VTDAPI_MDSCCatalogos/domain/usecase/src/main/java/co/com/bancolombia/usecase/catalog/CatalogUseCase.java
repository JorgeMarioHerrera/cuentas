package co.com.bancolombia.usecase.catalog;


import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response.Status;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.idtransactional.IDTransactional;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Actions;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Constants;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.EnumLoggerLevel;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Services;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestCatalogueAdd;
import co.com.bancolombia.usecase.enums.Errors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CatalogUseCase {


    private final IRequestCatalogueAdd iRequestCatalogue;
    private final IPropertyOfLoggerRepository loggerApp;
	private final IFirehoseService iFirehoseService;
    
    public static final long RANDOM_ID_SESSION = 100_000L;
    public static final int NUMBER_12 = Constants.NUMBER_12;

    public Either<ErrorEx, String> addNewCatalog(List<RequestCatalogue> catalog) {
    	IDTransactional instanciaId = new IDTransactional(generateSession("SHA1PRNG"));
        loggerApp.init(this.getClass().toString(), Services.UC_DOMAIN_USECASE_CATALOG, instanciaId.getIdSession());
        List<Either<ErrorEx, RequestCatalogue>> listResult = catalog.stream()
                .map(catlog -> iRequestCatalogue.addCatalog(catlog, instanciaId.getIdSession()))
				.collect(Collectors.toList());
        List<RequestCatalogue> success = listResult.stream().filter(Either::isRight)
                .map(Either::getRight).collect(Collectors.toList());
        List<ErrorEx> fail = listResult.stream().filter(Either::isLeft)
                .map(Either::getLeft).collect(Collectors.toList());
		
        success.forEach(in ->
        	iFirehoseService.save(dataReportHashBuild(in.getTypeCatalogue(), Constants.YES, Constants.BLANK,
					Constants.BLANK, Constants.BLANK, Constants.BLANK), instanciaId.getIdSession()));
		fail.forEach(in ->
			iFirehoseService.save(dataReportHashBuild(in.getName(), Constants.NOT, in.getService(),
					in.getOperation(),in.getCode(), in.getDescription()), instanciaId.getIdSession()));
		
        if (!success.isEmpty() && fail.isEmpty()) {
            loggerApp.logger(Actions.MU_CATALOG_SUCCESS, Status.OK.toString(), EnumLoggerLevel.INFO, null);
            return Either.right(Status.ACCEPTED.toString());
        } else {
            loggerApp.logger(Actions.MU_CATALOG_EMPTY, Status.NOT_ACCEPTABLE.toString(), EnumLoggerLevel.INFO, null);
            return Either.left(Errors.MUECS001.buildError());
        }

    }

    public String generateSession(String alg) {

        SecureRandom randon;
        try {
            randon = SecureRandom.getInstance(alg);
            byte bytes[] = new byte[NUMBER_12];
            randon.nextBytes(bytes);
            Encoder encoder = Base64.getUrlEncoder().withoutPadding();
            return encoder.encodeToString(bytes).replace(Constants.PIPE,Constants.STRING);
        } catch (NoSuchAlgorithmException e) {
            BigDecimal random = BigDecimal.valueOf(Math.abs((double) ((long) new SecureRandom().nextInt() *
                    RANDOM_ID_SESSION + 1L)));
            return random.toPlainString().replace(Constants.PIPE,Constants.STRING);
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
}
