package au.com.cosight.xero.plugin.service.xero;

import au.com.cosight.sdk.plugin.runtime.CosightExecutionContext;
import au.com.cosight.sdk.plugin.runtime.helper.EntityServiceWrapper;
import au.com.cosight.sdk.plugin.runtime.helper.RelationshipServiceWrapper;
import com.xero.models.accounting.Invoice;
import com.xero.models.accounting.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void upsertInvoice(Invoice invoice) {
        invoice.getPayments().get().get


    }
}
