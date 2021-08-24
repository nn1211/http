package nn1211.http;

import java.io.IOException;

/**
 * A HTTP response message.
 *
 * @author nn1211
 * @since 1.0
 */
public interface HttpResponse {

    /**
     * Get the body of this response.
     *
     * @return the body of this response
     * @throws java.io.IOException I/0 exception
     * @since 1.0
     */
    Content body() throws IOException;

}
