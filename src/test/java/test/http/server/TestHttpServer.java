package test.http.server;

import java.io.IOException;
import nn1211.http.server.HttpServer;
import static nn1211.http.server.ServerResponse.*;
//import org.junit.Test;

/**
 * HttpServer's test cases.
 *
 * @author nn1211
 * @since 1.0
 */
public class TestHttpServer {
    
//    @Test
//    public void test400() throws IOException {
//        new HttpServer().start();
//    }
    
    public static void main(String[] args) throws IOException {
        new HttpServer()
                .registerHandler("GET /oauth-result", r -> ok(r.param("code")))
                .start();
    }
}
