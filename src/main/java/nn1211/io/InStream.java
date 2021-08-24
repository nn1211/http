package nn1211.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream utility.
 *
 * @author nn1211
 * @since 1.0
 */
public final class InStream {

    /**
     *
     * @since 1.0
     */
    private InStream() {
    }

    /**
     * Read a string from an input stream.
     *
     * @param in the given input stream
     * @param bufferSize size of the buffer
     * @return a string or null in the cases max buffer size was exceeded or end
     * of stream
     * @throws IOException I/O exception
     * @since 1.0
     */
    public static String readString(InputStream in, int bufferSize)
            throws IOException {

        byte[] buf = new byte[bufferSize];
        int read = 0;
        int b;

        while ((b = in.read()) != -1) {
            if (b == 32) {
                byte[] method = new byte[read];
                System.arraycopy(buf, 0, method, 0, read);
                return new String(method);
            }

            if (read < buf.length) {
                buf[read++] = (byte) b;
                continue;
            }

            break;
        }

        return null;
    }

}
