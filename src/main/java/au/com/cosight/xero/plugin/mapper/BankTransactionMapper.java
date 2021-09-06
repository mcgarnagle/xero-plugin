package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.BankTransaction;
import com.xero.models.accounting.LineItem;
import com.xero.models.accounting.ValidationError;
import org.threeten.bp.DateTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BankTransactionMapper extends BaseMapper {
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
        bankTransactionInstanceValues.add(new InstanceValue("Date", getDate(bankTransaction.getDateAsDate())));
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
            List<EntityInstance> contact = ContactMapper.toEntityInstances(bankTransaction.getContact());
            contactInstanceList.addAll(contact);
        }

        // setup line Item
        if (bankTransaction.getLineItems() != null) {
            List<LineItem> lineItems = bankTransaction.getLineItems();
            lineItems.forEach(lineItem -> {
                bankTransactionInstanceList.add(LineItemMapper.toInstance(lineItem));

                if (lineItem.getTracking() != null && lineItem.getTracking().size() > 0) {
                    lineItem.getTracking().forEach(lineItemTracking -> {

                        EntityInstance lineItemtrack = LineItemTrackingMapper.toInstance(lineItemTracking);
                        // hack to store reference - remove this later
                        lineItemtrack.getInstanceValues().add(new InstanceValue("LineItem", lineItem.getLineItemID()));
                        bankTransactionInstanceList.add(lineItemtrack);
                    });
                }
            });
        }

        // setup validation errors
        if (bankTransaction.getValidationErrors() != null) {
            List<ValidationError> xeroObjectList = bankTransaction.getValidationErrors();
            xeroObjectList.forEach(xeroObject -> bankTransactionInstanceList.add(ValidationErrorMapper.toInstance(xeroObject)));
        }

        return returnMap;
    }
}
