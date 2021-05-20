package au.com.cosight.forex.plugin.service;

import au.com.cosight.entity.domain.EntityInstance;
import au.com.cosight.forex.plugin.service.dto.ForexQuoteDTO;
import au.com.cosight.sdk.plugin.runtime.CosightRuntimeFieldMap;

import au.com.cosight.sdk.plugin.runtime.helper.EntityServiceWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ForexQuoteServiceImpl implements ForexQuoteService {

    private final CurrencylayerApiService currencylayerApiService;
    private final EntityServiceWrapper entityServiceWrapper;


    public ForexQuoteServiceImpl(CurrencylayerApiService currencylayerApiService, EntityServiceWrapper entityServiceWrapper) {
        this.currencylayerApiService = currencylayerApiService;
        this.entityServiceWrapper = entityServiceWrapper;
    }

    @Override
    public List<EntityInstance> updateQuote(final CosightRuntimeFieldMap fieldMap, List<String> currencies) {


         final Map<String,List<String>> sourceListMap = CurrencyCodeUtil.covert(currencies);
         List<ForexQuoteDTO> result = sourceListMap.entrySet().parallelStream()
                 .flatMap(e -> currencylayerApiService.quotes(e.getKey(),e.getValue()).stream()).collect(Collectors.toList());
        final List<EntityInstance> instances = new ArrayList<>();
        result.forEach( f -> instances.add(entityServiceWrapper.save(fieldMap,f)));
        return instances;
    }
}
