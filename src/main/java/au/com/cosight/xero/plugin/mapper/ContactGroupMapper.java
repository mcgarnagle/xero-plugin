package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.ContactGroup;

import java.util.ArrayList;
import java.util.List;

public class ContactGroupMapper extends BaseMapper{

    public static EntityInstance toInstance(ContactGroup contactGroup) {
        EntityInstance contactGroupInstance = new EntityInstance();
        contactGroupInstance.set_vertexName(PluginConstants.XERO_ENTITY_CONTACT_GROUP);
        contactGroupInstance.set_label(PluginConstants.XERO_ENTITY_CONTACT_GROUP);
        List<InstanceValue> contactGroupInstanceValues = new ArrayList<>();
        contactGroupInstance.setInstanceValues(contactGroupInstanceValues);
        contactGroupInstanceValues.add(new InstanceValue("ContactGroupID", contactGroup.getContactGroupID()));
        contactGroupInstanceValues.add(new InstanceValue("Name", contactGroup.getContactGroupID()));
        contactGroupInstanceValues.add(new InstanceValue("Status", contactGroup.getContactGroupID()));

        return contactGroupInstance;
    }
}
