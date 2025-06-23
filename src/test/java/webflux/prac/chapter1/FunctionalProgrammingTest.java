package webflux.prac.chapter1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

@SpringBootTest
public class FunctionalProgrammingTest {

    @DisplayName("리펙토링 전")
    @Test
    public void Case1_basic() {
        List<Integer> sink = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            sink.add(i);
        }

        // 모든 요소 * 2
        List<Integer> newSink = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            newSink.add(sink.get(i) * 2);
        }
        sink = newSink;

        // 4 배수만 필터
        List<Integer> newSink2 = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if ( sink.get(i) % 4 == 0) {
                newSink2.add(sink.get(i));
            }
        }
        sink = newSink2;

        for (int i = 0; i < sink.size(); i++) {
            System.out.println(sink.get(i));
        }
    }

    @DisplayName("리펙토링 1 - 람다, 인자")
    @Test
    public void Case2_lamda() {
        List<Integer> sink = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            sink.add(i);
        }

        sink = map(sink, (data) -> data * 2); // 모든 요소 * 2
        sink = filter(sink, (data) -> (data % 4 == 0)); // 4 배수만 필터
        forEach(sink, (data) -> System.out.println(data));
    }

    private static List<Integer> map(List<Integer> sink, Function<Integer, Integer> mapper) {
        List<Integer> newSink = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            newSink.add(mapper.apply(sink.get(i)));
        }
        return newSink;
    }

    private static List<Integer> filter(List<Integer> sink, Function<Integer, Boolean> predicate) {
        List<Integer> newSink2 = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if ( predicate.apply(sink.get(i)) ) {
                newSink2.add(sink.get(i));
            }
        }
        return newSink2;
    }

    private static void forEach(List<Integer> sink, Consumer<Integer> consumer) {
        for (int i = 0; i < sink.size(); i++) {
            consumer.accept(sink.get(i));
        }
    }

    @DisplayName("리펙토링 2 - Stream")
    @Test
    public void Case2_stream() {
        List<Integer> sink = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            sink.add(i);
        }

        sink.stream()
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .forEach(data -> System.out.println(data)); // 축약: System.out::println
    }

    @DisplayName("리펙토링 2.2 - Stream, for문 축약")
    @Test
    public void Case2_stream2() {
        IntStream.rangeClosed(1, 9).boxed()
                .map(data -> data * 4)
                .filter(data -> data % 4 == 0)
                .forEach(System.out::println);
    }

    @DisplayName("리펙토링 3 - Flux")
    @Test
    public void Case3_Flux() {
        Flux<Integer> intFlux = Flux.create(sink -> {
            for (int i = 1; i <= 9; i++) {
                sink.next(i);
            }

            sink.complete();
        });

        intFlux.subscribe(data -> System.out.println("WebFlux 구독 중: " + data));
        System.out.println("Netty 이벤트 루프로 스레드 복귀");
    }

    @DisplayName("리펙토링 4 - Flux.fromIterable")
    @Test
    public void Case3_Flux_fromIterable() {
        Flux.fromIterable(IntStream.rangeClosed(1, 9).boxed().toList())
                // operator 대부분이 stream과 유사하게 동작
                .map(data -> data * 4)
                .filter(data -> data % 4 == 0)
                .subscribe(System.out::println);
    }
}
