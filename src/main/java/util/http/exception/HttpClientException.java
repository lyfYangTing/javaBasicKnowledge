package util.http.exception;

/**
 * http client异常
 *
 * @author luffy
 * @date 15/6/3
 */
public class HttpClientException extends RuntimeException {

    public HttpClientException() {
        super();
    }

    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(String message, Exception e) {
        super(message, e);
    }
}
