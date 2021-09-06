package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.LineItemTracking;

import java.util.ArrayList;
import java.util.List;

public class LineItemTrackingMapper extends BaseMapper{

    public static EntityInstance toInstance(LineItemTracking lineItemTracking) {
        EntityInstance lineItemTrackingInstance = new EntityInstance();
        List<InstanceValue> lineItemTrackingInstanceValues = new ArrayList<>();
        lineItemTrackingInstance.set_label(PluginConstants.XERO_ENTITY_LINE_ITEM_TRACKING);
        lineItemTrackingInstance.set_vertexName(PluginConstants.XERO_ENTITY_LINE_ITEM_TRACKING);
        lineItemTrackingInstance.setInstanceValues(lineItemTrackingInstanceValues);

        lineItemTrackingInstanceValues.add(new InstanceValue("Name", lineItemTracking.getName()));
        lineItemTrackingInstanceValues.add(new InstanceValue("TrackingCategoryId", lineItemTracking.getTrackingCategoryID()));
        lineItemTrackingInstanceValues.add(new InstanceValue("TrackingOptionId", lineItemTracking.getTrackingOptionID()));
        lineItemTrackingInstanceValues.add(new InstanceValue("Option", lineItemTracking.getOption()));
        return lineItemTrackingInstance;
    }
}
