package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.LineItem;

import java.util.ArrayList;
import java.util.List;

public class LineItemMapper extends BaseMapper{

    public static EntityInstance toInstance(LineItem lineItem) {
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

        return lineItemInstance;
    }
}
