package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.Overpayment;

import java.util.ArrayList;

public class OverpaymentMapper extends BaseMapper{

    public static EntityInstance toInstance(Overpayment overpayment) {
        EntityInstance overpaymentInstance = new EntityInstance();
        overpaymentInstance.set_vertexName(PluginConstants.XERO_ENTITY_OVERPAYMENT);
        overpaymentInstance.set_label(PluginConstants.XERO_ENTITY_OVERPAYMENT);

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
        overpaymentInstanceValues.add(new InstanceValue("UpdatedDateUTC", getDate(overpayment.getUpdatedDateUTCAsDate())));
        return overpaymentInstance;
    }
}
