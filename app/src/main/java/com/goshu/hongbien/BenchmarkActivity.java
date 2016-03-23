package com.goshu.hongbien;

import android.app.Activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.goshu.hongbien.igmodel.IgListOfModels;
import com.goshu.hongbien.igmodel.IgModelRequest;
import com.goshu.hongbien.igmodel.IgModelWorker;
import com.goshu.hongbien.ommodel.OmListOfModels;
import com.goshu.hongbien.ommodel.OmModelRequest;
import com.goshu.hongbien.ommodel.OmModelWorker;

import com.google.common.io.Closeables;
import com.goshu.hongbien.service.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BenchmarkActivity extends Activity {

    private String mJsonString;
    private String mJsonString2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        new HttpGetJsonAsync().execute();

        try {
            mJsonString = loadFromFileBase64(R.raw.input);
            mJsonString2 = loadFromFileJson(R.raw.jsondata);
        } catch (IOException e) {
            Toast.makeText(this, "IOException", Toast.LENGTH_LONG).show();
            return;
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean useIgParser = (view == findViewById(R.id.ig_parse_button));
                boolean useOmParser = (view == findViewById(R.id.om_parse_button));

                BenchmarkStats bs = new BenchmarkStats();

                int iterations = getIterationCount();
                if (iterations == 1) {
                    IgModelRequest igModel;
                    OmModelRequest omModel;
                    try {
                        bs.before();
                        if (useIgParser) {
                            igModel = new IgModelWorker().parseFromString(mJsonString);
                        } else if (useOmParser) {
                            omModel = new OmModelWorker().parseFromString(mJsonString);
                        }
                        bs.after();
                    } catch (IOException ex) {
                        Toast.makeText(BenchmarkActivity.this, "IOException", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                } else {
                    String multiIterationInputString = generateInputString(iterations, mJsonString);
                    String multiIterationInputString2 = generateInputString(iterations, mJsonString2);
                    ((TextView) findViewById(R.id.shows)).setMovementMethod(new ScrollingMovementMethod());
                    ((TextView) findViewById(R.id.shows)).setText(multiIterationInputString2);

                    IgListOfModels igListofModels;
                    OmListOfModels omListofModels;
                    try {
                        bs.before();
                        if (useIgParser) {
                            igListofModels = new IgModelWorker().parseListFromString(multiIterationInputString);
                        } else if (useOmParser) {
                            omListofModels = new OmModelWorker().parseListFromString(multiIterationInputString);
                        }
                        bs.after();
                    } catch (IOException ex) {
                        Toast.makeText(BenchmarkActivity.this, "IOException", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                }

                ((TextView) findViewById(R.id.results)).setText(bs.renderResultsToText());
            }
        };

        findViewById(R.id.ig_parse_button).setOnClickListener(listener);
        findViewById(R.id.om_parse_button).setOnClickListener(listener);

        findViewById(R.id.quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
    }

    class HttpPostJsonAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        private static final String myUrl = "http://hmkcode.appspot.com/rest/controller/get.json";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(BenchmarkActivity.this);
            pDialog.setMessage("Attempting post JSON data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();

                Log.d("Request", "starting");

                JSONObject jsonObj = jsonParser.makeHttpRequest(myUrl, JSONParser.POST, params);

                if (jsonObj != null) {
                    Log.d("JSON result", jsonObj.toString());
                    return jsonObj;
                }
            } catch (Exception e) {
                Log.e("Exception", e.getStackTrace().toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObj) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (jsonObj != null) {
                Toast.makeText(BenchmarkActivity.this, jsonObj.toString(), Toast.LENGTH_LONG).show();
                try {
                    JSONArray articleList = jsonObj.getJSONArray("articleList");
                } catch (JSONException e) {
                    Log.e("JSON Error", e.getStackTrace().toString());
                }
            }
        }
    }

    class HttpGetJsonAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        private static final String myUrl = "http://hmkcode.appspot.com/rest/controller/get.json";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(BenchmarkActivity.this);
            pDialog.setMessage("Attempting get JSON data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();

                Log.d("Request", "starting");

                JSONObject jsonObj = jsonParser.makeHttpRequest(myUrl, JSONParser.GET, params);

                if (jsonObj != null) {
                    Log.d("JSON result", jsonObj.toString());
                    return jsonObj;
                }
            } catch (Exception e) {
                Log.e("Exception", e.getStackTrace().toString());
            }

            return null;
        }

        protected void onPostExecute(JSONObject jsonObj) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (jsonObj != null) {
                Toast.makeText(BenchmarkActivity.this, jsonObj.toString(), Toast.LENGTH_LONG).show();
                String res = "";
                try {
                    JSONArray articleList = jsonObj.getJSONArray("articleList");
                    for (int i=0; i < articleList.length(); i++) {
                        JSONObject tmpObj = articleList.getJSONObject(i);
                        res += "TITLE: " + tmpObj.getString("title") + "\n"
                                + "URL: " + tmpObj.getString("url") + "\n";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((TextView) findViewById(R.id.shows)).setText(res);
            }
        }

    }

    private String generateInputString(int iterations, String mJsonString) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\"list\": [");

        for (int ix = 0; ix < iterations; ix++) {
            if (ix != 0) {
                sb.append(",");
            }
            sb.append(mJsonString);
        }

        sb.append("]}");

        return sb.toString();
    }

    String loadFromFileBase64(int resourceId) throws IOException {
        InputStreamReader inputStreamReader = null;

        try {
            // we're doing this absurd thing with encoding the json file in base64 because phabricator
            // chokes on it otherwise.
            inputStreamReader =
                    new InputStreamReader(
                            new Base64InputStream(getResources().openRawResource(resourceId), Base64.DEFAULT),
                            "UTF-8");
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[8 * 1024];
            int bytesRead;

            while ((bytesRead = inputStreamReader.read(buffer)) != -1) {
                sb.append(buffer, 0, bytesRead);
            }

            return sb.toString();
        } finally {
            Closeables.closeQuietly(inputStreamReader);
        }
    }

    String loadFromFileJson(int resourceId) throws IOException {
        InputStream inputStream = getResources().openRawResource(resourceId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("Read file error", e.getStackTrace().toString());
        }
        return outputStream.toString();
    }

    int getIterationCount() {
        return Integer.valueOf(((EditText) findViewById(R.id.iterations)).getText().toString());
    }
}
