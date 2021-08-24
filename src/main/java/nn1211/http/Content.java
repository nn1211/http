package nn1211.http;

import java.nio.charset.Charset;

/**
 * A content.
 *
 * @author nn1211
 * @since 1.0
 */
public abstract class Content {

    /**
     * application/octet-stream
     *
     * @since 1.0
     */
    private static final String BINARY = "application/octet-stream";

    /**
     * application/x-www-form-urlencoded
     *
     * @since 1.0
     */
    private static final String FORM = "application/x-www-form-urlencoded";

    /**
     * text/html
     *
     * @since 1.0
     */
    private static final String HTML = "text/html";

    /**
     * text/plain
     *
     * @since 1.0
     */
    private static final String TEXT = "text/plain";

    /**
     * Get the encoding of this.
     * <p>
     * <i>Default value is null</i>
     * </p>
     *
     * @return the encoding of this or null if no encoding was applied
     * @since 1.0
     */
    public String encoding() {
        return null;
    }

    /**
     * Try converting this to a {@link TextContent}
     *
     * @return a {@link TextContent} if success, null otherwise
     * @since 1.0
     */
    public TextContent asText() {
        return null;
    }

    /**
     * Determine this content is text type or not.
     *
     * @return true if this content is text type, false otherwise
     * @since 1.0
     */
    public boolean isText() {
        return false;
    }

    /**
     * Get the length in bytes.
     *
     * @return the length in bytes
     * @since 1.0
     */
    public int length() {
        return -1;
    }

    /**
     * Convert this to a byte array.
     *
     * @return a byte array
     * @since 1.0
     */
    public abstract byte[] toBytes();

    /**
     * Get the type of this.
     * <p>
     * <i>Default value is {@link #BINARY}</i>
     * </p>
     *
     * @return the type of this
     * @since 1.0
     */
    public String type() {
        return BINARY;
    }

    /**
     * A byte array content.
     *
     * @author nn1211
     * @since 1.0
     */
    static class ByteArrayContent extends Content {

        private final byte[] data;
        private final String encoding;

        /**
         * Create an instance with a give byte array and an encoding.
         *
         * @param data
         * @param encoding
         * @since 1.0
         */
        ByteArrayContent(byte[] data, String encoding) {
            this.data = data;
            this.encoding = encoding;
        }

        /**
         * Create an instance with a give byte array.
         *
         * @param data
         * @since 1.0
         */
        public ByteArrayContent(byte[] data) {
            this(data, null);
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public String encoding() {
            return encoding;
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public int length() {
            return data.length;
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public final byte[] toBytes() {
            return data;
        }

    }

    /**
     * A base class for all text content classes.
     *
     * @author nn1211
     * @since 1.0
     */
    public static class TextContent extends ByteArrayContent {

        private final String type;
        private final String text;

        /**
         * Create an instance from a text, a type, and a charset.
         *
         * @param text
         * @param type
         * @param charset
         * @since 1.0
         */
        TextContent(String text, String type, Charset charset) {
            super(text.getBytes(charset));
            this.type = type + "; charset=" + charset.name();
            this.text = text;
        }

        /**
         * Create an instance from a text, a type, and a system's default
         * charset.
         *
         * @param text
         * @param type
         * @since 1.0
         */
        TextContent(String text, String type) {
            this(text, type, Charset.defaultCharset());
        }

        /**
         * Create an instance from a text and a charset.
         *
         * @param text data of this content
         * @param charset a charset
         * @since 1.0
         */
        TextContent(String text, Charset charset) {
            this(text, TEXT, charset);
        }

        /**
         * Create an instance from a text.
         *
         * @param text data of this content
         * @since 1.0
         */
        TextContent(String text) {
            this(text, TEXT);
        }

        /**
         * Create an instance from a text.
         *
         * @param text text
         * @return a {@link TextContent} instance
         * @since 1.0
         */
        public static TextContent from(String text) {
            return new TextContent(text);
        }

        /**
         * Create an instance from a byte array and a charset.
         *
         * @param bytes a byte array
         * @param charset a character encoding
         * @return a {@link TextContent}
         * @since 1.0
         */
        public static TextContent from(byte[] bytes, Charset charset) {
            return new TextContent(new String(bytes, charset), charset);
        }
        
        /**
         * Create an instance from a byte array.
         *
         * @param bytes a byte array
         * @return a {@link TextContent}
         * @since 1.0
         */
        public static TextContent from(byte[] bytes) {
            return new TextContent(new String(bytes));
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public TextContent asText() {
            return this;
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public boolean isText() {
            return true;
        }

        /**
         *
         * @return data
         * @since 1.0
         */
        @Override
        public String toString() {
            return text;
        }

        /**
         *
         * @since 1.0
         */
        @Override
        public final String type() {
            return type;
        }

    }

    /**
     * A HTML content.
     *
     * @author nn1211
     * @since 1.0
     */
    static class HtmlContent extends TextContent {

        private HtmlContent(String html, Charset charset) {
            super(html, HTML, charset);
        }

        private HtmlContent(String html) {
            this(html, Charset.defaultCharset());
        }

    }

    /**
     * A HTML URL encoded form.
     *
     * @author nn1211
     * @since 1.0
     */
    public static final class Form extends TextContent {

        /**
         *
         * @param data
         * @param charset
         * @since 1.0
         */
        private Form(String data, Charset charset) {
            super(data, FORM, charset);
        }

        /**
         * Get a new form builder.
         *
         * @return a new form builder
         * @since 1.0
         */
        public static Builder newBuilder() {
            return new Builder();
        }

        /**
         * Get a new form builder that is filled up with a given
         * {@link URIEncodedString}.
         *
         * @param data a {@link URIEncodedString}
         * @return
         * @since 1.0
         */
        public static Builder newBuilder(URIEncodedString data) {
            return new Builder(data);
        }

        /**
         * A builder of a {@link Form}.
         *
         * @author nn1211
         * @since 1.0
         */
        public static final class Builder {

            private final URIEncodedString data;

            /**
             * Create an instance with an empty {@link URIEncodedString}.
             *
             * @since 1.0
             */
            private Builder() {
                this(new URIEncodedString());
            }

            /**
             * Create an instance with a given {@link URIEncodedString}.
             *
             * @param data a {@link URIEncodedString}
             * @since 1.0
             */
            private Builder(URIEncodedString data) {
                this.data = data;
            }

            /**
             * Set the form's charset.
             *
             * @param charset form's charset
             * @return this
             * @since 1.0
             */
            public Builder charset(Charset charset) {
                this.data.charset(charset);
                return this;
            }

            /**
             * Set a form's field.
             *
             * @param name field's name
             * @param value field's value
             * @return this
             * @since 1.0
             */
            public Builder set(String name, String value) {
                data.set(name, value);
                return this;
            }

            /**
             * Build a new from based on given information.
             *
             * @return a new {@link Form}
             * @since 1.0
             */
            public Form build() {
                return new Form(data.toString(), data.charset());
            }
        }

    }

}
