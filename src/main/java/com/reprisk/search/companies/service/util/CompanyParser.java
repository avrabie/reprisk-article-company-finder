package com.reprisk.search.companies.service.util;

import com.reprisk.search.companies.data.CompanyData;

public interface CompanyParser {
    CompanyData parseCompany(String line);
}
