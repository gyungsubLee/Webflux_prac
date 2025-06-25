package webflux.prac.chapter1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class WebClientTest {

    private WebClient webClient = WebClient.builder().build();

    @Test
    @DisplayName("WebClient 테스트")
    void testWebClient() {
        Flux<Integer> intFlux = webClient.get()
                .uri("http://localhost:8080/reactive/onenine/flux")
                .retrieve()
                .bodyToFlux(Integer.class);

        intFlux.subscribe(data -> {
            System.out.println("처리되고 있는 스레드 이름: " + Thread.currentThread().getName());
            System.out.println("WebFlux 구독: " + data);
        });
        LocalDateTime endTime = LocalDateTime.now();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
    }

    @Test
    @DisplayName("WebClient Flux 테스트 2 - WebCline 내에서 Signal로 분기")
    void testWebClient_Signal() {
        LocalDateTime startTime = LocalDateTime.now();

        CountDownLatch latch = new CountDownLatch(1);

        Flux<Integer> intFlux = webClient.get()
                .uri("http://localhost:8080/reactive/onenine/flux")
                .retrieve()
                .bodyToFlux(Integer.class)
                .doOnNext(data -> {
                    System.out.println("처리 스레드: " + Thread.currentThread().getName());
                    System.out.println("받은 데이터: " + data);
                })
                .doOnComplete(() -> {
                    LocalDateTime endTime = LocalDateTime.now();
                    Duration duration = Duration.between(startTime, endTime);
                    System.out.println("총 처리 시간(ms): " + duration.toMillis());
                    latch.countDown(); // 완료 신호
                })
                .doOnError(error -> {
                    System.err.println("에러 발생: " + error.getMessage());
                    latch.countDown();
                });

        intFlux.subscribe();

        try {
            latch.await(); // 비동기 작업이 완료될 때까지 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
