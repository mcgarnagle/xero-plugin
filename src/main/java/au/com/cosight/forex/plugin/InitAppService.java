package au.com.cosight.forex.plugin;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.forex.plugin.service.ForexQuoteService;
import au.com.cosight.sdk.plugin.runtime.CosightExecutionContext;
import au.com.cosight.sdk.plugin.runtime.CosightRuntimeFieldMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InitAppService implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger("forex-plugin");


    private final CosightExecutionContext cosightExecutionContext;
    private final ForexQuoteService forexQuoteService;
    public InitAppService(CosightExecutionContext cosightExecutionContext, ForexQuoteService forexQuoteService) {
        this.cosightExecutionContext = cosightExecutionContext;
        this.forexQuoteService = forexQuoteService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("================================== START PROCESS ========================================");
        long ts = System.currentTimeMillis();
        if (args.length == 0) {
            throw new IllegalStateException("Unknown Entry point, no entry point provided");
        }
        if (cosightExecutionContext.getAllMapEntities() == null || cosightExecutionContext.getAllMapEntities().size() != 1){
            throw new IllegalStateException("No Field Mapping Provided");
        }
        final CosightRuntimeFieldMap fieldMap = cosightExecutionContext.getMapping(0);
        if (!"currencyQuote".equals(args[0])) {
            throw new IllegalStateException("Invalid action present");
        }
        List<String> currencyCodes = (List<String>)cosightExecutionContext.getParameters().get("currencyCodes");
        logger.info("processing {}",currencyCodes);
        if (currencyCodes == null) {
            throw new IllegalStateException("missing currency code");
        }
        if ("currencyQuote".equals(args[0])) {
            List<EntityInstance> instances = forexQuoteService.updateQuote(fieldMap,currencyCodes);
            logger.info("INSERTED/UPDATED {}",instances.size());
        }
        logger.info("==================== PROCESS COMPLETED took {} ms.===================",(System.currentTimeMillis() -ts));
    }
}
