package com.reprisk.search.companies.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reprisk.search.companies.data.CompanyData;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Log4j2
public class SimpleCompanyParser implements CompanyParser {
    private final ObjectMapper objectMapper;
    private final Pattern pattern;

    public SimpleCompanyParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        this.pattern = Pattern.compile("(^.*)\\((.*)\\).*$");
//        this.pattern = Pattern.compile("PLC");
    }


    @Override
    public CompanyData parseCompany(String line) {
        String[] split = line.split(";", 2);

        Matcher matcher = this.pattern.matcher(split[1]);
        boolean matches = matcher.matches();
        if (matches) {
            String group = matcher.group(2);
            return new CompanyData(Integer.parseInt(split[0]), matcher.group(1).trim(), matcher.group(2).trim());
        }

        return new CompanyData(Integer.parseInt(split[0]), split[1].trim(), null);
    }
}
