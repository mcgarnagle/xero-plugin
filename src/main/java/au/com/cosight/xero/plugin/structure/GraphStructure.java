package au.com.cosight.xero.plugin.structure;

import au.com.cosight.entity.domain.EntityInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphStructure {

    private Map<String, EntityInstance> entityInstances = new HashMap<>();
    private List<InstanceLinks> links = new ArrayList<>();

    public Map<String, EntityInstance> getEntityInstances() {
        return entityInstances;
    }

    public void setEntityInstances(Map<String, EntityInstance> entityInstances) {
        this.entityInstances = entityInstances;
    }

    public List<InstanceLinks> getLinks() {
        return links;
    }

    public void setLinks(List<InstanceLinks> links) {
        this.links = links;
    }
}
