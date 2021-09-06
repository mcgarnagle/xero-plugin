package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import au.com.cosight.xero.plugin.structure.GraphStructure;
import com.xero.models.accounting.Contact;
import com.xero.models.accounting.Invoice;

import java.util.ArrayList;

public class InvoiceMapper extends BaseMapper {

    public static EntityInstance toEntityInstance(Invoice invoice) {
        // create the invoice

        EntityInstance invoiceInstance = new EntityInstance();
        ArrayList<InstanceValue> invoiceInstanceValues = new ArrayList<>();
        invoiceInstance.setInstanceValues(invoiceInstanceValues);
        invoiceInstance.set_vertexName(PluginConstants.XERO_ENTITY_INVOICE);
        invoiceInstance.set_label(PluginConstants.XERO_ENTITY_INVOICE);

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
        if(invoice.getExpectedPaymentDateAsDate()!=null) {
            invoiceInstanceValues.add(new InstanceValue("ExpectedPaymentDate", getDate(invoice.getExpectedPaymentDateAsDate())));
        }
        if(invoice.getFullyPaidOnDateAsDate()!=null) {
            invoiceInstanceValues.add(new InstanceValue("FullyPaidOnDate", getDate(invoice.getFullyPaidOnDateAsDate())));
        }
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




        return invoiceInstance;
    }
}
