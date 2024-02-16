package foo.bar.rest;

import org.eclipse.jetty.http2.client.HTTP2Client;
import org.springframework.stereotype.Service;


@Service
public class RequestsManagerJetty {
    void test() throws Exception {
        HTTP2Client client = new HTTP2Client();
        client.start();
    }
}

