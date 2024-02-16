package foo.bar.rest;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public void makeRequests1() {
        int requestsCount = 1;
        long startTime = System.currentTimeMillis();
        Flux.range(0, requestsCount).flatMap(number -> {
            return client.get().uri(REQUEST_URL).retrieve()
                    .bodyToMono(String.class);
        }).collectList().block();
        log.info("Асинхрон({} запросов): {} мс",
                requestsCount, System.currentTimeMillis() - startTime);
    }

    public void makeRequests1000() {
        int requestsCount = 1000;
        long startTime = System.currentTimeMillis();
        Flux.range(0, requestsCount).flatMap(number -> {
            return client.get().uri(REQUEST_URL).retrieve()
                    .bodyToMono(String.class);
        }).collectList().block();
        log.info("Асинхрон({} запросов): {} мс",
                requestsCount, System.currentTimeMillis() - startTime);
    }

    public void makeRequests10000() {
        int requestsCount = 10000;
        long startTime = System.currentTimeMillis();
        Flux.range(0, requestsCount).flatMap(number -> {
            return client.get().uri(REQUEST_URL).retrieve()
                    .bodyToMono(String.class);
        }).collectList().block();
        log.info("Асинхрон({} запросов): {} мс",
                requestsCount, System.currentTimeMillis() - startTime);
    }
}
