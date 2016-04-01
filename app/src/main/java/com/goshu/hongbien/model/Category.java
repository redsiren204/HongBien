package com.goshu.hongbien.model;

import android.util.Log;

import com.goshu.hongbien.service.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tamtv on 3/23/2016.
 */
public class Category {

    public static final String TAG_CATEGORY_ID = "category_id";
    public static final String TAG_NAME = "name";
    public static final String TAG_SLUG = "slug";
    public static final String TAG_LOCATION = "location";
    public static final String TAG_LINK = "link";
    public static final String TAG_ORDER = "order";
    public static final String TAG_STATUS = "status";
    public static final String TAG_VIEWED = "viewed";
    public static final String TAG_POSTED = "posted";
    public static final String TAG_PARENT_ID = "parent_id";
    public static final String TAG_PARENT_SLUG = "parent_slug";
    public static final String TAG_STATE = "state";

    private int categoryId;
    private String name;

    public Category() {

    }

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Category> getListCategories() {
        JSONParser jsonParser = new JSONParser();
        JSONArray arrayCategories = null;
        ArrayList<Category> listCategories = new ArrayList<>();
        try {
            HashMap<String, String> params = new HashMap<>();
            Log.d("Request", "starting");
            arrayCategories = jsonParser.makeJSONArrHttpRequest(Api.API_CATEGORIES, JSONParser.GET, params);
        } catch (Exception e) {
            Log.e("Exception", e.getStackTrace().toString());
        }
        if (arrayCategories != null) {
            Log.d("JSON result", arrayCategories.toString());
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

        return listCategories;
    }
}
