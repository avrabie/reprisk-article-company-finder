package com.reprisk.search.companies.service.util;

import com.reprisk.search.companies.data.CompanyData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


class SimpleCompanyParserTest {


    private String company = "19;BAE Systems PLC (British Aerospace)";

    private SimpleCompanyParser simpleCompanyParser = new SimpleCompanyParser(null);
    @Test
    void parseCompany() {
        CompanyData companyData = simpleCompanyParser.parseCompany(company);
        System.out.println(companyData);
    }
//    2116;Repsol Oil & Gas Canada Inc (formerly Talisman Energy Inc (Talisman))

    @Test
    void parseCompanyRepsol() {
        String line = "2116;Repsol Oil & Gas Canada Inc (formerly Talisman Energy Inc (Talisman))";
        Pattern pattern = Pattern.compile("(^.*)[^n].*");
        String[] split = line.split(";", 2);

        Matcher matcher = pattern.matcher(split[1]);
        while (matcher.find()) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
        }

    }


}