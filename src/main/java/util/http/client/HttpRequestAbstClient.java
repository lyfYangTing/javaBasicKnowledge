package util.http.client;


import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import util.http.exception.HttpClientException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * http请求客户端
 *
 * @author luffy
 * @date 15/6/1
 */
public abstract class HttpRequestAbstClient {

    static HttpClient httpClient;


    //设置连接超时时间
    static int REQUEST_TIMEOUT = 20 * 1000;  //设置请求超时20秒钟
    static int SO_TIMEOUT = 20 * 1000;       //设置等待数据超时时间20秒钟
    static int MAX_PER_ROUTE = 20;        //每个主机的最大并行链接数
    static int MAX_TOTAL = 100;           //客户端总并行链接最大数
    static String DEFAULT_CHARSET = "UTF-8";
//    @Resource
//    private CacheManager cacheManager;

    /**
     * 获取http client
     *
     * @return
     */
    private HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * GET请求
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public String doGet(String url, Map<String, ? extends Object> params) throws HttpClientException {
        HttpGet get = null;
        String responseStr = "";
        try {
            URI uri = generateURLParams(url, params);
            get = new HttpGet(uri);
            HttpResponse httpResponse = getHttpClient().execute(get);
            responseStr = generateHttpResponse(httpResponse);
        } catch (Exception e) {
            throw new HttpClientException(e.getMessage(), e);
        } finally {
            if (get != null) get.releaseConnection();
        }
        return responseStr;
    }
    /**
     * GET请求
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
//    public String doGetWithSign(String url, Map<String, Object> params) throws HttpClientException {
//        params.put("appId", WSConfigure.APPID);
//        params.put("signature", WSConfigure.SIGN);
//        params.put("timestamp", new Date());
//        return doGet(url,params);
//    }

    /**
     * GET请求，获取二进制数据
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public byte[] doGetResource(String url, Map<String, ? extends Object> params) throws HttpClientException {
        HttpGet get = null;
        byte[] bytes = null;
        try {
            URI uri = generateURLParams(url, params);
            get = new HttpGet(uri);
            HttpResponse httpResponse = getHttpClient().execute(get);
            bytes = generateHttpByteResponse(httpResponse);
        } catch (Exception e) {
            throw new HttpClientException(e.getMessage(), e);
        } finally {
            if (get != null) get.releaseConnection();
        }
        return bytes;
    }

    /**
     * POST请求，请求参数支持json、xml、表单
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public String doPost(String url, ContentType contentType, String params) throws HttpClientException {
        HttpPost post = new HttpPost(url);
        String responseStr = null;
        try {
            if (ContentType.APPLICATION_JSON == contentType) {
                HttpEntity entity = new StringEntity(params, DEFAULT_CHARSET);
                post.setEntity(entity);
            } else if (ContentType.APPLICATION_XML == contentType) {
                HttpEntity entity = new StringEntity(params, DEFAULT_CHARSET);
                post.setEntity(entity);
            } else if (ContentType.APPLICATION_FORM_URLENCODED == contentType) {
                HttpEntity entity = new UrlEncodedFormEntity(getPostParams(JSONObject.fromObject(params)), DEFAULT_CHARSET);
                post.setEntity(entity);
            }
            post.setHeader("Content-Type", contentType.getMimeType());
            HttpResponse httpResponse = getHttpClient().execute(post);
            responseStr = generateHttpResponse(httpResponse);
        } catch (Exception e) {
            throw new HttpClientException(e.getMessage(), e);
        } finally {
            post.releaseConnection();
        }
        return responseStr;
    }

    /**
     * POST请求，请求参数支持json、xml、表单
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public String doPut(String url, ContentType contentType, String params) throws HttpClientException {
        HttpPut put = new HttpPut(url);
        String responseStr = null;
        try {
            if (ContentType.APPLICATION_JSON == contentType) {
                HttpEntity entity = new StringEntity(params, DEFAULT_CHARSET);
                put.setEntity(entity);
            } else if (ContentType.APPLICATION_XML == contentType) {
                HttpEntity entity = new StringEntity(params, DEFAULT_CHARSET);
                put.setEntity(entity);
            } else if (ContentType.APPLICATION_FORM_URLENCODED == contentType) {
                HttpEntity entity = new UrlEncodedFormEntity(getPostParams(JSONObject.fromObject(params)), DEFAULT_CHARSET);
                put.setEntity(entity);
            }
            put.setHeader("Content-Type", contentType.getMimeType());
            HttpResponse httpResponse = getHttpClient().execute(put);
            responseStr = generateHttpResponse(httpResponse);
        } catch (Exception e) {
            throw new HttpClientException(e.getMessage(), e);
        } finally {
            put.releaseConnection();
        }
        return responseStr;
    }

    /**
     * POST请求，请求参数支持json、xml、表单
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
//    public String doPostWithSign(String url, ContentType contentType, com.alibaba.fastjson.JSONObject params) throws HttpClientException {
//
//        params.put("appId", WSConfigure.APPID);
//        params.put("signature", WSConfigure.SIGN);
//        params.put("timestamp", new Date());
//
//        String responseStr = doPost(url, contentType, params.toString());
//        return responseStr;
//    }

    /**
     * 上传文件
     *
     * @param url
     * @param filesMap
     * @param params
     * @return
     */
    public String upload(String url, Map<String, File> filesMap, Map<String, ? extends Object> params) {

        if (filesMap == null || filesMap.size() <= 0) {
            throw new IllegalArgumentException("文件为空");
        }

        HttpPost post = new HttpPost(url);
        String responseStr = null;
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            ContentType contentType = ContentType.create("multipart/form-data", Charset.forName(DEFAULT_CHARSET));
            if (params != null) {
                params.forEach((key, value) -> builder.addPart(key, new StringBody(String.valueOf(value), contentType)));
            }
            filesMap.forEach((key, value) -> builder.addPart(key, new FileBody(value, contentType)));
            post.setEntity(builder.build());
            HttpResponse httpResponse = getHttpClient().execute(post);
            responseStr = generateHttpResponse(httpResponse);
        } catch (Exception e) {
            throw new HttpClientException(e.getMessage(), e);
        } finally {
            post.releaseConnection();
        }
        return responseStr;
    }

    /**
     * 设置URL链接参数
     *
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     */
    private URI generateURLParams(String url, Map<String, ? extends Object> params) throws URISyntaxException {
        URI uri = null;
        URIBuilder uriBuilder = new URIBuilder(url);
        if (null != params) {
            for (Map.Entry<String, ? extends Object> entry : params.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        uri = uriBuilder.build();
        return uri;
    }

    /**
     * 获取post参数
     *
     * @param params
     * @return
     */
    private List<NameValuePair> getPostParams(JSONObject params) {
        if (params == null) return null;
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Iterator<String> keys = params.keys();
        int i = 0;
        while (keys.hasNext()) {
            String key = keys.next();
            nameValuePairs.add(new BasicNameValuePair(key, params.getString(key)));
            i++;
        }
        return nameValuePairs;
    }

    /**
     * 执行HTTP请求，获取结果
     *
     * @param httpResponse
     * @return
     * @throws IOException
     */
    private String generateHttpResponse(HttpResponse httpResponse) throws IOException, HttpClientException {
        String responseStr = "";
        if (null != httpResponse && (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode() || HttpStatus.SC_CREATED == httpResponse.getStatusLine().getStatusCode())) {
            HttpEntity entity = httpResponse.getEntity();
            responseStr = EntityUtils.toString(entity, DEFAULT_CHARSET);
        } else {
            HttpEntity entity = httpResponse.getEntity();
            responseStr = entity != null ? EntityUtils.toString(entity, DEFAULT_CHARSET) : "";
            throw new HttpClientException("请求失败，code: " + httpResponse.getStatusLine().getStatusCode() + (StringUtils.isNotEmpty(responseStr) ? ", content: " + responseStr : ""));
        }
        return responseStr;
    }

    /**
     * 执行HTTP请求，获取二进制数据
     *
     * @param httpResponse
     * @return
     * @throws IOException
     */
    private byte[] generateHttpByteResponse(HttpResponse httpResponse) throws IOException, HttpClientException {
        byte[] bytes = null;
        if (null != httpResponse && HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
            HttpEntity entity = httpResponse.getEntity();
            bytes = EntityUtils.toByteArray(entity);
        } else {
            throw new HttpClientException("请求失败，code: " + httpResponse.getStatusLine().getStatusCode());
        }
        return bytes;
    }
}
