package au.com.cosight.xero.plugin;

import au.com.cosight.sdk.auth.external.oauth.ExternalOAuth2Credentials;
import au.com.cosight.sdk.plugin.runtime.CosightExecutionContext;
import au.com.cosight.xero.plugin.service.EntityManagementServiceImpl;
import au.com.cosight.xero.plugin.service.ForexQuoteService;
import au.com.cosight.xero.plugin.service.xero.AccountService;
import au.com.cosight.xero.plugin.service.xero.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xero.api.ApiClient;
import com.xero.api.client.AccountingApi;
import com.xero.models.accounting.Accounts;
import com.xero.models.accounting.Contacts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class InitAppService implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger("xero-plugin");

    // will have a parameter for 'classPrefix' that will be used to prefix classes


    private final CosightExecutionContext cosightExecutionContext;
    private final ForexQuoteService forexQuoteService;
    private final ObjectMapper objectMapper;
    private final EntityManagementServiceImpl entityManagementService;
    private final ContactService contactService;
    private final AccountService accountService;


    public InitAppService(CosightExecutionContext cosightExecutionContext, ForexQuoteService forexQuoteService, ObjectMapper objectMapper, EntityManagementServiceImpl entityManagementService, ContactService contactService, AccountService accountService) {
        this.cosightExecutionContext = cosightExecutionContext;
        this.forexQuoteService = forexQuoteService;
        this.objectMapper = objectMapper;
        this.entityManagementService = entityManagementService;
        this.contactService = contactService;
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("================================== START PROCESS ========================================");
        long ts = System.currentTimeMillis();
        if (args.length == 0) {
            throw new IllegalStateException("Unknown Entry point, no entry point provided");
        }

        logger.info("============= runtime info ================");
        logger.info(" runtime.database -> {}", cosightExecutionContext.getRuntimeInfo().getDatabase());
        logger.info(" orgId -> {}", cosightExecutionContext.getRuntimeInfo().getOrgId());
        logger.info(" OrgName -> {}", cosightExecutionContext.getRuntimeInfo().getOrgName());
        logger.info(" PluginName -> {}", cosightExecutionContext.getRuntimeInfo().getPluginName());
        logger.info(" PluginUUID -> {}", cosightExecutionContext.getRuntimeInfo().getPluginUuid());
        logger.info(" User -> {}", cosightExecutionContext.getRuntimeInfo().getUser());
        logger.info("============= END runtime info ================");

        // lets not worry about classprefix for now
//        String classPrefix = (String) cosightExecutionContext.getParameters().get("classPrefix");
//        logger.info("================================== USING CLASS PREFIX {} ========================================", classPrefix);
//        logger.info("context has values {} {}", cosightExecutionContext.getRuntimeInfo().getPluginName(), cosightExecutionContext.getRuntimeInfo().getPluginUuid());
//        logger.info("context {}", cosightExecutionContext.toString());

        // in here we may also build a config entity so that we can check on the state of the entities and see if they
        // need updating etc....

        // build contacts
        logger.info("================================== CHECKING IF CONTACTS BUILT ========================================");
        // we'll put check in here later. need to update SDK
//        buildContactsEntity();
        buildAccountsEntity();
        logger.info("================================== CHECKING IF CONTACTS BUILT SUCCESS ========================================");
        ArrayList tenId = (ArrayList) cosightExecutionContext.getParameters().get("Organisation ID");
        String xeroTenantId = (String) tenId.get(0);
        logger.info("================================== FETCHING OAUTH2 DETAILS ========================================");
        ExternalOAuth2Credentials deets = ExternalOAuth2Credentials.getInstance();
        String accessToken = deets.getToken().getAccessToken();

        logger.info("ACCESS TOKEN =" + accessToken);
        logger.info("================================== FETCHING OAUTH2 DETAILS SUCCESS ========================================");

        // now lets process the action
        if ("getContacts".equalsIgnoreCase(args[0])) {
            logger.info("================== FETCHING ACCOUNTS.CONTACTS FROM XERO =====================");
            if (args.length != 1) {

                logger.error("Need to provide the action for xero for this method");
                throw new IllegalStateException("Need to provide the action and the tenant-id for xero for this method");
            }
            cosightExecutionContext.getParameters().forEach((s, o) -> {
                logger.info("Parameter name {} = {}", s, o);
            });
            logger.info("================== FETCHING ACCOUNTS.CONTACTS FROM XERO USING TENANT-ID {} =====================", xeroTenantId);

            // XERO API CALLS - we'll move this to a service later
            ApiClient defaultClient = new ApiClient();
            AccountingApi accountingApi = AccountingApi.getInstance(defaultClient);
            try {
                Contacts contacts = accountingApi.getContacts(accessToken, xeroTenantId, null, null, null, null, null, null, null);

                contacts.getContacts().forEach(contact -> {
                    logger.info("Contact details {}", contact.getContactID());
                    contactService.upsertContact(contact);
                });

            } catch (Exception e) {
                logger.error("error getting contacts {}", e.getMessage());
                e.printStackTrace();
            }

        }
        if ("getAccounts".equalsIgnoreCase(args[0])) {

            ApiClient defaultClient = new ApiClient();
            AccountingApi accountingApi = AccountingApi.getInstance(defaultClient);
            try {
                Accounts accounts = accountingApi.getAccounts(accessToken, xeroTenantId, null, null, null);

                accounts.getAccounts().forEach(account -> {
                    accountService.upsertAccount(account);
                });

            } catch (Exception e) {
                logger.error("error getting Accounts {}", e.getMessage());
                e.printStackTrace();
            }

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

        logger.info("================================== Finished! ========================================");

    }

    private boolean checkContactsEntityExists() {
        return false;
    }

    private void buildContactsEntity() {
        entityManagementService.createContactClass("");
    }

    private void buildAccountsEntity() {
        entityManagementService.createAccountClass("");
    }

}
