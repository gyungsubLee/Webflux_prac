package webflux.prac.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reactive")
public class PracController {

    @GetMapping("/onenine/list")
    public List<Integer> produceOneToNine() {
        List<Integer> sink = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sink.add(i);
        }
        return sink;
    }

    @GetMapping("/onenine/flux")
    public Flux<Integer> produceOneToNineFlux() {
        return Flux.create(sink -> {
            for (int i = 0; i < 9; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sink.next(i);
            }
            sink.complete();
        });
    }
}