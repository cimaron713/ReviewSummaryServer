package com.capstone.reviewsummary.service.impl;
import com.capstone.reviewsummary.service.CrawlingService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlingServiceImpl implements CrawlingService {

    @Override
    public String crawlReview(String url) throws IOException {
        String endPoint = "https://smartstore.naver.com/i/v1/contents/reviews/query-pages";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3 Safari/605.1.15";

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

            ///////////// 종료 ////////////

            ///////////// 여기서 부터는 이제 리뷰 크롤링 ////////////
            Connection conn = Jsoup.connect(endPoint)
                    .data("page", "1")
                    .data("pageSize", "30")
                    .data("checkoutMerchantNo", checkoutMerchantNo)
                    .data("originProductNo", originProductNo)
                    .data("reviewSearchSortType", "REVIEW_CREATE_DATE_DESC")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3 Safari/605.1.15")
                    //.header("Content-Type", "application/json")
                    .ignoreContentType(true)
                    .header("Host", "smartstore.naver.com")
                    .header("Origin", "https://smartstore.naver.com")
                    .header("Referer", url)
                    .header("Accept", "application/json, text/plain, */*")
                    .header("Accept-Language", "ko-KR,ko;q=0.9")
                    .header("Connection", "keep-alive")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .method(Connection.Method.POST);

            Document html = conn.post(); // conn.post();
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

            return String.join("---",review);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
