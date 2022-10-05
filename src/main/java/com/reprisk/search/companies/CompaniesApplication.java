package com.reprisk.search.companies;

import com.reprisk.search.companies.service.CompanyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;
import java.util.regex.Pattern;

@SpringBootApplication
public class CompaniesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompaniesApplication.class, args);
    }

}


