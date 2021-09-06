package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.Allocation;

import java.util.ArrayList;
import java.util.List;

public class AllocationMapper extends BaseMapper {

    public static EntityInstance toInstance(Allocation allocation) {
        EntityInstance allocationInstance = new EntityInstance();
        List<InstanceValue> allocationInstanceValues = new ArrayList<>();
        allocationInstance.setInstanceValues(allocationInstanceValues);
        allocationInstance.set_label(PluginConstants.XERO_ENTITY_ALLOCATION);
        allocationInstance.set_vertexName(PluginConstants.XERO_ENTITY_ALLOCATION);

        allocationInstanceValues.add(new InstanceValue("Amount", allocation.getAmount()));
        allocationInstanceValues.add(new InstanceValue("Date", getDate(allocation.getDateAsDate())));
        allocationInstanceValues.add(new InstanceValue("Status", allocation.getStatusAttributeString()));

        return allocationInstance;

    }
}
