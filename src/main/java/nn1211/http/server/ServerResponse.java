package nn1211.http.server;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import nn1211.http.Content;
import nn1211.http.Content.TextContent;
import nn1211.http.HttpResponse;
import static nn1211.http.HttpHeader.*;

/**
 *
 * @author nn1211
 * @since 1.0
 */
public abstract class ServerResponse implements HttpResponse {

    /**
     * Return a 404 response
     *
     * @return a 404 response
     * @since 1.0
     */
    public static ServerResponse badRequest() {
        return new DefaultResponse(StatusCode.NOT_FOUND,
                TextContent.from("404"));
    }

    /**
     * Return a 200 response
     *
     * @param data response data
     * @return a 200 response
     * @since 1.0
     */
    public static ServerResponse ok(String data) {
        return new DefaultResponse(StatusCode.OK,
                TextContent.from(data));
    }

    /**
     * Utility method to loop on each header.
     *
     * @param consumer handler
     * @since 1.0
     */
    public abstract void forEachHeader(BiConsumer<String, String> consumer);

    /**
     * Set a response's header.
     *
     * @param name header's name
     * @param value header's value
     * @return this
     * @since 1.0
     */
    public abstract ServerResponse header(String name, String value);

    /**
     * Get the status code.
     *
     * @return the status code
     * @since 1.0
     */
    public abstract StatusCode statusCode();

    /**
     * A status code of a HTTP response
     *
     * @author nn1211
     * @since 1.0
     */
    public static final class StatusCode {

        /**
         * 404 status code
         *
         * @since 1.0
         */
        public static final StatusCode NOT_FOUND
                = new StatusCode(404, "Not Found");

        /**
         * 200 status code
         *
         * @since 1.0
         */
        public static final StatusCode OK
                = new StatusCode(200, "OK");

        private final int code;
        private final String value;

        /**
         * Create an instance from given code and description.
         *
         * @param code status code
         * @param desc status description
         * @since 1.0
         */
        private StatusCode(int code, String desc) {
            this.code = code;
            this.value = code + " " + desc;
        }

        /**
         * Get as an integer.
         *
         * @return status code as an integer
         * @since 1.0
         */
        public int asInt() {
            return code;
        }

        /**
         * Determine the same value or not.
         *
         * @param value a status code value
         * @return true if same code, otherwise false
         * @since 1.0
         */
        public boolean equals(int value) {
            return code == value;
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Default {@link HttpResponse}'s implementation.
     *
     * @author nn1211
     * @since 1.0
     */
    static final class DefaultResponse extends ServerResponse {

        /**
         *
         * @since 1.0
         */
        private final Map<String, String> headers = new HashMap<>();

        /**
         *
         * @since 1.0
         */
        private final StatusCode statusCode;

        /**
         *
         * @since 1.0
         */
        private final Content body;

        /**
         * Create an instance with given status code and content.
         *
         * @param statusCode a valid status code of a HTTP response
         * @param content the content of this response
         * @since 1.0
         */
        DefaultResponse(StatusCode statusCode, Content content) {
            this.statusCode = statusCode;
            body = content;

            headers.put(CONTENT_TYPE, content.type());
            headers.put(CONTENT_LENGTH, Integer.toString(content.length()));
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public Content body() {
            return body;
        }

        @Override
        public void forEachHeader(BiConsumer<String, String> consumer) {
            headers.forEach(consumer);
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public ServerResponse header(String name, String value) {
            headers.put(name, value);
            return this;
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public StatusCode statusCode() {
            return statusCode;
        }

    }

}
