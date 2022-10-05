package com.reprisk.search.companies.service;

import com.reprisk.search.companies.data.CompanyData;
import com.reprisk.search.companies.service.util.CompanyParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class CompanyService {


    private final CompanyParser companyParser;

    public CompanyService(CompanyParser companyParser) {
        this.companyParser = companyParser;
    }

    public Flux<String> aggregateCompanyNames() {
        Flux<CompanyData> companyDataFlux = parseCompanies();
        Mono<Map<String, Integer>> mapMono = mapCompanyNamesToIds(companyDataFlux);
        Flux<String> companyNames = mapMono.map(Map::keySet).flatMapMany(Flux::fromIterable);

        return companyNames;
    }

    // TODO: 04/10/2022 to be moved in a helper function
    public Mono<String> getRegExPattern(Flux<String> companyNames) {
        Mono<String> collect = companyNames.collect(Collectors.joining("|"));
        Mono<String> pattern = Mono.just("(").concatWith(collect).concatWith(Mono.just(")")).collect(Collectors.joining());
        return pattern;
    }

    public Flux<CompanyData> parseCompanies() {
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:160408_company_list.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        File finalFile = file;
        Flux<String> using = Flux.using(
                () -> Files.lines(Path.of(finalFile.getPath())),
                Flux::fromStream,
                Stream::close
        );

        Flux<CompanyData> takeSample = using.skip(1)
                .map(companyParser::parseCompany);

//                .skip(100000)
//                .take(30000);
//                .log();
        return takeSample;
    }

    // we want to have a Name of a company that maps to an ID, obviously it will be many -> 1
    // the reason: some companies have former names, or known as
    public Mono<Map<String, Integer>> mapCompanyNamesToIds(Flux<CompanyData> companyDataFlux) {

        Flux<CompanyData> share = companyDataFlux.share(); //publish it

        Map<String, Integer> collectorMap = new HashMap<>();
        Supplier<Map<String, Integer>> customCollectorSupplierMap = () -> collectorMap;
        Mono<Map<String, Integer>> companyName = share
                .collectMap(CompanyData::name, CompanyData::id, customCollectorSupplierMap);

        Mono<Map<String, Integer>> formerCompanyName = share
                .filter(companyData -> companyData.formerName() != null)
                .collectMap(CompanyData::formerName, CompanyData::id, customCollectorSupplierMap);


//        return companyName.then(formerCompanyName);
        return companyName;

    }


}
