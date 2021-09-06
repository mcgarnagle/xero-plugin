package au.com.cosight.xero.plugin.structure;

import au.com.cosight.entity.domain.EntityInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GraphStructure {

    private List<EntityInstance> entityInstances = new ArrayList<>();
    private List<InstanceLink> links = new ArrayList<>();

    public List<EntityInstance> getEntityInstances() {
        return entityInstances;
    }

    public void setEntityInstances(List<EntityInstance> entityInstances) {
        this.entityInstances = entityInstances;
    }

    public void addEntity(EntityInstance instance) {
        getEntityInstances().add(instance);
    }

    public void addLink(EntityInstance from, EntityInstance to, String relationshipName) {
        // might check here as well to see that this link doesn't already exist
        InstanceLink link = new InstanceLink();
        link.setInstanceFrom(from);
        link.setInstanceTo(to);
        link.setRelationshipName(relationshipName);
        getLinks().add(link);
    }

    public List<InstanceLink> getLinks() {
        return links;
    }

    public void setLinks(List<InstanceLink> links) {
        this.links = links;
    }

    // will have to test this out to see if it works
    public Optional<EntityInstance> findEntityInstanceByValue(String entityType, String keyValueName, String keyValueValue) {
        return this.getEntityInstances().stream().filter(x -> x.getInstanceValues()
                        .stream().allMatch(iv -> iv.getName().equalsIgnoreCase(keyValueName)))
                .findFirst();


    }
}
