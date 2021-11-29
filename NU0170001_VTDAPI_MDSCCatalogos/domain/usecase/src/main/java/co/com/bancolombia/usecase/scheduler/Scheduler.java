package co.com.bancolombia.usecase.scheduler;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.usecase.customcatalog.CustomCatalogUseCase;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class Scheduler {
    

	private final CustomCatalogUseCase customCatalogUC;
	
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.FORMAT);

  
    @Scheduled(cron = Constant.CROMEEXPLESSION, zone = Constant.TIME_ZONE)
    public void executeTask() {
        LocalDateTime d1= LocalDateTime.now();
        log.info("Execute Crome Catalogue --> Current Time = {}", LocalDateTime.now());
        
        Either<ErrorEx, String>  taskCatalog = customCatalogUC.updateCatalog();
        if (taskCatalog.isRight()) {
        	log.info("Finish Execute Crome Catalogue Status--> Ok = {}",taskCatalog.getRight());
        }else {
        	log.info("Finish Execute Crome Catalogue Status--> Fail = {}",taskCatalog.getLeft().getDescription());
        }

        LocalDateTime d2= LocalDateTime.now();
        long diff = ChronoUnit.SECONDS.between(d1, d2);
        log.info("Finish Execute Crome Catalogue --> Time dif = {} SECONDS", diff);
    }
}