package com.capstone.reviewsummary.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class Crawl {
            // 1. 수집 대상 URL
        private static String URL = "https://smartstore.naver.com/i/v1/contents/reviews/query-pages";
        public static String crawl() throws IOException{
        // 2. Connection 생성
        Connection conn = Jsoup.connect(URL)
                .data("page", "10")
                .data("pageSize", "30")
                .data("checkoutMerchantNo", "511833481")
                .data("originProductNo", "8672080791")
                .data("reviewSearchSortType", "REVIEW_CREATE_DATE_DESC")
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3 Safari/605.1.15")
                //.header("Content-Type", "*/*")
                .ignoreContentType(true)
                .header("Host", "smartstore.naver.com")
                .header("Origin", "https://smartstore.naver.com")
                .header("Referer", "https://smartstore.naver.com/jangsinmall/products/8713193772")
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "ko-KR,ko;q=0.9")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .method(Connection.Method.POST);

        // 3. HTML 파싱.
        Document html = conn.post(); // conn.post();
        System.out.println(html.toString());

        Element body = html.body();
        String jsonData = body.text(); // JSON 데이터 추출
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray contents = jsonObject.getJSONArray("contents");

        for (int i = 0; i < contents.length(); i++) {
            JSONObject content = contents.getJSONObject(i);
            String reviewContent = content.getString("reviewContent");
            System.out.println(reviewContent);
        }

        return html.toString();
        // 4. HTML 출력
        // System.out.println(html.toString());
    }

    public static String ccrawl() {
        try {
            return crawl();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}

