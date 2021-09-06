package au.com.cosight.xero.plugin.service.xero;

import au.com.cosight.common.dto.plugin.CosightExecutionContext;
import au.com.cosight.common.dto.plugin.helper.EntityServiceWrapper;
import au.com.cosight.common.dto.plugin.helper.RelationshipServiceWrapper;
import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.service.dto.RelationshipsDTO;
import au.com.cosight.xero.plugin.PluginConstants;
import au.com.cosight.xero.plugin.mapper.AccountMapper;
import au.com.cosight.xero.plugin.mapper.ContactMapper;
import com.xero.models.accounting.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private static Logger logger = LoggerFactory.getLogger("xero-plugin:AccountServiceImpl");
    private RelationshipServiceWrapper relationshipServiceWrapper;
    private EntityServiceWrapper entityServiceWrapper;
    private CosightExecutionContext cosightExecutionContext;

    public AccountServiceImpl(RelationshipServiceWrapper relationshipServiceWrapper, EntityServiceWrapper entityServiceWrapper, CosightExecutionContext cosightExecutionContext) {
        this.relationshipServiceWrapper = relationshipServiceWrapper;
        this.entityServiceWrapper = entityServiceWrapper;
        this.cosightExecutionContext = cosightExecutionContext;
    }

    @Override
    public void upsertAccount(Account account) {
        List<EntityInstance> mappedInstance = AccountMapper.toEntityInstance(account);

        EntityInstance accountInstance = mappedInstance.stream().filter(x -> x.get_vertexName().equalsIgnoreCase(PluginConstants.XERO_ENTITY_ACCOUNT)).findFirst().get();
        accountInstance = entityServiceWrapper.save(accountInstance);
        // Now that we have saved the contact, lets remove it from the array and sort through the rest.
        EntityInstance finalaccountInstance = accountInstance;
        mappedInstance.removeIf(x -> x.get_label().equalsIgnoreCase(PluginConstants.XERO_ENTITY_ACCOUNT));
        mappedInstance.forEach(entityInstance -> {
            EntityInstance newInstance = entityServiceWrapper.save(entityInstance);
            RelationshipsDTO relo = new RelationshipsDTO();
            relo.setFromEntityId(finalaccountInstance.getId().replaceAll("#",""));
            relo.setToEntityId(newInstance.getId().replaceAll("#",""));
            relo.setAllowDuplicate(false);
            if (entityInstance.get_label().equalsIgnoreCase(PluginConstants.XERO_ENTITY_VALIDATION_ERROR)) {
                relo.setName(PluginConstants.XERO_RELATIONSHIP_ACCOUNT_TO_VALIDATION_ERROR);
            }
            logger.info("saving relationship {}", relo.toString());
            relationshipServiceWrapper.saveRelationshipInstance(relo);
        });
    }
}
