package com.spring.camel.route.routes;

import com.spring.camel.route.domain.Country;
import com.spring.camel.route.processor.BuildSQLProcessor;
import com.spring.camel.route.processor.CountryJSONProcessor;
import com.spring.camel.route.processor.CountrySelectProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.converter.stream.InputStreamCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * THis Route is the Route2 as per the application overview. Which is basically defining another route using camel rest dsl library camel-rest to accept the payload from Route1.
 *
 * REF:
 * 1. https://camel.apache.org/manual/rest-dsl.html
 * 2. https://camel.apache.org/components/4.0.x/rest-component.html
 */
@Component
@Slf4j
public class CountryRESTRoute extends RouteBuilder {

    @Autowired
    private BuildSQLProcessor buildSQLProcessor;

    @Autowired
    private CountryJSONProcessor countryJSONProcessor;

    @Override
    public void configure() throws Exception {
        GsonDataFormat countryFormat = new GsonDataFormat(Country.class);

    //        restConfiguration().component("jetty");
    //        restConfiguration().setApiComponent("jetty");
    //        restConfiguration().setApiHost("localhost");
    //        restConfiguration().setPort("9090");
    //        restConfiguration().setApiContextPath("/api");

    from("jetty:http://localhost:9090/country?httpMethodRestrict=POST")
        .routeId("CountryPostRoute")
        .log("Received Body in CountryRESTRoute is : ${body}")
        .convertBodyTo(String.class) // converting body to string where we see the output is JSON String.
        .process(countryJSONProcessor) //since by default the Exchange body is the JSONArray containing 1 JSON object and hence this was failing at UnMarshall State as Unmarshall only requires a JSONObject as string instead of JSONArray with objects. To resolve this added a separate layer of processor 'CountryJSONProcessor' to get the first JSON Object from the JSON Array and assign it as JSONString back to the Exchange Object body. This is in turn replace the current body of Exchange Object which was earlier having JSONArray of single JSONObjects.
        .unmarshal(countryFormat) // to convert the above JSON String to JAVA Object.
        //                .unmarshal(new JacksonDataFormat(Country.class))
        .log("Unmarshalled record is ${body}")
        .process(buildSQLProcessor)
        .to("{{toRoute2}}") // this is the route where we are persisting the data.
        .to(
            "sql:select * from country where country_code= :#CountryCode?dataSource=#dataSource") // here it is to print the data post data saved into DB. with :#skuID means to take the skuID from the Exchange Header which is set into class BuildSQLProcessor. Post skuID we required to pass the dataSource.
        .log("Read item from db is ${body}"); // finally logging/printing the select query

        log.info("Route Creation for CountryRESTRoute is COMPLETE !!!!!");
    }
}
