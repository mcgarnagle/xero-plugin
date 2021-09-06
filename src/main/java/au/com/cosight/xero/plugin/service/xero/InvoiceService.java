package au.com.cosight.xero.plugin.service.xero;

import com.xero.models.accounting.Invoice;

public interface InvoiceService {
    void upsertInvoice(Invoice invoice);
}
