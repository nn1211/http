package test.http.client;

import java.io.IOException;
import nn1211.http.client.ClientRequest;
import nn1211.http.client.ClientResponse;
import org.junit.Test;

/**
 * HTTP request test cases
 *
 * @author nn1211
 */
public class TestClientRequest {

    @Test
    public void testGet200_01() throws IOException {
        ClientResponse resp = ClientRequest.newBuilder("https://google.com")
                .build().get();

        assert 200 == resp.statusCode();
        System.out.println(resp.body().asText());
    }
    
    @Test
    public void testGetWithHeader() throws IOException {
        ClientResponse resp = ClientRequest.newBuilder("https://www.googleapis.com/oauth2/v1/userinfo?alt=json")
                .authorization("Bearer " + "ya29.a0ARrdaM_6fioEyh_zb40E_fGjyZ2dpx3FY-lhD0VqYCpIGCOH0KKGsfGQkhPgSEPCX4w5CMi82OXD17bzH76VU15pW5rzt7D2vFaHlPCK7N8t4zyDvGuJq26PFIjIJ0L5TM8hjrb11xdPqMWSWQVDUJ4yv_ga")
                .build()
                .get();

        System.out.println(resp.statusCode());
        System.out.println(resp.body().asText());
    }
}
