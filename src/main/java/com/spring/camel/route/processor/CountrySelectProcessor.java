package com.spring.camel.route.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class CountrySelectProcessor implements Processor{

    List<String> countryList = Arrays.asList("india", "usa", "gb", "japan", "kenya", "saudi arabia", "china", "singapore", "thailand", "indonesia");
    @Override
    public void process(Exchange exchange) throws Exception {
        Random random = new Random();
        String countryName = countryList.get(random.nextInt(countryList.size() -1 ));

        log.info("Selected country name is : " + countryName);

        //set the header of exchange object.
        exchange.getIn().setHeader("countryName", countryName);
    }
}
