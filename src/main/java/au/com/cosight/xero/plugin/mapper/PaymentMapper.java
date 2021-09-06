package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.Payment;

import java.util.ArrayList;

public class PaymentMapper extends BaseMapper{

    public static EntityInstance toInstance(Payment payment)  {
        EntityInstance theInstance = new EntityInstance();
        ArrayList<InstanceValue> theInstanceValues = new ArrayList<>();
        theInstance.setInstanceValues(theInstanceValues);
        theInstance.set_vertexName(PluginConstants.XERO_ENTITY_PAYMENT);
        theInstance.set_label(PluginConstants.XERO_ENTITY_PAYMENT);

        theInstanceValues.add(new InstanceValue("CurrencyRate", payment.getCurrencyRate()));
        theInstanceValues.add(new InstanceValue("Date", getDate(payment.getDateAsDate())));
        if(payment.getStatus()!=null){
            theInstanceValues.add(new InstanceValue("Status", payment.getStatus().getValue()));
        }
        theInstanceValues.add(new InstanceValue("InvoiceNumber", payment.getInvoiceNumber()));
        theInstanceValues.add(new InstanceValue("Amount", payment.getAmount()));
        theInstanceValues.add(new InstanceValue("BankAccountNumber", payment.getBankAccountNumber()));
        if(payment.getBatchPaymentID()!=null){
            theInstanceValues.add(new InstanceValue("BatchPaymentId", payment.getBatchPaymentID().toString()));
        }
        theInstanceValues.add(new InstanceValue("Code", payment.getCode()));
        theInstanceValues.add(new InstanceValue("CreditNoteNumber", payment.getCreditNoteNumber()));
        theInstanceValues.add(new InstanceValue("Details", payment.getDetails()));
        theInstanceValues.add(new InstanceValue("HasAccount", payment.getHasAccount()));
        theInstanceValues.add(new InstanceValue("HasValidationErrors", payment.getHasValidationErrors()));
        theInstanceValues.add(new InstanceValue("IsReconciled", payment.getIsReconciled()));
        theInstanceValues.add(new InstanceValue("Particulars", payment.getParticulars()));
        theInstanceValues.add(new InstanceValue("PaymentId", payment.getPaymentID()));
        theInstanceValues.add(new InstanceValue("Reference", payment.getReference()));
        theInstanceValues.add(new InstanceValue("UpdatedDateUTC", getDate(payment.getUpdatedDateUTCAsDate())));
        return theInstance;
    }
}
