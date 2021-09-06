package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.SalesTrackingCategory;

import java.util.ArrayList;
import java.util.List;

public class SalesTrackingCategoryMapper extends BaseMapper {

    public static EntityInstance toInstance(SalesTrackingCategory xeroObject) {
        EntityInstance cosightInstance = new EntityInstance();
        cosightInstance.set_vertexName(PluginConstants.XERO_ENTITY_SALES_TRACKING_CATEGORY);
        cosightInstance.set_label(PluginConstants.XERO_ENTITY_SALES_TRACKING_CATEGORY);
        List<InstanceValue> cosightInstanceValues = new ArrayList<>();
        cosightInstance.setInstanceValues(cosightInstanceValues);
        cosightInstanceValues.add(new InstanceValue("TrackingOptionName", xeroObject.getTrackingOptionName()));
        cosightInstanceValues.add(new InstanceValue("TrackingCategoryName", xeroObject.getTrackingCategoryName()));
        return cosightInstance;
    }
}
