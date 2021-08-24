package nn1211.http.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import nn1211.http.Content;
import static nn1211.http.HttpHeader.*;
import nn1211.http.client.ClientResponse.URLResponse;

/**
 * A client HTTP request.
 *
 * @author nn1211
 * @since 1.0
 */
public abstract class ClientRequest {

    /**
     * Create a new HTTP request builder.
     *
     * @param uri the request URI
     * @return a new HTTP request builder
     * @since 1.0
     */
    public static Builder newBuilder(String uri) {
        return new DefaultBuilder(uri);
    }

    /**
     * Send a GET request to server and return a {@link ClientResponse}.
     *
     * @return a {@link ClientResponse}
     * @throws IOException I/O exception
     * @since 1.0
     */
    public abstract ClientResponse get() throws IOException;

    /**
     * Send a POST request to server and return a {@link ClientResponse}.
     *
     * @param content the POST body
     * @return a {@link ClientResponse}
     * @throws IOException I/O exception
     * @since 1.0
     */
    public abstract ClientResponse post(Content content) throws IOException;

    /**
     * A HTTP request builder.
     *
     * @author nn1211
     * @since 1.0
     */
    public interface Builder {

        /**
         * Build a new HTTP request.
         *
         * @return a new HTTP request
         * @since 1.0
         */
        ClientRequest build();
        
        /**
         * Set Authorization header value.
         * 
         * @param value Authorization header value
         * @return this
         * @since 1.0
         */
        Builder authorization(String value);

    }

    /**
     * The default implementation of {@link Builder}.
     *
     * @author nn1211
     * @since 1.0
     */
    static class DefaultBuilder implements Builder {

        private final String uri;
        private final Map<String, String> headers = new HashMap<>();

        /**
         * Create a new builder with a given URI.
         *
         * @param uri the request URI
         * @since 1.0
         */
        DefaultBuilder(String uri) {
            this.uri = uri;
        }
        
        @Override
        public DefaultBuilder authorization(String value) {
            headers.put(AUTHORIZATION, value);
            return this;
        }

        /**
         * @since 1.0
         */
        @Override
        public ClientRequest build() {
            return new URLRequest(this);
        }

    }

    /**
     * Default implementation for {@link ClientRequest} using
     * {@link HttpURLConnection}.
     *
     * @author nn1211
     * @since 1.0
     */
    static class URLRequest extends ClientRequest {

        /**
         * Bypass host name verification.
         * 
         * @since 1.0
         */
        static {
            HttpsURLConnection.setDefaultHostnameVerifier((u, s) -> {
                System.out.println(u);
                return true;
            });
        }

        /**
         * 
         * @since 1.0
         */
        private final DefaultBuilder builder;

        /**
         * Create a new HTTP request from a given builder.
         *
         * @param builder a {@link Builder}
         * @since 1.0
         */
        URLRequest(DefaultBuilder builder) {
            this.builder = builder;
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public ClientResponse get() throws IOException {
            final HttpURLConnection conn
                    = (HttpURLConnection) new URL(builder.uri).openConnection();
            
            builder.headers.forEach((k, v) -> conn.addRequestProperty(k, v));

            return URLResponse.from(conn);
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public ClientResponse post(Content content) throws IOException {

            HttpURLConnection conn
                    = (HttpURLConnection) new URL(builder.uri).openConnection();

            byte[] reqData = content.toBytes();
            conn.addRequestProperty(CONTENT_TYPE, content.type());
            conn.addRequestProperty(CONTENT_LENGTH,
                    Integer.toString(reqData.length));

            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            out.write(reqData, 0, reqData.length);
            out.flush();

            return URLResponse.from(conn);
        }

    }

}
