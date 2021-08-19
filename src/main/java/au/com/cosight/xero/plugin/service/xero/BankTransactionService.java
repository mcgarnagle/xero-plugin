package au.com.cosight.xero.plugin.service.xero;

import com.xero.models.accounting.BankTransaction;

public interface BankTransactionService {
    void upsertBankTransaction(BankTransaction bankTransaction);
}
