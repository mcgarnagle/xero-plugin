package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.Prepayment;

import java.util.ArrayList;

public class PrepaymentMapper extends BaseMapper {

    public static EntityInstance toInstance(Prepayment prepayment) {
        EntityInstance theInstance = new EntityInstance();
        theInstance.set_vertexName(PluginConstants.XERO_ENTITY_PREPAYMENT);
        theInstance.set_label(PluginConstants.XERO_ENTITY_PREPAYMENT);

        ArrayList<InstanceValue> theInstanceValues = new ArrayList<>();
        theInstance.setInstanceValues(theInstanceValues);
        theInstanceValues.add(new InstanceValue("CurrencyRate", prepayment.getCurrencyRate()));
        theInstanceValues.add(new InstanceValue("CurrencyCode", prepayment.getCurrencyCode().getValue()));
        theInstanceValues.add(new InstanceValue("AppliedAmount", prepayment.getAppliedAmount()));
        theInstanceValues.add(new InstanceValue("Date", getDate(prepayment.getDateAsDate())));
        theInstanceValues.add(new InstanceValue("HasAttachments", prepayment.getHasAttachments()));
        theInstanceValues.add(new InstanceValue("LineAmountTypes", prepayment.getLineAmountTypes()));
        theInstanceValues.add(new InstanceValue("PrepaymentId", prepayment.getPrepaymentID()));
        theInstanceValues.add(new InstanceValue("Reference", prepayment.getReference()));
        theInstanceValues.add(new InstanceValue("RemainingCredit", prepayment.getRemainingCredit()));
        theInstanceValues.add(new InstanceValue("Status", prepayment.getStatus()));
        theInstanceValues.add(new InstanceValue("SubTotal", prepayment.getSubTotal()));
        theInstanceValues.add(new InstanceValue("Total", prepayment.getTotal()));
        theInstanceValues.add(new InstanceValue("TotalTax", prepayment.getTotalTax()));
        theInstanceValues.add(new InstanceValue("Type", prepayment.getType()));
        theInstanceValues.add(new InstanceValue("UpdatedDateUTC", getDate(prepayment.getUpdatedDateUTCAsDate())));

        return theInstance;
    }
}
