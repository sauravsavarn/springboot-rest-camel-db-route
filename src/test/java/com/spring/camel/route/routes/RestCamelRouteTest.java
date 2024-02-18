package com.spring.camel.route.routes;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles(value = "dev")
@RunWith(CamelSpringBootRunner.class) //that is which class this Test to run with.
@SpringBootTest //since this is a SpringBoot application, hence added the annotation.
public class RestCamelRouteTest extends CamelTestSupport {

    @Autowired
    ProducerTemplate producerTemplate;
    //is used to produce some files and we are using this to the file into the input directory.

    @Autowired
    ConsumerTemplate consumerTemplate;

    @Override
    protected RouteBuilder createRouteBuilder() { return new CountryRESTRoute(); }

    @Autowired
    Environment environment;

    @Test
    public void persistCountry() {
        String input = "{\"name\":{\"common\":\"Japan\",\"official\":\"Japan\",\"nativeName\":{\"jpn\":{\"official\":\"日本\",\"common\":\"日本\"}}},\"tld\":[\".jp\",\".みんな\"],\"cca2\":\"JP\",\"ccn3\":\"392\",\"cca3\":\"JPN\",\"cioc\":\"JPN\",\"independent\":true,\"status\":\"officially-assigned\",\"unMember\":true,\"currencies\":{\"JPY\":{\"name\":\"Japanese yen\",\"symbol\":\"¥\"}},\"idd\":{\"root\":\"+8\",\"suffixes\":[\"1\"]},\"capital\":[\"Tokyo\"],\"altSpellings\":[\"JP\",\"Nippon\",\"Nihon\"],\"region\":\"Asia\",\"subregion\":\"Eastern Asia\",\"languages\":{\"jpn\":\"Japanese\"},\"translations\":{\"ara\":{\"official\":\"اليابان\",\"common\":\"اليابان\"},\"bre\":{\"official\":\"Japan\",\"common\":\"Japan\"},\"ces\":{\"official\":\"Japonsko\",\"common\":\"Japonsko\"},\"cym\":{\"official\":\"Japan\",\"common\":\"Japan\"},\"deu\":{\"official\":\"Japan\",\"common\":\"Japan\"},\"est\":{\"official\":\"Jaapan\",\"common\":\"Jaapan\"},\"fin\":{\"official\":\"Japani\",\"common\":\"Japani\"},\"fra\":{\"official\":\"Japon\",\"common\":\"Japon\"},\"hrv\":{\"official\":\"Japan\",\"common\":\"Japan\"},\"hun\":{\"official\":\"Japán\",\"common\":\"Japán\"},\"ita\":{\"official\":\"Giappone\",\"common\":\"Giappone\"},\"jpn\":{\"official\":\"日本\",\"common\":\"日本\"},\"kor\":{\"official\":\"일본국\",\"common\":\"일본\"},\"nld\":{\"official\":\"Japan\",\"common\":\"Japan\"},\"per\":{\"official\":\"ژاپن\",\"common\":\"ژاپن\"},\"pol\":{\"official\":\"Japonia\",\"common\":\"Japonia\"},\"por\":{\"official\":\"Japão\",\"common\":\"Japão\"},\"rus\":{\"official\":\"Япония\",\"common\":\"Япония\"},\"slk\":{\"official\":\"Japonsko\",\"common\":\"Japonsko\"},\"spa\":{\"official\":\"Japón\",\"common\":\"Japón\"},\"srp\":{\"official\":\"Јапан\",\"common\":\"Јапан\"},\"swe\":{\"official\":\"Japan\",\"common\":\"Japan\"},\"tur\":{\"official\":\"Japonya\",\"common\":\"Japonya\"},\"urd\":{\"official\":\"جاپان\",\"common\":\"جاپان\"},\"zho\":{\"official\":\"日本国\",\"common\":\"日本\"}},\"latlng\":[36.0,138.0],\"landlocked\":false,\"area\":377930.0,\"demonyms\":{\"eng\":{\"f\":\"Japanese\",\"m\":\"Japanese\"},\"fra\":{\"f\":\"Japonaise\",\"m\":\"Japonais\"}},\"flag\":\"\\uD83C\\uDDEF\\uD83C\\uDDF5\",\"maps\":{\"googleMaps\":\"https://goo.gl/maps/NGTLSCSrA8bMrvnX9\",\"openStreetMaps\":\"https://www.openstreetmap.org/relation/382313\"},\"population\":125836021,\"gini\":{\"2013\":32.9},\"fifa\":\"JPN\",\"car\":{\"signs\":[\"J\"],\"side\":\"left\"},\"timezones\":[\"UTC+09:00\"],\"continents\":[\"Asia\"],\"flags\":{\"png\":\"https://flagcdn.com/w320/jp.png\",\"svg\":\"https://flagcdn.com/jp.svg\",\"alt\":\"The flag of Japan features a crimson-red circle at the center of a white field.\"},\"coatOfArms\":{\"png\":\"https://mainfacts.com/media/images/coats_of_arms/jp.png\",\"svg\":\"https://mainfacts.com/media/images/coats_of_arms/jp.svg\"},\"startOfWeek\":\"monday\",\"capitalInfo\":{\"latlng\":[35.68,139.75]},\"postalCode\":{\"format\":\"###-####\",\"regex\":\"^(\\\\d{7})$\"}}";

        String response = producerTemplate.requestBody("http://localhost:9090/country", input, String.class); //here we are going to expect String class as response hence the 3rd parameter is given.

        System.out.println("persists country is COMPLETED!!!" + response);
    }
}
