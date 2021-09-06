package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.Phone;

import java.util.ArrayList;
import java.util.List;

public class PhoneMapper extends BaseMapper{

    public static EntityInstance toInstance(Phone xeroObject) {
        EntityInstance cosightInstance = new EntityInstance();
        cosightInstance.set_vertexName(PluginConstants.XERO_ENTITY_PHONE);
        cosightInstance.set_label(PluginConstants.XERO_ENTITY_PHONE);
        List<InstanceValue> cosightInstanceValues = new ArrayList<>();
        cosightInstance.setInstanceValues(cosightInstanceValues);
        cosightInstanceValues.add(new InstanceValue("PhoneAreaCode", xeroObject.getPhoneAreaCode()));
        cosightInstanceValues.add(new InstanceValue("PhoneNumber", xeroObject.getPhoneNumber()));
        cosightInstanceValues.add(new InstanceValue("PhoneCountryCode", xeroObject.getPhoneCountryCode()));
        cosightInstanceValues.add(new InstanceValue("PhoneType", xeroObject.getPhoneType()));
        return cosightInstance;
    }
}
