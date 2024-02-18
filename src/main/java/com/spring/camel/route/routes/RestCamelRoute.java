package com.spring.camel.route.routes;

import com.spring.camel.route.alert.MailProcessor;
import com.spring.camel.route.exception.DataException;

import javax.sql.DataSource;

import com.spring.camel.route.processor.CountrySelectProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RestCamelRoute extends RouteBuilder {

    @Autowired
    private Environment environment;

    //wiring this dataSource bean in order to connect to the database, for which all config parameters defined in application.yaml file.
    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Autowired
    private CountrySelectProcessor countrySelectProcessor;

    @Autowired
    private MailProcessor mailProcessor; //Bean auto-wired for sending exception events to the mail as per application overview architecture


    @Override
    public void configure() throws Exception {
//        to add logging into the current code using lombok
        log.info("Starting the Camel Route");

        //
        onException(PSQLException.class).log(LoggingLevel.ERROR, "PSQLException in the route ${body}")
                .maximumRedeliveries(3).redeliveryDelay(3000).backOffMultiplier(2).retryAttemptedLogLevel(LoggingLevel.ERROR);

        //What happens below is that if there is a DataException or RuntimeException or both, then log that error. In addition to that send that event as
        //an email using the 'mailProcessor'.
        //Thus under the scenario during the data-exception what it is going to do. It is going to the mailProcessor
        //and in the mailProcessor the process method is going to get invoked, where we have mailSender completely loaded 
         //with the mail configuration using which to trigger mail to the sender with all exception/error events info.
         onException(DataException.class, RuntimeException.class).log(LoggingLevel.ERROR, "Data Exception in the route ${body}").process(mailProcessor);

        from("{{fromRoute1}}").routeId("mainRoute") ///adding routeId for the APP.
                .log("Current environment is - " + environment.getProperty("message") )
                .process(countrySelectProcessor) //so every 10sec it is going to call the process method inside class CountrySelectProcessor which is going to populate the countryName in the Exchange Header
 //                .to("{{fromRoute2}}/india").convertBodyTo(String.class)
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.HTTP_URI, simple("{{fromRoute2}}/${header.countryName}")) //this is actually overriding the below to URI
                .to("{{fromRoute2}}/india").convertBodyTo(String.class)
                        .log("The REST Countries API response is ${body}")
                .removeHeader(Exchange.HTTP_URI)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .convertBodyTo(String.class)
                .to("{{toRESTRoute}}")
        .to("{{toRoute}}");
        log.info("Ending the Camel Route");

    }
}
