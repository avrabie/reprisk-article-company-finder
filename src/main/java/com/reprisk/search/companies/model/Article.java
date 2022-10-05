package com.reprisk.search.companies.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "news-item")
@Data
public class Article {

    String id;
    String date;
    String title;
    String source;
    String author;
    String text;

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }
}
