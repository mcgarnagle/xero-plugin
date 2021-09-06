package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressMapper extends BaseMapper{

    public static EntityInstance toInstance(Address address) {
        EntityInstance addressInstance = new EntityInstance();
        addressInstance.set_vertexName(PluginConstants.XERO_ENTITY_ADDRESS);
        addressInstance.set_label(PluginConstants.XERO_ENTITY_ADDRESS);
        List<InstanceValue> addressInstanceValues = new ArrayList<>();
        addressInstance.setInstanceValues(addressInstanceValues);
        addressInstanceValues.add(new InstanceValue("AddressLine1", address.getAddressLine1()));
        addressInstanceValues.add(new InstanceValue("AddressLine2", address.getAddressLine2()));
        addressInstanceValues.add(new InstanceValue("AddressLine3", address.getAddressLine3()));
        addressInstanceValues.add(new InstanceValue("AddressLine4", address.getAddressLine4()));
        addressInstanceValues.add(new InstanceValue("AttentionTo", address.getAttentionTo()));
        addressInstanceValues.add(new InstanceValue("AddressType", address.getAddressType()));
        addressInstanceValues.add(new InstanceValue("City", address.getCity()));
        addressInstanceValues.add(new InstanceValue("Country", address.getCountry()));
        addressInstanceValues.add(new InstanceValue("PostalCode", address.getPostalCode()));
        addressInstanceValues.add(new InstanceValue("Region", address.getRegion()));
        return addressInstance;
    }
}
