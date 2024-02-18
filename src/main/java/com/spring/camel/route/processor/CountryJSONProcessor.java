package com.spring.camel.route.processor;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spring.camel.route.domain.Country;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CountryJSONProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Inside CountryJSONProcessor==");
        String countryJSON = (String) exchange.getIn().getBody();
        log.info(" Country in CountryJSONProcessor is : " + countryJSON);

        ////no filtering the JSON ARRAY to JSON OBject

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(countryJSON);

        for (int i = 0; i < jsonArray.size(); i++)
        {
            JsonObject jsonObj = (JsonObject) jsonArray.get(i);

            log.info("SINGLE JSON OBJECT IS : " + jsonObj.toString());

            //added back to the Exchange Object body. NOTE: since by default the body is the JSONArray containing 1 JSON object and hence this was failing at UnMarshall State. To resolve this added a separate layer of processor 'CountryJSONProcessor' to get the first JSON Object from the JSON Array and assign it as JSONString back to the Exchange Object body. This is in turn replace the current body of Exchange Object which was earlier having JSONArray of single JSONObjects.
            exchange.getIn().setBody(jsonObj.toString());
        }

        log.info("Inside COuntryJSONProcessor-END==");
  }
}
