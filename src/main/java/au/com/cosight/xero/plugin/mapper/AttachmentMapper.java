package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.Attachment;

import java.util.ArrayList;
import java.util.List;

public class AttachmentMapper extends BaseMapper{
    
    public static EntityInstance toInstance(Attachment attachment) {
        EntityInstance cosightInstance = new EntityInstance();
        cosightInstance.set_vertexName(PluginConstants.XERO_ENTITY_ATTACHMENT);
        cosightInstance.set_label(PluginConstants.XERO_ENTITY_ATTACHMENT);
        List<InstanceValue> cosightInstanceValues = new ArrayList<>();
        cosightInstance.setInstanceValues(cosightInstanceValues);
        cosightInstanceValues.add(new InstanceValue("AttachmentID", attachment.getAttachmentID()));
        cosightInstanceValues.add(new InstanceValue("ContentLength", attachment.getAttachmentID()));
        cosightInstanceValues.add(new InstanceValue("FileName", attachment.getAttachmentID()));
        cosightInstanceValues.add(new InstanceValue("IncludeOnline", attachment.getAttachmentID()));
        cosightInstanceValues.add(new InstanceValue("MimeType", attachment.getAttachmentID()));
        cosightInstanceValues.add(new InstanceValue("URL", attachment.getAttachmentID()));
        return cosightInstance;
    }
}
