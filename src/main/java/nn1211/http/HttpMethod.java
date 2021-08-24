package nn1211.http;

import java.io.IOException;
import java.io.InputStream;
import nn1211.io.InStream;

/**
 * A HTTP request's method.
 *
 * @author nn1211
 * @since 1.0
 */
public final class HttpMethod {

    /**
     * Get from a request's input stream.
     *
     * @param in request's input stream
     * @return a HTTP request method or null in the case request is invalid
     * @throws java.io.IOException I/O exception
     * @since 1.0
     */
    public static String from(InputStream in) throws IOException {
        return InStream.readString(in, 4);
    }
}
