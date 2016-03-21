package com.goshu.hongbien.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Service Handler
 */
public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {

    }

    /**
     * Make service call
     * @url - url to make request
     * @medthod - http request method
     */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Make service call
     * @url - url to make request
     * @medthod - http request method
     * @params - http request params
     */
    public String makeServiceCall(String url, int method, List<NameValuePair> params) {
        try {
            // Http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntry = null;
            HttpResponse httpResponse = null;

            // Check http request medthod
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpResponse = httpClient.execute(httpPost);
            } else if (method == GET) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
//                if (params != null) {
//                    String paramString = URLEncodedUtils.format(params, "utf-8");
//                    url += "?" + paramString;
//                }
//                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpPost);
            }
            httpEntry = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntry);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
