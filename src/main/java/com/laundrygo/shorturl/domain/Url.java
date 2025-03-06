package com.laundrygo.shorturl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Url {
    private Integer id;
    private String oriUrl;
    private String shortUrl;
    private Integer requestCount;
}
