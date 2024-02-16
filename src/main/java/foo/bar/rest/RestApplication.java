package foo.bar.rest;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@AllArgsConstructor
@SpringBootApplication
public class RestApplication {

    static final String REQUEST_URL = "http://192.168.0.110:8080/test-get";

    private final TestClientService testClientService;
    private final RequestsManagerNetty requestsManagerNetty;

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

    @GetMapping("/test1")
    public String test1() {
        requestsManagerNetty.makeRequests(1);
        requestsManagerNetty.makeRequests(1);
        requestsManagerNetty.makeRequests(100);
//        requestsManagerNetty.makeRequests(1000);
//        requestsManagerNetty.makeRequests1000();
//        requestsManagerNetty.makeRequests10000();
//        requestsManagerNetty.makeRequests10000();
//        requestsManagerNetty.makeRequests10000();
//        requestsManagerNetty.makeRequests10000();
        return "test";
    }

    @GetMapping("/test")
    public String test() throws InterruptedException, IOException, ExecutionException {

        log.info("... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ...");
        log.info("Crash Test Started...");

        testClientService.makeWarmingUp();
        testClientService.makeWarmingUp();
        testClientService.makeWarmingUp();
        testClientService.makeSinkRequests1000();
        testClientService.makeAsyncRequests1000by1();
        testClientService.makeAsyncRequests1000by10();
        testClientService.makeAsyncRequests1000by100();
        testClientService.makeAsyncRequests1000by1000();
        testClientService.makeAsyncRequests1000byVirtual();

        log.info("Crash Test Finished...");

        return "test";
    }

    @GetMapping("/test-get")
    public TestClientService.ResponseJsonObject testGet() {
        return TestClientService.ResponseJsonObject.builder()
                .text("test")
                .build();
    }
}

