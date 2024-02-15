package foo.bar.rest;


import java.net.http.HttpClient;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MimeTypeUtils;

@Configuration
public class AppConfiguration {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .executor(Executors.newVirtualThreadPerTaskExecutor())
                .version(HttpClient.Version.HTTP_2)
                .build();
    }


//    private static BasicHttpClientConnectionManager conMgr = new BasicHttpClientConnectionManager();
//    public static CloseableHttpClient closeableHttpClient =  HttpClientBuilder.create().disableAutomaticRetries().useSystemProperties()
//            .disableCookieManagement().disableRedirectHandling().setConnectionManager(conMgr).build();

//    private static BasicHttpClientConnectionManager conMgr = new BasicHttpClientConnectionManager();
//    public static CloseableHttpClient closeableHttpClient =  HttpClientBuilder.create().disableAutomaticRetries().useSystemProperties()
//            .disableCookieManagement().disableRedirectHandling().setConnectionManager(conMgr).build();
//
//
//    public void noneStaticMethodUsingHttpClient() throws IOException {
//        closeableHttpClient.close();
//    }
}
