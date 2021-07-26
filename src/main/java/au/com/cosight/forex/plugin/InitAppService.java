package au.com.cosight.forex.plugin;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.forex.plugin.service.ForexQuoteService;
import au.com.cosight.sdk.oauth2callback.Oauth2Details;
import au.com.cosight.sdk.plugin.runtime.CosightExecutionContext;
import au.com.cosight.sdk.plugin.runtime.CosightRuntimeFieldMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitAppService implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger("forex-plugin");


    private final CosightExecutionContext cosightExecutionContext;
    private final ForexQuoteService forexQuoteService;
    private final ObjectMapper objectMapper;
    public InitAppService(CosightExecutionContext cosightExecutionContext, ForexQuoteService forexQuoteService, ObjectMapper objectMapper) {
        this.cosightExecutionContext = cosightExecutionContext;
        this.forexQuoteService = forexQuoteService;
        this.objectMapper = objectMapper;
    }


    @Override
    public void run(String...args) throws Exception {
        logger.info("================================== START PROCESS ========================================");

        Oauth2Details deets = new Oauth2Details(cosightExecutionContext);
//        Oauth2Details deets = oAuth2Manager.getOauth2Details();
        logger.info("context has values {} {}",cosightExecutionContext.getPluginName(),cosightExecutionContext.getPluginUuId());
        logger.info("context {}",cosightExecutionContext.toString());
        String accessToken = deets.getAccessToken();
        logger.info("ACCESS TOKEN ="+accessToken);

    }
    public void run2(String... args) throws Exception {
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
        logger.info("RUNTIME CONTEXT: {}",objectMapper.writeValueAsString(cosightExecutionContext));

        final List<String> currencyCodes = new ArrayList<>();
        //support both batch process . if you define as for-each then no need to support batch
        if (cosightExecutionContext.isBatchProcess()) {
            // if batch process , use cosightExecutionContext.getParametersBatch();
            logger.info("BATCH PROCESSING , BATCH SIZE {}",cosightExecutionContext.getParametersBatch().size());
            cosightExecutionContext.getParametersBatch().forEach( m ->{
                logger.info("{}",m);
                currencyCodes.addAll((List<String>) m.get("currencyCodes"));
            });
        }
        else {
            // non batch process / run through 'run radial button' cosightExecutionContext.getParameters()
            currencyCodes.addAll ((List<String>) cosightExecutionContext.getParameters().get("currencyCodes"));
        }
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
