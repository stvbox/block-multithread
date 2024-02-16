package foo.bar.rest;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@Component
@AllArgsConstructor
public class RequestsManagerOrigin implements RequestsManager {

    private static Cache<Integer, HttpClient> cache = CacheBuilder.newBuilder().build();

    HttpClient createClient(int threads) throws ExecutionException {
        return cache.get(threads, () -> {
            ExecutorService executorService = getExecutorService(threads);
            return HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .executor(executorService).build();
        });
    }

    HttpRequest createRequest(String requestUrl) {
        return HttpRequest.newBuilder(URI.create(requestUrl)).GET()
                .headers(
                        "Accept", MimeTypeUtils.APPLICATION_JSON_VALUE,
                        "Content-type", MimeTypeUtils.APPLICATION_JSON_VALUE
                ).build();
    }

    List<HttpRequest> createRequests(String requestUrl, int requestsCount) {
        return IntStream.range(1, requestsCount).mapToObj(i -> {
            return createRequest(requestUrl);
        }).toList();
    }

    HttpResponse<String> makeSinkTestRequest(HttpClient client, HttpRequest request)
            throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    void makeAsyncRequests(List<HttpRequest> requests, int threadsCount) {
        try {
            var client = createClient(threadsCount);
            var completableFutures = requests.stream()
                    .map(request -> {
                        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                .thenApply(res -> {
                                    return res.body();
                                });
                    }).toList();

            CompletableFuture<Object[]> combinedFutures = CompletableFuture
                    .allOf(completableFutures.toArray(new CompletableFuture[0]))
                    .thenApply(future -> {
                        return completableFutures.stream()
                                .map(CompletableFuture::join)
                                .toArray();
                    });

            combinedFutures.join();

        } catch (Exception ex) {
            log.error("error: {}", ex.getMessage());
            if (ex.getCause() != null)
                log.error("cause: {}", ex.getCause().getMessage());
        }
    }

    @SneakyThrows
    static private ExecutorService getExecutorService(Integer threads) {
        if (threads == -1) {
            return Executors.newVirtualThreadPerTaskExecutor();
        } else {
            return threads == 1 ? Executors.newSingleThreadScheduledExecutor()
                    : Executors.newScheduledThreadPool(threads);
        }
    }
}
