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
    public static EntityInstance toEntityInstance(Contact contact) {
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
        return contactInstance;

    }
    public static List<EntityInstance> toEntityInstances(Contact contact) {
        List<EntityInstance> instanceList = new ArrayList<>();

        instanceList.add(toEntityInstance(contact));

        // ContactGroups
        if (contact.getContactGroups() != null && contact.getContactGroups().size() > 0) {
            List<ContactGroup> contactGroups = contact.getContactGroups();
            contactGroups.forEach(contactGroup -> instanceList.add(ContactGroupMapper.toInstance(contactGroup)));
        }

        // Address
        if (contact.getAddresses() != null && contact.getAddresses().size() > 0) {
            List<Address> addresses = contact.getAddresses();
            addresses.forEach(address -> {
                // if there are no details in any of the four address lines, lets not save it
                if (address.getAddressLine1() != null && !address.getAddressLine1().isEmpty()
                ) {
                    instanceList.add(AddressMapper.toInstance(address));
                }
            });
        }
        // Attachment
        if (contact.getAttachments() != null && contact.getAttachments().size() > 0) {
            List<Attachment> xeroObjectList = contact.getAttachments();
            xeroObjectList.forEach(xeroObject -> instanceList.add(AttachmentMapper.toInstance(xeroObject)));
        }

        // BatchPaymentDetails is flat, but we have separate entity for it
        if (contact.getBatchPayments() != null) {
            BatchPaymentDetails xeroObject = contact.getBatchPayments();
            instanceList.add(BatchPaymentDetailsMapper.toInstance(xeroObject));
        }

        // ContactPerson
        if (contact.getContactPersons() != null && contact.getContactPersons().size() > 0) {
            List<ContactPerson> xeroObjectList = contact.getContactPersons();
            xeroObjectList.forEach(xeroObject -> {
                instanceList.add(ContactPersonMapper.toInstance(xeroObject));
            });
        }

        // Phone
        if (contact.getPhones() != null && contact.getPhones().size() > 0) {
            List<Phone> xeroObjectList = contact.getPhones();
            xeroObjectList.forEach(xeroObject -> {
                if (xeroObject.getPhoneNumber() != null && !xeroObject.getPhoneNumber().isEmpty()) {
                    instanceList.add(PhoneMapper.toInstance(xeroObject));
                }
            });
        }

        // SalesTrackingCategory
        if (contact.getSalesTrackingCategories() != null && contact.getSalesTrackingCategories().size() > 0) {
            List<SalesTrackingCategory> xeroObjectList = contact.getSalesTrackingCategories();
            xeroObjectList.forEach(xeroObject -> {
                instanceList.add(SalesTrackingCategoryMapper.toInstance(xeroObject));
            });
        }

        // ValidationError
        if (contact.getValidationErrors() != null && contact.getValidationErrors().size() > 0) {
            List<ValidationError> xeroObjectList = contact.getValidationErrors();
            xeroObjectList.forEach(xeroObject -> {
                instanceList.add(ValidationErrorMapper.toInstance(xeroObject));
            });
        }

        return instanceList;

    }
}
