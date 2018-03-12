package util.http.client;

import com.infofuse.util.http.exception.HttpClientException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * ssl http client
 *
 * @author luffy
 * @date 15/6/1
 */
public class SSLHttpRequestClient extends HttpRequestAbstClient {

    private static SSLHttpRequestClient sslHttpRequestClient;

    private SSLHttpRequestClient() {}

    public static synchronized SSLHttpRequestClient getInstance() throws HttpClientException {
        if(sslHttpRequestClient == null) {
            sslHttpRequestClient = new SSLHttpRequestClient();
            try {
                sslHttpRequestClient.setHttpClient();
            } catch (Exception e) {
                throw new HttpClientException(e.getMessage(), e);
            }
        }
        return sslHttpRequestClient;
    }

    private void setHttpClient() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);

        X509TrustManager x509TrustMgr = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[]{x509TrustMgr}, null);
        SSLSocketFactory socketFactory = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        SchemeRegistry schreg = new SchemeRegistry();
        schreg.register(new Scheme("https", 443, socketFactory));

        PoolingClientConnectionManager pccm = new PoolingClientConnectionManager(schreg);
        pccm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        pccm.setMaxTotal(MAX_TOTAL);

        httpClient = new DefaultHttpClient(pccm, params);
    }
}
