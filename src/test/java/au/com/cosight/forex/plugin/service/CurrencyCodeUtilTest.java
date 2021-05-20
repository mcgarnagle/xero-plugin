package au.com.cosight.forex.plugin.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class CurrencyCodeUtilTest {
    private static Logger logger = LoggerFactory.getLogger(CurrencyCodeUtilTest.class);

    @Test
    void convert(){
        List<String> currencyCodes = new ArrayList<>();
        currencyCodes.add("USDAUD");
        currencyCodes.add("USDCAD");
        currencyCodes.add("USDLKR");
        Map<String, List<String>>  map = CurrencyCodeUtil.covert(currencyCodes);
        logger.info("{}",map);
        Assert.assertEquals(1,map.keySet().size());
        Assert.assertNotNull(map.get("USD"));
        Assert.assertEquals(3,map.get("USD").size());

    }
    @Test
    void convertMultipl(){
        List<String> currencyCodes = new ArrayList<>();
        currencyCodes.add("USDAUD");
        currencyCodes.add("USDCAD");
        currencyCodes.add("AUDLKR");


        Map<String, List<String>>  map = CurrencyCodeUtil.covert(currencyCodes);
        logger.info("{}",map);
        Assert.assertEquals(2,map.keySet().size());
        Assert.assertNotNull(map.get("USD"));
        Assert.assertEquals(2,map.get("USD").size());
        Assert.assertNotNull(map.get("AUD"));
        Assert.assertEquals(1,map.get("AUD").size());
    }
}
