package test.http;

import nn1211.http.Content.Form;
import org.junit.Test;

/**
 * Content test cases.
 *
 * @author nn1211
 * @since 1.0
 */
public class TestContent {

    @Test
    public void testFormContent() {
        System.out.println(Form.newBuilder().set("code", "code")
                .set("client_id", "abc")
                .set("client_secret", "abc")
                .set("redirect_uri", "http://localhost:50505")
                .set("grant_type", "authorization_code")
                .build());
    }
}