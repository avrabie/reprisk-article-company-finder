package com.reprisk.search.companies.service;

import com.reprisk.search.companies.model.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@SpringBootTest
class ArticleServiceTestIT {

    @Autowired
    CompanyService companyService;
    @Autowired
    ArticleService articleService;

    @Test
    public void itTest() {

        Flux<String> stringFlux = companyService.aggregateCompanyNames();
        Mono<String> regExPattern = companyService.getRegExPattern(stringFlux);

        Pattern pattern = Pattern.compile(regExPattern.block());


        Stream<Path> allArticleFiles = articleService.getAllArticleFiles();
        Stream<Article> articleStream = allArticleFiles.map(path -> articleService.parseXML(path));
        Stream<Matcher> matcherStream = articleStream.map(article -> {
            Matcher matcher = pattern.matcher(article.getText());
            System.out.println("Article id:"+article.getId()+" Title: " +article.getTitle().substring(0,30));
            while (matcher.find()) {
//                System.out.println("Article: " + article.getTitle().substring(0, 20));
                System.out.println("Company: " + matcher.group() + "  Found at: " + matcher.start());

            }
            return matcher;
        });
        matcherStream.forEach(matcher -> {
        });


    }

}