package nn1211.http.server;

import java.io.IOException;

/**
 * A handler for a HTTP request.
 *
 * @author nn1211
 * @since 1.0
 */
@FunctionalInterface
public interface Handler {

    /**
     * Handle a HTTP request and return a HTTP response.
     *
     * @param req User request
     * @return a {@link ServerResponse}
     * @throws IOException I/O exception
     * @since 1.0
     */
    ServerResponse handle(ServerRequest req) throws IOException;
}
