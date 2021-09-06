package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.BatchPaymentDetails;

import java.util.ArrayList;
import java.util.List;

public class BatchPaymentDetailsMapper extends BaseMapper{

    public static EntityInstance toInstance(BatchPaymentDetails xeroObject) {
        EntityInstance batchPayments = new EntityInstance();
        batchPayments.set_vertexName(PluginConstants.XERO_ENTITY_BATCH_PAYMENT_DETAILS);
        batchPayments.set_label(PluginConstants.XERO_ENTITY_BATCH_PAYMENT_DETAILS);
        List<InstanceValue> batchInstanceValues = new ArrayList<>();
        batchPayments.setInstanceValues(batchInstanceValues);
        batchInstanceValues.add(new InstanceValue("BankAccountName", xeroObject.getBankAccountName()));
        batchInstanceValues.add(new InstanceValue("Code", xeroObject.getCode()));
        batchInstanceValues.add(new InstanceValue("BankAccountNumber", xeroObject.getBankAccountNumber()));
        batchInstanceValues.add(new InstanceValue("Reference", xeroObject.getReference()));
        batchInstanceValues.add(new InstanceValue("Details", xeroObject.getDetails()));
        return batchPayments;
    }
}
