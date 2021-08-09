package au.com.cosight.xero.plugin;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.sdk.auth.external.oauth.ExternalOAuth2Credentials;
import au.com.cosight.sdk.plugin.runtime.CosightExecutionContext;
import au.com.cosight.sdk.plugin.runtime.CosightRuntimeFieldMap;
import au.com.cosight.xero.plugin.service.EntityManagementServiceImpl;
import au.com.cosight.xero.plugin.service.ForexQuoteService;
import au.com.cosight.xero.plugin.service.xero.ContactService;
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
    private static Logger logger = LoggerFactory.getLogger("xero-plugin");

    // will have a parameter for 'classPrefix' that will be used to prefix classes


    private final CosightExecutionContext cosightExecutionContext;
    private final ForexQuoteService forexQuoteService;
    private final ObjectMapper objectMapper;
    private final EntityManagementServiceImpl entityManagementService;
    private final ContactService contactService;


    public InitAppService(CosightExecutionContext cosightExecutionContext, ForexQuoteService forexQuoteService, ObjectMapper objectMapper, EntityManagementServiceImpl entityManagementService, ContactService contactService) {
        this.cosightExecutionContext = cosightExecutionContext;
        this.forexQuoteService = forexQuoteService;
        this.objectMapper = objectMapper;
        this.entityManagementService = entityManagementService;
        this.contactService = contactService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("================================== START PROCESS ========================================");
        long ts = System.currentTimeMillis();
        if (args.length == 0) {
            throw new IllegalStateException("Unknown Entry point, no entry point provided");
        }
        ExternalOAuth2Credentials deets = ExternalOAuth2Credentials.getInstance();
        // lets not worry about classprefix for now
//        String classPrefix = (String) cosightExecutionContext.getParameters().get("classPrefix");
//        logger.info("================================== USING CLASS PREFIX {} ========================================", classPrefix);
//        logger.info("context has values {} {}", cosightExecutionContext.getRuntimeInfo().getPluginName(), cosightExecutionContext.getRuntimeInfo().getPluginUuid());
//        logger.info("context {}", cosightExecutionContext.toString());
        logger.info("================================== FETCHING OAUTH2 DETAILS ========================================");
        String accessToken = deets.getToken().getAccessToken();

        logger.info("ACCESS TOKEN =" + accessToken);
        logger.info("================================== FETCHING OAUTH2 DETAILS SUCCESS ========================================");

        // in here we may also build a config entity so that we can check on the state of the entities and see if they
        // need updating etc....

        // build contacts
        logger.info("================================== CHECKING IF CONTACTS BUILT ========================================");
        // we'll put check in here later. need to update SDK
        buildContactsEntity();

        logger.info("================================== CHECKING IF CONTACTS BUILT SUCCESS ========================================");

        // now lets process the action
        if ("getContacts".equalsIgnoreCase(args[0])) {
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
                    contactService.upsertContact(contact);

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
        entityManagementService.createContactClass("");
    }

}
