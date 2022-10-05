package com.reprisk.search.companies.model.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyArticle {
    private String articleId;
    private String articleTitle;
    private LinkedHashMap<String, Integer> mentionedCompanies = new LinkedHashMap<>(); //because we would like to see  WHICH company and WHERE exactly it was mentioned


    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void setCompany(String comapnyName, Integer index) {
        this.mentionedCompanies.put(comapnyName, index);
    }
}
