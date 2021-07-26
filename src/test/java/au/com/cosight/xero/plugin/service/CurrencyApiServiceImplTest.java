package au.com.cosight.xero.plugin.service;

import au.com.cosight.xero.plugin.service.dto.ForexQuoteDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CurrencyApiServiceImplTest {

    @Autowired
    private CurrencylayerApiService currencylayerApiService;

    @Test
    void quotes() {
        List<String> currencyCode = new ArrayList<>();
        currencyCode.add("AUD");
        currencyCode.add("NZD");
        currencyCode.add("LKR");

        List<ForexQuoteDTO> result = currencylayerApiService.quotes("USD",currencyCode);
        Assert.assertEquals(3,result.size());
        Assert.assertEquals("USDAUD",result.get(0).getCurrencyCode());
        System.out.println(result);

    }
}
