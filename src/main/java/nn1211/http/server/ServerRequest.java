package nn1211.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import nn1211.http.Content;
import nn1211.http.HttpMethod;
import nn1211.http.QueryString;
import nn1211.io.InStream;
import nn1211.io.URICoder;

/**
 * A HTTP request at server side.
 *
 * @author nn1211
 * @since 1.0
 */
public abstract class ServerRequest {

    /**
     * Return a {@link ServerRequest} wrapper for a {@link Socket}.
     *
     * @param socket connection
     * @return a {@link ServerRequest} wrapper for a {@link Socket}.
     * @throws java.io.IOException I/O exception
     * @since 1.0
     */
    public static ServerRequest from(Socket socket) throws IOException {
        return SocketRequest.wrap(socket);
    }

    /**
     * Get the body of this request.
     *
     * @return the body of this request.
     * @since 1.0
     */
    public abstract Content body();

    /**
     * Get the client's IP.
     *
     * @return the client's IP
     * @since 1.0
     */
    public abstract String clientIP();

    /**
     * Get the value of a HTTP header.
     *
     * @param name a header's name
     * @return the value of a HTTP header
     * @since 1.0
     */
    public abstract String header(String name);

    /**
     * Get the request method.
     *
     * @return the request method
     * @since 1.0
     */
    public abstract String method();

    /**
     * Get the value of a parameter.
     *
     * @param name parameter's name
     * @return found value or null
     * @since 1.0
     */
    public abstract String param(String name);

    /**
     * Get the URI of this request.
     *
     * @return the URI of this request.
     * @since 1.0
     */
    public abstract String uri();

    /**
     *
     * @since 1.0
     */
    @Override
    public String toString() {
        return clientIP() + ":" + method() + " " + uri();
    }

    /**
     * A {@link ServerRequest} wrapper for a {@link Socket}.
     *
     * @author nn1211
     * @since 1.0
     */
    static final class SocketRequest extends ServerRequest {

        /**
         *
         * @since 1.0
         */
        private final Map<String, String> params = new HashMap<>();

        /**
         *
         * @since 1.0
         */
        private String method;

        /**
         *
         * @since 1.0
         */
        private String uri;

        /**
         *
         * @since 1.0
         */
        private String query;

        /**
         *
         * @since 1.0
         */
        private final Socket conn;

        /**
         * Wrap a {@link Socket} to a new instance.
         *
         * @param socket
         * @since 1.0
         */
        private SocketRequest(Socket socket) {
            conn = socket;
        }

        /**
         * Wrap a {@link Socket} to a new {@link ServerRequest} instance
         *
         * @param socket
         * @return a new {@link ServerRequest} instance
         * @throws IOException I/O exception
         * @since 1.0
         */
        static ServerRequest wrap(Socket socket) throws IOException {
            SocketRequest req = new SocketRequest(socket);
            req.readRequestLine();
            return req;
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public Content body() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String clientIP() {
            return conn.getLocalAddress().toString();
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public String header(String name) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public String method() {
            return method;
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public String param(String name) {
            return params.get(name);
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public String uri() {
            return uri;
        }

        /**
         *
         * @throws IOException I/O exception
         * @since 1.0
         */
        private void readRequestLine() throws IOException {
            InputStream in = conn.getInputStream();
            if (null == (method = HttpMethod.from(in))) {
                return;
            }

            if (null == (uri = readURI(in))) {
                return;
            }

            int qi = uri.indexOf(QueryString.QUERY_CHAR);
            if (-1 != qi) {
                query = uri.substring(qi + 1);
                uri = uri.substring(0, qi);
                parseQuery();
            }
        }

        /**
         * Read the request's URI from the request's input stream.
         *
         * @param in request's input stream
         * @return request's URI or null in the case, the request is invalid
         * @throws IOException I/O exception
         * @since 1.0
         */
        private String readURI(InputStream in) throws IOException {
            return InStream.readString(in, 1024);
        }

        /**
         * Parse query string into parameters.
         *
         * @since 1.0
         */
        private void parseQuery() {
            System.out.println(query);
            String[] fields = query.split(QueryString.FIELD_SEPARATOR);
            for (String field : fields) {
                String[] parts = field.split(QueryString.NAME_VALUE_SEPARATOR);
                if (parts.length == 2) {
                    params.put(parts[0], URICoder.decode(parts[1]));
                }
            }
        }

    }
}
