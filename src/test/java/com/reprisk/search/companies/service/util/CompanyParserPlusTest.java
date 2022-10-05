package com.reprisk.search.companies.service.util;

import com.reprisk.search.companies.data.CompanyData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CompanyParserPlusTest {

    CompanyParser companyParser = new CompanyParserPlus(null);

    @Test
    void anotherWeirdness() {
        String line = "6570;Ensco PLC (formerly Ensco International PLC) (Ensco)";
        CompanyData companyData = companyParser.parseCompany(line);
        System.out.println(companyData);
    }

    @Test
    void testSplit() {
        String iaka = "iaka un string";
        String[] split = iaka.split(";");
        System.out.println(split.length);
    }


}