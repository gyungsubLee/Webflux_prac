package webflux.prac.chapter2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
public class BasicFluxMonoTest {

    /** Flux, Mono 기본 구조
     1. just를 통해 데이터 시작
     2. map, filter를 통해 데이터 가공
     3. subscribe를 통해 데이터 방출
    */

    /**
     * Mono: 0~1개의 데이터만 방출할 수 있는 객체    -> Optional 정도
     * Flux: 0개 이상의 데 이터를 방출할 수 있는 객체 -> List, Stream 같이 0개 이상의 데이터 방출
     */

    @Test
    void testBasicFlux() {
         Flux.<Integer>just(1, 2, 3, 4, 5)  // 1. 빈 함수, 2. 데이터로부터 시작 가능하다.
                 .map(data -> data * 2)
                 .filter(data -> data % 4 == 0)
                 .subscribe(data -> System.out.println("Flux가 구독한 data: " + data  ));

    }

    /**
     * 1 개의 데이터를 사용하는 경우 Mono가 Flux보다 훨씬 가독성이 좋고 유지보수도 수월하고 훨씬 편하다.
     */
    @Test
    void testBasicMono() {
        Mono.<Integer>just(1)  // 1. 빈 함수, 2. 데이터로부터 시작 가능하다.
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .subscribe(data -> System.out.println("Mono가 구독한 data: " + data  ));
    }

    @Test
    void testBlock() {
        Mono<String> justString = Mono.just("string");
        String s = justString.block();
        System.out.println("s = " + s);
    }
}
