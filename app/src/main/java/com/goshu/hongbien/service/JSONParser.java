package com.goshu.hongbien.service;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * Service Handler
 */
public class JSONParser {

    static InputStream response = null;
    static JSONObject jsonObj = null;
    static String jsonStr = "";

    static StringBuilder sbParams = null;
    static URL urlObj = null;
    static HttpURLConnection conn = null;
    static String paramsStr = "";
    static DataOutputStream dos = null;
    static StringBuilder results = null;

    public final static int GET = 1;
    public final static int POST = 2;

    public JSONParser() {

    }

    /**
     * Get JSON data from URL
     * @url - url to make request
     */
    public JSONObject getJSONFromUrl(String url) {
        // Making HTTP request
        try {
            // Http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            Log.e("Encoding Error", e.getStackTrace().toString());
        } catch (ClientProtocolException e) {
            Log.e("Client Protocol Error", e.getStackTrace().toString());
        } catch (IOException e) {
            Log.e("IO error", e.getStackTrace().toString());
        } catch (Exception e) {
            Log.d("Error", e.getLocalizedMessage());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            response.close();
            jsonStr = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // Parse the string to a JSON object
        try {
            jsonObj = new JSONObject(jsonStr);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON
        return jsonObj;
    }

    /**
     * Make service call
     * @url - url to make request
     * @medthod - http request method
     * @params - http request params
     */
    public JSONObject makeServiceCall(String url, int method, List<NameValuePair> params) {
        try {
            // Http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Check http request medthod
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpResponse = httpClient.execute(httpPost);
            } else if (method == GET) {
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            response = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            Log.e("Encoding Error", e.getStackTrace().toString());
        } catch (ClientProtocolException e) {
            Log.e("Client Protocol Error", e.getStackTrace().toString());
        } catch (IOException e) {
            Log.e("IO error", e.getStackTrace().toString());
        } catch (Exception e) {
            Log.d("Error", e.getLocalizedMessage());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            response.close();
            jsonStr = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // Parse the string to a JSON object
        try {
            jsonObj = new JSONObject(jsonStr);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // Return JSON
        return jsonObj;
    }

    public JSONObject makeHttpRequest(String url, int method, HashMap<String, String> params) {
        sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                sbParams.append(key).append("=").append(URLEncoder.encode(params.get(key), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e("Encoding Error", e.getStackTrace().toString());
            }
            i++;
        }

        if (method == POST) {
            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", "utf-8");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.connect();

                paramsStr = sbParams.toString();
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(paramsStr);
                dos.flush();
                dos.close();
            } catch (IOException e) {
                Log.e("URL Error", e.getStackTrace().toString());
            }
        } else if (method == GET) {
            if (sbParams.length() != 0) {
                url += "?" + sbParams.toString();
            }

            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accepted-Charset", "utf-8");
                conn.setConnectTimeout(15000);
                conn.connect();
            } catch (IOException e) {
                Log.e("URL Error", e.getStackTrace().toString());
            }
        }

        try {
            // Receive the response from server
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(is));
            results = new StringBuilder();
            String line;

            while ((line = bfReader.readLine()) != null) {
                results.append(line);
            }

            Log.d("JSON Parser", "result: " + results.toString());
        } catch (IOException e) {
            Log.e("Input Stream Error", e.getStackTrace().toString());
        }

        conn.disconnect();

        // Parse the string to JSON object
        try {
            jsonObj = new JSONObject(results.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.getStackTrace().toString());
        }

        // Return JSON
        return jsonObj;
    }
}
