package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.BankTransaction;
import com.xero.models.accounting.Contact;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;

import java.util.ArrayList;
import java.util.List;

public class BankTransactionMapper {

    public static List<EntityInstance> toEntityInstance(BankTransaction bankTransaction) {
        List<EntityInstance> instanceList = new ArrayList<>();
        EntityInstance bankInstance = new EntityInstance();
        bankInstance.set_vertexName(PluginConstants.XERO_ENTITY_BANK_TRANSACTION);
        bankInstance.set_label(PluginConstants.XERO_ENTITY_BANK_TRANSACTION);
        ArrayList<InstanceValue> contactInstanceValues = new ArrayList<>();
        contactInstanceValues.add(new InstanceValue("BankTransactionID", bankTransaction.getBankTransactionID()));
        contactInstanceValues.add(new InstanceValue("Type", bankTransaction.getType().getValue()));
//        contactInstanceValues.add(new InstanceValue("Contact", bankTransaction.getc));
        contactInstanceValues.add(new InstanceValue("CurrencyCode", bankTransaction.getCurrencyCode()));
        contactInstanceValues.add(new InstanceValue("CurrencyRate", bankTransaction.getCurrencyRate()));
        contactInstanceValues.add(new InstanceValue("Date", DateTimeUtils.toDate(Instant.from(bankTransaction.getDateAsDate()))));
        contactInstanceValues.add(new InstanceValue("HasAttachments", bankTransaction.getHasAttachments()));
        contactInstanceValues.add(new InstanceValue("IsReconciled", bankTransaction.getIsReconciled()));
        contactInstanceValues.add(new InstanceValue("LineAmountTypes", bankTransaction.getLineAmountTypes().getValue()));
        contactInstanceValues.add(new InstanceValue("OverpaymentId", bankTransaction.getOverpaymentID()));
        contactInstanceValues.add(new InstanceValue("PrepaymentId", bankTransaction.getPrepaymentID()));
        contactInstanceValues.add(new InstanceValue("Reference", bankTransaction.getReference()));
//        contactInstanceValues.add(new InstanceValue("Status", bankTransaction.getStatus().getValue()));
        contactInstanceValues.add(new InstanceValue("Status", bankTransaction.getStatusAttributeString()));
        contactInstanceValues.add(new InstanceValue("Subtotal", bankTransaction.getSubTotal()));
        contactInstanceValues.add(new InstanceValue("Total", bankTransaction.getTotal()));
        contactInstanceValues.add(new InstanceValue("TotalTax", bankTransaction.getTotalTax()));
        contactInstanceValues.add(new InstanceValue("UpdatedDateUTC", DateTimeUtils.toDate(bankTransaction.getUpdatedDateUTCAsDate().toInstant())));
        contactInstanceValues.add(new InstanceValue("Url", bankTransaction.getUrl()));

        // link to accounts

        // link to contacts

        // link to validation errors


        return instanceList;
    }
}
