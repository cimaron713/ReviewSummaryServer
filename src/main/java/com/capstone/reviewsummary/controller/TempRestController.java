package com.capstone.reviewsummary.controller;

import com.capstone.reviewsummary.common.urlDTO;
import com.capstone.reviewsummary.service.ChatGPTClient;
import com.capstone.reviewsummary.service.CrawlingTools;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TempRestController {

    private final CrawlingTools crawlingTools ;

    @GetMapping("/ttest")
    public String test3(){
        return ChatGPTClient.sendMessage();
    }

    @GetMapping("/hingguri")
    public String hingguri(@RequestBody urlDTO url) throws IOException {
        return crawlingTools.crawlReview(url.getUrl()).toString();
    }



}