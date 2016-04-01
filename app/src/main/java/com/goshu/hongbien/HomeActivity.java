package com.goshu.hongbien;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.goshu.hongbien.model.Api;
import com.goshu.hongbien.model.Attachment;
import com.goshu.hongbien.model.Category;
import com.goshu.hongbien.model.Post;
import com.goshu.hongbien.service.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment verticalFragment;
    private ArrayList<Category> listCategories = null;
    private ArrayList<Post> listPosts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        new HttpGetCategoryJsonAsync().execute();
        new HttpGetPostsByCategoryJsonAsync().execute("36");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        verticalFragment = new InfiniteVerticalFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, verticalFragment).commit();
    }

    class HttpGetCategoryJsonAsync extends AsyncTask<String, String, JSONArray> {
        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Attempting get JSON data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args) {
            JSONArray arrayCategories = null;
            try {
                HashMap<String, String> params = new HashMap<>();
                Log.d("Request", "category");
                arrayCategories = jsonParser.makeJSONArrHttpRequest(Api.API_CATEGORIES, JSONParser.GET, params);
                if (arrayCategories != null) {
                    return arrayCategories;
                }
            } catch (Exception e) {
                Log.e("Exception", e.getStackTrace().toString());
            }
            return null;
        }

        protected void onPostExecute(JSONArray arrayCategories) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (arrayCategories != null) {
                listCategories = new ArrayList<>();
                try {
                    for (int i=0; i<arrayCategories.length(); i++) {
                        JSONObject categoryObj = arrayCategories.getJSONObject(i);
                        Category category = new Category(categoryObj.getInt(Category.TAG_CATEGORY_ID), categoryObj.getString(Category.TAG_NAME));
                        listCategories.add(category);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class HttpGetPostsByCategoryJsonAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Attempting get JSON data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject jsonObject = null;
            try {
                HashMap<String, String> params = new HashMap<>();
                Log.d("Request", "post");
                jsonObject = jsonParser.makeHttpRequest(Api.API_POSTS_BY_CATEGORY + args[0], JSONParser.GET, params);
                if (jsonObject != null) {
                    return jsonObject;
                }
            } catch (Exception e) {
                Log.e("Exception", e.getStackTrace().toString());
            }
            return null;
        }

        protected void onPostExecute(JSONObject jsonObject) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (jsonObject != null) {
                try {
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data != null) {
                        listPosts = new ArrayList<>();
                        for (int i=0; i<data.length(); i++) {
                            JSONObject postObj = data.getJSONObject(i);
                            JSONArray arrayAttachments = new JSONArray(postObj.getString(Post.TAG_ATTACHMENTS).replaceAll("\\\\", ""));
                            ArrayList<Attachment> listAttachments = new ArrayList<>();
                            if (arrayAttachments != null) {
                                for (int j=0; j<arrayAttachments.length(); j++) {
                                    JSONObject attachmentObj = arrayAttachments.getJSONObject(j);
                                    Attachment attachment = new Attachment(attachmentObj.getString(Attachment.TAG_TYPE),
                                            attachmentObj.getString(Attachment.TAG_SRC));
                                    listAttachments.add(attachment);
                                }
                            }
                            Post post = new Post(postObj.getInt(Post.TAG_POST_ID),
                                    postObj.getString(Post.TAG_CONTENT),
                                    listAttachments,
                                    postObj.getString(Post.TAG_UPDATED),
                                    postObj.getInt(Post.TAG_LIKED),
                                    postObj.getInt(Post.TAG_COMMENTED),
                                    postObj.getInt(Post.TAG_SHARED),
                                    postObj.getString(Post.TAG_DISPLAY_NAME),
                                    postObj.getString(Post.TAG_AVATAR).replaceAll("\\\\", ""));
                            listPosts.add(post);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else  {
            if (verticalFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(verticalFragment).commit();
                verticalFragment = null;
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "Scroll View", Toast.LENGTH_SHORT).show();
            Intent scrollingActivity = new Intent(HomeActivity.this, ScrollingActivity.class);
            startActivity(scrollingActivity);
        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "Test JSON parser", Toast.LENGTH_SHORT).show();
            Intent benchmarkActivity = new Intent(HomeActivity.this, BenchmarkActivity.class);
            startActivity(benchmarkActivity);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
