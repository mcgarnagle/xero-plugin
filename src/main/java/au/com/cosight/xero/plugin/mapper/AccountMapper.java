package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.Account;
import com.xero.models.accounting.ValidationError;
import org.threeten.bp.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountMapper {

    public static List<EntityInstance> toEntityInstance(Account account) {
        List<EntityInstance> instanceList = new ArrayList<>();

        EntityInstance accountInstance = new EntityInstance();
        accountInstance.set_vertexName(PluginConstants.XERO_ENTITY_ACCOUNT);
        accountInstance.set_label(PluginConstants.XERO_ENTITY_ACCOUNT);
        ArrayList<InstanceValue> accountInstanceValues = new ArrayList<>();
        accountInstance.setInstanceValues(accountInstanceValues);
        accountInstanceValues.add(new InstanceValue("AccountID", account.getAccountID()));
        accountInstanceValues.add(new InstanceValue("BankAccountNumber", account.getBankAccountNumber()));
        accountInstanceValues.add(new InstanceValue("BankAccountType", account.getBankAccountType()));
        accountInstanceValues.add(new InstanceValue("Code", account.getCode()));
        if(account.getSystemAccount()!=null){
            accountInstanceValues.add(new InstanceValue("SystemAccount", account.getSystemAccount().getValue()));
        }
        accountInstanceValues.add(new InstanceValue("AddToWatchlist", account.getAddToWatchlist()));
        accountInstanceValues.add(new InstanceValue("Name", account.getName()));
        accountInstanceValues.add(new InstanceValue("CurrencyCode", account.getCurrencyCode()));
        accountInstanceValues.add(new InstanceValue("Description", account.getDescription()));
        accountInstanceValues.add(new InstanceValue("EnablePaymentsToAccount", account.getEnablePaymentsToAccount()));
        accountInstanceValues.add(new InstanceValue("HasAttachments", account.getHasAttachments()));
        accountInstanceValues.add(new InstanceValue("PropertyClass", account.getPropertyClass()));
        accountInstanceValues.add(new InstanceValue("ReportingCode", account.getReportingCode()));
        accountInstanceValues.add(new InstanceValue("ReportingCodeName", account.getReportingCodeName()));
        accountInstanceValues.add(new InstanceValue("ShowInExpenseClaims", account.getShowInExpenseClaims()));
        accountInstanceValues.add(new InstanceValue("Status", account.getStatus()));
        accountInstanceValues.add(new InstanceValue("Type", account.getType()));
        accountInstanceValues.add(new InstanceValue("TaxType", account.getTaxType()));
        accountInstanceValues.add(new InstanceValue("UpdatedDateUTC", DateTimeUtils.toDate(account.getUpdatedDateUTCAsDate().toInstant())));

        instanceList.add(accountInstance);

        // add validation errors if needed
        // ValidationError
        if (account.getValidationErrors() != null && account.getValidationErrors().size() > 0) {
            List<ValidationError> xeroObjectList = account.getValidationErrors();
            xeroObjectList.forEach(xeroObject -> {
                EntityInstance cosightInstance = new EntityInstance();
                cosightInstance.set_vertexName(PluginConstants.XERO_ENTITY_VALIDATION_ERROR);
                cosightInstance.set_label(PluginConstants.XERO_ENTITY_VALIDATION_ERROR);
                List<InstanceValue> cosightInstanceValues = new ArrayList<>();
                cosightInstance.setInstanceValues(cosightInstanceValues);
                cosightInstanceValues.add(new InstanceValue("Message", xeroObject.getMessage()));
                instanceList.add(cosightInstance);
            });
        }

        return instanceList;
    }
}
