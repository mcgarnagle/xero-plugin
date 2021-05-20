package au.com.cosight.forex.plugin.service;

import au.com.cosight.forex.plugin.service.dto.ForexQuoteDTO;

import java.util.List;

public interface CurrencylayerApiService {
    List<ForexQuoteDTO> quotes(final String source, List<String> currencyCode);
}
