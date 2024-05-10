package com.capstone.reviewsummary;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;



@SpringBootTest
public class WebClientTest {

        private WebClient webClient;

        public WebClientTest() {
            this.webClient = WebClient.builder()
                    .baseUrl("http://www.coupang.com")
                    .defaultHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Safari/605.1.15")
                    .defaultHeader("Accept", "*/*")
                    .defaultHeader("Host", "www.coupang.com")
                    .defaultHeader("Referer","https://www.coupang.com/vp/products/6792297030L")
                    .defaultCookie("x-coupang-accept-language", "ko-KR")
                    .build();
        }

        public Flux<String> fetchReviews(Long productId) {
            return Flux.range(1, 3)  // 페이지 1에서 3까지
                    .flatMap(page -> this.webClient.get()
                            .uri(uriBuilder -> uriBuilder.path("/vp/product/reviews")
                                    .queryParam("productId", productId)
                                    .queryParam("size", 30)
                                    .queryParam("sortBy", "DATE_DESC")
                                    .queryParam("page", page)
                                    .build())
                            .retrieve()
                            .bodyToMono(String.class) // 전체 HTML 응답을 문자열로 변환
                            .flatMapMany(html -> Flux.fromIterable(parseReviews(html))) // HTML 파싱
                    );
        }

        private List<String> parseReviews(String html) {
            Document doc = Jsoup.parse(html);
            Elements reviewElements = doc.select(".sdp-review__article__list__review__content");
            return reviewElements.stream()
                    .map(Element::text)
                    .collect(Collectors.toList());
        }

    public static void main(String[] args) {
        WebClientTest fetcher = new WebClientTest();
        Long productId = 6792297030L; // 예제 제품 ID

        // 리뷰 가져오기 실행 및 결과 출력
        fetcher.fetchReviews(productId).subscribe(
                review -> System.out.println("Review: " + review),
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Completed fetching reviews")
        );

        // 애플리케이션을 계속 실행 상태로 유지 (비동기 처리 대기)
        try {
            Thread.sleep(1000); // 10초 동안 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}


