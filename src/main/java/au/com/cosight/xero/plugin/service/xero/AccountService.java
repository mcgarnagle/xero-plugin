package au.com.cosight.xero.plugin.service.xero;

import com.xero.models.accounting.Account;

public interface AccountService {
    void upsertAccount(Account account);
}
