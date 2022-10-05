package com.reprisk.search.companies.service;

import com.reprisk.search.companies.model.Article;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

class ArticleServiceTest {

    ArticleService articleService = new ArticleService();

    @Test
    void testingWaters() {
        Stream<Path> pathStream = articleService.getAllArticleFiles();
        pathStream.forEach(System.out::println);
    }

    @Test
    void testUnmarshlar() throws FileNotFoundException, URISyntaxException {
        URL url = ResourceUtils.getURL("classpath:data/0C06-D88D-D7CF-EF82.xml");
        Path path = Paths.get(url.toURI());
        Article xml = articleService.parseXML(path);
        Assertions.assertThat(xml.getTitle()).startsWith("'I broke the council");
    }

    @Test
    void getArticles() {
        Stream<Article> artiles = articleService.getArtiles();
        artiles.forEach(System.out::println);
    }
}