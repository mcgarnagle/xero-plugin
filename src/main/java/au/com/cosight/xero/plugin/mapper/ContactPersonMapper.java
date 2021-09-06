package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.ContactPerson;

import java.util.ArrayList;
import java.util.List;

public class ContactPersonMapper extends BaseMapper{

    public static EntityInstance toInstance(ContactPerson xeroObject) {
        EntityInstance cosightInstance = new EntityInstance();
        cosightInstance.set_vertexName(PluginConstants.XERO_ENTITY_CONTACT_PERSON);
        cosightInstance.set_label(PluginConstants.XERO_ENTITY_CONTACT_PERSON);
        List<InstanceValue> cosightInstanceValues = new ArrayList<>();
        cosightInstance.setInstanceValues(cosightInstanceValues);
        cosightInstanceValues.add(new InstanceValue("FirstName", xeroObject.getFirstName()));
        cosightInstanceValues.add(new InstanceValue("EmailAddress", xeroObject.getEmailAddress()));
        cosightInstanceValues.add(new InstanceValue("LastName", xeroObject.getLastName()));
        cosightInstanceValues.add(new InstanceValue("IncludeInEmails", xeroObject.getIncludeInEmails()));
        return cosightInstance;
    }
}
