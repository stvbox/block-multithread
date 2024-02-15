package foo.bar.rest;

import static foo.bar.rest.RestApplication.REQUEST_URL;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@AllArgsConstructor
public class TestClientService {

    private final RequestsManager requestsManager;

    public void makeWarmingUp() throws IOException, InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        try (var client = requestsManager.createClient(1)) {
            requestsManager.makeSinkTestRequest(
                    client, requestsManager.createRequest(REQUEST_URL)
            );
        }
        log.info("Прогрев: {} мс", (System.currentTimeMillis() - startTime));
    }

    public void makeSinkRequests1000() throws IOException, InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        try (var client = requestsManager.createClient(1)) {
            for (var i = 0; i < 1000; i++) {
                requestsManager.makeSinkTestRequest(
                        client, requestsManager.createRequest(REQUEST_URL)
                );
            }
        }
        log.info("Синхрон(1000): {} мс", (System.currentTimeMillis() - startTime));
    }

    public void makeAsyncRequests1000by1() {
        List<HttpRequest> requests = requestsManager.createRequests(REQUEST_URL, 1000);
        long startTime = System.currentTimeMillis();
        requestsManager.makeAsyncRequests(requests, 1);
        log.info("Асинхрон({} на {}): {} мс", 1000, 1, System.currentTimeMillis() - startTime);
    }

    public void makeAsyncRequests1000by10() {
        List<HttpRequest> requests = requestsManager.createRequests(REQUEST_URL, 1000);
        long startTime = System.currentTimeMillis();
        requestsManager.makeAsyncRequests(requests, 10);
        log.info("Асинхрон({} на {}): {} мс", 1000, 10, System.currentTimeMillis() - startTime);
    }

    public void makeAsyncRequests1000by100() {
        List<HttpRequest> requests = requestsManager.createRequests(REQUEST_URL, 1000);
        long startTime = System.currentTimeMillis();
        requestsManager.makeAsyncRequests(requests, 100);
        log.info("Асинхрон({} на {}): {} мс", 1000, 100, System.currentTimeMillis() - startTime);
    }

    public void makeAsyncRequests1000by1000() {
        List<HttpRequest> requests = requestsManager.createRequests(REQUEST_URL, 1000);
        long startTime = System.currentTimeMillis();
        requestsManager.makeAsyncRequests(requests, 1000);
        log.info("Асинхрон({} на {}): {} мс", 1000, 1000, System.currentTimeMillis() - startTime);
    }

    public void makeAsyncRequests1000byVirtual() {
        List<HttpRequest> requests = requestsManager.createRequests(REQUEST_URL, 1000);
        long startTime = System.currentTimeMillis();
        requestsManager.makeAsyncRequests(requests, -1);
        log.info("Асинхрон({} на VIRT): {} мс", 1000, System.currentTimeMillis() - startTime);
    }

    @Builder
    @Jacksonized
    public static class ResponseJsonObject {
        private String text;
    }
}
