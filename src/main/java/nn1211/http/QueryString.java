package nn1211.http;

import java.util.HashMap;

/**
 *
 * @author nn1211
 */
public class QueryString extends URIEncodedString {

    /**
     * ?
     *
     * @since 1.0
     */
    public static final char QUERY_CHAR = '?';

    /**
     * Create an empty query string.
     *
     * @since 1.0
     */
    public QueryString() {
        super(new HashMap<>());
    }

    /**
     *
     * @return an encoded query string
     * @since 1.0
     */
    @Override
    public String toString() {
        return QUERY_CHAR + super.toString();
    }

}
