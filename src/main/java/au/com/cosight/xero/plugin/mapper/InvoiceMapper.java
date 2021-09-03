package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.structure.GraphStructure;
import com.xero.models.accounting.Invoice;

import java.util.ArrayList;

public class InvoiceMapper extends BaseMapper {

    public static GraphStructure toEntityInstance(Invoice invoice) {
        // create the invoice
        GraphStructure theStructure = new GraphStructure();

        EntityInstance invoiceInstance = new EntityInstance();
        theStructure.getEntityInstances().put(invoice.getInvoiceID().toString(), invoiceInstance);
        ArrayList<InstanceValue> invoiceInstanceValues = new ArrayList<>();
        invoiceInstance.setInstanceValues(invoiceInstanceValues);

        invoiceInstanceValues.add(new InstanceValue("AmountCredited", invoice.getAmountCredited()));
        invoiceInstanceValues.add(new InstanceValue("AmountDue", invoice.getAmountDue()));
        invoiceInstanceValues.add(new InstanceValue("AmountPaid", invoice.getAmountPaid()));
        invoiceInstanceValues.add(new InstanceValue("InvoiceId", invoice.getInvoiceID()));
        invoiceInstanceValues.add(new InstanceValue("BrandingThemeId", invoice.getBrandingThemeID()));
        invoiceInstanceValues.add(new InstanceValue("CiSDeduction", invoice.getCiSDeduction()));
        invoiceInstanceValues.add(new InstanceValue("CisRate", invoice.getCiSRate()));
        invoiceInstanceValues.add(new InstanceValue("CurrencyCode", invoice.getCurrencyCode().getValue()));
        invoiceInstanceValues.add(new InstanceValue("CurrencyRate", invoice.getCurrencyRate()));
        invoiceInstanceValues.add(new InstanceValue("Date", getDate(invoice.getDateAsDate())));
        invoiceInstanceValues.add(new InstanceValue("DueDate", getDate(invoice.getDueDateAsDate())));
        invoiceInstanceValues.add(new InstanceValue("ExpectedPaymentDate", getDate(invoice.getExpectedPaymentDateAsDate())));
        invoiceInstanceValues.add(new InstanceValue("FullyPaidOnDate", getDate(invoice.getFullyPaidOnDateAsDate())));
        invoiceInstanceValues.add(new InstanceValue("HasAttachments", invoice.getHasAttachments()));
        invoiceInstanceValues.add(new InstanceValue("HasErrors", invoice.getHasErrors()));
        invoiceInstanceValues.add(new InstanceValue("InvoiceNumber", invoice.getInvoiceNumber()));
        invoiceInstanceValues.add(new InstanceValue("IsDiscounted", invoice.getIsDiscounted()));
        invoiceInstanceValues.add(new InstanceValue("LineAmountTypes", invoice.getLineAmountTypes().getValue()));
        invoiceInstanceValues.add(new InstanceValue("PlannedPaymentAsDate", getDate(invoice.getPlannedPaymentDateAsDate())));
        invoiceInstanceValues.add(new InstanceValue("Reference", invoice.getReference()));
        invoiceInstanceValues.add(new InstanceValue("RepeatingInvoiceId", invoice.getRepeatingInvoiceID()));
        invoiceInstanceValues.add(new InstanceValue("SentToContact", invoice.getSentToContact()));
        invoiceInstanceValues.add(new InstanceValue("Status", invoice.getStatusAttributeString()));
        invoiceInstanceValues.add(new InstanceValue("SubTotal", invoice.getSubTotal()));
        invoiceInstanceValues.add(new InstanceValue("Total", invoice.getTotal()));
        invoiceInstanceValues.add(new InstanceValue("TotalDiscount", invoice.getTotalDiscount()));
        invoiceInstanceValues.add(new InstanceValue("TotalTax", invoice.getTotalTax()));
        invoiceInstanceValues.add(new InstanceValue("Type", invoice.getType()));
        invoiceInstanceValues.add(new InstanceValue("UpdatedDate", getDate(invoice.getUpdatedDateUTCAsDate())));
        invoiceInstanceValues.add(new InstanceValue("Url", invoice.getUrl()));

        // invoice added to structure, now map and depth map others....

        // first, we'll go ahead and create all the entities.
        // once we've done that, we'll cycle through the xero objects again
        // and build relationships from the entities that are sitting in the graph structure


        invoice.getOverpayments().forEach(overpayment -> {
            EntityInstance overpaymentInstance = new EntityInstance();
            ArrayList<InstanceValue> overpaymentInstanceValues = new ArrayList<>();
            overpaymentInstance.setInstanceValues(overpaymentInstanceValues);
            overpaymentInstanceValues.add(new InstanceValue("CurrencyRate", overpayment.getCurrencyRate()));
            overpaymentInstanceValues.add(new InstanceValue("CurrencyCode", overpayment.getCurrencyCode().getValue()));
            overpaymentInstanceValues.add(new InstanceValue("Date", getDate(overpayment.getDateAsDate())));
            overpaymentInstanceValues.add(new InstanceValue("AppliedAmount", overpayment.getAppliedAmount()));
            overpaymentInstanceValues.add(new InstanceValue("HasAttachemnts", overpayment.getHasAttachments()));
            overpaymentInstanceValues.add(new InstanceValue("LineAmountTypes", overpayment.getLineAmountTypes().getValue()));
            overpaymentInstanceValues.add(new InstanceValue("OverpaymentId", overpayment.getOverpaymentID()));
            overpaymentInstanceValues.add(new InstanceValue("RemainingCredit", overpayment.getRemainingCredit()));
            overpaymentInstanceValues.add(new InstanceValue("Status", overpayment.getStatus().getValue()));
            overpaymentInstanceValues.add(new InstanceValue("SubTotal", overpayment.getSubTotal()));
            overpaymentInstanceValues.add(new InstanceValue("Total", overpayment.getTotal()));
            overpaymentInstanceValues.add(new InstanceValue("TotalTax", overpayment.getTotalTax()));
            overpaymentInstanceValues.add(new InstanceValue("Type", overpayment.getType().getValue()));
            overpaymentInstanceValues.add(new InstanceValue("UpdatedDate", getDate(overpayment.getUpdatedDateUTCAsDate())));

            theStructure.getEntityInstances().put(overpayment.getOverpaymentID().toString(), overpaymentInstance);

        });

        invoice.getPayments().forEach(payment -> {
            EntityInstance theInstance = new EntityInstance();
            ArrayList<InstanceValue> theInstanceValues = new ArrayList<>();
            theInstance.setInstanceValues(theInstanceValues);
            theInstanceValues.add(new InstanceValue("CurrencyRate", payment.getCurrencyRate()));
            theInstanceValues.add(new InstanceValue("Date", getDate(payment.getDateAsDate())));
            theInstanceValues.add(new InstanceValue("Status", payment.getStatus().getValue()));
            theInstanceValues.add(new InstanceValue("InvoiceNumber", payment.getInvoiceNumber()));
            theInstanceValues.add(new InstanceValue("Amount", payment.getAmount()));
            theInstanceValues.add(new InstanceValue("BankAccountNumber", payment.getBankAccountNumber()));
            theInstanceValues.add(new InstanceValue("BatchPaymentId", payment.getBatchPaymentID().toString()));
            theInstanceValues.add(new InstanceValue("Code", payment.getCode()));
            theInstanceValues.add(new InstanceValue("CreditNoteNumber", payment.getCreditNoteNumber()));
            theInstanceValues.add(new InstanceValue("Details", payment.getDetails()));
            theInstanceValues.add(new InstanceValue("HasAccount", payment.getHasAccount()));
            theInstanceValues.add(new InstanceValue("HasValidationErrors", payment.getHasValidationErrors()));
            theInstanceValues.add(new InstanceValue("IsReconciled", payment.getIsReconciled()));
            theInstanceValues.add(new InstanceValue("Particulars", payment.getParticulars()));
            theInstanceValues.add(new InstanceValue("PaymentId", payment.getPaymentID()));
            theInstanceValues.add(new InstanceValue("Reference", payment.getReference()));
            theInstanceValues.add(new InstanceValue("UpdatedDate", getDate(payment.getUpdatedDateUTCAsDate())));

            theStructure.getEntityInstances().put(payment.getPaymentID().toString(), theInstance);
        });

        invoice.getPrepayments().forEach(prepayment -> {

            EntityInstance theInstance = new EntityInstance();
            ArrayList<InstanceValue> theInstanceValues = new ArrayList<>();
            theInstance.setInstanceValues(theInstanceValues);
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getCurrencyRate()));
            theInstanceValues.add(new InstanceValue("CurrencyCode", prepayment.getCurrencyCode().getValue()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getAppliedAmount()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", getDate(prepayment.getDateAsDate())));
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getHasAttachments()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getLineAmountTypes()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getPrepaymentID()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getReference()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getRemainingCredit()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getStatus()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getSubTotal()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getTotal()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getTotalTax()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getType()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", getDate(prepayment.getUpdatedDateUTCAsDate())));

            theStructure.getEntityInstances().put(prepayment.getPrepaymentID().toString(),theInstance);

        });

        invoice.getCreditNotes().forEach(creditNote -> {
            EntityInstance theInstance = new EntityInstance();
            ArrayList<InstanceValue> theInstanceValues = new ArrayList<>();
            theInstance.setInstanceValues(theInstanceValues);
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getCurrencyRate()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getCurrencyCode().getValue()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getCreditNoteNumber()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getCiSRate()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getAppliedAmount()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getBrandingThemeID()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getCiSDeduction()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getCreditNoteID().toString()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", getDate(creditNote.getDateAsDate())));
            theInstanceValues.add(new InstanceValue("CurrencyRate", getDate(creditNote.getFullyPaidOnDateAsDate())));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getHasAttachments()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getHasErrors()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getLineAmountTypes()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getReference()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getRemainingCredit()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getSentToContact()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getStatus()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getSubTotal()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getTotal()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getTotalTax()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getType().getValue()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", getDate(creditNote.getUpdatedDateUTCAsDate())));
            theStructure.getEntityInstances().put(creditNote.getCreditNoteID().toString(),theInstance);
        });

        invoice.getAttachments().forEach(attachment -> {
            EntityInstance theInstance = new EntityInstance();
            ArrayList<InstanceValue> theInstanceValues = new ArrayList<>();
            theInstance.setInstanceValues(theInstanceValues);
            theInstanceValues.add(new InstanceValue("CurrencyRate", attachment.getAttachmentID()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", attachment.getUrl()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", attachment.getContentLength()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", attachment.getFileName()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", attachment.getIncludeOnline()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", attachment.getMimeType()));

            theStructure.getEntityInstances().put(attachment.getAttachmentID().toString(),theInstance);
        });

        invoice.getLineItems().forEach(lineItem -> {
            EntityInstance theInstance = new EntityInstance();
            ArrayList<InstanceValue> theInstanceValues = new ArrayList<>();
            theInstance.setInstanceValues(theInstanceValues);
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getLineItemID().toString()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getUnitAmount()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getTaxType()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getRepeatingInvoiceID().toString()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getLineAmount()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getTaxAmount()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getQuantity()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getDiscountRate()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getDiscountAmount()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getDescription()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getItemCode()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getAccountCode()));
            theInstanceValues.add(new InstanceValue("CurrencyRate", lineItem.getAccountID().toString()));

            theStructure.getEntityInstances().put(lineItem.getLineItemID().toString(),theInstance);
        });

        // mapContact - this will only contain basic information, what should we do here?
        invoice.getContact();



        invoice.getValidationErrors().forEach(validationError -> {

        });
        invoice.getWarnings().forEach(validationErrorWarning -> {

        });

        // now do mappings from other objects that have relations...


        return theStructure;
    }
}
