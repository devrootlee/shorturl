package com.laundrygo.shorturl.controller;

import com.laundrygo.shorturl.domain.Url;
import com.laundrygo.shorturl.requestdto.UrlRequestDto;
import com.laundrygo.shorturl.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/url")
public class UrlController {

    UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * 단축 url 생성
     * @param requestDto
     * @return
     */
    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody UrlRequestDto.shortenUrl requestDto) {
        return ResponseEntity.ok(urlService.shorten(requestDto));
    }

    /**
     * 원본 url 조회 + 단축 url 요청 수 저장
     * @param requestDto
     * @return
     */
    @GetMapping("/origin")
    public ResponseEntity<String> getOriUrl(@ModelAttribute UrlRequestDto.getOriUrl requestDto) {
        return ResponseEntity.ok(urlService.getOriUrl(requestDto));
    }

    /**
     * url 기록 조회
     * @return
     */
    @GetMapping("/record")
    public ResponseEntity<List<Url>> getUrlRecord() {
        return ResponseEntity.ok(urlService.getUrlRecord());
    }
}
