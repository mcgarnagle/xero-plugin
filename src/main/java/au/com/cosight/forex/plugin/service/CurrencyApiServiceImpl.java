package au.com.cosight.forex.plugin.service;

import au.com.cosight.forex.plugin.service.dto.ForexQuoteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurrencyApiServiceImpl implements CurrencylayerApiService {

    @Value("${api.key:}")
    private String apiKey;


    @Override
    public List<ForexQuoteDTO> quotes(final String source, List<String> currencyCode){
        RestTemplate template = new RestTemplate();
        if (currencyCode == null || currencyCode.size() == 0){
            throw new IllegalStateException("need provide a currency pair");
        }
        String uri = "http://api.currencylayer.com/live";

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("access_key",apiKey)
                .queryParam("format","1");

            builder.queryParam("source",source);
            builder.queryParam("currencies",currencyCode.stream().collect(Collectors.joining(",")));

        try {
            ResponseEntity<RawResult> res = template.getForEntity(builder.build().toUri(), RawResult.class);
            if (res.getBody() == null) {
                return Collections.emptyList();
            }
            final List<ForexQuoteDTO> result = new ArrayList<>();
            res.getBody().getQuotes().forEach( (k,v) ->{
                if (!source.equals(k)) {
                    ForexQuoteDTO quoteDTO = new ForexQuoteDTO();
                    quoteDTO.setCurrencyCode(k);
                    quoteDTO.setRate(v);
                    Date date = new Date();

                    quoteDTO.setDateTime(new Date(res.getBody().getTimestamp()* 1000));
                    result.add(quoteDTO);
                }
            });
            return result;
        }catch (HttpClientErrorException e){
            throw new IllegalStateException(e);
        }
    }
}
