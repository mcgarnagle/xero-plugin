package au.com.cosight.xero.plugin.service;

import au.com.cosight.xero.plugin.service.dto.ForexQuoteDTO;

import java.util.List;

public interface CurrencylayerApiService {
    List<ForexQuoteDTO> quotes(final String source, List<String> currencyCode);
}
