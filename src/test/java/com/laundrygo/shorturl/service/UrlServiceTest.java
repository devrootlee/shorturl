package com.laundrygo.shorturl.service;

import com.laundrygo.shorturl.domain.Url;
import com.laundrygo.shorturl.repository.UrlRepository;
import com.laundrygo.shorturl.requestdto.UrlRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class) //mockito
class UrlServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(UrlServiceTest.class);

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    //DB에 존재하는 URL 로 호출
    @Test
    void test_shorten_exist() {
        //given
        String oriUrl = "https://www.laundrygo.com/";
        String existShortUrl = "ABCDEFGH";
        UrlRequestDto.shortenUrl requestDto = new UrlRequestDto.shortenUrl();
        requestDto.setOriUrl(oriUrl);
        Url existUrl = new Url(1, oriUrl, existShortUrl, 0);
        given(urlRepository.findByOriUrl(oriUrl)).willReturn(existUrl);

        //when
        String shortUrl = urlService.shorten(requestDto);

        //then
        assertEquals(existShortUrl, shortUrl);
        //findByOriUrl 호출 확인
        verify(urlRepository, times(1)).findByOriUrl(oriUrl);
        //insert 미호출 확인
        verify(urlRepository, never()).insert(anyString(), anyString());

        logger.info("success test_shorten_exist");
    }

    //새로운 URL 로 호출
    @Test
    void test_shorten_notExist() {
        //given
        String oriUrl = "https://www.laundrygo.com/";
        UrlRequestDto.shortenUrl requestDto = new UrlRequestDto.shortenUrl();
        requestDto.setOriUrl(oriUrl);
        //DB안에 URL이 없다고 가정
        given(urlRepository.findByOriUrl(oriUrl)).willReturn(null);
        //생성된 shortUrl이 중복되지 않는다고 가정
        given(urlRepository.findByShortUrl(anyString())).willReturn(null);

        //when
        String shortUrl = urlService.shorten(requestDto);

        //then
        //생성된 shortUrl 길이 확인
        assertEquals(8, shortUrl.length());
        //insert 호출 확인
        verify(urlRepository, times(1)).insert(oriUrl, shortUrl);
        //oriUrl 조회 확인
        verify(urlRepository, times(1)).findByOriUrl(oriUrl);

        logger.info("success test_shorten_notExist");
    }

    //DB에 존재하는 URL 호출
    @Test
    void test_getOriUrl_exist() {
        //given
        String shortUrl = "ABCDEFGH";
        String existOriUrl = "https://www.laundrygo.com/";
        UrlRequestDto.getOriUrl requestDto = new UrlRequestDto.getOriUrl();
        requestDto.setShortUrl(shortUrl);
        Url existUrl = new Url(1, existOriUrl, shortUrl, 0);
        given(urlRepository.findByShortUrl(shortUrl)).willReturn(existUrl);

        //when
        String oriUrl = urlService.getOriUrl(requestDto);

        //then
        assertEquals(existOriUrl, oriUrl);
        //findByShortUrl 호출 확인
        verify(urlRepository, times(1)).findByShortUrl(shortUrl);
        //updateRequestCount 호출 확인
        verify(urlRepository, times(1)).updateRequestCount(shortUrl);

        logger.info("success test_getOriUrl_exist");
    }

    //DB에 없는 URL 호출
    @Test
    void test_getOriUrl_notExist() {
        //given
        String shortUrl = "ABCDEFGH";
        UrlRequestDto.getOriUrl requestDto = new UrlRequestDto.getOriUrl();
        requestDto.setShortUrl(shortUrl);
        //DB안에 URL이 없다고 가정
        given(urlRepository.findByShortUrl(shortUrl)).willReturn(null);

        //when
        String oriUrl = urlService.getOriUrl(requestDto);

        //then
        assertEquals("oriUrl이 존재하지 않습니다.", oriUrl);
        //findByShortUrl 호출 확인
        verify(urlRepository, times(1)).findByShortUrl(shortUrl);
        //updateRequestCount 호출 확인
        verify(urlRepository, never()).updateRequestCount(shortUrl);

        logger.info("success test_getOriUrl_notExist");
    }
}