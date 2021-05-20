package au.com.cosight.forex.plugin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyCodeUtil {

    public static Map<String, List<String>> covert(List<String> currencyCodes){

        final Map<String, List<String>> map = new HashMap<>();
        currencyCodes.forEach( code ->{

            String source =  code.substring(0,3);
            String currency = code.substring(3,6);
            List<String> list = new ArrayList<>();
            if (map.containsKey(source)) {
                list = map.get(source);
            }
            list.add(currency);
            map.put(source,list);
        });
        return map;
    }

}
