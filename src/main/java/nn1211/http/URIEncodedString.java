package nn1211.http;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import nn1211.http.Content.Form;
import nn1211.io.URICoder;

/**
 * A query string.
 *
 * @author nn1211
 * @since 1.0
 */
public class URIEncodedString {

    /**
     * &amp;
     *
     * @since 1.0
     */
    public static final String FIELD_SEPARATOR = "&";

    /**
     * =
     *
     * @since 1.0
     */
    public static final String NAME_VALUE_SEPARATOR = "=";

    /**
     *
     * @since 1.0
     */
    private static final String EMPTY = "";

    /**
     *
     * @since 1.0
     */
    private final Map<String, String> data;

    private Charset charset = Charset.defaultCharset();

    /**
     * Create an empty URI encoded string.
     *
     * @since 1.0
     */
    public URIEncodedString() {
        data = new HashMap<>();
    }
    
    /**
     * Create a URI encoded string from a map.
     *
     * @param data a map
     * @since 1.0
     */
    protected URIEncodedString(Map<String, String> data) {
        this.data = data;
    }

    /**
     * Return the current character encoding.
     *
     * @return the current character encoding
     */
    public final Charset charset() {
        return charset;
    }

    /**
     * Set the character encoding for this string.
     *
     * @param value a {@link Charset}
     * @return this
     * @since 1.0
     */
    public URIEncodedString charset(Charset value) {
        charset = value;
        return this;
    }

    /**
     * Return the corresponding value of a specific field's name.
     *
     * @param name field's name
     * @return the corresponding value or null
     * @since 1.0
     */
    public String get(String name) {
        return data.get(name);
    }

    /**
     * Put a field (name and value) into this.
     *
     * @param name field's name
     * @param value field's value
     * @return this
     */
    public URIEncodedString set(String name, String value) {
        data.put(name, value);
        return this;
    }
    
    public final Form asForm() {
        return null;
    }

    /**
     *
     * @since 1.0
     */
    @Override
    public String toString() {
        if (data.isEmpty()) {
            return EMPTY;
        }

        final StringBuilder sb = new StringBuilder();

        data.forEach((k, v) -> sb.append(k).append(NAME_VALUE_SEPARATOR)
                .append(URICoder.encode(v, charset)).append(FIELD_SEPARATOR));

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

}
