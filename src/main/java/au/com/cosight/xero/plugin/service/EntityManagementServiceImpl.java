package au.com.cosight.xero.plugin.service;

import au.com.cosight.entity.domain.DatafieldConfig;
import au.com.cosight.entity.domain.EntitiesCreateCreateRequest;
import au.com.cosight.entity.domain.RelationshipsCreateCreateRequest;
import au.com.cosight.entity.domain.enums.CosightDataType;
import au.com.cosight.entity.domain.enums.EntityVisibilityTypes;
import au.com.cosight.entity.service.dto.DataFieldsDTO;
import au.com.cosight.entity.service.dto.EntitiesDTO;
import au.com.cosight.entity.service.dto.RelationshipsDTO;
import au.com.cosight.sdk.plugin.runtime.helper.EntityServiceWrapper;
import au.com.cosight.sdk.plugin.runtime.helper.RelationshipServiceWrapper;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.ValidationError;
import org.springframework.stereotype.Service;

@Service
public class EntityManagementServiceImpl {

    private EntityServiceWrapper entityServiceWrapper;
    private RelationshipServiceWrapper relationshipServiceWrapper;

    public EntityManagementServiceImpl(EntityServiceWrapper entityServiceWrapper, RelationshipServiceWrapper relationshipServiceWrapper) {
        this.entityServiceWrapper = entityServiceWrapper;
        this.relationshipServiceWrapper = relationshipServiceWrapper;
    }

    public void createAccountClass(String prefix) {

    }

    public void createContactClass(String prefix) {
        entityServiceWrapper.auth();
        relationshipServiceWrapper.auth();
        EntitiesCreateCreateRequest request = new EntitiesCreateCreateRequest()
                .withName(prefix + "Contact")
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addField(new DataFieldsDTO().withName("AccountNumber")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("ContactID")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("ContactStatus")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("ACTIVE")
                                .withDataListItem("ARCHIVE")
                                .withDataListItem("GDPRREQUEST")))
                .addField(new DataFieldsDTO().withName("CurrencyCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("AccountsPayableTaxType")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("AccountsReceivableTaxType")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("BankAccountDetails")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("ContactNumber")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("SalesPaymentTerms")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("SalesPaymentTermsDay")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("BillingPaymentTerms")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("BillingPaymentTermsDay")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Discount")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("EmailAddress")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("FirstName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("HasAttachments")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("HasValidationErrors")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("IsCustomer")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("IsSupplier")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("LastName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Name")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addField(new DataFieldsDTO().withName("PayableOverdue")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("PayableOutstanding")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("ReceivableOverdue")
                        .withDataType(CosightDataType.DOUBLE)
                        .withLabel(true))
                .addField(new DataFieldsDTO().withName("ReceivableOutstanding")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("PurchasesDefaultAccountCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("SalesDefaultAccountCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("SkypeUserName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("StatusAttributeString")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("TaxNumber")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("TrackingCategoryName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("TrackingCategoryOption")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("UpdatedDateUTC")
                        .withDataType(CosightDataType.DATE_TIME))
                .addField(new DataFieldsDTO().withName("Website")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("XeroNetworkKey")
                        .withDataType(CosightDataType.STRING))
                .addIndex("ContactID");

        //Above is the flat contact object, it has many other things that hang off it that aren't flat that
        //we need to create as well as the relationships between them.
        EntitiesDTO contact = null;
        try {
            contact = entityServiceWrapper.createEntityStructure(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - assume everything is done for now, return");
                return;
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }

        //
        // now we need to create the following and link
        // ContactGroup
        EntitiesCreateCreateRequest contactGroupRequest = new EntitiesCreateCreateRequest().withName(prefix + "ContactGroup")
                .addField(new DataFieldsDTO().withName("ContactGroupID")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Name")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addField(new DataFieldsDTO().withName("Status")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("ACTIVE")
                                .withDataListItem("DELETED")))
                .addIndex("ContactGroupID");
        ; // need to add config to add list items here
        EntitiesDTO contactGroup = null;
        try {
            contactGroup = entityServiceWrapper.createEntityStructure(contactGroupRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        RelationshipsCreateCreateRequest contactToContactGroup =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_CONTACT_GROUP)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(contactGroup.getId())
                        .withDescription("Link between Contact and Contact Group");

        RelationshipsDTO contactToContactGroupResult = null;
        try {
            contactToContactGroupResult = relationshipServiceWrapper.createRelationship(contactToContactGroup);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        // Address
        EntitiesCreateCreateRequest addressRequest = new EntitiesCreateCreateRequest().withName(prefix + "Address")
                .addField(new DataFieldsDTO().withName("AddressLine1")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addField(new DataFieldsDTO().withName("AddressLine2")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("AddressLine3")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("AddressLine4")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("AttentionTo")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("AddressType")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("POBOX")
                                .withDataListItem("STREET")))
                .addField(new DataFieldsDTO().withName("City")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Country")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("PostalCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Region")
                        .withDataType(CosightDataType.STRING))
                .addIndex("AddressLine1").addIndex("PostalCode");
        EntitiesDTO address = null;
        try {
            address = entityServiceWrapper.createEntityStructure(addressRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        RelationshipsCreateCreateRequest contactToAddress =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_ADDRESS)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(address.getId())
                        .withDescription("Link between Contact and Address");

        RelationshipsDTO contactToAddressResult = null;
        try {
            contactToAddressResult = relationshipServiceWrapper.createRelationship(contactToAddress);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        // Attachment
        EntitiesCreateCreateRequest attachmentRequest = new EntitiesCreateCreateRequest().withName(prefix + "Attachment")
                .addField(new DataFieldsDTO().withName("AttachmentID")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("ContentLength")
                        .withDataType(CosightDataType.LONG))
                .addField(new DataFieldsDTO().withName("FileName")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addField(new DataFieldsDTO().withName("IncludeOnline")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("MimeType")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("URL")
                        .withDataType(CosightDataType.STRING))
                .addIndex("AttachmentID");
        ;
        EntitiesDTO attachment = null;
        try {
            attachment = entityServiceWrapper.createEntityStructure(attachmentRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        RelationshipsCreateCreateRequest contactToAttachment =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_ATTACHMENT)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(attachment.getId())
                        .withDescription("Link between Contact and Contact Group");

        RelationshipsDTO contactToAttachmentResult = null;
        try {
            contactToAttachmentResult = relationshipServiceWrapper.createRelationship(contactToAttachment);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }

        // Balances - these can be part of root instead of own object

//        EntitiesCreateCreateRequest balancesRequest = new EntitiesCreateCreateRequest().withName(prefix + "Balances")
//                .addField(new DataFieldsDTO().withName("PayableOverdue")
//                        .withDataType(CosightDataType.DOUBLE))
//                .addField(new DataFieldsDTO().withName("PayableOutstanding")
//                        .withDataType(CosightDataType.DOUBLE))
//                .addField(new DataFieldsDTO().withName("ReceivableOverdue")
//                        .withDataType(CosightDataType.DOUBLE)
//                        .withLabel(true))
//                .addField(new DataFieldsDTO().withName("ReceivableOutstanding")
//                        .withDataType(CosightDataType.DOUBLE));
//
//        EntitiesDTO balances = entityServiceWrapper.createEntityStructure(balancesRequest);

        // BatchPaymentDetails - maybe should be fields of the contact since it isn't a list.
        EntitiesCreateCreateRequest batchpaymentDetailsRequest = new EntitiesCreateCreateRequest().withName(prefix + "BatchPaymentDetails")
                .addField(new DataFieldsDTO().withName("BankAccountName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Code")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addField(new DataFieldsDTO().withName("BankAccountNumber")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Reference")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Details")
                        .withDataType(CosightDataType.STRING))
                .addIndex("BankAccountName");

        EntitiesDTO batchPaymentDetails = null;
        try {
            batchPaymentDetails = entityServiceWrapper.createEntityStructure(batchpaymentDetailsRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }

        RelationshipsCreateCreateRequest contactTobatchPaymentDetails =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_BATCH_PAYMENT_DETAILS)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(batchPaymentDetails.getId())
                        .withDescription("Link between Contact and Contact Group");

        RelationshipsDTO contactTobatchPaymentDetailsResult = null;
        try {
            contactTobatchPaymentDetailsResult = relationshipServiceWrapper.createRelationship(contactTobatchPaymentDetails);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        // BrandingTheme? - nope

        // ContactPerson
        EntitiesCreateCreateRequest contactPersonRequest = new EntitiesCreateCreateRequest().withName(prefix + "ContactPerson")
                .addField(new DataFieldsDTO().withName("FirstName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("EmailAddress")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addField(new DataFieldsDTO().withName("LastName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("IncludeInEmails")
                        .withDataType(CosightDataType.BOOLEAN))
                .addIndex("EmailAddress");
        EntitiesDTO contactPerson = null;
        try {
            contactPerson = entityServiceWrapper.createEntityStructure(contactPersonRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        RelationshipsCreateCreateRequest contactTocontactPerson =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_CONTACT_PERSON)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(contactPerson.getId())
                        .withDescription("Link between Contact and Contact Group");

        RelationshipsDTO contactTocontactPersonResult = null;
        try {
            contactTocontactPersonResult = relationshipServiceWrapper.createRelationship(contactTocontactPerson);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        // Phone
        EntitiesCreateCreateRequest phoneRequest = new EntitiesCreateCreateRequest().withName(prefix + "Phone")
                .addField(new DataFieldsDTO().withName("PhoneAreaCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("PhoneNumber")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addField(new DataFieldsDTO().withName("PhoneCountryCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("PhoneType")
                        .withDataType(CosightDataType.STRING))
                .addIndex("PhoneNumber").addIndex("PhoneAreaCode");

        EntitiesDTO phone = null;
        try {
            phone = entityServiceWrapper.createEntityStructure(phoneRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        RelationshipsCreateCreateRequest contactTophone =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_PHONE)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(phone.getId())
                        .withDescription("Link between Contact and Contact Group");

        RelationshipsDTO contactTophoneResult = null;
        try {
            contactTophoneResult = relationshipServiceWrapper.createRelationship(contactTophone);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        // SalesTrackingCategory
        EntitiesCreateCreateRequest salesTrackingCatRequest = new EntitiesCreateCreateRequest().withName(prefix + "SalesTrackingCategory")
                .addField(new DataFieldsDTO().withName("TrackingOptionName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("TrackingCategoryName")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addIndex("TrackingOptionName");
        EntitiesDTO salesTrackingCategory = null;
        try {
            salesTrackingCategory = entityServiceWrapper.createEntityStructure(salesTrackingCatRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        RelationshipsCreateCreateRequest contactTosalesTrackingCategory =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_SALES_TRACKING_CATEGORY)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(salesTrackingCategory.getId())
                        .withDescription("Link between Contact and Contact Group");

        RelationshipsDTO contactTosalesTrackingCategoryResult = null;
        try {
            contactTosalesTrackingCategoryResult = relationshipServiceWrapper.createRelationship(contactTosalesTrackingCategory);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        // ValidationError
        ValidationError v = new ValidationError();
        EntitiesCreateCreateRequest validationErrorReq = new EntitiesCreateCreateRequest().withName(prefix + "ValidationError")
                .addField(new DataFieldsDTO().withName("Message")
                        .withDataType(CosightDataType.STRING).withLabel(true));
        EntitiesDTO validationError = null;
        try {
            validationError = entityServiceWrapper.createEntityStructure(validationErrorReq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }


        RelationshipsCreateCreateRequest contactTovalidationError =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_VALIDATION_ERROR)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(validationError.getId())
                        .withDescription("Link between Contact and Contact Group");

        RelationshipsDTO contactTovalidationErrorResult = null;
        try {
            contactTovalidationErrorResult = relationshipServiceWrapper.createRelationship(contactTovalidationError);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
                System.out.println("Entity already created - continue");
                // will have to think about an update mechanism here with versioning etc.
            } else {
                e.printStackTrace();
            }
        }
    }
}
