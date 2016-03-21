package com.goshu.hongbien;

import android.app.Activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.widget.TextView;
import android.widget.Toast;

import com.goshu.hongbien.igmodel.IgListOfModels;
import com.goshu.hongbien.igmodel.IgModelRequest;
import com.goshu.hongbien.igmodel.IgModelWorker;
import com.goshu.hongbien.ommodel.OmListOfModels;
import com.goshu.hongbien.ommodel.OmModelRequest;
import com.goshu.hongbien.ommodel.OmModelWorker;

import com.google.common.io.Closeables;
import com.goshu.hongbien.service.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BenchmarkActivity extends Activity {

    private ProgressDialog pDialog;

    // URL to get JSON data
    private static String url = "http://api.beat.vn/api/post/api_get_posts_by_category_id/37";

    // JSOn node names
    private static final String TAG_POST_ID = "post_id";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_ATTACHMENTS = "attachments";
    private static final String TAG_CREATED = "created";
    private static final String TAG_UPDATED = "updated";
    private static final String TAG_STATUS = "status";
    private static final String TAG_VIEWED = "viewed";
    private static final String TAG_LIKED = "liked";
    private static final String TAG_UNlIKED = "unliked";
    private static final String TAG_COMMENTED = "commented";
    private static final String TAG_CATEGORY_ID = "category_id";
    private static final String TAG_USER_ID = "user_id";
    private static final String TAG_IS_ANONYMOUS = "is_anonymous";
    private static final String TAG_SHARED = "shared";
    private static final String TAG_IS_TOP = "is_top";
    private static final String TAG_IS_GHIM = "is_ghim";
    private static final String TAG_TOP_LIKE = "top_like";
    private static final String TAG_CATEGORY_NAME = "category_name";
    private static final String TAG_CATEGORY_SLUG = "category_slug";
    private static final String TAG_CATEGORY_PARENT_SLUG = "category_parent_slug";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_DISPLAY_NAME = "display_name";
    private static final String TAG_AVATAR = "avatar";
    private static final String TAG_IS_LIKED = "is_liked";
    private static final String TAG_IS_SHARED = "is_shared";

    JSONArray posts = null;

    ArrayList<HashMap<String, String>> postList;

    String tmpStr = "";

    private String mJsonString;
    private String mJsonString2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        try {
            mJsonString = loadFromFileBase64(R.raw.input);
            mJsonString2 = loadFromFileJson(R.raw.jsondata);
        } catch (IOException e) {
            Toast.makeText(this, "IOException", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Calling async task to get json data
        new GetPosts().execute();

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

    private class GetPosts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(BenchmarkActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    posts = jsonObj.getJSONArray("data");

                    // looping through all posts
                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject c = posts.getJSONObject(i);

                        int postId = c.getInt(TAG_POST_ID);
                        String content = c.getString(TAG_CONTENT);
                        JSONArray attachments = c.getJSONArray(TAG_ATTACHMENTS);
                        String categoryName = c.getString(TAG_CATEGORY_NAME);
                        String displayName = c.getString(TAG_DISPLAY_NAME);
                        String avatar = c.getString(TAG_AVATAR);

                        tmpStr += "Post ID: " + postId + "\n" +
                                "Content: " + content + "\n" +
                                "Category Name: " + categoryName + "\n" +
                                "Display Name: " + displayName  + "\n" +
                                "Avatar: " + avatar + "\n";


                        // Tmp hashmap for single post
                        HashMap<String, String> postList = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        postList.put(TAG_POST_ID, String.valueOf(postId));
                        postList.put(TAG_CONTENT, content);
                        postList.put(TAG_CATEGORY_NAME, categoryName);
                        postList.put(TAG_DISPLAY_NAME, displayName);
                        postList.put(TAG_AVATAR, avatar);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            /**
             * Updateing parsed JSON data into View
             */
            ((TextView) findViewById(R.id.shows)).setText(tmpStr);
        }
    }

    private String generateInputString(int iterations, String mJsonString) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\"list\": [");

        for (int ix = 0; ix < iterations; ix ++) {
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
        InputStream inputStream = getResources().openRawResource(R.raw.jsondata);
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

        }
        return outputStream.toString();
    }

    int getIterationCount() {
        return Integer.valueOf(((EditText) findViewById(R.id.iterations)).getText().toString());
    }
}
