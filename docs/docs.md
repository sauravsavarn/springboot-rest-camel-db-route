# Getting Started

### Application Overview
![application-overview.png](assets%2Fimages%2Fapplication-overview.png)

* As per the above application-overview, we are going to build 2 Routes, Route 1 & Route 2.
* Route 1 is going to start with a Timer, where it is going to poll Countries REST API every 10 seconds. 
* The countries API is the public API, anyone with the URL can access it.  
* So everytime as per the application-overview it's going to call the API, we are going to pass a different country code and get the response for that country code. 
* Once we get the response basically then we make the post call to the Route 2.  The second route is going to get the response and that 2nd endpoint takes care of persisting that data to the database.
* Endpoint Route2 is created using camel-restlet library and it is going to take care of persisting that data to the database.

### Countries API URL:

        https://restcountries.com/

> <b>NOTE:</b> Here we will make use of below endpoint to search by country-name <br/>
        https://restcountries.com/v3.1/name/<country_name_in_full>

        e.g.
        https://restcountries.com/v3.1/name/india

### Dependencies

* camel-http library in order to start consuming the REST API from the Camel Route.
 ![dependency-camel-http-library.png](assets%2Fimages%2Fdependency-camel-http-library.png)


### Creating Route 2 which is actually a rest service.

To create this Route, we are going to use below dependency

       camel-jetty
![dependency-camel-jetty.png](assets%2Fimages%2Fdependency-camel-jetty.png)


### Combining Route1 & Route2 
 To Combine Route1 & Route2 the following code is written :

    .removeHeader(Exchange.HTTP_URI)
    .setHeader(Exchange.HTTP_METHOD, constant("POST"))
    .convertBodyTo(String.class)
    .to("{{toRESTRoute}}")

    > Here we have removed HTTP_URI from Header and resetting the HTTP_METHOD to POST as the ROUTE2 needs data over POST Method.
      and then using 'to' call the ROUTE2 which is defined by 'toRESTRoute'.
