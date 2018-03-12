package util.http.exception;

import com.infofuse.extend.exception.FunctionException;

/**
 * http client异常
 *
 * @author luffy
 * @date 15/6/3
 */
public class HttpClientException extends FunctionException {

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
