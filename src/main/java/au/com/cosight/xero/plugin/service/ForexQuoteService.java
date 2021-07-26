package au.com.cosight.xero.plugin.service;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.sdk.plugin.runtime.CosightRuntimeFieldMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ForexQuoteService {

    List<EntityInstance> updateQuote(CosightRuntimeFieldMap fieldMap, List<String> currencies);
}
