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

    public static final String XERO_ENTITY_INVOICE = "Invoice";
    public static final String XERO_ENTITY_ALLOCATION = "Allocation";
    public static final String XERO_ENTITY_LINE_ITEM = "LineItem";
    public static final String XERO_ENTITY_LINE_ITEM_TRACKING = "LineItemTracking";
    public static final String XERO_ENTITY_CREDIT_NOTE = "CreditNote";
    public static final String XERO_ENTITY_OVERPAYMENT = "Overpayment";
    public static final String XERO_ENTITY_PAYMENT = "Payment";
    public static final String XERO_ENTITY_PREPAYMENT = "Prepayment";

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

    // Invoice
    public static final String XERO_RELATIONSHIP_INVOICE_TO_ATTACHMENT = "InvoiceToAttachment";
    public static final String XERO_RELATIONSHIP_INVOICE_TO_CONTACT = "InvoiceToContact";
    public static final String XERO_RELATIONSHIP_INVOICE_TO_CREDITNOTE = "InvoiceToCreditNote";
    public static final String XERO_RELATIONSHIP_INVOICE_TO_LINEITEM = "InvoiceToLineItem";
    public static final String XERO_RELATIONSHIP_INVOICE_TO_OVERPAYMENT = "InvoiceToOverpayment";
    public static final String XERO_RELATIONSHIP_INVOICE_TO_PAYMENT = "InvoiceToPayment";
    public static final String XERO_RELATIONSHIP_INVOICE_TO_PREPAYMENT = "InvoiceToPrepayment";
    public static final String XERO_RELATIONSHIP_INVOICE_TO_VALIDATION_ERROR = "InvoiceToValidtionError";
    public static final String XERO_RELATIONSHIP_INVOICE_TO_VALIDATION_WARNING = "InvoiceToValidtionError_Warning";

    // Credit_note

    public static final String XERO_RELATIONSHIP_CREDIT_NOTE_TO_ALLOCATION = "CreditNoteToAllocation";
    public static final String XERO_RELATIONSHIP_CREDIT_NOTE_TO_ATTACHMENT = "CreditNoteToAttachment";
    public static final String XERO_RELATIONSHIP_CREDIT_NOTE_TO_CONTACT = "CreditNoteToContact";
    public static final String XERO_RELATIONSHIP_CREDIT_NOTE_TO_PAYMENT = "CreditNoteToPayment";
    public static final String XERO_RELATIONSHIP_CREDIT_NOTE_TO_LINEITEM = "CreditNoteToAllocation";
    public static final String XERO_RELATIONSHIP_CREDIT_NOTE_TO_VALIDATION_ERROR = "CreditNoteToValidationError";
    public static final String XERO_RELATIONSHIP_CREDIT_NOTE_TO_VALIDATION_WARNING = "CreditNoteToValidationError_Warning";
    
    // Prepayment

    public static final String XERO_RELATIONSHIP_PREPAYMENT_TO_CONTACT = "PrepaymentToContact";
    public static final String XERO_RELATIONSHIP_PREPAYMENT_TO_ALLOCATION = "PrepaymentToAllocation";
    public static final String XERO_RELATIONSHIP_PREPAYMENT_TO_ATTACHMENT = "PrepaymentToAttachment";
    public static final String XERO_RELATIONSHIP_PREPAYMENT_TO_LINEITEM = "PrepaymentToLineItem";
    public static final String XERO_RELATIONSHIP_PREPAYMENT_TO_PAYMENT = "PrepaymentToPayment";

    // Overpayment

    public static final String XERO_RELATIONSHIP_OVERPAYMENT_TO_CONTACT = "OverpaymentToContact";
    public static final String XERO_RELATIONSHIP_OVERPAYMENT_TO_ALLOCATION = "OverpaymentToAllocation";
    public static final String XERO_RELATIONSHIP_OVERPAYMENT_TO_ATTACHMENT = "OverpaymentToAttachment";
    public static final String XERO_RELATIONSHIP_OVERPAYMENT_TO_LINEITEM = "OverpaymentToLineItem";
    public static final String XERO_RELATIONSHIP_OVERPAYMENT_TO_PAYMENT = "OverpaymentToPayment";

    // Payment


    public static final String XERO_RELATIONSHIP_PAYMENT_TO_ACCOUNT = "PaymentToAccount";
    public static final String XERO_RELATIONSHIP_PAYMENT_TO_CONTACT = "PaymentToContact";
    public static final String XERO_RELATIONSHIP_PAYMENT_TO_ALLOCATION = "PaymentToAllocation";
    public static final String XERO_RELATIONSHIP_PAYMENT_TO_ATTACHMENT = "PaymentToAttachment";
    public static final String XERO_RELATIONSHIP_PAYMENT_TO_LINEITEM = "PaymentToLineItem";
    public static final String XERO_RELATIONSHIP_PAYMENT_TO_PAYMENT = "PaymentToPayment";

}
