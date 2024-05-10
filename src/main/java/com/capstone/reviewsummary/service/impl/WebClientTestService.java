package com.capstone.reviewsummary.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;

@Service
public class WebClientTestService {

    public String getHTML(String basedurl){

        basedurl = "http://www.coupang.com/vp/product/reviews?productId=7719393118&page=1&size=5&sortBy=DATE_DESC&ratings=&q=&viRoleCode=3&ratingSummary=true";
        String result = "";
        for (int i =0; i < 3 ; i++) {
            System.out.println("=========================");
            System.out.println("웹 클라이언트 생성 시작");
            WebClient webClient = WebClient.builder()
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(clientCodecConfigurer -> {
                                clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024);
                            }).build())
                    .filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
                        System.out.println("Status code: " + clientResponse.statusCode());
                        System.out.println("Headers: " + clientResponse.headers().asHttpHeaders());
                        return Mono.just(clientResponse);
                    }))
                    .filter((clientRequest, next) -> next.exchange(clientRequest)
                            .flatMap(clientResponse -> {
                                if (clientResponse.statusCode().is3xxRedirection()) {
                                    String newUrl = clientResponse.headers().header("Location").get(0);
                                    return next.exchange(ClientRequest.from(clientRequest).url(URI.create(newUrl)).build());
                                }
                                return Mono.just(clientResponse);
                            }))

                    .baseUrl(basedurl)
                    .build();
            String UA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4.1 Safari/605.1.15";
            System.out.println("HTTP 커넥션 시작 ");
            String response =
                    webClient
                            .get()
                            .header("Accept", "text/html")
                            .header("Accept-Language", "ko-KR,ko;q=0.9")
                            .header("User-Agent", UA)
                            .header("Referer", "https://www.coupang.com/vp/products/7719393118")

                            // 쿠키 설정
//                        .cookie("_fbp", "fb.1.1714703185125.688799692")
//                        .cookie("bm_sv", "1D45E92AD5E654D9B7C4C1F6AE4E0595~YAAQY3pGaJwsMF+PAQAAuiQnYhefj8b0hjv59ztGLdL8OFg0s7bSWuoBYwRqXzfR6HwbYTD/nqAIONkBVDi0mjN4vbfASf9UtnspRUA2DzNyffF23NW61rG4Km2ZHb25wbuB6vO6bLwWz3vDFZWOLj1OREqxTnVMEj93ZPAwKzRiVq00DRn2L79p/KwZcJhb/YjNjQI6wKzvbxtZTJzv+k2fjAI9nMnZj1zMcdDlEb0A0orZzTkrEzBLH7JMd9+BIgU=~1")
//                        .cookie("sid", "f5df5f34325445b8bacaf752d3fe1dbc5ff52b82")
                            .cookie("x-coupang-accept-language", "ko-KR")
//                        .cookie("baby-isWide", "wide")
//                        .cookie("PCID", "10185615762117591145688")
//                        .cookie("cto_bundle", "0pp5M19yaUEydlI4MiUyQjd3dkpWUnRFWVR5RGg2Z0IwVzAlMkIlMkI2UU9TTiUyQndPYyUyRm0lMkJFaERHZ3BKbjR1ZWlJblRCUGM2cjZYZ3ltWEQ4JTJGTk45TlJTdSUyQnpGZFZxaEpnWm5VdTQ3dXVUajMzbnhkZ2x5VjkySmNZRGxRMHdXbyUyRjl4NkRjWW5MNg")
//                        .cookie("bm_sz", "AB03DBC7F6B68792F23B493E5F341B22~YAAQY3pGaIcfMF+PAQAANsUmYhf0R0osuAuPP5qsm7FQEt/f6O/BNiXP9Z1aGpRbMREhjwgdvPkpSY+92JTVUxLiYZMuCsb1SPnK9xjpjeWZj3rJ7dU4sVBA3OiDDcJiKCf1+85V08ffQWg9q6xJy4V4D89oJmKjfmyVYwr0zSsZKgIbECR/oxgw")
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();
            System.out.println("HTTP 커넥션 완료");
            result += response + "\n ===========================";
        }
        return result;

    }
}
