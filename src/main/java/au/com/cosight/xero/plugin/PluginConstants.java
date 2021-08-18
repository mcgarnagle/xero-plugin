package au.com.cosight.xero.plugin;

public class PluginConstants {
    //entities
    public static final String XERO_ENTITY_CONTACT = "Contact";
    public static final String XERO_ENTITY_BANK_TRANSACTION = "BankTransaction";
    public static final String XERO_ENTITY_CONTACT_GROUP = "ContactGroup";
    public static final String XERO_ENTITY_ADDRESS = "Address";
    public static final String XERO_ENTITY_ATTACHMENT = "Attachment";
    public static final String XERO_ENTITY_BATCH_PAYMENT_DETAILS = "BatchPaymentDetails";
    public static final String XERO_ENTITY_CONTACT_PERSON = "ContactPerson";
    public static final String XERO_ENTITY_PHONE = "Phone";
    public static final String XERO_ENTITY_SALES_TRACKING_CATEGORY = "SalesTrackingCategory";
    public static final String XERO_ENTITY_VALIDATION_ERROR = "ValidationError";
    public static final String XERO_ENTITY_LINE_ITEM = "LineItem";
    public static final String XERO_ENTITY_LINE_ITEM_TRACKING = "LineItemTracking";

    public static final String XERO_ENTITY_ACCOUNT = "Account";
    //relationships
    public static final String XERO_RELATIONSHIP_CONTACT_TO_CONTACT_GROUP = "ContactToContactGroup";
    public static final String XERO_RELATIONSHIP_CONTACT_TO_ADDRESS = "ContactToAddress";
    public static final String XERO_RELATIONSHIP_CONTACT_TO_ATTACHMENT = "ContactToAttachment";
    public static final String XERO_RELATIONSHIP_CONTACT_TO_BATCH_PAYMENT_DETAILS = "ContactToBatchPaymentDetails";
    public static final String XERO_RELATIONSHIP_CONTACT_TO_CONTACT_PERSON = "ContactToContactPerson";
    public static final String XERO_RELATIONSHIP_CONTACT_TO_PHONE = "ContactToPhone";
    public static final String XERO_RELATIONSHIP_CONTACT_TO_SALES_TRACKING_CATEGORY = "ContactToSalesTrackingCategory";
    public static final String XERO_RELATIONSHIP_CONTACT_TO_VALIDATION_ERROR = "ContactToValidationError";

    public static final String XERO_RELATIONSHIP_ACCOUNT_TO_VALIDATION_ERROR = "AccountToValidationError";

    public static final String XERO_RELATIONSHIP_BANK_TRANSACTION_TO_VALIDATION_ERROR = "BankTransactionToValidationError";
    public static final String XERO_RELATIONSHIP_BANK_TRANSACTION_TO_CONTACT = "BankTransactionToContact";
    public static final String XERO_RELATIONSHIP_BANK_TRANSACTION_TO_ACCOUNT = "BankTransactionToAccount";
    public static final String XERO_RELATIONSHIP_BANK_TRANSACTION_TO_LINE_ITEM = "BankTransactionToLineItem";
    public static final String XERO_RELATIONSHIP_LINE_ITEM_TO_LINE_ITEM_TRACKING = "LineItemToLineItemTracking";


}
