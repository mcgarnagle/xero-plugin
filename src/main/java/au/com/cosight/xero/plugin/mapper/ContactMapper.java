package au.com.cosight.xero.plugin.mapper;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.entity.domain.InstanceValue;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.*;
import org.threeten.bp.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactMapper {

    // when we want to put back to XERO - do this later
    public Contact toXeroContact(List<EntityInstance> contact) {
        Contact xeroContact = new Contact();


        return xeroContact;
    }


    // XERO to entity instance
    public static List<EntityInstance> toEntityInstance(Contact contact) {
        List<EntityInstance> instanceList = new ArrayList<>();

        EntityInstance contactInstance = new EntityInstance();
        contactInstance.set_vertexName(PluginConstants.XERO_ENTITY_CONTACT);
        contactInstance.set_label(PluginConstants.XERO_ENTITY_CONTACT);
        ArrayList<InstanceValue> contactInstanceValues = new ArrayList<>();
        contactInstanceValues.add(new InstanceValue("AccountNumber", contact.getAccountNumber()));
        contactInstanceValues.add(new InstanceValue("ContactID", contact.getContactID()));
        contactInstanceValues.add(new InstanceValue("ContactStatus", contact.getContactStatus()));
        if (contact.getDefaultCurrency() != null) {
            contactInstanceValues.add(new InstanceValue("CurrencyCode", contact.getDefaultCurrency().getValue()));
        } else {
            contactInstanceValues.add(new InstanceValue("CurrencyCode", ""));

        }
        contactInstanceValues.add(new InstanceValue("AccountsPayableTaxType", contact.getAccountsPayableTaxType()));
        contactInstanceValues.add(new InstanceValue("AccountsReceivableTaxType", contact.getAccountsReceivableTaxType()));
        contactInstanceValues.add(new InstanceValue("BankAccountDetails", contact.getBankAccountDetails()));
        contactInstanceValues.add(new InstanceValue("ContactNumber", contact.getContactNumber()));
        if (contact.getPaymentTerms() != null) {
            if (contact.getPaymentTerms().getSales() != null) {
                contactInstanceValues.add(new InstanceValue("SalesPaymentTermsDay", contact.getPaymentTerms().getSales().getDay()));
                if (contact.getPaymentTerms().getSales().getType() != null) {
                    contactInstanceValues.add(new InstanceValue("SalesPaymentTerms", contact.getPaymentTerms().getSales().getType().getValue()));
                }
            }
            if (contact.getPaymentTerms().getBills() != null) {
                contactInstanceValues.add(new InstanceValue("BillingPaymentTermsDay", contact.getPaymentTerms().getBills().getDay()));
                if (contact.getPaymentTerms().getBills().getType() != null) {
                    contactInstanceValues.add(new InstanceValue("BillingPaymentTerms", contact.getPaymentTerms().getBills().getType().getValue()));
                }
            }
        }
        contactInstanceValues.add(new InstanceValue("Discount", contact.getDiscount()));
        contactInstanceValues.add(new InstanceValue("EmailAddress", contact.getEmailAddress()));
        contactInstanceValues.add(new InstanceValue("FirstName", contact.getFirstName()));
        contactInstanceValues.add(new InstanceValue("HasAttachments", contact.getHasAttachments()));
        contactInstanceValues.add(new InstanceValue("HasValidationErrors", contact.getHasValidationErrors()));
        contactInstanceValues.add(new InstanceValue("IsCustomer", contact.getIsCustomer()));
        contactInstanceValues.add(new InstanceValue("IsSupplier", contact.getIsSupplier()));
        contactInstanceValues.add(new InstanceValue("LastName", contact.getLastName()));
        contactInstanceValues.add(new InstanceValue("Name", contact.getName()));
        if (contact.getBalances() != null) {
            if (contact.getBalances().getAccountsReceivable() != null) {
                contactInstanceValues.add(new InstanceValue("ReceivableOverdue", contact.getBalances().getAccountsReceivable().getOverdue()));
                contactInstanceValues.add(new InstanceValue("ReceivableOutstanding", contact.getBalances().getAccountsReceivable().getOutstanding()));

            }
            if (contact.getBalances().getAccountsPayable() != null) {
                contactInstanceValues.add(new InstanceValue("PayableOverdue", contact.getBalances().getAccountsPayable().getOverdue()));
                contactInstanceValues.add(new InstanceValue("PayableOutstanding", contact.getBalances().getAccountsPayable().getOutstanding()));

            }
        }
        contactInstanceValues.add(new InstanceValue("PurchasesDefaultAccountCode", contact.getPurchasesDefaultAccountCode()));
        contactInstanceValues.add(new InstanceValue("SalesDefaultAccountCode", contact.getSalesDefaultAccountCode()));
        contactInstanceValues.add(new InstanceValue("SkypeUserName", contact.getSkypeUserName()));
        contactInstanceValues.add(new InstanceValue("StatusAttributeString", contact.getStatusAttributeString()));
        contactInstanceValues.add(new InstanceValue("TaxNumber", contact.getTaxNumber()));
        contactInstanceValues.add(new InstanceValue("TrackingCategoryName", contact.getTrackingCategoryName()));
        contactInstanceValues.add(new InstanceValue("TrackingCategoryOption", contact.getTrackingCategoryOption()));
        if (contact.getUpdatedDateUTCAsDate() != null) {
            contactInstanceValues.add(new InstanceValue("UpdatedDateUTC", DateTimeUtils.toDate(contact.getUpdatedDateUTCAsDate().toInstant())));
        }
        contactInstanceValues.add(new InstanceValue("Website", contact.getAccountNumber()));
        contactInstanceValues.add(new InstanceValue("XeroNetworkKey", contact.getAccountNumber()));
        contactInstance.setInstanceValues(contactInstanceValues);
        instanceList.add(contactInstance);

        // ContactGroups
        if (contact.getContactGroups() != null && contact.getContactGroups().size() > 0) {
            List<ContactGroup> contactGroups = contact.getContactGroups();
            contactGroups.forEach(contactGroup -> {
                EntityInstance contactGroupInstance = new EntityInstance();
                contactGroupInstance.set_vertexName(PluginConstants.XERO_ENTITY_CONTACT_GROUP);
                contactGroupInstance.set_label(PluginConstants.XERO_ENTITY_CONTACT_GROUP);
                List<InstanceValue> contactGroupInstanceValues = new ArrayList<>();
                contactGroupInstance.setInstanceValues(contactGroupInstanceValues);
                contactGroupInstanceValues.add(new InstanceValue("ContactGroupID", contactGroup.getContactGroupID()));
                contactGroupInstanceValues.add(new InstanceValue("Name", contactGroup.getContactGroupID()));
                contactGroupInstanceValues.add(new InstanceValue("Status", contactGroup.getContactGroupID()));
                instanceList.add(contactGroupInstance);
            });
        }

        // Address
        if (contact.getAddresses() != null && contact.getAddresses().size() > 0) {
            List<Address> addresses = contact.getAddresses();
            addresses.forEach(address -> {
                // if there are no details in any of the four address lines, lets not save it
                if (address.getAddressLine1() != null && !address.getAddressLine1().isEmpty()
                ) {
                    EntityInstance addressInstance = new EntityInstance();
                    addressInstance.set_vertexName(PluginConstants.XERO_ENTITY_ADDRESS);
                    addressInstance.set_label(PluginConstants.XERO_ENTITY_ADDRESS);
                    List<InstanceValue> addressInstanceValues = new ArrayList<>();
                    addressInstance.setInstanceValues(addressInstanceValues);
                    addressInstanceValues.add(new InstanceValue("AddressLine1", address.getAddressLine1()));
                    addressInstanceValues.add(new InstanceValue("AddressLine2", address.getAddressLine2()));
                    addressInstanceValues.add(new InstanceValue("AddressLine3", address.getAddressLine3()));
                    addressInstanceValues.add(new InstanceValue("AddressLine4", address.getAddressLine4()));
                    addressInstanceValues.add(new InstanceValue("AttentionTo", address.getAttentionTo()));
                    addressInstanceValues.add(new InstanceValue("AddressType", address.getAddressType()));
                    addressInstanceValues.add(new InstanceValue("City", address.getCity()));
                    addressInstanceValues.add(new InstanceValue("Country", address.getCountry()));
                    addressInstanceValues.add(new InstanceValue("PostalCode", address.getPostalCode()));
                    addressInstanceValues.add(new InstanceValue("Region", address.getRegion()));
                    instanceList.add(addressInstance);
                }
            });
        }
        // Attachment
        if (contact.getAttachments() != null && contact.getAttachments().size() > 0) {
            List<Attachment> xeroObjectList = contact.getAttachments();
            xeroObjectList.forEach(xeroObject -> {
                EntityInstance cosightInstance = new EntityInstance();
                cosightInstance.set_vertexName(PluginConstants.XERO_ENTITY_ATTACHMENT);
                cosightInstance.set_label(PluginConstants.XERO_ENTITY_ATTACHMENT);
                List<InstanceValue> cosightInstanceValues = new ArrayList<>();
                cosightInstance.setInstanceValues(cosightInstanceValues);
                cosightInstanceValues.add(new InstanceValue("AttachmentID", xeroObject.getAttachmentID()));
                cosightInstanceValues.add(new InstanceValue("ContentLength", xeroObject.getAttachmentID()));
                cosightInstanceValues.add(new InstanceValue("FileName", xeroObject.getAttachmentID()));
                cosightInstanceValues.add(new InstanceValue("IncludeOnline", xeroObject.getAttachmentID()));
                cosightInstanceValues.add(new InstanceValue("MimeType", xeroObject.getAttachmentID()));
                cosightInstanceValues.add(new InstanceValue("URL", xeroObject.getAttachmentID()));
                instanceList.add(cosightInstance);
            });
        }

        // BatchPaymentDetails is flat, but we have separate entity for it
        if (contact.getBatchPayments() != null) {
            BatchPaymentDetails xeroObject = contact.getBatchPayments();
            EntityInstance batchPayments = new EntityInstance();
            batchPayments.set_vertexName(PluginConstants.XERO_ENTITY_BATCH_PAYMENT_DETAILS);
            batchPayments.set_label(PluginConstants.XERO_ENTITY_BATCH_PAYMENT_DETAILS);
            List<InstanceValue> batchInstanceValues = new ArrayList<>();
            batchPayments.setInstanceValues(batchInstanceValues);
            batchInstanceValues.add(new InstanceValue("BankAccountName", xeroObject.getBankAccountName()));
            batchInstanceValues.add(new InstanceValue("Code", xeroObject.getCode()));
            batchInstanceValues.add(new InstanceValue("BankAccountNumber", xeroObject.getBankAccountNumber()));
            batchInstanceValues.add(new InstanceValue("Reference", xeroObject.getReference()));
            batchInstanceValues.add(new InstanceValue("Details", xeroObject.getDetails()));
            instanceList.add(batchPayments);
        }

        // ContactPerson
        if (contact.getContactPersons() != null && contact.getContactPersons().size() > 0) {
            List<ContactPerson> xeroObjectList = contact.getContactPersons();
            xeroObjectList.forEach(xeroObject -> {
                EntityInstance cosightInstance = new EntityInstance();
                cosightInstance.set_vertexName(PluginConstants.XERO_ENTITY_CONTACT_PERSON);
                cosightInstance.set_label(PluginConstants.XERO_ENTITY_CONTACT_PERSON);
                List<InstanceValue> cosightInstanceValues = new ArrayList<>();
                cosightInstance.setInstanceValues(cosightInstanceValues);
                cosightInstanceValues.add(new InstanceValue("FirstName", xeroObject.getFirstName()));
                cosightInstanceValues.add(new InstanceValue("EmailAddress", xeroObject.getEmailAddress()));
                cosightInstanceValues.add(new InstanceValue("LastName", xeroObject.getLastName()));
                cosightInstanceValues.add(new InstanceValue("IncludeInEmails", xeroObject.getIncludeInEmails()));
                instanceList.add(cosightInstance);
            });
        }

        // Phone
        if (contact.getPhones() != null && contact.getPhones().size() > 0) {
            List<Phone> xeroObjectList = contact.getPhones();
            xeroObjectList.forEach(xeroObject -> {
                if (xeroObject.getPhoneNumber() != null && !xeroObject.getPhoneNumber().isEmpty()) {
                    EntityInstance cosightInstance = new EntityInstance();
                    cosightInstance.set_vertexName(PluginConstants.XERO_ENTITY_PHONE);
                    cosightInstance.set_label(PluginConstants.XERO_ENTITY_PHONE);
                    List<InstanceValue> cosightInstanceValues = new ArrayList<>();
                    cosightInstance.setInstanceValues(cosightInstanceValues);
                    cosightInstanceValues.add(new InstanceValue("PhoneAreaCode", xeroObject.getPhoneAreaCode()));
                    cosightInstanceValues.add(new InstanceValue("PhoneNumber", xeroObject.getPhoneNumber()));
                    cosightInstanceValues.add(new InstanceValue("PhoneCountryCode", xeroObject.getPhoneCountryCode()));
                    cosightInstanceValues.add(new InstanceValue("PhoneType", xeroObject.getPhoneType()));
                    instanceList.add(cosightInstance);

                }
            });
        }

        // SalesTrackingCategory
        if (contact.getSalesTrackingCategories() != null && contact.getSalesTrackingCategories().size() > 0) {
            List<SalesTrackingCategory> xeroObjectList = contact.getSalesTrackingCategories();
            xeroObjectList.forEach(xeroObject -> {
                EntityInstance cosightInstance = new EntityInstance();
                cosightInstance.set_vertexName(PluginConstants.XERO_ENTITY_SALES_TRACKING_CATEGORY);
                cosightInstance.set_label(PluginConstants.XERO_ENTITY_SALES_TRACKING_CATEGORY);
                List<InstanceValue> cosightInstanceValues = new ArrayList<>();
                cosightInstance.setInstanceValues(cosightInstanceValues);
                cosightInstanceValues.add(new InstanceValue("TrackingOptionName", xeroObject.getTrackingOptionName()));
                cosightInstanceValues.add(new InstanceValue("TrackingCategoryName", xeroObject.getTrackingCategoryName()));
                instanceList.add(cosightInstance);
            });
        }

        // ValidationError
        if (contact.getValidationErrors() != null && contact.getValidationErrors().size() > 0) {
            List<ValidationError> xeroObjectList = contact.getValidationErrors();
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
