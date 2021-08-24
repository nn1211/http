package nn1211.io;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * An utility is used to encode or decode.
 *
 * @author nn1211
 * @since 1.0
 */
public final class URICoder {

    private static final String CHARSET = Charset.defaultCharset().name();

    /**
     *
     * @since 1.0
     */
    private URICoder() {

    }

    /**
     * Encode a string with a specified charset.
     *
     * @param value an encoding string
     * @param charset a charset encoding
     * @return the encoded string
     * @since 1.0
     */
    public static String encode(String value, String charset) {
        try {
            return URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * Encode a string with a specified charset.
     *
     * @param value an encoding string
     * @param charset a charset encoding
     * @return the encoded string
     * @since 1.0
     */
    public static String encode(String value, Charset charset) {
        return encode(value, charset.name());
    }

    /**
     * Encode a string with system's default charset.
     *
     * @param value an encoding string
     * @return the encoded string
     * @since 1.0
     */
    public static String encode(String value) {
        return encode(value, CHARSET);
    }

    /**
     * Decode an encoded string
     *
     * @param value an encoded string
     * @param charset a charset encoding
     * @return the decoded string
     * @since 1.0
     */
    public static String decode(String value, String charset) {
        try {
            return URLDecoder.decode(value, charset);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * Decode an encoded string with system's default charset.
     *
     * @param value an encoded string
     * @return the decoded string
     * @since 1.0
     */
    public static String decode(String value) {
        return decode(value, CHARSET);
    }
}
