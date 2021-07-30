package au.com.cosight.xero.plugin.service;

import au.com.cosight.entity.domain.EntitiesCreateCreateRequest;
import au.com.cosight.entity.domain.enums.CosightDataType;
import au.com.cosight.entity.domain.enums.EntityVisibilityTypes;
import au.com.cosight.entity.service.dto.DataFieldsDTO;
import au.com.cosight.entity.service.dto.EntitiesDTO;
import au.com.cosight.sdk.plugin.runtime.helper.EntityServiceWrapper;
import com.xero.models.accounting.*;
import org.springframework.stereotype.Service;

@Service
public class EntityManagementServiceImpl {

    private EntityServiceWrapper entityServiceWrapper;
    public EntityManagementServiceImpl(EntityServiceWrapper entityServiceWrapper) {
        this.entityServiceWrapper = entityServiceWrapper;
    }

    public void createContactClass(String prefix) {
        entityServiceWrapper.auth();
        EntitiesCreateCreateRequest request = new EntitiesCreateCreateRequest()
                .withName(prefix + "contacts")
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addFiled(new DataFieldsDTO().withName("AccountNumber")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("ContactID")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("ContactStatus")
                        .withDataType(CosightDataType.LIST))
                .addFiled(new DataFieldsDTO().withName("CurrencyCode")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("AccountsPayableTaxType")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("AccountsReceivableTaxType")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("BankAccountDetails")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("ContactNumber")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("SalesPaymentTerms")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("SalesPaymentTermsDay")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("BillingPaymentTerms")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("BillingPaymentTermsDay")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("Discount")
                        .withDataType(CosightDataType.DOUBLE))
                .addFiled(new DataFieldsDTO().withName("EmailAddress")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("FirstName")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("HasAttachments")
                        .withDataType(CosightDataType.BOOLEAN))
                .addFiled(new DataFieldsDTO().withName("HasValidationErrors")
                        .withDataType(CosightDataType.BOOLEAN))
                .addFiled(new DataFieldsDTO().withName("IsCustomer")
                        .withDataType(CosightDataType.BOOLEAN))
                .addFiled(new DataFieldsDTO().withName("IsSupplier")
                        .withDataType(CosightDataType.BOOLEAN))
                .addFiled(new DataFieldsDTO().withName("LastName")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("Name")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addFiled(new DataFieldsDTO().withName("PayableOverdue")
                        .withDataType(CosightDataType.DOUBLE))
                .addFiled(new DataFieldsDTO().withName("PayableOutstanding")
                        .withDataType(CosightDataType.DOUBLE))
                .addFiled(new DataFieldsDTO().withName("ReceivableOverdue")
                        .withDataType(CosightDataType.DOUBLE)
                        .withLabel(true))
                .addFiled(new DataFieldsDTO().withName("ReceivableOutstanding")
                        .withDataType(CosightDataType.DOUBLE))
                .addFiled(new DataFieldsDTO().withName("PurchasesDefaultAccountCode")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("SalesDefaultAccountCode")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("SkypeUserName")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("StatusAttributeString")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("TaxNumber")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("TrackingCategoryName")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("TrackingCategoryOption")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("UpdatedDateUTC")
                        .withDataType(CosightDataType.DATE_TIME))
                .addFiled(new DataFieldsDTO().withName("Website")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("XeroNetworkKey")
                        .withDataType(CosightDataType.STRING))
                .addIndex("ContactID");
        ;

        //Above is the flat contact object, it has many other things that hang off it that aren't flat that
        //we need to create as well as the relationships between them.
        EntitiesDTO contact = entityServiceWrapper.createEntityStructure(request);
        // now we need to create the following and link
        // ContactGroup

        EntitiesCreateCreateRequest contactGroupRequest = new EntitiesCreateCreateRequest().withName(prefix + "ContactGroup")
                .addFiled(new DataFieldsDTO().withName("ContactGroupID")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("Name")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addFiled(new DataFieldsDTO().withName("Status")
                        .withDataType(CosightDataType.LIST))
                .addIndex("ContactGroupID");
        ; // need to add config to add list items here
        EntitiesDTO contactGroup = entityServiceWrapper.createEntityStructure(contactGroupRequest);

        // Address
        EntitiesCreateCreateRequest addressRequest = new EntitiesCreateCreateRequest().withName(prefix + "Address")
                .addFiled(new DataFieldsDTO().withName("AddressLine1")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addFiled(new DataFieldsDTO().withName("AddressLine2")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("AddressLine3")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("AddressLine4")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("AttentionTo")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("AddressType")
                        .withDataType(CosightDataType.LIST))
                .addFiled(new DataFieldsDTO().withName("City")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("Country")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("PostalCode")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("Region")
                        .withDataType(CosightDataType.STRING));

        EntitiesDTO address = entityServiceWrapper.createEntityStructure(addressRequest);



        // Attachment
        EntitiesCreateCreateRequest attachmentRequest = new EntitiesCreateCreateRequest().withName(prefix + "Attachment")
                .addFiled(new DataFieldsDTO().withName("AttachmentID")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("ContentLength")
                        .withDataType(CosightDataType.LONG))
                .addFiled(new DataFieldsDTO().withName("FileName")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addFiled(new DataFieldsDTO().withName("IncludeOnline")
                        .withDataType(CosightDataType.BOOLEAN))
                .addFiled(new DataFieldsDTO().withName("MimeType")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("URL")
                        .withDataType(CosightDataType.STRING))
                .addIndex("AttachmentID");
        ;
        EntitiesDTO attachment = entityServiceWrapper.createEntityStructure(attachmentRequest);
        // Balances - these can be part of root instead of own object

//        EntitiesCreateCreateRequest balancesRequest = new EntitiesCreateCreateRequest().withName(prefix + "Balances")
//                .addFiled(new DataFieldsDTO().withName("PayableOverdue")
//                        .withDataType(CosightDataType.DOUBLE))
//                .addFiled(new DataFieldsDTO().withName("PayableOutstanding")
//                        .withDataType(CosightDataType.DOUBLE))
//                .addFiled(new DataFieldsDTO().withName("ReceivableOverdue")
//                        .withDataType(CosightDataType.DOUBLE)
//                        .withLabel(true))
//                .addFiled(new DataFieldsDTO().withName("ReceivableOutstanding")
//                        .withDataType(CosightDataType.DOUBLE));
//
//        EntitiesDTO balances = entityServiceWrapper.createEntityStructure(balancesRequest);

        // BatchPaymentDetails - maybe should be fields of the contact since it isn't a list.
        EntitiesCreateCreateRequest batchpaymentDetailsRequest = new EntitiesCreateCreateRequest().withName(prefix + "BatchPaymentDetails")
                .addFiled(new DataFieldsDTO().withName("BankAccountName")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("Code")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addFiled(new DataFieldsDTO().withName("BankAccountNumber")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("Reference")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("Details")
                        .withDataType(CosightDataType.STRING))
                .addIndex("BankAccountName");
        ;
        EntitiesDTO batchpaymentDetails = entityServiceWrapper.createEntityStructure(batchpaymentDetailsRequest);


        // BrandingTheme? - nope

        // ContactPerson
        EntitiesCreateCreateRequest contactPersonRequest = new EntitiesCreateCreateRequest().withName(prefix + "ContactPerson")
                .addFiled(new DataFieldsDTO().withName("FirstName")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("EmailAddress")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addFiled(new DataFieldsDTO().withName("LastName")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("IncludeInEmails")
                        .withDataType(CosightDataType.BOOLEAN))
                .addIndex("EmailAddress");
        ;
        EntitiesDTO contactPerson = entityServiceWrapper.createEntityStructure(contactPersonRequest);


        // Phone
        EntitiesCreateCreateRequest phoneRequest = new EntitiesCreateCreateRequest().withName(prefix + "Phone")
                .addFiled(new DataFieldsDTO().withName("PhoneAreaCode")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("PhoneNumber")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addFiled(new DataFieldsDTO().withName("PhoneCountryCode")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("PhoneType")
                        .withDataType(CosightDataType.STRING));
        EntitiesDTO phone = entityServiceWrapper.createEntityStructure(phoneRequest);


        // SalesTrackingCategory
        EntitiesCreateCreateRequest salesTrackingCatRequest = new EntitiesCreateCreateRequest().withName(prefix + "SalesTrackingCategory")
                .addFiled(new DataFieldsDTO().withName("TrackingOptionName")
                        .withDataType(CosightDataType.STRING))
                .addFiled(new DataFieldsDTO().withName("TrackingCategoryName")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addIndex("TrackingOptionName");
        EntitiesDTO salesTrackingCat = entityServiceWrapper.createEntityStructure(salesTrackingCatRequest);

        // ValidationError
        ValidationError v  = new ValidationError();
        EntitiesCreateCreateRequest validationErrorReq = new EntitiesCreateCreateRequest().withName(prefix + "ValidationError")
                .addFiled(new DataFieldsDTO().withName("Message")
                        .withDataType(CosightDataType.STRING).withLabel(true));
        EntitiesDTO validationError = entityServiceWrapper.createEntityStructure(validationErrorReq);

    }
}
