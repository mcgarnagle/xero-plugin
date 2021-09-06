package au.com.cosight.xero.plugin.service.xero;


import au.com.cosight.common.dto.plugin.CosightExecutionContext;
import au.com.cosight.common.dto.plugin.helper.EntityServiceWrapper;
import au.com.cosight.common.dto.plugin.helper.RelationshipServiceWrapper;
import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.entity.service.dto.RelationshipsDTO;
import au.com.cosight.xero.plugin.PluginConstants;
import au.com.cosight.xero.plugin.mapper.BankTransactionMapper;
import com.xero.models.accounting.BankTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankTransactionServiceImpl implements BankTransactionService {

    private static Logger logger = LoggerFactory.getLogger("xero-plugin:BankTransactionServiceImpl");
    private RelationshipServiceWrapper relationshipServiceWrapper;
    private EntityServiceWrapper entityServiceWrapper;
    private CosightExecutionContext cosightExecutionContext;
    private AccountService accountService;
    private ContactService contactService;

    public BankTransactionServiceImpl(RelationshipServiceWrapper relationshipServiceWrapper,
                                      EntityServiceWrapper entityServiceWrapper,
                                      CosightExecutionContext cosightExecutionContext,
                                      AccountService accountService, ContactService contactService) {
        this.relationshipServiceWrapper = relationshipServiceWrapper;
        this.entityServiceWrapper = entityServiceWrapper;
        this.cosightExecutionContext = cosightExecutionContext;
        this.accountService = accountService;
        this.contactService = contactService;
    }

    @Override
    public void upsertBankTransaction(BankTransaction bankTransaction) {

        // need to make this more simple, accounts and contacts dont fully come through, only their ids do. change

        HashMap<String, List<EntityInstance>> mappedinstances = BankTransactionMapper.toEntityInstance(bankTransaction);

        // first, save the bank transaction
        EntityInstance bankTransactionInstance = mappedinstances.get(BankTransactionMapper.BANK_TRANSACTION_INSTANCES)
                .stream().filter(x -> x.get_vertexName().equalsIgnoreCase(PluginConstants.XERO_ENTITY_BANK_TRANSACTION))
                .findFirst().get();

        bankTransactionInstance = entityServiceWrapper.save(bankTransactionInstance);

        // now we look at the other components to save/fetch them

        EntityInstance accountInstance = mappedinstances.get(BankTransactionMapper.ACCOUNT_INSTANCES)
                .stream().filter(x -> x.get_vertexName().equalsIgnoreCase(PluginConstants.XERO_ENTITY_ACCOUNT))
                .findFirst().get();
        if (accountInstance != null) {
            // if there is an account attached, lets process...
            // if there is an account, either find it, or create it, but don't update it
            accountInstance = entityServiceWrapper.save(accountInstance);
            RelationshipsDTO relo = new RelationshipsDTO();
            relo.setFromEntityId(bankTransactionInstance.getId().replaceAll("#", ""));
            relo.setToEntityId(accountInstance.getId().replaceAll("#", ""));
            relo.setAllowDuplicate(false);
            relo.setName(PluginConstants.XERO_RELATIONSHIP_BANK_TRANSACTION_TO_ACCOUNT);
            relationshipServiceWrapper.saveRelationshipInstance(relo);
        }
        EntityInstance finalBankTransactionInstance = bankTransactionInstance;
        mappedinstances.get(BankTransactionMapper.CONTACT_INSTANCES)
                .stream().filter(x -> x.get_vertexName().equalsIgnoreCase(PluginConstants.XERO_ENTITY_CONTACT))
                .findFirst().ifPresent(contactInstance -> {
                    contactInstance = entityServiceWrapper.save(contactInstance);
                    RelationshipsDTO relo = new RelationshipsDTO();
                    relo.setFromEntityId(finalBankTransactionInstance.getId().replaceAll("#", ""));
                    relo.setToEntityId(contactInstance.getId().replaceAll("#", ""));
                    relo.setAllowDuplicate(false);
                    relo.setName(PluginConstants.XERO_RELATIONSHIP_BANK_TRANSACTION_TO_CONTACT);
                    relationshipServiceWrapper.saveRelationshipInstance(relo);

                });


        List<EntityInstance> lineItemtracking = mappedinstances.get(BankTransactionMapper.BANK_TRANSACTION_INSTANCES)
                .stream().filter(x -> x.get_vertexName().equalsIgnoreCase(PluginConstants.XERO_ENTITY_LINE_ITEM_TRACKING))
                .collect(Collectors.toList());


        // Line Items
        List<EntityInstance> lineItemList = mappedinstances.get(BankTransactionMapper.BANK_TRANSACTION_INSTANCES)
                .stream().filter(x -> x.get_vertexName().equalsIgnoreCase(PluginConstants.XERO_ENTITY_LINE_ITEM))
                .collect(Collectors.toList());

        for (EntityInstance entityInstance : lineItemList) {
            logger.info("line item exists");
            EntityInstance lineItemEntity = entityServiceWrapper.save(entityInstance);
            RelationshipsDTO relo = new RelationshipsDTO();
            relo.setFromEntityId(bankTransactionInstance.getId().replaceAll("#", ""));
            relo.setToEntityId(lineItemEntity.getId().replaceAll("#", ""));
            relo.setAllowDuplicate(false);
            relo.setName(PluginConstants.XERO_RELATIONSHIP_BANK_TRANSACTION_TO_LINE_ITEM);
            relationshipServiceWrapper.saveRelationshipInstance(relo);
            InstanceValue lineItemId = entityInstance.getInstanceValues().stream().filter(x -> x.getName().equalsIgnoreCase("LineItemId"))
                    .findFirst().get();


            // find related lineItemTrackers, save and link
            List<EntityInstance> lineItemTracks = lineItemtracking.stream().filter(x -> x.getInstanceValues()
                            .stream().allMatch(iv -> iv.getValue().toString().equalsIgnoreCase(lineItemId.getValue().toString())))
                    .collect(Collectors.toList());

            lineItemTracks.forEach(instance -> {
                EntityInstance instance1 = entityServiceWrapper.save(instance);
                RelationshipsDTO relo1 = new RelationshipsDTO();
                relo.setFromEntityId(lineItemEntity.getId().replaceAll("#", ""));
                relo.setToEntityId(instance1.getId().replaceAll("#", ""));
                relo.setAllowDuplicate(false);
                relo.setName(PluginConstants.XERO_RELATIONSHIP_LINE_ITEM_TO_LINE_ITEM_TRACKING);
                relationshipServiceWrapper.saveRelationshipInstance(relo);
            });


        }

    }
}
