package com.capstone.reviewsummary.summary.service.impl;
import com.capstone.reviewsummary.summary.dto.RequestDTO;
import com.capstone.reviewsummary.summary.service.CrawlingService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class CrawlingServiceImpl implements CrawlingService {

    @Override
    public String crawlReview(String url) throws IOException {
        String endPoint = "http://smartstore.naver.com/i/v1/contents/reviews/query-pages";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Safari/605.1.15";

        // 리뷰 결과 저장 리스트.
        List<String> review = new ArrayList<>();
        try {
            ///////////// 여기서 부터는 originProductNo, checkoutMerchantNo를 구하기 위한 코드 ////////////
            // 상품 HTML - GET 요청
            Document doc = Jsoup.connect(url).userAgent(userAgent).get();

            // 상품 타이틀 - h3 태그 찾기
            Elements h3Tags = doc.select("h3");

            // 상품 타이틀 - h3 리스트에서 타이틀 찾기
            String title = "";
            if (!h3Tags.isEmpty()) {
                title = h3Tags.get(0).text();
            } else {
                System.out.println("<h3> 태그를 찾을 수 없습니다.");
            }

            // checkoutMerchantNo & originProductNo - window.__PRELOADED_STATE__ 문자열을 포함하는 <script> 태그를 찾습니다.
            Element scriptTag = doc.selectFirst("script:containsData(window.__PRELOADED_STATE__)");

            // checkoutMerchantNo & originProductNo - 만약 해당 스크립트 태그가 있다면 그 내용을 출력합니다.
            if (scriptTag == null) {
                System.out.println("window.__PRELOADED_STATE__를 포함하는 <script> 태그를 찾을 수 없습니다.");
            }

            // checkoutMerchantNo & originProductNo - 스크립트 태그 내용 추출
            String scriptContent = scriptTag.data();

            // checkoutMerchantNo & originProductNo - "window.__PRELOADED_STATE__ = " 부분의 위치 찾기
            int startIdx = scriptContent.indexOf("window.__PRELOADED_STATE__ = ") + "window.__PRELOADED_STATE__ = ".length() - 1;

            // checkoutMerchantNo & originProductNo - 전체 문자열에서 해당 부분부터 끝까지 가져오기
            String jsonString = scriptContent.substring(startIdx);

            // checkoutMerchantNo & originProductNo - 마지막 세미콜론 제거
            jsonString = jsonString.replaceAll(";", "");

            // checkoutMerchantNo & originProductNo - JSON 문자열 파싱
            JSONObject data = new JSONObject(jsonString);

            // checkoutMerchantNo & originProductNo - 이제 data 객체를 통해 원하는 정보 접근
            String originProductNo = data.getJSONObject("product").getJSONObject("A").getString("productNo");
            String checkoutMerchantNo = data.getJSONObject("smartStoreV2").getJSONObject("channel").getString("payReferenceKey");

            ///////////// 여기서 부터는 이제 리뷰 크롤링 ////////////
            for (int a = 1; a <= 3; a++) {
                Document html = Jsoup.connect(endPoint)
                        .ignoreContentType(true)
                        .header("Accept", "application/json, text/plain, */*")
                        //.header("Accept-Encoding", "gzip, deflate, br, zstd")
                        //.header("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7")
                        //.header("Connction","keep-alive")
                        .header("Host", "smartstore.naver.com")
                        //.header("Origin", "https://smartstore.naver.com")
                        //.header("Referer", url)
                        //.header("Sec-Fetch-Dest", "empty")
                        //.header("Sec-Fetch-Mode", "cors")
                        //.header("Sec-Fetch-Site", "same-origin")
                        .userAgent(userAgent)
                        //.header("x-client-version", "20240426145909")
                        .data("page", "1")
                        .data("pageSize", "30")
                        .data("checkoutMerchantNo", checkoutMerchantNo)
                        .data("originProductNo", originProductNo)
                        .data("reviewSearchSortType", "REVIEW_CREATE_DATE_DESC")
                        .post();

                //Document html = conn.post(); // conn.post();
                Element body = html.body();
                String jsonData = body.text(); // JSON 데이터 추출
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray contents = jsonObject.getJSONArray("contents");


                for (int i = 0; i < contents.length(); i++) {
                    JSONObject content = contents.getJSONObject(i);
                    String reviewContent = content.getString("reviewContent");
                    System.out.println(reviewContent);
                    review.add(reviewContent);
                }
            }

            return String.join("---", review);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public String crawlCoupangReview(RequestDTO.CoupangRequestDTO coupangRequestDTO) throws IOException {
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0120.0.0 Safari/537.36";
        String reviewUrlTemplate = "http://www.coupang.com/vp/product/reviews?productId=" + coupangRequestDTO.getProductNo() + "&size=30&sortBy=DATE_DESC&page=";

        // 리뷰 결과 저장 리스트.
        List<String> meaningfulReviews = new ArrayList<>();
        int page = 1;
        int totalReviewLength = 0;
        Random random = new Random();

        try {
            while (meaningfulReviews.size() < 100 && totalReviewLength < 6000) {
                try {
                    int sleepTime = 300 + random.nextInt(101);
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String reviewUrl = reviewUrlTemplate + page;
                System.out.println(reviewUrl);

                // 상품 HTML - GET 요청
                Document doc = Jsoup.connect(reviewUrl)
                        .userAgent(userAgent)
                        .cookie("x-coupang-accept-language", "ko-KR")
                        .cookie("x-coupang-target-market", "KR")
                        .ignoreContentType(true)
                        .header("Accept", "*/*")
                        .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Connection", "keep-alive")
                        .header("host", "www.coupang.com")
                        .header("Referer", reviewUrl)
                        .get();

                // 리뷰 내용이 있는 모든 요소를 선택합니다.
                Elements reviewElements = doc.select(".sdp-review__article__list__review__content");

                if (reviewElements.isEmpty()) {
                    // 더 이상 리뷰가 없으면 종료
                    break;
                }

                // 선택된 모든 요소에서 텍스트를 추출합니다.
                for (Element reviewElement : reviewElements) {
                    String reviewContent = reviewElement.text();
                    if (reviewContent.length() >= 10) {
                        meaningfulReviews.add(reviewContent);
                        totalReviewLength += reviewContent.length();
                    }
                    if (meaningfulReviews.size() >= 100 && totalReviewLength >= 6000) {
                        break;
                    }
                }
                page++;
            }
            System.out.println(meaningfulReviews);
            return String.join("---", meaningfulReviews);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String crawlSmartStoreReview(RequestDTO.SmartStoreRequestDTO smartStoreRequestDTO) throws IOException{
        String endPoint = "http://smartstore.naver.com/i/v1/contents/reviews/query-pages";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Safari/605.1.15";

        // 리뷰 결과 저장 리스트.
        List<String> meaningfulReviews = new ArrayList<>();
        int page = 1;
        int totalReviewLength = 0;
        try {
            while (meaningfulReviews.size() < 100 && totalReviewLength < 6000) {
                System.out.println("네이버 페이지"+page);

                Document html = Jsoup.connect(endPoint)
                        .ignoreContentType(true)
                        .header("Accept", "application/json, text/plain, */*")
                        .header("Host", "smartstore.naver.com")
                        .userAgent(userAgent)
                        .data("page", Integer.toString(page))
                        .data("pageSize", "30")
                        .data("checkoutMerchantNo", smartStoreRequestDTO.getCheckoutMerchantNo())
                        .data("originProductNo", smartStoreRequestDTO.getOriginProductNo())
                        .data("reviewSearchSortType", "REVIEW_CREATE_DATE_DESC")
                        .post();

                Element body = html.body();
                String jsonData = body.text(); // JSON 데이터 추출
                if(jsonData.isEmpty()){
                    break;
                }
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray contents = jsonObject.getJSONArray("contents");

                for (int i = 0; i < contents.length(); i++) {
                    JSONObject content = contents.getJSONObject(i);
                    String reviewContent = content.getString("reviewContent");
                    if (reviewContent.length() >= 10) {
                        meaningfulReviews.add(reviewContent);
                        totalReviewLength += reviewContent.length();
                    }
                    if (meaningfulReviews.size() >= 100 && totalReviewLength >= 6000) {
                        break;
                    }
                    System.out.println(reviewContent);
                }
                page += 1;
            }
            return String.join("---",meaningfulReviews);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


//
//    @Override
//    public String crawlSmartStoreReview2(RequestDTO.SmartStoreRequestDTO smartStoreRequestDTO) throws IOException {
//        String endPoint = "http://smartstore.naver.com/i/v1/contents/reviews/query-pages";
//        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Safari/605.1.15";
//
//        // 리뷰 결과 저장 리스트.
//        List<String> review = new ArrayList<>();
//        try {
//            for (int a = 1; a <= 3; a++) {
//                Document html = Jsoup.connect(endPoint)
//                        .ignoreContentType(true)
//                        .header("Accept", "application/json, text/plain, */*")
//                        .header("Host", "smartstore.naver.com")
//                        .userAgent(userAgent)
//                        .data("page", Integer.toString(a))
//                        .data("pageSize", "30")
//                        .data("checkoutMerchantNo", smartStoreRequestDTO.getCheckoutMerchantNo())
//                        .data("originProductNo", smartStoreRequestDTO.getOriginProductNo())
//                        .data("reviewSearchSortType", "REVIEW_CREATE_DATE_DESC")
//                        .post();
//
//
//                Element body = html.body();
//                String jsonData = body.text(); // JSON 데이터 추출
//                if (jsonData.isEmpty()) {
//                    break;
//                }
//                JSONObject jsonObject = new JSONObject(jsonData);
//                JSONArray contents = jsonObject.getJSONArray("contents");
//
//                for (int i = 0; i < contents.length(); i++) {
//                    JSONObject content = contents.getJSONObject(i);
//                    String reviewContent = content.getString("reviewContent");
//                    System.out.println(reviewContent);
//                    review.add(reviewContent);
//                }
//            }
//
//            return String.join("---", review);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}