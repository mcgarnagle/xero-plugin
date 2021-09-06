package au.com.cosight.xero.plugin.service.xero;

import au.com.cosight.common.dto.plugin.CosightExecutionContext;
import au.com.cosight.common.dto.plugin.helper.EntityServiceWrapper;
import au.com.cosight.common.dto.plugin.helper.RelationshipServiceWrapper;
import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.xero.plugin.PluginConstants;
import au.com.cosight.xero.plugin.mapper.*;
import au.com.cosight.xero.plugin.structure.GraphStructure;
import com.xero.models.accounting.Contact;
import com.xero.models.accounting.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static Logger logger = LoggerFactory.getLogger("xero-plugin:InvocieServiceImpl");
    private RelationshipServiceWrapper relationshipServiceWrapper;
    private EntityServiceWrapper entityServiceWrapper;
    private CosightExecutionContext cosightExecutionContext;

    public InvoiceServiceImpl(RelationshipServiceWrapper relationshipServiceWrapper,
                              EntityServiceWrapper entityServiceWrapper,
                              CosightExecutionContext cosightExecutionContext) {
        this.relationshipServiceWrapper = relationshipServiceWrapper;
        this.entityServiceWrapper = entityServiceWrapper;
        this.cosightExecutionContext = cosightExecutionContext;
    }

    @Override
    public void upsertInvoice(Invoice invoice) {
        GraphStructure structure = new GraphStructure();
        // get the flat invoice
        EntityInstance invoiceInstance = InvoiceMapper.toEntityInstance(invoice);
        invoiceInstance = entityServiceWrapper.save(invoiceInstance);
        structure.addEntity(invoiceInstance);

        // get the things that hang off it
        EntityInstance finalInvoiceInstance = invoiceInstance;
        invoice.getOverpayments().forEach(overpayment -> {
            // get the overpayment
            // save the overpayment
            // create relationship with the overpayment
            EntityInstance overpaymentInstance = OverpaymentMapper.toInstance(overpayment);
            overpaymentInstance = entityServiceWrapper.save(overpaymentInstance);
            structure.addEntity(overpaymentInstance);
            structure.addLink(finalInvoiceInstance, overpaymentInstance, PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_OVERPAYMENT);
        });

        invoice.getPayments().forEach(payment -> {
            EntityInstance paymentInstance = PaymentMapper.toInstance(payment);
            paymentInstance = entityServiceWrapper.save(paymentInstance);
            structure.addEntity(paymentInstance);
            structure.addLink(finalInvoiceInstance, paymentInstance, PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_PAYMENT);
        });

        invoice.getPrepayments().forEach(prepayment -> {
            EntityInstance prepaymentInstance = PrepaymentMapper.toInstance(prepayment);
            prepaymentInstance = entityServiceWrapper.save(prepaymentInstance);
            structure.addEntity(prepaymentInstance);
            structure.addLink(finalInvoiceInstance, prepaymentInstance, PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_PREPAYMENT);
        });

        invoice.getCreditNotes().forEach(creditNote -> {
            EntityInstance creditNoteInstance = CreditNoteMapper.toInstance(creditNote);
            creditNoteInstance = entityServiceWrapper.save(creditNoteInstance);
            structure.addEntity(creditNoteInstance);
            structure.addLink(finalInvoiceInstance, creditNoteInstance, PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_CREDITNOTE);

        });

        invoice.getAttachments().forEach(attachment -> {
            EntityInstance attachmentInstance = AttachmentMapper.toInstance(attachment);
            attachmentInstance = entityServiceWrapper.save(attachmentInstance);
            structure.addEntity(attachmentInstance);
            structure.addLink(finalInvoiceInstance, attachmentInstance, PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_ATTACHMENT);

        });

        invoice.getLineItems().forEach(lineItem -> {
            EntityInstance lineItemInstance = LineItemMapper.toInstance(lineItem);
            lineItemInstance = entityServiceWrapper.save(lineItemInstance);
            structure.addEntity(lineItemInstance);
            structure.addLink(finalInvoiceInstance, lineItemInstance, PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_LINEITEM);

        });


        Contact theContact = invoice.getContact();
        EntityInstance theContactInstance = ContactMapper.toEntityInstance(theContact);
        theContactInstance = entityServiceWrapper.save(theContactInstance);
        structure.addEntity(theContactInstance);
        structure.addLink(finalInvoiceInstance, theContactInstance, PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_CONTACT);

        invoice.getValidationErrors().forEach(validationError -> {
            EntityInstance validationErrorInstance = ValidationErrorMapper.toInstance(validationError);
            validationErrorInstance = entityServiceWrapper.save(validationErrorInstance);
            structure.addEntity(validationErrorInstance);
            structure.addLink(finalInvoiceInstance, validationErrorInstance, PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_VALIDATION_ERROR);


        });
        invoice.getWarnings().forEach(validationErrorWarning -> {
            EntityInstance validationWarning = ValidationErrorMapper.toInstance(validationErrorWarning);
            validationWarning = entityServiceWrapper.save(validationWarning);
            structure.addEntity(validationWarning);
            structure.addLink(finalInvoiceInstance, validationWarning, PluginConstants.XERO_RELATIONSHIP_INVOICE_TO_VALIDATION_WARNING);
        });

        // now do mappings from other objects that have relations...
        // this is phase two
        // Allocation


        // Overpayment
        invoice.getOverpayments().forEach(overpayment -> {
            overpayment.getAttachments().forEach(attachment -> {
                // link overpayment to attachment
//                xx = structure.getEntityInstances().stream().filter(x -> x.getInstanceValues().)
            });
            overpayment.getAllocations().forEach(allocation -> {
                // link allocation to attatchment

            });
            overpayment.getPayments().forEach(payment -> {
                // link payment to attatchment

            });
            overpayment.getLineItems().forEach(lineItem -> {
                // link lineitem to attachment
            });
        });
        // payments
        invoice.getPayments().forEach(payment -> {
            // single links
        });

        // prepayments
        invoice.getPrepayments().forEach(prepayment -> {
            prepayment.getAllocations().forEach(allocation -> {

            });
            prepayment.getAttachments().forEach(attachment -> {

            });
            prepayment.getLineItems().forEach(lineItem -> {

            });
            prepayment.getPayments().forEach(payment -> {

            });
        });

        // Creditnote
        invoice.getCreditNotes().forEach(creditNote -> {
            creditNote.getAllocations().forEach(allocation -> {

            });
            creditNote.getPayments().forEach(payment -> {

            });
            creditNote.getLineItems().forEach(lineItem -> {

            });
            creditNote.getValidationErrors().forEach(validationError -> {

            });
        });

        // LineItem
        invoice.getLineItems().forEach(lineItem -> {
            lineItem.getTracking().forEach(lineItemTracking -> {
                // save line item tracking

            });
        });

        structure.getLinks().forEach(instanceLink -> {
            relationshipServiceWrapper.saveRelationshipInstance(instanceLink.getRelationshipDTO());
        });

    }

    private void saveInstanceAndLinkFromParent(GraphStructure structure, EntityInstance parentEntity, EntityInstance instanceToSave, String linkName) {
        instanceToSave = entityServiceWrapper.save(instanceToSave);
        structure.addEntity(instanceToSave);
        structure.addLink(parentEntity, instanceToSave, linkName);

    }
}
