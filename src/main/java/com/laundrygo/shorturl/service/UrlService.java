package com.laundrygo.shorturl.service;

import com.laundrygo.shorturl.domain.Url;
import com.laundrygo.shorturl.repository.UrlRepository;
import com.laundrygo.shorturl.requestdto.UrlRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class UrlService {
    private static final int SHORT_URL_LENGTH = 8;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Transactional
    public String shorten(UrlRequestDto.shortenUrl requestDto) {
        //DB 에서 oriUrl 존재 확인
        Url url = urlRepository.findByOriUrl(requestDto.getOriUrl());

        //존재하는 shortUrl return
        if (url != null) {
            return url.getShortUrl();
        } else {
            String shortUrl;

            //shortUrl 중복방지
            do {
                shortUrl = createShortUrl();
            } while(urlRepository.findByShortUrl(shortUrl) != null);

            urlRepository.insert(requestDto.getOriUrl(), shortUrl);

            return shortUrl;
        }
    }

    @Transactional
    public String getOriUrl(UrlRequestDto.getOriUrl requestDto) {
        //oriUrl 조회
        Url url = urlRepository.findByShortUrl(requestDto.getShortUrl());

        if (url != null) {
            //shortUrl 요청 수 증가
            urlRepository.updateRequestCount(requestDto.getShortUrl());

            return url.getOriUrl();
        } else {
            return "oriUrl이 존재하지 않습니다.";
        }
    }

    @Transactional(readOnly = true)
    public List<Url> getUrlRecord() {
        return urlRepository.findAll();
    }

    //shortUrl 생성
    public String createShortUrl() {
        StringBuilder sb = new StringBuilder(SHORT_URL_LENGTH);
        Random random = new Random();

        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}
