package au.com.cosight.xero.plugin.service.xero;

import com.xero.models.accounting.Contact;

public interface ContactService {
    void upsertContact(Contact contact);
}
