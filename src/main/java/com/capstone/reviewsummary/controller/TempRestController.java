package com.capstone.reviewsummary.controller;

import com.capstone.reviewsummary.common.urlDTO;
import com.capstone.reviewsummary.service.ChatGPTClient;
import com.capstone.reviewsummary.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TempRestController {

    private final CrawlingService crawlingService;
    @GetMapping("/ttest")
    public String test3(){

        return ChatGPTClient.sendMessage();
    }
    @GetMapping("/hingguri")
    public String hingguri(@RequestBody urlDTO url){
        return crawlingService.getReview(url.getUrl()).toString();

    }


}