package au.com.cosight.xero.plugin.structure;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.service.dto.RelationshipsDTO;

public class InstanceLinks {
    private EntityInstance instanceFrom;
    private EntityInstance instanceTo;
    private String relationshipName;

    public RelationshipsDTO getRelationshipDTO() {
        RelationshipsDTO relationshipsDTO = new RelationshipsDTO();
        relationshipsDTO.setFromEntityId(instanceFrom.getId());
        relationshipsDTO.setToEntityId(instanceTo.getId());
        relationshipsDTO.setName(relationshipName);
        relationshipsDTO.setAllowDuplicate(false);
        return relationshipsDTO;
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
