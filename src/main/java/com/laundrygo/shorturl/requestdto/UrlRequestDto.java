package com.laundrygo.shorturl.requestdto;

import lombok.Getter;
import lombok.Setter;

//확장성을 위해 requestDto 생성
public class UrlRequestDto {

    @Getter
    @Setter
    public static class shortenUrl {
        String oriUrl;
    }

    @Getter
    @Setter
    public static class getOriUrl {
        String shortUrl;
    }
}
