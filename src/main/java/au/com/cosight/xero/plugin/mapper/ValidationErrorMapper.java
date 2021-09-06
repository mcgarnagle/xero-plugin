package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.ValidationError;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorMapper extends BaseMapper {

    public static EntityInstance toInstance(ValidationError error) {
        EntityInstance cosightInstance = new EntityInstance();
        cosightInstance.set_vertexName(PluginConstants.XERO_ENTITY_VALIDATION_ERROR);
        cosightInstance.set_label(PluginConstants.XERO_ENTITY_VALIDATION_ERROR);
        List<InstanceValue> cosightInstanceValues = new ArrayList<>();
        cosightInstance.setInstanceValues(cosightInstanceValues);
        cosightInstanceValues.add(new InstanceValue("Message", error.getMessage()));
        return cosightInstance;

    }

}
