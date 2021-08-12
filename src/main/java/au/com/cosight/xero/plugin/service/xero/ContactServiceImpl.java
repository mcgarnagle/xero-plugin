package au.com.cosight.xero.plugin.service.xero;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.service.dto.RelationshipsDTO;
import au.com.cosight.sdk.plugin.runtime.CosightExecutionContext;
import au.com.cosight.sdk.plugin.runtime.helper.EntityServiceWrapper;
import au.com.cosight.sdk.plugin.runtime.helper.RelationshipServiceWrapper;
import au.com.cosight.xero.plugin.PluginConstants;
import au.com.cosight.xero.plugin.mapper.ContactMapper;
import com.xero.models.accounting.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private RelationshipServiceWrapper relationshipServiceWrapper;
    private EntityServiceWrapper entityServiceWrapper;
    private CosightExecutionContext cosightExecutionContext;
    private static Logger logger = LoggerFactory.getLogger("xero-plugin:contactServiceImpl");

    public ContactServiceImpl(RelationshipServiceWrapper relationshipServiceWrapper, EntityServiceWrapper entityServiceWrapper, CosightExecutionContext cosightExecutionContext) {
        this.relationshipServiceWrapper = relationshipServiceWrapper;
        this.entityServiceWrapper = entityServiceWrapper;
        this.cosightExecutionContext = cosightExecutionContext;
    }

    @Override
    public void upsertContact(Contact contact) {
        // save contact and related entities.
        List<EntityInstance> mappedInstance = ContactMapper.toEntityInstance(contact);

        //Further logic here to figure out what to create/link/delete? etc

        // save contact
        EntityInstance contactInstance = mappedInstance.stream().filter(x -> x.get_vertexName().equalsIgnoreCase(PluginConstants.XERO_ENTITY_CONTACT)).findFirst().get();
        contactInstance = entityServiceWrapper.save(contactInstance);
        // Now that we have saved the contact, lets remove it from the array and sort through the rest.
        EntityInstance finalContactInstance = contactInstance;
        mappedInstance.removeIf(x -> x.get_label().equalsIgnoreCase(PluginConstants.XERO_ENTITY_CONTACT));
        mappedInstance.forEach(entityInstance -> {
            // basic strategy - save and link. set link to not duplicate. rely on
            // unique indexes to not duplicate entities
            EntityInstance newInstance = entityServiceWrapper.save(entityInstance);
            RelationshipsDTO relo = new RelationshipsDTO();
            relo.setFromEntityId(finalContactInstance.getId().replaceAll("#",""));
            relo.setToEntityId(newInstance.getId().replaceAll("#",""));
            relo.setAllowDuplicate(false);
            if (entityInstance.get_label().equalsIgnoreCase(PluginConstants.XERO_ENTITY_ADDRESS)) {
                relo.setName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_ADDRESS);
            }
            if (entityInstance.get_label().equalsIgnoreCase(PluginConstants.XERO_ENTITY_ATTACHMENT)) {
                relo.setName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_ATTACHMENT);
            }
            if (entityInstance.get_label().equalsIgnoreCase(PluginConstants.XERO_ENTITY_BATCH_PAYMENT_DETAILS)) {
                relo.setName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_BATCH_PAYMENT_DETAILS);
            }
            if (entityInstance.get_label().equalsIgnoreCase(PluginConstants.XERO_ENTITY_CONTACT_GROUP)) {
                relo.setName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_CONTACT_GROUP);
            }

            if (entityInstance.get_label().equalsIgnoreCase(PluginConstants.XERO_ENTITY_CONTACT_PERSON)) {
                relo.setName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_CONTACT_PERSON);
            }
            if (entityInstance.get_label().equalsIgnoreCase(PluginConstants.XERO_ENTITY_PHONE)) {
                relo.setName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_PHONE);
            }
            if (entityInstance.get_label().equalsIgnoreCase(PluginConstants.XERO_ENTITY_SALES_TRACKING_CATEGORY)) {
                relo.setName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_SALES_TRACKING_CATEGORY);
            }
            if (entityInstance.get_label().equalsIgnoreCase(PluginConstants.XERO_ENTITY_VALIDATION_ERROR)) {
                relo.setName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_VALIDATION_ERROR);
            }
            logger.info("saving relationship {}", relo.toString());
            relationshipServiceWrapper.saveRelationshipInstance(relo);

        });
//        System.out.println(result.getId() + " Saved!");


    }
}
