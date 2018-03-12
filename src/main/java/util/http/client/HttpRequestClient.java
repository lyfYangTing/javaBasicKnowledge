package util.http.client;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

/**
 * http client
 *
 * @author luffy
 * @date 15/6/1
 */
public class HttpRequestClient extends HttpRequestAbstClient {

    private static HttpRequestClient httpRequestClient;

    private HttpRequestClient() {}

    public static synchronized HttpRequestClient getInstance() {
        if(httpRequestClient == null) {
            httpRequestClient = new HttpRequestClient();
            httpRequestClient.setHttpClient();
        }
        return httpRequestClient;
    }

    private void setHttpClient() {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);

        PoolingClientConnectionManager conMgr = new PoolingClientConnectionManager();
        conMgr.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        conMgr.setMaxTotal(MAX_TOTAL);

        httpClient = new DefaultHttpClient(conMgr, params);
    }
}
