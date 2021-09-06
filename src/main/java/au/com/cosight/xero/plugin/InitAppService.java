package au.com.cosight.xero.plugin;

import au.com.cosight.common.dto.plugin.CosightExecutionContext;
import au.com.cosight.sdk.auth.external.oauth.ExternalOAuth2Credentials;
import au.com.cosight.xero.plugin.service.EntityManagementServiceImpl;
import au.com.cosight.xero.plugin.service.xero.AccountService;
import au.com.cosight.xero.plugin.service.xero.BankTransactionService;
import au.com.cosight.xero.plugin.service.xero.ContactService;
import au.com.cosight.xero.plugin.service.xero.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xero.api.ApiClient;
import com.xero.api.client.AccountingApi;
import com.xero.api.client.IdentityApi;
import com.xero.models.accounting.*;
import com.xero.models.identity.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Configuration
public class InitAppService implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger("xero-plugin");

    // will have a parameter for 'classPrefix' that will be used to prefix classes


    private final CosightExecutionContext cosightExecutionContext;
    private final ObjectMapper objectMapper;
    private final EntityManagementServiceImpl entityManagementService;
    private final ContactService contactService;
    private final AccountService accountService;
    private final BankTransactionService bankTransactionService;
    private final InvoiceService invoiceService;


    public InitAppService(CosightExecutionContext cosightExecutionContext,
                          ObjectMapper objectMapper, EntityManagementServiceImpl entityManagementService,
                          ContactService contactService, AccountService accountService,
                          BankTransactionService bankTransactionService, InvoiceService invoiceService) {
        this.cosightExecutionContext = cosightExecutionContext;
        this.objectMapper = objectMapper;
        this.entityManagementService = entityManagementService;
        this.contactService = contactService;
        this.accountService = accountService;
        this.bankTransactionService = bankTransactionService;
        this.invoiceService = invoiceService;
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
//        buildAccountsEntity();
//        buildBankTransactionEntity();
        logger.info("================================== CHECKING IF CONTACTS BUILT SUCCESS ========================================");

        ArrayList params = (ArrayList) cosightExecutionContext.getParameters().get("OrganisationName");
        String xeroOrgName = (String) params.get(0); // first parameter

//        ArrayList tenId = (ArrayList) cosightExecutionContext.getParameters().get("Organisation ID");
//        String xeroTenantId = (String) tenId.get(0);
        logger.info("================================== FETCHING OAUTH2 DETAILS ========================================");
        ExternalOAuth2Credentials deets = ExternalOAuth2Credentials.getInstance();
        String accessToken = deets.getToken().getAccessToken();
        logger.info("================================== FETCHING OAUTH2 DETAILS SUCCESS ========================================");

        ApiClient defaultClient = new ApiClient();
//        DecodedJWT jwt = JWT.decode(accessToken);
//        DecodedJWT verified = defaultClient.verify(accessToken);
//        logger.info("ACCESS TOKEN verified=" +verified.getToken());

        ApiClient defaultIdentityClient = new ApiClient("https://api.xero.com", null, null, null, null);

//        IdentityApi idApi = new IdentityApi(defaultIdentityClient);
//        List<Connection> connection = idApi.getConnections(jwt.getToken(),null);
        String xeroTenantId = getTenantId(xeroOrgName, accessToken);
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
        if ("init".equalsIgnoreCase(args[0])) {
            buildContactsEntity();
            buildAccountsEntity();
            buildBankTransactionEntity();
            buildInvoiceEntities();

        }

        if ("getAccounts".equalsIgnoreCase(args[0])) {
            AccountingApi accountingApi = AccountingApi.getInstance(defaultClient);
            try {
                Accounts accounts = accountingApi.getAccounts(accessToken, xeroTenantId, null, null, null);
                accountingApi.getItems(accessToken, xeroTenantId, null, null, null, null);
                accounts.getAccounts().forEach(account -> {
                    accountService.upsertAccount(account);
                });

            } catch (Exception e) {
                logger.error("error getting Accounts {}", e.getMessage());
                e.printStackTrace();
            }

        }
        if ("getInvoices".equalsIgnoreCase(args[0])) {
            AccountingApi accountingApi = AccountingApi.getInstance(defaultClient);
            try {
                Invoices invoices = accountingApi.getInvoices(accessToken, xeroTenantId, null,
                        null, null, null, null, null, null,
                        null, false, false, null, false);

                invoices.getInvoices().forEach(invoice -> {
                    invoiceService.upsertInvoice(invoice);
                    // iplement invoices
                });
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("error getting Invoices {} ", e.getMessage());
            }

        }
        if ("getBankTransactions".equalsIgnoreCase(args[0])) {
//            buildBankTransactionEntity();
            AccountingApi accountingApi = AccountingApi.getInstance(defaultClient);
            try {
                BankTransactions bankTransactions = accountingApi.getBankTransactions(accessToken, xeroTenantId, null, null, null, null, null);

                bankTransactions.getBankTransactions().forEach(bankTransaction -> {
                    bankTransactionService.upsertBankTransaction(bankTransaction);

                });

            } catch (Exception e) {
                logger.error("error getting Accounts {}", e.getMessage());
                e.printStackTrace();
            }


        }
        if ("getBankTransfers".equalsIgnoreCase(args[0])) {


        }
        if ("getBatchPayments".equalsIgnoreCase(args[0])) {


        }
        if ("getBudgets".equalsIgnoreCase(args[0])) {


        }
        if ("getContactGroups".equalsIgnoreCase(args[0])) {


        }
        if ("getCreditNotes".equalsIgnoreCase(args[0])) {


        }
        if ("getCurrencies".equalsIgnoreCase(args[0])) {


        }
        if ("getEmployees".equalsIgnoreCase(args[0])) {


        }
        if ("getExpenseClaims".equalsIgnoreCase(args[0])) {


        }
        if ("getInvoiceReminders".equalsIgnoreCase(args[0])) {


        }
        if ("getItems".equalsIgnoreCase(args[0])) {
            AccountingApi accountingApi = AccountingApi.getInstance(defaultClient);
            try {
                Items items = accountingApi.getItems(accessToken, xeroTenantId, null, null, null, null);
                items.getItems().forEach(item -> {

//                    item.get

                });

            } catch (Exception e) {
                logger.error("error getting Accounts {}", e.getMessage());
                e.printStackTrace();
            }


        }
        if ("getJournals".equalsIgnoreCase(args[0])) {


        }
        if ("getLinkedTransactions".equalsIgnoreCase(args[0])) {


        }
        if ("getManualJournals".equalsIgnoreCase(args[0])) {


        }
        if ("getOrganisations".equalsIgnoreCase(args[0])) {


        }
        if ("getOverpayments".equalsIgnoreCase(args[0])) {


        }
        if ("getPayments".equalsIgnoreCase(args[0])) {


        }
        if ("getPaymentServices".equalsIgnoreCase(args[0])) {


        }
        if ("getPrepayments".equalsIgnoreCase(args[0])) {


        }
        if ("getPurchaseOrders".equalsIgnoreCase(args[0])) {


        }
        if ("getQuotes".equalsIgnoreCase(args[0])) {


        }
        if ("getReceipts".equalsIgnoreCase(args[0])) {


        }
        if ("getRepeatingInvoices".equalsIgnoreCase(args[0])) {


        }
        if ("getReports".equalsIgnoreCase(args[0])) {


        }
        if ("getSetup".equalsIgnoreCase(args[0])) {


        }
        if ("getTaxRates".equalsIgnoreCase(args[0])) {


        }

        logger.info("================================== Finished! ========================================");

    }

    private String getTenantId(String xeroOrgName, String accessToken) throws IOException {

        ApiClient defaultIdentityClient = new ApiClient("https://api.xero.com", null, null, null, null);
        IdentityApi idApi = new IdentityApi(defaultIdentityClient);
        List<Connection> connection = idApi.getConnections(accessToken, null);
        // correct way to set value inside lambda
        // java 10+ looks like below
        //        var wrapper = new Object(){ int ordinal = 0; };
        //        list.forEach(s -> {
        //            s.setOrdinal(wrapper.ordinal++);
        //        });
        AtomicReference<String> tenantId = new AtomicReference<>("");
        connection.stream().filter(x -> x.getTenantName().equalsIgnoreCase(xeroOrgName)).findFirst().ifPresent(connection1 -> {
            tenantId.set(connection1.getTenantId().toString());
        });

        return tenantId.get();
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

    private void buildBankTransactionEntity() {
        entityManagementService.createBankTransactionClass("");

    }

    private void buildInvoiceEntities() {
        entityManagementService.createInvoiceClass("");
    }

}
