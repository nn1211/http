package nn1211.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import nn1211.http.Content;
import static nn1211.http.HttpHeader.*;

/**
 * A simple HTTP/1.0 server.
 *
 * @author nn1211
 * @since 1.0
 */
public final class HttpServer {

    /**
     *
     * @since 1.0
     */
    private ServerSocket listener;

    /**
     *
     * @since 1.0
     */
    private volatile boolean isRunning = false;

    /**
     *
     * @since 1.0
     */
    private int port = 12183;

    /**
     *
     * @since 1.0
     */
    private final Map<String, Handler> handlers = new HashMap<>();
    
    /**
     * Return the listening port.
     * 
     * @return the listening port
     * @since 1.0
     */
    public int port() {
        return port;
    }

    /**
     * Set the listening port of this server.
     *
     * @param value
     * @return this
     * @throws IllegalStateException if running
     * @since 1.0
     */
    public synchronized HttpServer port(int value) {
        if (isRunning) {
            throw new IllegalStateException();
        }

        port = value;
        return this;
    }

    /**
     * Register an handler for a specific path.
     *
     * @param path request path
     * @param handler a {@link Handler}
     * @return this
     * @since 1.0
     */
    public HttpServer registerHandler(String path, Handler handler) {
        handlers.put(path, handler);
        return this;
    }

    /**
     * Start this HTTP server.
     *
     * @throws IOException I/O exception
     * @throws IllegalStateException if running
     * @since 1.0
     */
    public synchronized void start() throws IOException {
        if (isRunning) {
            throw new IllegalStateException("Already running");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> stop()));

        listener = new ServerSocket(port);
        isRunning = true;

        new Thread(() -> {
            while (isRunning) {
                accept();
            }
        }).start();
    }

    /**
     * Stop this HTTP server.
     *
     * @since 1.0
     */
    public void stop() {
        if (!isRunning) {
            return;
        }

        isRunning = false;

        try {
            listener.close();
        } catch (IOException ioEx) {
            ioEx.printStackTrace(System.err);
        }
    }

    /**
     *
     * @since 1.0
     */
    private void accept() {
        try {
            Socket conn = listener.accept();
            new Thread(() -> handle(conn)).start();
        } catch (IOException ioEx) {
            ioEx.printStackTrace(System.err);
        }
    }

    /**
     *
     * @since 1.0
     */
    private void handle(Socket conn) {
        try {
            ServerRequest req = ServerRequest.from(conn);
            if (null == req.method() || null == req.uri()) {
                System.out.println("Rejected an invalid request from "
                        + conn.getLocalAddress() + "\r\n");

                close(conn);
                return;
            }

            System.out.println(req);

            ServerResponse resp;
            Handler handler = handlers.get(req.method() + " " + req.uri());
            if (null == handler) {
                resp = ServerResponse.badRequest();
            } else {
                resp = handler.handle(req);
            }

            writeResponse(conn.getOutputStream(), resp);
            close(conn);
        } catch (IOException ioEx) {
            ioEx.printStackTrace(System.err);
        }
    }

    /**
     * Write the server's response to client.
     *
     * @param out the output stream
     * @param resp server response
     * @throws IOException I/O exception
     * @since 1.0
     */
    private void writeResponse(OutputStream out, ServerResponse resp)
            throws IOException {

        resp.header(CONNECTION, "close");

        StringBuilder sb = new StringBuilder().append("HTTP/1.1 ")
                .append(resp.statusCode())
                .append("\r\n");

        resp.forEachHeader((k, v) -> sb.append(k).append(": ")
                .append(v).append("\r\n"));

        sb.append("\r\n");

        out.write(sb.toString().getBytes());

        Content respBody = resp.body();
        if (null != respBody) {
            out.write(respBody.toBytes());
        }

        out.flush();

        System.out.println(resp.statusCode() + "\r\n");
    }

    /**
     * Try to read all request data and close the socket.
     *
     * @param socket
     * @throws IOException I/O exception
     * @since 1.0
     */
    private void close(Socket socket) throws IOException {
        try (InputStream in = socket.getInputStream()) {
            int b;
            while (-1 != (b = in.read())) {
            }

            socket.close();
        }
    }

}
