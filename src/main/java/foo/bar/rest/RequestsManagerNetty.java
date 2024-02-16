package foo.bar.rest;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

import static foo.bar.rest.RestApplication.REQUEST_URL;


@Slf4j
@Service
public class RequestsManagerNetty {

    private WebClient client;

    @PostConstruct
    void init() {
        this.client = WebClient.builder()
                .build();
    }

    public void makeRequests(int requestsCount) {
        long startTime = System.currentTimeMillis();

        Scheduler scheduler
                = Schedulers.fromExecutor(Executors.newVirtualThreadPerTaskExecutor());

        Flux.range(0, requestsCount)
                .publishOn(scheduler)
                .subscribeOn(scheduler)
                .flatMap(number -> {
                    return client.get().uri(REQUEST_URL).retrieve()
                            .bodyToMono(String.class);
                }).collectList().block();
        log.info("Асинхрон({} запросов): {} мс",
                requestsCount, System.currentTimeMillis() - startTime);
    }
}
