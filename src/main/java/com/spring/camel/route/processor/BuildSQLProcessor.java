package com.spring.camel.route.processor;

import com.spring.camel.route.domain.Country;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BuildSQLProcessor implements org.apache.camel.Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        //first step is that we are going to get the Item record using the Exchange object.
        Country country = (Country) exchange.getIn().getBody();
        log.info(" Country in Processor is : " + country);
        //
        String name = country.getCommonName();
        log.info(" Name in Processor is : " + name);

        StringBuilder query = new StringBuilder();
        query.append("INSERT into country (name, country_code, population) VALUES('");
            query.append(name+ "','" + country.getCcn3() + "'," + country.getPopulation() + ")" );

        //
        log.info("Final Query is : " + query) ;

        //sets the exchange body with the query built.
        exchange.getIn().setBody(query.toString()); //so what we need to do is set the query and pass it to the JDBC object.
        exchange.getIn().setHeader("CountryCode", country.getCcn3()); ///this is added to print the data using sql query post data updated into the db
    }
}
