package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.CreditNote;

import java.util.ArrayList;

public class CreditNoteMapper extends BaseMapper {

    public static EntityInstance toInstance(CreditNote creditNote) {
        EntityInstance theInstance = new EntityInstance();
        ArrayList<InstanceValue> theInstanceValues = new ArrayList<>();
        theInstance.set_vertexName(PluginConstants.XERO_ENTITY_CREDIT_NOTE);
        theInstance.set_label(PluginConstants.XERO_ENTITY_CREDIT_NOTE);

        theInstance.setInstanceValues(theInstanceValues);
        theInstanceValues.add(new InstanceValue("CurrencyRate", creditNote.getCurrencyRate()));
        if(creditNote.getCurrencyCode()!=null){
            theInstanceValues.add(new InstanceValue("CurrencyCode", creditNote.getCurrencyCode().getValue()));

        }
        theInstanceValues.add(new InstanceValue("CreditNoteNumber", creditNote.getCreditNoteNumber()));
        theInstanceValues.add(new InstanceValue("CiSRate", creditNote.getCiSRate()));
        theInstanceValues.add(new InstanceValue("AppliedAmount", creditNote.getAppliedAmount()));
        theInstanceValues.add(new InstanceValue("BrandingThemeId", creditNote.getBrandingThemeID()));
        theInstanceValues.add(new InstanceValue("CiSDeduction", creditNote.getCiSDeduction()));
        theInstanceValues.add(new InstanceValue("CreditNoteId", creditNote.getCreditNoteID().toString()));
        theInstanceValues.add(new InstanceValue("Date", getDate(creditNote.getDateAsDate())));
        theInstanceValues.add(new InstanceValue("FullyPaidOnDate", getDate(creditNote.getFullyPaidOnDateAsDate())));
        theInstanceValues.add(new InstanceValue("HasAttachments", creditNote.getHasAttachments()));
        theInstanceValues.add(new InstanceValue("HasErrors", creditNote.getHasErrors()));
        theInstanceValues.add(new InstanceValue("LineAmountTypes", creditNote.getLineAmountTypes()));
        theInstanceValues.add(new InstanceValue("Reference", creditNote.getReference()));
        theInstanceValues.add(new InstanceValue("RemainingCredit", creditNote.getRemainingCredit()));
        theInstanceValues.add(new InstanceValue("SentToContact", creditNote.getSentToContact()));
        theInstanceValues.add(new InstanceValue("Status", creditNote.getStatus()));
        theInstanceValues.add(new InstanceValue("SubTotal", creditNote.getSubTotal()));
        theInstanceValues.add(new InstanceValue("Total", creditNote.getTotal()));
        theInstanceValues.add(new InstanceValue("TotalTax", creditNote.getTotalTax()));
        if(creditNote.getType()!=null){
            theInstanceValues.add(new InstanceValue("Type", creditNote.getType().getValue()));
        }
        theInstanceValues.add(new InstanceValue("UpdatedDateUTC", getDate(creditNote.getUpdatedDateUTCAsDate())));
        return theInstance;
    }
}
