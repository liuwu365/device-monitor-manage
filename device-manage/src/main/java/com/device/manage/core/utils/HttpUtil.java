package com.device.manage.core.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/1.
 * httpUtil
 */
public class HttpUtil {
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int SOCKETCOOECTION_TIMEOUT = 5000;
    public static final int NORMAL = 200;
    public static final String ENCODING = "UTF-8";
    private static CloseableHttpClient httpClient = createSSLClientDefault();
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public HttpUtil() {
    }

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext e = (new SSLContextBuilder()).loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(e);
            return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public static String get(String url) throws HttpException {
        Map<String, String> paramsMap = null;
        return get(url, paramsMap);
    }

    public static String get(String url, Map<String, String> paramsMap) throws HttpException {
        if (null == httpClient) {
            httpClient = createSSLClientDefault();
        }

        CloseableHttpClient client = httpClient;
        String responseText = null;
        HttpEntity entity = null;
        CloseableHttpResponse response = null;

        try {
            StringBuilder e = new StringBuilder();
            if (paramsMap != null) {
                Iterator method = paramsMap.entrySet().iterator();

                while (method.hasNext()) {
                    Map.Entry requestConfig = (Map.Entry) method.next();
                    e.append("&" + (String) requestConfig.getKey() + "=" + (String) requestConfig.getValue());
                }

                url = url + "?" + e.toString().substring(1);
            }

            HttpGet method1 = new HttpGet(url);
            RequestConfig requestConfig1 = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(5000).build();
            method1.setConfig(requestConfig1);
            response = client.execute(method1);
            entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new HttpException(responseText);
            } else {
                if (response != null) {
                    response.close();
                }

                return responseText;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new HttpException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpException();
        }
    }

    public static String get(String url, NameValuePair[] nameValuePair) throws HttpException {
        HashMap paramsMap = new HashMap();
        NameValuePair[] arr = nameValuePair;
        int len = nameValuePair.length;

        for (int i = 0; i < len; ++i) {
            NameValuePair t = arr[i];
            paramsMap.put(t.getName(), t.getValue());
        }

        return get(url, (Map) paramsMap);
    }

    public static String post(String url, NameValuePair[] nameValuePair) throws HttpException {
        HashMap paramsMap = new HashMap();
        NameValuePair[] arrHttpException = nameValuePair;
        int lenHttpException = nameValuePair.length;

        for (int iHttpException = 0; iHttpException < lenHttpException; ++iHttpException) {
            NameValuePair t = arrHttpException[iHttpException];
            paramsMap.put(t.getName(), t.getValue());
        }

        return post(url, (Map) paramsMap);
    }

    public static String post(String url, Map<String, Object> paramsMap) throws HttpException {
        if (null == httpClient) {
            httpClient = createSSLClientDefault();
        }

        CloseableHttpClient client = httpClient;
        String responseText = "";
        HttpPost method = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
        method.setConfig(requestConfig);
        HttpEntity entity = null;
        CloseableHttpResponse response = null;

        try {
            if (paramsMap != null) {
                ArrayList e = new ArrayList();
                Iterator i = paramsMap.entrySet().iterator();

                while (i.hasNext()) {
                    Map.Entry<String, Object> param = (Map.Entry) i.next();
                    BasicNameValuePair pair = new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue()));
                    e.add(pair);
                }

                method.setEntity(new UrlEncodedFormEntity(e, ENCODING));
            }

            response = client.execute(method);
            entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }

            if (response != null) {
                response.close();
            }

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new HttpException(responseText);
            } else {
                return responseText;
            }
        } catch (ClientProtocolException e) {
            //e.printStackTrace();
            throw new HttpException();
        } catch (IOException e) {
            //e.printStackTrace();
            throw new HttpException();
        }
    }


    public static String post(String url, String json) {

        CloseableHttpClient httpclient = httpClient;
        HttpPost post = new HttpPost(url);
        String result = null;
        try {
            StringEntity s = new StringEntity(json);
            post.setHeader("Content-Type", "application/json;charset=UTF-8");
            post.setEntity(s);
            HttpResponse res = httpclient.execute(post);
            result = EntityUtils.toString(res.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;

    }


}
