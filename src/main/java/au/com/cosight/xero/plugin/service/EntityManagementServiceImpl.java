package au.com.cosight.xero.plugin.service;

import au.com.cosight.common.dto.plugin.helper.EntityServiceWrapper;
import au.com.cosight.common.dto.plugin.helper.RelationshipServiceWrapper;
import au.com.cosight.entity.domain.DatafieldConfig;
import au.com.cosight.entity.domain.EntitiesCreateCreateRequest;
import au.com.cosight.entity.domain.RelationshipsCreateCreateRequest;
import au.com.cosight.entity.domain.enums.CosightDataType;
import au.com.cosight.entity.domain.enums.EntityVisibilityTypes;
import au.com.cosight.entity.service.dto.DataFieldsDTO;
import au.com.cosight.entity.service.dto.EntitiesDTO;
import au.com.cosight.entity.service.dto.ExpandedEntitiesDTO;
import au.com.cosight.entity.service.dto.RelationshipsDTO;
import au.com.cosight.xero.plugin.PluginConstants;
import com.xero.models.accounting.Account;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EntityManagementServiceImpl {

    private EntityServiceWrapper entityServiceWrapper;
    private RelationshipServiceWrapper relationshipServiceWrapper;

    public EntityManagementServiceImpl(EntityServiceWrapper entityServiceWrapper, RelationshipServiceWrapper relationshipServiceWrapper) {
        this.entityServiceWrapper = entityServiceWrapper;
        this.relationshipServiceWrapper = relationshipServiceWrapper;
    }

    private Optional<EntitiesDTO> createEntity(EntitiesCreateCreateRequest request) {
        EntitiesDTO theObject = null;
        try {
            theObject = entityServiceWrapper.createEntityStructure(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicated Entity Name")) {
//                System.out.println("Entity already created - assume everything is done for now, return");
                System.out.println("Duplicate entity name " + request.getName() + " : " + request.getProjectId());
                ExpandedEntitiesDTO dto = entityServiceWrapper.getEntityByClassname(request.getName());

                theObject = dto.getEnitiy();
            } else {
                return Optional.empty();
            }
        }
        return Optional.ofNullable(theObject);

    }

    private Optional<RelationshipsDTO> createRelationship(RelationshipsCreateCreateRequest request) {
        RelationshipsDTO relo = null;
        try {
            relo = relationshipServiceWrapper.createRelationship(request);
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicated RelationshipName")) {
                // will have to think about an update mechanism here with versioning etc.
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(relo);

    }

    public void createAccountClass(String prefix) {
        Account x = new Account();

        EntitiesCreateCreateRequest request = new EntitiesCreateCreateRequest()
                .withProjectId("0")
                .withName(prefix + PluginConstants.XERO_ENTITY_ACCOUNT)
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addField(new DataFieldsDTO().withName("AccountID")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("BankAccountNumber")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Code")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("AddToWatchlist")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("Name")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addField(new DataFieldsDTO().withName("BankAccountType")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("CurrencyCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Description")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("EnablePaymentsToAccount")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("HasAttachments")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("PropertyClass")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("ReportingCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("ReportingCodeName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("ShowInExpenseClaims")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("Status")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("SystemAccount")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("TaxType")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Type")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("UpdatedDateUTC")
                        .withDataType(CosightDataType.DATE_TIME))
                .addIndex("AccountID");

        EntitiesDTO account = createEntity(request).orElseThrow(IllegalStateException::new);

        EntitiesDTO validationErr = entityServiceWrapper.getEntityByClassname(PluginConstants.XERO_ENTITY_VALIDATION_ERROR).getEnitiy();

        // just one relationship - account to validation errors
        // we have to get class
        RelationshipsCreateCreateRequest contactToAddress =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_ACCOUNT_TO_VALIDATION_ERROR)
                        .withFromEntityId(account.getId())
                        .withToEntityId(validationErr.getId())
                        .withDescription("Link between Contact and Address");

        createRelationship(contactToAddress);

    }


    public void createBankTransactionClass(String prefix) {
        entityServiceWrapper.auth();
        relationshipServiceWrapper.auth();
        EntitiesCreateCreateRequest bankTransactionRequest = new EntitiesCreateCreateRequest()
                .withProjectId("0")
                .withName(prefix + PluginConstants.XERO_ENTITY_BANK_TRANSACTION)
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addField(new DataFieldsDTO().withName("BankTransactionId")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Type")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("CurrencyCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("CurrencyRate")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Date")
                        .withDataType(CosightDataType.DATE_TIME))
                .addField(new DataFieldsDTO().withName("HasAttachments")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("IsReconciled")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("LineAmountTypes")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("OverpaymentId")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("PrepaymentId")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Reference")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Status")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Subtotal")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Total")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("TotalTax")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("UpdatedDateUTC")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Url")
                        .withDataType(CosightDataType.STRING))
                .addIndex("BankTransactionId");

        EntitiesDTO bankTransactionEntity = createEntity(bankTransactionRequest).orElseThrow(IllegalStateException::new);

        EntitiesDTO validationErrEntity = entityServiceWrapper.getEntityByClassname(PluginConstants.XERO_ENTITY_VALIDATION_ERROR).getEnitiy();
        EntitiesDTO accountEntity = entityServiceWrapper.getEntityByClassname(PluginConstants.XERO_ENTITY_ACCOUNT).getEnitiy();
        EntitiesDTO contactEntity = entityServiceWrapper.getEntityByClassname(PluginConstants.XERO_ENTITY_CONTACT).getEnitiy();

        RelationshipsCreateCreateRequest bankTransactionToValidationError =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_BANK_TRANSACTION_TO_VALIDATION_ERROR)
                        .withFromEntityId(bankTransactionEntity.getId())
                        .withToEntityId(validationErrEntity.getId())
                        .withDescription("Link between BankTransaction and Validation Error");

        createRelationship(bankTransactionToValidationError);

        RelationshipsCreateCreateRequest bankTransactionToAccountRequest =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_BANK_TRANSACTION_TO_ACCOUNT)
                        .withFromEntityId(bankTransactionEntity.getId())
                        .withToEntityId(accountEntity.getId())
                        .withDescription("Link between BankTransaction and Account");

        createRelationship(bankTransactionToAccountRequest);

        RelationshipsCreateCreateRequest bankTransactionToContactRequest =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_BANK_TRANSACTION_TO_CONTACT)
                        .withFromEntityId(bankTransactionEntity.getId())
                        .withToEntityId(contactEntity.getId())
                        .withDescription("Link between BankTransaction and Contact");

        createRelationship(bankTransactionToContactRequest);


        EntitiesCreateCreateRequest lineItems = new EntitiesCreateCreateRequest()
                .withProjectId("0")
                .withName(prefix + PluginConstants.XERO_ENTITY_LINE_ITEM)
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addField(new DataFieldsDTO().withName("AccountCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("LineItemId")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addField(new DataFieldsDTO().withName("AccountId")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("BankTransactionId")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Description")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("DiscountAmount")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("DiscountRate")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("ItemCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("LineAmount")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Quantity")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("RepeatingInvoiceId")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("TaxAmount")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("TaxType")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("UnitAmount")
                        .withDataType(CosightDataType.DOUBLE))
                .addIndex("AccountId").addIndex("LineItemId");

        EntitiesDTO lineItemEntity = createEntity(lineItems).orElseThrow(IllegalStateException::new);

        RelationshipsCreateCreateRequest bankTransactionToLineItemRequest =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_BANK_TRANSACTION_TO_LINE_ITEM)
                        .withFromEntityId(bankTransactionEntity.getId())
                        .withToEntityId(lineItemEntity.getId())
                        .withDescription("Link between BankTransaction and Line Item");

        createRelationship(bankTransactionToLineItemRequest);


        EntitiesCreateCreateRequest lineItemTracking = new EntitiesCreateCreateRequest()
                .withProjectId("0")
                .withName(prefix + PluginConstants.XERO_ENTITY_LINE_ITEM_TRACKING)
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addField(new DataFieldsDTO().withName("Name")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addField(new DataFieldsDTO().withName("Option")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("TrackingCategoryId")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("TrackingOptionId")
                        .withDataType(CosightDataType.STRING))
                .addIndex("TrackingCategoryId").addIndex("TrackingOptionId");

        EntitiesDTO lineItemTrackingEntity = createEntity(lineItemTracking).orElseThrow(IllegalStateException::new);

        RelationshipsCreateCreateRequest lineItemToLineItemTrackingRequest =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_LINE_ITEM_TO_LINE_ITEM_TRACKING)
                        .withFromEntityId(lineItemEntity.getId())
                        .withToEntityId(lineItemTrackingEntity.getId())
                        .withDescription("Link between LineItem and Line Item Tracking");

        createRelationship(lineItemToLineItemTrackingRequest);


    }

    public void createInvoiceClass(String prefix) {
        entityServiceWrapper.auth();
        relationshipServiceWrapper.auth();
        EntitiesCreateCreateRequest request = new EntitiesCreateCreateRequest()
                .withProjectId("0")
                .withName(prefix + PluginConstants.XERO_ENTITY_INVOICE)
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addField(new DataFieldsDTO().withName("AmountCredited")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("AmountDue")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("AmountPaid")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("InvoiceId")
                        .withDataType(CosightDataType.STRING)
                        .withLabel(true))
                .addField(new DataFieldsDTO().withName("BrandingThemeId")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("CiSDeduction")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("CiSRate")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("CurrencyCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("CurrencyRate")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Date")
                        .withDataType(CosightDataType.DATE))
                .addField(new DataFieldsDTO().withName("DueDate")
                        .withDataType(CosightDataType.DATE))
                .addField(new DataFieldsDTO().withName("ExpectedPaymentDate")
                        .withDataType(CosightDataType.DATE))
                .addField(new DataFieldsDTO().withName("FullyPaidOnDate")
                        .withDataType(CosightDataType.DATE))
                .addField(new DataFieldsDTO().withName("HasAttachments")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("HasErrors")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("InvoiceNumber")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("IsDiscounted")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("PlannedPaymentDate")
                        .withDataType(CosightDataType.DATE))
                .addField(new DataFieldsDTO().withName("Reference")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("RepeatingInvoiceId")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("SentToContact")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("Status")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("AUTHORISED")
                                .withDataListItem("DELETED")
                                .withDataListItem("DRAFT")
                                .withDataListItem("PAID")
                                .withDataListItem("SUBMITTED")
                                .withDataListItem("VOIDED")))
                .addField(new DataFieldsDTO().withName("Subtotal")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Total")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("TotalDiscount")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("TotalTax")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Type")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("ACCPAY")
                                .withDataListItem("ACCPAYCREDIT")
                                .withDataListItem("ACCREC")
                                .withDataListItem("ACCRECCREDIT")
                                .withDataListItem("APOVERPAYMENT")
                                .withDataListItem("APPREPAYMENT")
                                .withDataListItem("AROVERPAYMENT")
                                .withDataListItem("ARPREPAYMENT")))
                .addField(new DataFieldsDTO().withName("UpdatedDateUTC")
                        .withDataType(CosightDataType.DATE_TIME))
                .addField(new DataFieldsDTO().withName("URL")
                        .withDataType(CosightDataType.STRING))
                .addIndex("InvoiceId");

        EntitiesDTO invoiceEntity = createEntity(request).orElseThrow(IllegalStateException::new);
        EntitiesDTO attachmentEntity = entityServiceWrapper.getEntityByClassname(PluginConstants.XERO_ENTITY_ATTACHMENT).getEnitiy();
        EntitiesDTO validationErr = entityServiceWrapper.getEntityByClassname(PluginConstants.XERO_ENTITY_VALIDATION_ERROR).getEnitiy();
        EntitiesDTO contactEntity = entityServiceWrapper.getEntityByClassname(PluginConstants.XERO_ENTITY_CONTACT).getEnitiy();
        EntitiesDTO lineItemEntity = entityServiceWrapper.getEntityByClassname(PluginConstants.XERO_ENTITY_LINE_ITEM).getEnitiy();
        EntitiesDTO accountEntity = entityServiceWrapper.getEntityByClassname(PluginConstants.XERO_ENTITY_ACCOUNT).getEnitiy();


        // create credit note
        // credit note is linked to From Invoice
        // Credit note links to Allocation, LineItem, Contact and Payment

        EntitiesCreateCreateRequest creditNoteRequest = new EntitiesCreateCreateRequest()
                .withProjectId("0")
                .withName(prefix + PluginConstants.XERO_ENTITY_CREDIT_NOTE)
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addField(new DataFieldsDTO().withName("CreditNoteId")
                        .withDataType(CosightDataType.DOUBLE).withLabel(true))
                .addField(new DataFieldsDTO().withName("AppliedAmount")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("CreditNoteNumber")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("BrandingThemeId")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("CiSDeduction")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("CiSRate")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("CurrencyCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("CurrencyRate")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Date")
                        .withDataType(CosightDataType.DATE))
                .addField(new DataFieldsDTO().withName("DueDate")
                        .withDataType(CosightDataType.DATE))
                .addField(new DataFieldsDTO().withName("FullyPaidOnDate")
                        .withDataType(CosightDataType.DATE))
                .addField(new DataFieldsDTO().withName("HasAttachments")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("HasErrors")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("LineAmountTypes")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Reference")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("RemainingCredit")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("SentToContact")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("Status")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("AUTHORISED")
                                .withDataListItem("DELETED")
                                .withDataListItem("DRAFT")
                                .withDataListItem("PAID")
                                .withDataListItem("SUBMITTED")
                                .withDataListItem("VOIDED")))
                .addField(new DataFieldsDTO().withName("Subtotal")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Total")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("TotalTax")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Type")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("ACCPAY")
                                .withDataListItem("ACCPAYCREDIT")
                                .withDataListItem("ACCREC")
                                .withDataListItem("ACCRECCREDIT")
                                .withDataListItem("APOVERPAYMENT")
                                .withDataListItem("APPREPAYMENT")
                                .withDataListItem("AROVERPAYMENT")
                                .withDataListItem("ARPREPAYMENT")))
                .addField(new DataFieldsDTO().withName("UpdatedDateUTC")
                        .withDataType(CosightDataType.DATE_TIME))
                .addIndex("CreditNoteId");

        EntitiesDTO creditNote = createEntity(creditNoteRequest).orElseThrow(IllegalStateException::new);


        // create allocation
        EntitiesCreateCreateRequest allocationRequest = new EntitiesCreateCreateRequest()
                .withProjectId("0")
                .withName(prefix + PluginConstants.XERO_ENTITY_ALLOCATION)
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addField(new DataFieldsDTO().withName("Status")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addField(new DataFieldsDTO().withName("Amount")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Date")
                        .withDataType(CosightDataType.DATE));

        EntitiesDTO allocationEntity = createEntity(allocationRequest).orElseThrow(IllegalStateException::new);

        // create payment
        EntitiesCreateCreateRequest paymentRequest = new EntitiesCreateCreateRequest()
                .withProjectId("0")
                .withName(prefix + PluginConstants.XERO_ENTITY_PAYMENT)
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addField(new DataFieldsDTO().withName("PaymentId")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addField(new DataFieldsDTO().withName("Amount")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Code")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("BankAccountNumber")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("BatchPaymentId")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("CreditNoteNumber")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("CurrencyRate")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Date")
                        .withDataType(CosightDataType.DATE))
                .addField(new DataFieldsDTO().withName("Details")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("HasAccount")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("HasValidationErrors")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("InvoiceNumber")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("IsReconciled")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("Particulars")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("PaymentType")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("ACCPAY")
                                .withDataListItem("ACCPAYCREDIT")
                                .withDataListItem("ACCREC")
                                .withDataListItem("ACCRECCREDIT")
                                .withDataListItem("APOVERPAYMENT")
                                .withDataListItem("APPREPAYMENT")
                                .withDataListItem("AROVERPAYMENT")
                                .withDataListItem("ARPREPAYMENT")))

                .addField(new DataFieldsDTO().withName("Reference")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Status")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("AUTHORISED")
                                .withDataListItem("DELETED")
                                .withDataListItem("DRAFT")
                                .withDataListItem("PAID")
                                .withDataListItem("SUBMITTED")
                                .withDataListItem("VOIDED")))
                .addField(new DataFieldsDTO().withName("UpdatedDateUTC")
                        .withDataType(CosightDataType.DATE_TIME));
        EntitiesDTO paymentEntity = createEntity(paymentRequest).orElseThrow(IllegalStateException::new);


        //create overpayment
        EntitiesCreateCreateRequest overpaymentRequest = new EntitiesCreateCreateRequest()
                .withProjectId("0")
                .withName(prefix + PluginConstants.XERO_ENTITY_OVERPAYMENT)
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addField(new DataFieldsDTO().withName("OverpaymentId")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addField(new DataFieldsDTO().withName("AppliedAmount")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("CurrencyCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("CurrencyRate")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Date")
                        .withDataType(CosightDataType.DATE))
                .addField(new DataFieldsDTO().withName("HasAttachments")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("LineAmountTypes")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("RemainingCredit")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Status")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("AUTHORISED")
                                .withDataListItem("DELETED")
                                .withDataListItem("DRAFT")
                                .withDataListItem("PAID")
                                .withDataListItem("SUBMITTED")
                                .withDataListItem("VOIDED")))
                .addField(new DataFieldsDTO().withName("Subtotal")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Total")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("TotalTax")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Type")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("ACCPAY")
                                .withDataListItem("ACCPAYCREDIT")
                                .withDataListItem("ACCREC")
                                .withDataListItem("ACCRECCREDIT")
                                .withDataListItem("APOVERPAYMENT")
                                .withDataListItem("APPREPAYMENT")
                                .withDataListItem("AROVERPAYMENT")
                                .withDataListItem("ARPREPAYMENT")))
                .addField(new DataFieldsDTO().withName("UpdatedDateUTC")
                        .withDataType(CosightDataType.DATE_TIME));

        EntitiesDTO overpaymentEntity = createEntity(overpaymentRequest).orElseThrow(IllegalStateException::new);


        //create prepayment
        EntitiesCreateCreateRequest prepaymentRequest = new EntitiesCreateCreateRequest()
                .withProjectId("0")
                .withName(prefix + PluginConstants.XERO_ENTITY_PREPAYMENT)
                .withEntityVisibilityType(EntityVisibilityTypes.GLOBAL)
                .addField(new DataFieldsDTO().withName("PrepaymentId").withDataType(CosightDataType.STRING).withLabel(true))
                .addField(new DataFieldsDTO().withName("AppliedAmount")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("CurrencyCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("CurrencyRate")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Date")
                        .withDataType(CosightDataType.DATE))
                .addField(new DataFieldsDTO().withName("HasAttachments")
                        .withDataType(CosightDataType.BOOLEAN))
                .addField(new DataFieldsDTO().withName("LineAmountTypes")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("Reference")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("RemainingCredit")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Status")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("AUTHORISED")
                                .withDataListItem("DELETED")
                                .withDataListItem("DRAFT")
                                .withDataListItem("PAID")
                                .withDataListItem("SUBMITTED")
                                .withDataListItem("VOIDED")))
                .addField(new DataFieldsDTO().withName("SubTotal")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Total")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("TotalTax")
                        .withDataType(CosightDataType.DOUBLE))
                .addField(new DataFieldsDTO().withName("Type")
                        .withDataType(CosightDataType.LIST)
                        .withConfig(new DatafieldConfig()
                                .withDataListItem("ACCPAY")
                                .withDataListItem("ACCPAYCREDIT")
                                .withDataListItem("ACCREC")
                                .withDataListItem("ACCRECCREDIT")
                                .withDataListItem("APOVERPAYMENT")
                                .withDataListItem("APPREPAYMENT")
                                .withDataListItem("AROVERPAYMENT")
                                .withDataListItem("ARPREPAYMENT")))
                .addField(new DataFieldsDTO().withName("UpdatedDateUTC")
                        .withDataType(CosightDataType.DATE_TIME));

        EntitiesDTO prepaymentEntity = createEntity(prepaymentRequest).orElseThrow(IllegalStateException::new);


        // link up to the attachement entity.
        RelationshipsCreateCreateRequest invoiceToAttachementRelationship =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_ATTACHMENT)
                        .withFromEntityId(invoiceEntity.getId())
                        .withToEntityId(attachmentEntity.getId())
                        .withDescription("Link between Invoice and Attachment Group");
        createRelationship(invoiceToAttachementRelationship);

        // link up to the creditNote entity.
        RelationshipsCreateCreateRequest invoiceToCreditNoteRelationship =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_CREDITNOTE)
                        .withFromEntityId(invoiceEntity.getId())
                        .withToEntityId(creditNote.getId())
                        .withDescription("Link between Invoice and Credit Note");
        createRelationship(invoiceToCreditNoteRelationship);

        RelationshipsCreateCreateRequest invoiceToContactRelationship =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_CONTACT)
                        .withFromEntityId(invoiceEntity.getId())
                        .withToEntityId(contactEntity.getId())
                        .withDescription("Link between Invoice and Contact");
        createRelationship(invoiceToContactRelationship);

        RelationshipsCreateCreateRequest invoiceToOverpaymentRelationship =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_OVERPAYMENT)
                        .withFromEntityId(invoiceEntity.getId())
                        .withToEntityId(overpaymentEntity.getId())
                        .withDescription("Link between Invoice and Overpayment");
        createRelationship(invoiceToOverpaymentRelationship);

        RelationshipsCreateCreateRequest invoiceToOverpaymentValidationError =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_VALIDATION_ERROR)
                        .withFromEntityId(invoiceEntity.getId())
                        .withToEntityId(validationErr.getId())
                        .withDescription("Link between Invoice and ValidationError");
        createRelationship(invoiceToOverpaymentValidationError);

        RelationshipsCreateCreateRequest invoiceToPrepayment =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_PREPAYMENT)
                        .withFromEntityId(invoiceEntity.getId())
                        .withToEntityId(prepaymentEntity.getId())
                        .withDescription("Link between Invoice and Prepayment");
        createRelationship(invoiceToPrepayment);

        RelationshipsCreateCreateRequest invoiceToPayment =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_PAYMENT)
                        .withFromEntityId(invoiceEntity.getId())
                        .withToEntityId(paymentEntity.getId())
                        .withDescription("Link between Invoice and Payment");
        createRelationship(invoiceToPayment);

        RelationshipsCreateCreateRequest invoiceToLineItem =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_LINEITEM)
                        .withFromEntityId(invoiceEntity.getId())
                        .withToEntityId(lineItemEntity.getId())
                        .withDescription("Link between Invoice and LineItem");
        createRelationship(invoiceToLineItem);

        // now lets do second level stuff that hands off invoice

        // Credit note

        RelationshipsCreateCreateRequest creditNoteToPayment =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CREDIT_NOTE_TO_PAYMENT)
                        .withFromEntityId(creditNote.getId())
                        .withToEntityId(paymentEntity.getId())
                        .withDescription("Link between CreditNote and Payment");
        createRelationship(creditNoteToPayment);

        RelationshipsCreateCreateRequest creditNoteToLineItem =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CREDIT_NOTE_TO_LINEITEM)
                        .withFromEntityId(creditNote.getId())
                        .withToEntityId(lineItemEntity.getId())
                        .withDescription("Link between CreditNote and LineItem");
        createRelationship(creditNoteToLineItem);

        RelationshipsCreateCreateRequest creditNoteToContact =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CREDIT_NOTE_TO_CONTACT)
                        .withFromEntityId(creditNote.getId())
                        .withToEntityId(contactEntity.getId())
                        .withDescription("Link between CreditNote and Contact");
        createRelationship(creditNoteToContact);

        RelationshipsCreateCreateRequest creditNoteToAttachment =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CREDIT_NOTE_TO_ATTACHMENT)
                        .withFromEntityId(creditNote.getId())
                        .withToEntityId(attachmentEntity.getId())
                        .withDescription("Link between CreditNote and Attachment");
        createRelationship(creditNoteToAttachment);

        RelationshipsCreateCreateRequest creditNoteToAllocation =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CREDIT_NOTE_TO_ALLOCATION)
                        .withFromEntityId(creditNote.getId())
                        .withToEntityId(allocationEntity.getId())
                        .withDescription("Link between CreditNote and Allocation");
        createRelationship(creditNoteToAllocation);

        RelationshipsCreateCreateRequest paymentToContact =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_PAYMENT_TO_CONTACT)
                        .withFromEntityId(paymentEntity.getId())
                        .withToEntityId(contactEntity.getId())
                        .withDescription("Link between Payment and Contact");
        createRelationship(paymentToContact);

        RelationshipsCreateCreateRequest paymentToAttchment =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_PAYMENT_TO_ATTACHMENT)
                        .withFromEntityId(paymentEntity.getId())
                        .withToEntityId(attachmentEntity.getId())
                        .withDescription("Link between Payment and Attachment");
        createRelationship(paymentToAttchment);

        RelationshipsCreateCreateRequest paymentToAccount =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_PAYMENT_TO_ACCOUNT)
                        .withFromEntityId(paymentEntity.getId())
                        .withToEntityId(accountEntity.getId())
                        .withDescription("Link between Payment and Account");
        createRelationship(paymentToAccount);

        RelationshipsCreateCreateRequest paymentToAllocation =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_PAYMENT_TO_ALLOCATION)
                        .withFromEntityId(paymentEntity.getId())
                        .withToEntityId(allocationEntity.getId())
                        .withDescription("Link between Payment and Allocation");
        createRelationship(paymentToAllocation);

        RelationshipsCreateCreateRequest paymentToLineItem =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_PAYMENT_TO_LINEITEM)
                        .withFromEntityId(paymentEntity.getId())
                        .withToEntityId(lineItemEntity.getId())
                        .withDescription("Link between Payment and LineItem");
        createRelationship(paymentToLineItem);

        // Prepayment relationships
        RelationshipsCreateCreateRequest prepaymentToLineItem =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_PREPAYMENT_TO_LINEITEM)
                        .withFromEntityId(prepaymentEntity.getId())
                        .withToEntityId(lineItemEntity.getId())
                        .withDescription("Link between Preayment and LineItem");
        createRelationship(prepaymentToLineItem);

        RelationshipsCreateCreateRequest prepaymentToAttachment =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_PREPAYMENT_TO_ATTACHMENT)
                        .withFromEntityId(prepaymentEntity.getId())
                        .withToEntityId(attachmentEntity.getId())
                        .withDescription("Link between Preayment and Attachment");
        createRelationship(prepaymentToAttachment);

        RelationshipsCreateCreateRequest prepaymentToContact =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_PREPAYMENT_TO_CONTACT)
                        .withFromEntityId(prepaymentEntity.getId())
                        .withToEntityId(contactEntity.getId())
                        .withDescription("Link between Preayment and Contact");
        createRelationship(prepaymentToContact);

        RelationshipsCreateCreateRequest prepaymentToPayment =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_PREPAYMENT_TO_PAYMENT)
                        .withFromEntityId(prepaymentEntity.getId())
                        .withToEntityId(paymentEntity.getId())
                        .withDescription("Link between Preayment and Payment");
        createRelationship(prepaymentToPayment);

        RelationshipsCreateCreateRequest prepaymentToAllocation =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_PREPAYMENT_TO_ALLOCATION)
                        .withFromEntityId(prepaymentEntity.getId())
                        .withToEntityId(allocationEntity.getId())
                        .withDescription("Link between Preayment and Payment");
        createRelationship(prepaymentToAllocation);

        // Overpayment relationships

        RelationshipsCreateCreateRequest overpaymentToAllocation =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_OVERPAYMENT_TO_ALLOCATION)
                        .withFromEntityId(overpaymentEntity.getId())
                        .withToEntityId(allocationEntity.getId())
                        .withDescription("Link between overpayment and Allocation");
        createRelationship(overpaymentToAllocation);

        RelationshipsCreateCreateRequest overpaymentToAttachment =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_OVERPAYMENT_TO_ATTACHMENT)
                        .withFromEntityId(overpaymentEntity.getId())
                        .withToEntityId(attachmentEntity.getId())
                        .withDescription("Link between overpayment and Attachment");
        createRelationship(overpaymentToAttachment);

        RelationshipsCreateCreateRequest overpaymentToLineItem =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_OVERPAYMENT_TO_LINEITEM)
                        .withFromEntityId(overpaymentEntity.getId())
                        .withToEntityId(lineItemEntity.getId())
                        .withDescription("Link between overpayment and LineItem");
        createRelationship(overpaymentToLineItem);

        RelationshipsCreateCreateRequest overpaymentToPayment =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_OVERPAYMENT_TO_PAYMENT)
                        .withFromEntityId(overpaymentEntity.getId())
                        .withToEntityId(paymentEntity.getId())
                        .withDescription("Link between overpayment and Payment");
        createRelationship(overpaymentToPayment);

        RelationshipsCreateCreateRequest overpaymentToContact =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_OVERPAYMENT_TO_CONTACT)
                        .withFromEntityId(overpaymentEntity.getId())
                        .withToEntityId(contactEntity.getId())
                        .withDescription("Link between overpayment and Contact");
        createRelationship(overpaymentToContact);

    }

    public void createContactClass(String prefix) {
        entityServiceWrapper.auth();
        relationshipServiceWrapper.auth();
        EntitiesCreateCreateRequest request = new EntitiesCreateCreateRequest()
                .withProjectId("0")
                .withName(prefix + PluginConstants.XERO_ENTITY_CONTACT)
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
        EntitiesDTO contact = createEntity(request).orElseThrow(IllegalStateException::new);

        //
        // now we need to create the following and link
        // ContactGroup
        EntitiesCreateCreateRequest contactGroupRequest = new EntitiesCreateCreateRequest().withName(prefix + PluginConstants.XERO_ENTITY_CONTACT_GROUP)
                .withProjectId("0")
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
        EntitiesDTO contactGroup = createEntity(contactGroupRequest).orElseThrow(IllegalStateException::new);

        RelationshipsCreateCreateRequest contactToContactGroup =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_CONTACT_GROUP)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(contactGroup.getId())
                        .withDescription("Link between Contact and Contact Group");

        createRelationship(contactToContactGroup);


        // Address
        EntitiesCreateCreateRequest addressRequest = new EntitiesCreateCreateRequest().withName(prefix + PluginConstants.XERO_ENTITY_ADDRESS)
                .withProjectId("0")
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
                .addIndex("AddressLine1");
        EntitiesDTO address = createEntity(addressRequest).orElseThrow(IllegalStateException::new);

        RelationshipsCreateCreateRequest contactToAddress =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_ADDRESS)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(address.getId())
                        .withDescription("Link between Contact and Address");

        createRelationship(contactToAddress);


        // Attachment
        EntitiesCreateCreateRequest attachmentRequest = new EntitiesCreateCreateRequest().withName(prefix + PluginConstants.XERO_ENTITY_ATTACHMENT)
                .withProjectId("0")
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
        EntitiesDTO attachment = createEntity(attachmentRequest).orElseThrow(IllegalStateException::new);

        RelationshipsCreateCreateRequest contactToAttachment =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_ATTACHMENT)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(attachment.getId())
                        .withDescription("Link between Contact and Contact Group");

        createRelationship(contactToAttachment);

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
        EntitiesCreateCreateRequest batchpaymentDetailsRequest = new EntitiesCreateCreateRequest().withName(prefix + PluginConstants.XERO_ENTITY_BATCH_PAYMENT_DETAILS)
                .withProjectId("0")
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

        EntitiesDTO batchPaymentDetails = createEntity(batchpaymentDetailsRequest).orElseThrow(IllegalStateException::new);

        RelationshipsCreateCreateRequest contactTobatchPaymentDetails =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_BATCH_PAYMENT_DETAILS)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(batchPaymentDetails.getId())
                        .withDescription("Link between Contact and Contact Group");

        createRelationship(contactTobatchPaymentDetails);

        // BrandingTheme? - nope

        // ContactPerson
        EntitiesCreateCreateRequest contactPersonRequest = new EntitiesCreateCreateRequest().withName(prefix + PluginConstants.XERO_ENTITY_CONTACT_PERSON)
                .withProjectId("0")
                .addField(new DataFieldsDTO().withName("FirstName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("EmailAddress")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addField(new DataFieldsDTO().withName("LastName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("IncludeInEmails")
                        .withDataType(CosightDataType.BOOLEAN))
                .addIndex("EmailAddress");
        EntitiesDTO contactPerson = createEntity(contactPersonRequest).orElseThrow(IllegalStateException::new);


        RelationshipsCreateCreateRequest contactTocontactPerson =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_CONTACT_PERSON)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(contactPerson.getId())
                        .withDescription("Link between Contact and Contact Group");

        createRelationship(contactTocontactPerson);


        // Phone
        EntitiesCreateCreateRequest phoneRequest = new EntitiesCreateCreateRequest().withName(prefix + PluginConstants.XERO_ENTITY_PHONE)
                .withProjectId("0")
                .addField(new DataFieldsDTO().withName("PhoneAreaCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("PhoneNumber")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addField(new DataFieldsDTO().withName("PhoneCountryCode")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("PhoneType")
                        .withDataType(CosightDataType.STRING))
                .addIndex("PhoneNumber").addIndex("PhoneAreaCode");

        EntitiesDTO phone = createEntity(phoneRequest).orElseThrow(IllegalStateException::new);

        RelationshipsCreateCreateRequest contactTophone =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_PHONE)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(phone.getId())
                        .withDescription("Link between Contact and Contact Group");

        createRelationship(contactTophone);

        // SalesTrackingCategory
        EntitiesCreateCreateRequest salesTrackingCatRequest = new EntitiesCreateCreateRequest().withName(prefix + PluginConstants.XERO_ENTITY_SALES_TRACKING_CATEGORY)
                .withProjectId("0")
                .addField(new DataFieldsDTO().withName("TrackingOptionName")
                        .withDataType(CosightDataType.STRING))
                .addField(new DataFieldsDTO().withName("TrackingCategoryName")
                        .withDataType(CosightDataType.STRING).withLabel(true))
                .addIndex("TrackingOptionName");
        EntitiesDTO salesTrackingCategory = createEntity(salesTrackingCatRequest).orElseThrow(IllegalStateException::new);


        RelationshipsCreateCreateRequest contactTosalesTrackingCategory =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_SALES_TRACKING_CATEGORY)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(salesTrackingCategory.getId())
                        .withDescription("Link between Contact and Contact Group");

        createRelationship(contactTosalesTrackingCategory);


        // ValidationError
        EntitiesCreateCreateRequest validationErrorReq = new EntitiesCreateCreateRequest().withName(prefix + PluginConstants.XERO_ENTITY_VALIDATION_ERROR)
                .withProjectId("0")
                .addField(new DataFieldsDTO().withName("Message")
                        .withDataType(CosightDataType.STRING).withLabel(true));
        EntitiesDTO validationError = createEntity(validationErrorReq)
                .orElseThrow(IllegalStateException::new);


        RelationshipsCreateCreateRequest contactTovalidationError =
                new RelationshipsCreateCreateRequest().withName(PluginConstants.XERO_RELATIONSHIP_CONTACT_TO_VALIDATION_ERROR)
                        .withFromEntityId(contact.getId())
                        .withToEntityId(validationError.getId())
                        .withDescription("Link between Contact and Contact Group");

        createRelationship(contactTovalidationError);
    }
}
