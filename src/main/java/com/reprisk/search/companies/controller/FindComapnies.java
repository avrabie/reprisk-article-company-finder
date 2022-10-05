package com.reprisk.search.companies.controller;

import com.reprisk.search.companies.model.Article;
import com.reprisk.search.companies.model.responses.CompanyArticle;
import com.reprisk.search.companies.service.ArticleService;
import com.reprisk.search.companies.service.CompanyRegExPatternService;
import com.reprisk.search.companies.service.CompanyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RestController
@Log4j2
public class FindComapnies {
    private final CompanyService companyService;
    private final ArticleService articleService;
    private final CompanyRegExPatternService companyRegExPatternService;


    public FindComapnies(CompanyService companyService, ArticleService articleService, CompanyRegExPatternService companyRegExPatternService) {
        this.companyService = companyService;
        this.articleService = articleService;
        this.companyRegExPatternService = companyRegExPatternService;
    }

    @GetMapping("/hello")
    Flux<String> hello() {
        return Flux.just("Hello ", "World", "!");
    }
    @GetMapping(value = "/companies", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<CompanyArticle> iaka() {
        Pattern pattern = companyRegExPatternService.getPattern();

        Stream<Path> allArticleFiles = articleService.getAllArticleFiles();
        Stream<Article> articleStream = allArticleFiles.map(path -> articleService.parseXML(path));
        Stream<CompanyArticle> companyArticleStream = articleStream.map(article -> {
            CompanyArticle companyArticle = new CompanyArticle();
            companyArticle.setArticleId(article.getId());
            companyArticle.setArticleTitle(article.getTitle());
            Matcher matcher = pattern.matcher(article.getText());

            while (matcher.find()) {
                companyArticle.setCompany(matcher.group(), matcher.start());
//                System.out.println("Article: " + article.getTitle().substring(0, 20));
                log.info("Company: " + matcher.group() + "  Found at: " + matcher.start());

            }
            return companyArticle;
        });
        return Flux.fromStream(companyArticleStream);
    }
}
