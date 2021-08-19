package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.BankTransaction;
import com.xero.models.accounting.LineItem;
import com.xero.models.accounting.ValidationError;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BankTransactionMapper {
    public static final String BANK_TRANSACTION_INSTANCES = "BankInstances";
    public static final String CONTACT_INSTANCES = "ContactInstances";
    public static final String ACCOUNT_INSTANCES = "AccountInstances";

    public static HashMap<String, List<EntityInstance>> toEntityInstance(BankTransaction bankTransaction) {
        HashMap returnMap = new HashMap();
        List<EntityInstance> bankTransactionInstanceList = new ArrayList<>();
        List<EntityInstance> contactInstanceList = new ArrayList<>();
        List<EntityInstance> accountInstanceList = new ArrayList<>();

        returnMap.put(BANK_TRANSACTION_INSTANCES, bankTransactionInstanceList);
        returnMap.put(CONTACT_INSTANCES, contactInstanceList);
        returnMap.put(ACCOUNT_INSTANCES, accountInstanceList);

        EntityInstance bankInstance = new EntityInstance();
        bankInstance.set_vertexName(PluginConstants.XERO_ENTITY_BANK_TRANSACTION);
        bankInstance.set_label(PluginConstants.XERO_ENTITY_BANK_TRANSACTION);
        ArrayList<InstanceValue> bankTransactionInstanceValues = new ArrayList<>();
        bankInstance.setInstanceValues(bankTransactionInstanceValues);
        bankTransactionInstanceValues.add(new InstanceValue("BankTransactionId", bankTransaction.getBankTransactionID()));
        bankTransactionInstanceValues.add(new InstanceValue("Type", bankTransaction.getType().getValue()));
//        contactInstanceValues.add(new InstanceValue("Contact", bankTransaction.getc));
        bankTransactionInstanceValues.add(new InstanceValue("CurrencyCode", bankTransaction.getCurrencyCode()));
        bankTransactionInstanceValues.add(new InstanceValue("CurrencyRate", bankTransaction.getCurrencyRate()));
        LocalDate instant = bankTransaction.getDateAsDate();
        Date date = DateTimeUtils.toSqlDate(instant);
        java.util.Date newDate = new java.util.Date(date.getTime());
        bankTransactionInstanceValues.add(new InstanceValue("Date", newDate));
        bankTransactionInstanceValues.add(new InstanceValue("HasAttachments", bankTransaction.getHasAttachments()));
        bankTransactionInstanceValues.add(new InstanceValue("IsReconciled", bankTransaction.getIsReconciled()));
        bankTransactionInstanceValues.add(new InstanceValue("LineAmountTypes", bankTransaction.getLineAmountTypes().getValue()));
        bankTransactionInstanceValues.add(new InstanceValue("OverpaymentId", bankTransaction.getOverpaymentID()));
        bankTransactionInstanceValues.add(new InstanceValue("PrepaymentId", bankTransaction.getPrepaymentID()));
        bankTransactionInstanceValues.add(new InstanceValue("Reference", bankTransaction.getReference()));
//        contactInstanceValues.add(new InstanceValue("Status", bankTransaction.getStatus().getValue()));
        bankTransactionInstanceValues.add(new InstanceValue("Status", bankTransaction.getStatusAttributeString()));
        bankTransactionInstanceValues.add(new InstanceValue("Subtotal", bankTransaction.getSubTotal()));
        bankTransactionInstanceValues.add(new InstanceValue("Total", bankTransaction.getTotal()));
        bankTransactionInstanceValues.add(new InstanceValue("TotalTax", bankTransaction.getTotalTax()));
        bankTransactionInstanceValues.add(new InstanceValue("UpdatedDateUTC", DateTimeUtils.toDate(bankTransaction.getUpdatedDateUTCAsDate().toInstant())));
        bankTransactionInstanceValues.add(new InstanceValue("Url", bankTransaction.getUrl()));
        bankTransactionInstanceList.add(bankInstance);
        // setup Account
        if (bankTransaction.getBankAccount() != null) {
            List<EntityInstance> bankAccount = AccountMapper.toEntityInstance(bankTransaction.getBankAccount());
            accountInstanceList.addAll(bankAccount);
        }
        // setup Contact
        if (bankTransaction.getContact() != null) {
            List<EntityInstance> contact = ContactMapper.toEntityInstance(bankTransaction.getContact());
            contactInstanceList.addAll(contact);
        }

        // setup line Item
        if (bankTransaction.getLineItems() != null) {
            List<LineItem> lineItems = bankTransaction.getLineItems();
            lineItems.forEach(lineItem -> {
                EntityInstance lineItemInstance = new EntityInstance();
                List<InstanceValue> lineItemInstanceValues = new ArrayList<>();
                lineItemInstance.setInstanceValues(lineItemInstanceValues);
                lineItemInstance.set_label(PluginConstants.XERO_ENTITY_LINE_ITEM);
                lineItemInstance.set_vertexName(PluginConstants.XERO_ENTITY_LINE_ITEM);

                lineItemInstanceValues.add(new InstanceValue("AccountId", lineItem.getAccountID().toString()));
                lineItemInstanceValues.add(new InstanceValue("AccountCode", lineItem.getAccountCode()));
                lineItemInstanceValues.add(new InstanceValue("Description", lineItem.getDescription()));
                lineItemInstanceValues.add(new InstanceValue("LineItemId", lineItem.getLineItemID()));
                lineItemInstanceValues.add(new InstanceValue("ItemCode", lineItem.getItemCode()));
                lineItemInstanceValues.add(new InstanceValue("DiscountAmount", lineItem.getDiscountAmount()));
                lineItemInstanceValues.add(new InstanceValue("DiscountRate", lineItem.getDiscountRate()));
                lineItemInstanceValues.add(new InstanceValue("LineAmount", lineItem.getLineAmount()));
                lineItemInstanceValues.add(new InstanceValue("Quantity", lineItem.getQuantity()));
                lineItemInstanceValues.add(new InstanceValue("RepeatingInvoiceId", lineItem.getRepeatingInvoiceID().toString()));
                lineItemInstanceValues.add(new InstanceValue("TaxAmount", lineItem.getTaxAmount()));
                lineItemInstanceValues.add(new InstanceValue("TaxType", lineItem.getTaxType()));
                lineItemInstanceValues.add(new InstanceValue("UnitAmount", lineItem.getUnitAmount()));

                bankTransactionInstanceList.add(lineItemInstance);

                if (lineItem.getTracking() != null && lineItem.getTracking().size() > 0) {
                    lineItem.getTracking().forEach(lineItemTracking -> {
                        EntityInstance lineItemTrackingInstance = new EntityInstance();
                        List<InstanceValue> lineItemTrackingInstanceValues = new ArrayList<>();
                        lineItemTrackingInstance.set_label(PluginConstants.XERO_ENTITY_LINE_ITEM_TRACKING);
                        lineItemTrackingInstance.set_vertexName(PluginConstants.XERO_ENTITY_LINE_ITEM_TRACKING);
                        lineItemTrackingInstance.setInstanceValues(lineItemTrackingInstanceValues);

                        lineItemTrackingInstanceValues.add(new InstanceValue("", lineItemTracking.getName()));
                        lineItemTrackingInstanceValues.add(new InstanceValue("", lineItemTracking.getTrackingCategoryID()));
                        lineItemTrackingInstanceValues.add(new InstanceValue("", lineItemTracking.getTrackingOptionID()));
                        lineItemTrackingInstanceValues.add(new InstanceValue("", lineItemTracking.getOption()));

                        // hack to store reference - remove this later
                        lineItemTrackingInstanceValues.add(new InstanceValue("LineItem", lineItem.getLineItemID()));
                        bankTransactionInstanceList.add(lineItemTrackingInstance);
                    });
                }
            });
        }

        // setup validation errors
        if (bankTransaction.getValidationErrors() != null) {
            List<ValidationError> xeroObjectList = bankTransaction.getValidationErrors();
            xeroObjectList.forEach(xeroObject -> {
                EntityInstance cosightInstance = new EntityInstance();
                cosightInstance.set_vertexName(PluginConstants.XERO_ENTITY_VALIDATION_ERROR);
                cosightInstance.set_label(PluginConstants.XERO_ENTITY_VALIDATION_ERROR);
                List<InstanceValue> cosightInstanceValues = new ArrayList<>();
                cosightInstance.setInstanceValues(cosightInstanceValues);
                cosightInstanceValues.add(new InstanceValue("Message", xeroObject.getMessage()));
                bankTransactionInstanceList.add(cosightInstance);
            });
        }

        return returnMap;
    }
}
