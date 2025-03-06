package com.laundrygo.shorturl.repository;

import com.laundrygo.shorturl.domain.Url;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UrlRepository {

    Url findByOriUrl(String oriUrl);

    Url findByShortUrl(String shortUrl);

    List<Url> findAll();

    void insert(String oriUrl, String shortUrl);

    void updateRequestCount(String shortUrl);
}
