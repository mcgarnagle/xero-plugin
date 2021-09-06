package au.com.cosight.xero.plugin.structure;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.service.dto.RelationshipsDTO;

public class InstanceLink {
    private EntityInstance instanceFrom;
    private String instanceFromKey;
    private EntityInstance instanceTo;
    private String instanceToKey;
    private String relationshipName;



    public RelationshipsDTO getRelationshipDTO() {
        RelationshipsDTO relationshipsDTO = new RelationshipsDTO();
        relationshipsDTO.setFromEntityId(instanceFrom.getId());
        relationshipsDTO.setToEntityId(instanceTo.getId());
        relationshipsDTO.setName(relationshipName);
        relationshipsDTO.setAllowDuplicate(false);
        return relationshipsDTO;
    }

    public String getInstanceFromKey() {
        return instanceFromKey;
    }

    public void setInstanceFromKey(String instanceFromKey) {
        this.instanceFromKey = instanceFromKey;
    }

    public String getInstanceToKey() {
        return instanceToKey;
    }

    public void setInstanceToKey(String instanceToKey) {
        this.instanceToKey = instanceToKey;
    }

    public EntityInstance getInstanceFrom() {
        return instanceFrom;
    }

    public void setInstanceFrom(EntityInstance instanceFrom) {
        this.instanceFrom = instanceFrom;
    }

    public EntityInstance getInstanceTo() {
        return instanceTo;
    }

    public void setInstanceTo(EntityInstance instanceTo) {
        this.instanceTo = instanceTo;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }
}
