package com.reprisk.search.companies.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class CompanyRegExPatternService {

    private final CompanyService companyService;
    private Pattern pattern;

    CompanyRegExPatternService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        Flux<String> stringFlux = companyService.aggregateCompanyNames();
        Mono<String> regExPattern = companyService.getRegExPattern(stringFlux);
        String regex = Objects.requireNonNull(regExPattern.publishOn(Schedulers.elastic()).block()); // it's a blocking operation
        pattern = Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return pattern;
    }
}