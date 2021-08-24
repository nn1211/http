package nn1211.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import nn1211.http.Content;
import nn1211.http.Content.TextContent;
import nn1211.http.HttpResponse;

/**
 *
 * @author nn1211
 */
public abstract class ClientResponse implements HttpResponse {

    /**
     * Get the value of a HTTP header.
     *
     * @param name header's name
     * @return the value of a HTTP header or null.
     * @since 1.0
     */
    public abstract String header(String name);

    /**
     * Get the status code of this response if any.
     *
     * @return the status code of this response
     * @since 1.0
     */
    public abstract int statusCode();

    /**
     * A wrapper for {@link URLConnection} as a HTTP response.
     *
     * @author nn1211
     * @since 0.0.1
     */
    public static class URLResponse extends ClientResponse {

        private final int statusCode;
        private final HttpURLConnection conn;

        private volatile boolean isBodyRead = false;
        private Content body;

        /**
         * @param conn a {@link HttpURLConnection}
         * @throws IOException
         * @since 0.0.1
         */
        private URLResponse(HttpURLConnection conn) throws IOException {
            statusCode = conn.getResponseCode();
            this.conn = conn;
        }

        /**
         * Wrap a {@link HttpURLConnection} into a {@link HttpResponse}.
         *
         * @param conn a {@link HttpURLConnection}
         * @return a {@link HttpResponse}
         * @throws java.io.IOException I/O exception
         * @since 1.0
         */
        public static ClientResponse from(HttpURLConnection conn) throws IOException {
            return new URLResponse(conn);
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public Content body() throws IOException {
            //conn.getHeaderField(CONTENT_TYPE);
            if (!isBodyRead) {
                Lock lock = new ReentrantLock();
                lock.lock();
                try {
                    if (!isBodyRead) {
                        InputStream in;
                        if (200 == statusCode) {
                            in = conn.getInputStream();
                        } else {
                            in = conn.getErrorStream();
                        }
                        
                        byte[] data = new byte[1024 * 64];
                        int count = 0;

                        byte[] buf = new byte[1024 * 8];

                        int read;
                        while ((read = in.read(buf)) > 0) {
                            System.arraycopy(buf, 0, data, count, read);
                            count += read;
                        }
                        
                        in.close();
                        
                        byte[] stripData = new byte[count];
                        System.arraycopy(data, 0, stripData, 0, count);
                        
                        body = TextContent.from(stripData, 
                                StandardCharsets.UTF_8);

                        isBodyRead = true;
                    }
                } finally {
                    lock.unlock();
                }
            }

            return body;
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public String header(String name) {
            return conn.getHeaderField(name);
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public int statusCode() {
            return statusCode;
        }

    }

}
