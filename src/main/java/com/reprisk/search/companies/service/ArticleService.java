package com.reprisk.search.companies.service;

import com.reprisk.search.companies.model.Article;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class ArticleService {

    private final JAXBContext jaxbContext;
    private Unmarshaller unmarshaller;

    @SneakyThrows
    public ArticleService() {
        this.jaxbContext = JAXBContext.newInstance(Article.class);
        this.unmarshaller = jaxbContext.createUnmarshaller();
    }

    public Stream<Article> getArtiles() {
        Stream<Path> articleFiles = getAllArticleFiles();
        return articleFiles.map(this::parseXML);

    }

    public Stream<Path> getAllArticleFiles()  {
        URL url = null;
        Stream<Path> pathStream = null;
        try {
            url = ResourceUtils.getURL("classpath:data");
            Path path = Paths.get(url.toURI());
            pathStream = Files.find(path, 1, (filePath, fileAttr) -> fileAttr.isRegularFile());
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return pathStream;

    }

    public Article parseXML(Path path)  {

        Article article = null;
        try {
            article = (Article) unmarshaller.unmarshal(path.toFile());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return article;
    }
}
