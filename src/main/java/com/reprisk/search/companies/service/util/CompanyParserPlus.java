package com.reprisk.search.companies.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reprisk.search.companies.data.CompanyData;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Log4j2
@Component
public class CompanyParserPlus implements CompanyParser {
    private final ObjectMapper objectMapper;
    private final Pattern pattern;

    public CompanyParserPlus(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        this.pattern = Pattern.compile("(^.*)\\((.*)\\).*$");
//        this.pattern = Pattern.compile("PLC");
    }


    @Override
    public CompanyData parseCompany(String line) {
        String[] split = line.split(";", 2);
        String name = split[1];
        if(name.startsWith("\"")){
            //70511;"Chelyabinsk Electrometallurgical Works (CHEMK); OJSC"
            name = name.substring(1, name.length() - 2);


        }

        Matcher matcher = this.pattern.matcher(name);
        if (matcher.matches()) {
            name = matcher.group(1).trim();
            Matcher doubleParanthesisMatcher = pattern.matcher(name);
            if (doubleParanthesisMatcher.matches()) {
                return new CompanyData(Integer.parseInt(split[0]), doubleParanthesisMatcher.group(1).trim(), doubleParanthesisMatcher.group(2).trim());
            }

            return new CompanyData(Integer.parseInt(split[0]), matcher.group(1).trim(), matcher.group(2).trim());
        }

        return new CompanyData(Integer.parseInt(split[0]), split[1].trim(), null);
    }
}
