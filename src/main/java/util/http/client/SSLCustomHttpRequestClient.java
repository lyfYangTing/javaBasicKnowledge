package util.http.client;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * 自定义证书ssl client
 *
 * @author luffy
 * @date 15/6/8
 */
public class SSLCustomHttpRequestClient extends HttpRequestAbstClient {

    Logger logger = LoggerFactory.getLogger(SSLCustomHttpRequestClient.class);

    private SSLCustomHttpRequestClient(String keyManageFile, String trustManageFile, String keyPassword) {
        try {
            if(keyPassword == null) keyPassword = "";
            KeyStore ks = KeyStore.getInstance("PKCS12");
            InputStream ksIs = new FileInputStream(keyManageFile);

            try {
                ks.load(ksIs, keyPassword.toCharArray());
            } finally {
                if (ksIs != null) {
                    ksIs.close();
                }
            }
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, keyPassword.toCharArray());


            KeyStore ts = KeyStore.getInstance("jks");
            InputStream tsIs = new FileInputStream(trustManageFile);
            try {
                ts.load(tsIs, keyPassword.toCharArray());
            } finally {
                if (tsIs != null) {
                    tsIs.close();
                }
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);

            SSLContext ctx = SSLContext.getInstance("TLSv1");
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            SchemeRegistry schreg = new SchemeRegistry();
            schreg.register(new Scheme("https", 443, socketFactory));

            PoolingClientConnectionManager pccm = new PoolingClientConnectionManager(schreg);
            pccm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
            pccm.setMaxTotal(MAX_TOTAL);

            HttpParams params = new BasicHttpParams();
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT);
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
            httpClient = new DefaultHttpClient(pccm, params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
