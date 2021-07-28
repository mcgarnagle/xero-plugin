package au.com.cosight.xero.plugin;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.sdk.oauth2callback.Oauth2Details;
import au.com.cosight.sdk.plugin.runtime.CosightExecutionContext;
import au.com.cosight.sdk.plugin.runtime.CosightRuntimeFieldMap;
import au.com.cosight.xero.plugin.service.ForexQuoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xero.api.ApiClient;
import com.xero.api.client.AccountingApi;
import com.xero.models.accounting.Contacts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitAppService implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger("forex-plugin");

    // will have a parameter for 'classPrefix' that will be used to prefix classes


    private final CosightExecutionContext cosightExecutionContext;
    private final ForexQuoteService forexQuoteService;
    private final ObjectMapper objectMapper;

    public InitAppService(CosightExecutionContext cosightExecutionContext, ForexQuoteService forexQuoteService, ObjectMapper objectMapper) {
        this.cosightExecutionContext = cosightExecutionContext;
        this.forexQuoteService = forexQuoteService;
        this.objectMapper = objectMapper;
    }


    @Override
    public void run(String... args) throws Exception {
        logger.info("================================== START PROCESS ========================================");

        long ts = System.currentTimeMillis();
        if (args.length == 0) {
            throw new IllegalStateException("Unknown Entry point, no entry point provided");
        }

        String classPrefix = (String) cosightExecutionContext.getParameters().get("classPrefix");
        logger.info("================================== USING CLASS PREFIX {} ========================================", classPrefix);

        Oauth2Details deets = new Oauth2Details(cosightExecutionContext);
//        Oauth2Details deets = oAuth2Manager.getOauth2Details();
        logger.info("context has values {} {}", cosightExecutionContext.getPluginName(), cosightExecutionContext.getPluginUuId());
        logger.info("context {}", cosightExecutionContext.toString());
        logger.info("================================== FETCHING OAUTH2 DETAILS ========================================");
        String accessToken = deets.getAccessToken();
        logger.info("================================== FETCHING OAUTH2 DETAILS ========================================");
        logger.info("ACCESS TOKEN =" + accessToken);
        logger.info("================================== FETCHING OAUTH2 DETAILS SUCCESS ========================================");

        // in here we may also build a config entity so that we can check on the state of the entities and see if they
        // need updating etc....

        // build contacts
        logger.info("================================== CHECKING IF CONTACTS BUILT ========================================");
        // we'll put check in here later. need to update SDK
        logger.info("================================== CHECKING IF CONTACTS BUILT SUCCESS ========================================");

        // now lets process the action
        if ("accounts.getContacts".equalsIgnoreCase(args[0])) {
            logger.info("================== FETCHING ACCOUNTS.CONTACTS FROM XERO =====================");
            if (args.length != 2) {
                logger.error("Need to provide the action and the tenant-id for xero for this method");
                throw new IllegalStateException("Need to provide the action and the tenant-id for xero for this method");
            }
            String xeroTenantId = args[1];
            logger.info("================== FETCHING ACCOUNTS.CONTACTS FROM XERO USING TENANT-ID {} =====================", xeroTenantId);

            // XERO API CALLS - we'll move this to a service later
            ApiClient defaultClient = new ApiClient();
            AccountingApi accountingApi = AccountingApi.getInstance(defaultClient);
            try {
                Contacts contacts = accountingApi.getContacts(accessToken, xeroTenantId, null, null, null, null, null, null, null);

                contacts.getContacts().forEach(contact -> {
                    logger.info("Contact details {}",contact.toString());
                });

            } catch (Exception e) {
                logger.error("error getting contacts {}", e.getMessage());
                e.printStackTrace();
            }

        }
        if ("accounts.getAccounts".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getBankTransactions".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getBankTransfers".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getBatchPayments".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getBudgets".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getContactGroups".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getCreditNotes".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getCurrencies".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getEmployees".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getExpenseClaims".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getInvoiceReminders".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getInvoices".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getItems".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getJournals".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getLinkedTransactions".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getManualJournals".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getOrganisations".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getOverpayments".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getPayments".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getPaymentServices".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getPrepayments".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getPurchaseOrders".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getQuotes".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getReceipts".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getRepeatingInvoices".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getReports".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getSetup".equalsIgnoreCase(args[0])) {


        }
        if ("accounts.getTaxRates".equalsIgnoreCase(args[0])) {


        }
    }

    private boolean checkContactsEntityExists() {
        return false;
    }

    private void buildContactsEntity() {

    }

    public void run2(String... args) throws Exception {
        logger.info("================================== START PROCESS ========================================");
        long ts = System.currentTimeMillis();
        if (args.length == 0) {
            throw new IllegalStateException("Unknown Entry point, no entry point provided");
        }
        if (cosightExecutionContext.getAllMapEntities() == null || cosightExecutionContext.getAllMapEntities().size() != 1) {
            throw new IllegalStateException("No Field Mapping Provided");
        }
        final CosightRuntimeFieldMap fieldMap = cosightExecutionContext.getMapping(0);
        if (!"currencyQuote".equals(args[0])) {
            throw new IllegalStateException("Invalid action present");
        }
        logger.info("RUNTIME CONTEXT: {}", objectMapper.writeValueAsString(cosightExecutionContext));

        final List<String> currencyCodes = new ArrayList<>();
        //support both batch process . if you define as for-each then no need to support batch
        if (cosightExecutionContext.isBatchProcess()) {
            // if batch process , use cosightExecutionContext.getParametersBatch();
            logger.info("BATCH PROCESSING , BATCH SIZE {}", cosightExecutionContext.getParametersBatch().size());
            cosightExecutionContext.getParametersBatch().forEach(m -> {
                logger.info("{}", m);
                currencyCodes.addAll((List<String>) m.get("currencyCodes"));
            });
        } else {
            // non batch process / run through 'run radial button' cosightExecutionContext.getParameters()
            currencyCodes.addAll((List<String>) cosightExecutionContext.getParameters().get("currencyCodes"));
        }
        logger.info("processing {}", currencyCodes);
        if (currencyCodes == null) {
            throw new IllegalStateException("missing currency code");
        }
        if ("currencyQuote".equals(args[0])) {
            List<EntityInstance> instances = forexQuoteService.updateQuote(fieldMap, currencyCodes);
            logger.info("INSERTED/UPDATED {}", instances.size());
        }
        logger.info("==================== PROCESS COMPLETED took {} ms.===================", (System.currentTimeMillis() - ts));
    }
}
