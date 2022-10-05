package com.reprisk.search.companies.service;

import com.reprisk.search.companies.data.CompanyData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootTest
class CompanyServiceTest {

    @Autowired
    CompanyService companyService;

    @Test
    public void testParseCompanies() {
        companyService.parseCompanies()
                .subscribe(System.out::println);
    }

    @Test
    public void testMerge() {
        Flux<CompanyData> companyDataFlux = companyService.parseCompanies();
        Mono<Map<String, Integer>> mapMono = companyService.mapCompanyNamesToIds(companyDataFlux);
        mapMono.subscribe(System.out::println);
    }

    @Test
    public void patternCreator() {
        Flux<String> stringFlux = companyService.aggregateCompanyNames();
        stringFlux.subscribe(System.out::println);
    }

    @Test
    public void patternCreatorPOC() {
        Flux<CompanyData> companyDataFlux = companyService.parseCompanies();
        Mono<Map<String, Integer>> mapMono = companyService.mapCompanyNamesToIds(companyDataFlux);
        Flux<String> stringFlux = mapMono.map(Map::keySet)
                .flatMapMany(Flux::fromIterable);


        Mono<List<String>> listMono = stringFlux.collectList();
        List<String> block = listMono.block();
        String companyPattern = block.stream().collect(Collectors.joining("|"));
        companyPattern = "(".concat(companyPattern).concat(")");
        System.out.println(companyPattern);

    }

    @Test
    public void companySearchPOC() {
        String text = "Some sample text with Company A and Company B inside it";
        Pattern pattern = Pattern.compile("(Company A|Company B)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println("Start" + matcher.start() + " End:" + matcher.end());
            System.out.println(matcher.group());
        }

    }

    @Test
    public void companySearchPOCWithService() {
        String text = "Some sample text with Alcan Inc and RBS Holdings N.V. inside it";
        Mono<String> regExPattern = companyService.getRegExPattern(companyService.aggregateCompanyNames());
        Pattern pattern = Pattern.compile(regExPattern.block());
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println("Start" + matcher.start() + " End:" + matcher.end());
            System.out.println(matcher.group());
        }

    }

}