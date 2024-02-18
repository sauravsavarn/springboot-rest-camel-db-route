package com.spring.camel.route.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@Data
@ToString
public class Country {
    Name name;
    String ccn3;
    BigInteger population;
//    private BigInteger create_timestamp;


    public String getOfficialName() {
        return name.getOfficial();
    }

    public String getCommonName() {
        return name.getCommon();
    }
}

@Data
@ToString
class Name {
    private String common;
    private String official;

}