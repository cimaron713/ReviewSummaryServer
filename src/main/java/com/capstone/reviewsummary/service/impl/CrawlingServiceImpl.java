package com.capstone.reviewsummary.service.impl;
import com.capstone.reviewsummary.service.CrawlingService;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlingServiceImpl implements CrawlingService {

    @Override
    public String crawlReview(String url) throws IOException {
        String endPoint = "https://smartstore.naver.com/i/v1/contents/reviews/query-pages";
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
            for(int a=1; a<=3; a++) {
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

            return String.join("---",review);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String crawlCoupangReview(String url) throws IOException {
        String endPoint = "https://www.coupang.com/vp/products/review-details";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Safari/605.1.15";

        // 리뷰 결과 저장 리스트.
        List<String> review = new ArrayList<>();
        try {
            ///////////// 여기서 부터는 originProductNo, checkoutMerchantNo를 구하기 위한 코드 ////////////
            // 상품 HTML - GET 요청
            Document doc = Jsoup.connect(url).userAgent(userAgent)
                    .header("Cookie", "MARKETID=95907726727829255299147; PCID=95907726727829255299147; _abck=83635057BB2512E2A9CAE4782CB97434~-1~YAAQP2HKF+ddqVCPAQAAZgw3VgvTDT967ECsm4oF6YAp7FcHOdVlLNEjO9BTfUlRXK/hFwDBE9AYXlttAr1IuC6NbXk1m6nO9vB9IpRsloO/5qb/UjDx/yj4eZFZMGBoBY2d7J8mmTh0npXl6s+r1Lx52VbJEUPLDMEkOAmb1AUcdKze6TxpDWf6mjb7JjEnqMKxKeZAbnydzjYeA4f3nBSGmyfwaZGwbaOruHALJyaDT7q9Jp6PVjHxjET5eDW0i3XEmH6RlkWzYFlg69yt8ZDotmlgcmI7obVh92BH5QLEAc6+yVyMYelPZ1CsTFFmAjM7DGBwa1gn7t5EZ2o8HYbRhvF6xLoyfUR7xiQgpNXu3zTGtj+QCkAQozE8LA==~-1~-1~-1; ak_bmsc=9383BD3064C149F1CC5DD2F619400E3F~000000000000000000000000000000~YAAQTmHKFwoFXk+PAQAAa6pTVhflh8//25U96r0XRva2DoSqijOfRAbvJr4mc0ZVy6zmB6YbPIeyFS1no3bsgSpIvqs52l4S75gEFeIJkpUJi/Kb5KwFbzuhrPW9jspt6mz7g5fMI+Ybr7Kyc9qwz+bkLEsZEFqH3HyzkJA2QRzpbl5OrG1G4xv22bFCCNiLFcq6Lezg4pgze/pY9kIJHlEQFgQthU9VLxem9g2KLQepc+j0Rg2Atk7JkOgZZev2AVTT3NrnLk91SSOkHuarvbpiEZnOagCkuBDV9o/C5bnldY1o4SqkTB+4bZwYaF+hS/CvyCmrLGNHQtFR+b9ZSERVuzGulpd0GoV3e9niOFOE45Nwh75jJsrK9QIzvxdejNRH+SoVaMKvQuKFOvZMZB+Sw/9aP3Jyy9MALY3v3pgS/1CI1l894sdMR9+8PC1TyX96MWTYuw==; bm_mi=9769B830FA0A87F0637999DE2F351522~YAAQTmHKF1e0XU+PAQAAZKNSVhcnLlSKSPG/CLUhGc3UfJGoX5AClLkPr7qFr3jllD5CByf/Z21RcU7UK3rNI/vmv+BCeOzkOgtj2yA+H/m+qroPoNAW89GGWC6nDoJ5zdfl2STKSbvYYKHPE/8esLQGHejKuETMw1h6TUk5yW2TiLGav/X8REm8XHAoKZy69rlHNorDkE61HeJCMfG1W2vFanUJg644UyqwMXoYX+btjYYB5Y2NtBm6YydkXmARpKizPduISsB6XmhciY6ESKUAcox7t/KB3aQxwou7QAwGI+wCVJLdYk00+ZrMI0Zi3ZnTt3EHQkg7XIYZVwujoY2IF9gu2C21IA==~1; bm_sv=07C1F1E95409F301379243E393894C40~YAAQTmHKF1i0XU+PAQAAZKNSVhdbHKH5kkc8eVIWnOQ7sWY0rtFTS1ih7TyMq20enRitvJzmrilqOF2Y+iBQzeUcZY9DPImjZ6vUas/p2bsfFls4thP7fz/mXtOaMtwM8XrpDip7v5H52vhUUgn1JzkwPdo68awL8vRRG7jZgb6iYP/jcXtrk2kIe9bk8rI5znWZ1tlYtw89ssbCo34axzX/RXa+cAoHhw9XLG35quU5GFg0MlWZRrf6MO3uZJMB7g==~1; bm_sz=44DF1BFCD5625F13B08DC7F3A0087A8F~YAAQP2HKF+pdqVCPAQAAZgw3VheqCygqL1FUfqGfeD9Hx6F9qeuOXaYIA0pgVf9j3y2U7HSZYD2E6JN7c00gL82r/DDme/gp9vyKQnPnfM4Iga+Vj/a/GZnQE//otrwwtWVZhhUy6G86q0nzGuX/36tO8fACX4/Z9VEJE4VSun224mxef5zvozBoIiYy8mYRgWqK6MbBs/9l74rO+hW09+RgkCMRGqp7E/xa3bFox26+MSSBcNqUOr0ILPsqOwVuY4/o/32X8c7aHxz92Qk35WU85P1WcW8ptvj6QHw/lCaOsxgfhS6tx9gtJmUBgsGl0lz6p9VOlRnfkuIH+8oovabPFcRd4LcH6VYiH566~4408899~4600901; overrideAbTestGroup=%5B%5D; sid=d8ff3e57fda2444591469a63dda801ac103c4a76; x-coupang-accept-language=ko-KR; x-coupang-target-market=KR")
                    .header("Referer",url)
                    .get();

            // 리뷰 내용이 있는 모든 요소를 선택합니다.
            Elements reviewElements = doc.select(".sdp-review__article__list__review__content");

            // 선택된 모든 요소에서 텍스트를 추출합니다.
            for (Element reviewElement : reviewElements) {
                String reviewContent = reviewElement.text();
                System.out.println("리뷰 내용: " + reviewContent);
                review.add(reviewContent);
            }
            return String.join("---",review);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
