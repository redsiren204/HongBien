package com.goshu.hongbien.model;

/**
 * Created by tamtv on 3/23/2016.
 */
public class Category {

    private static final String TAG_CATEGORY_ID = "category_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_SLUG = "slug";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_LINK = "link";
    private static final String TAG_ORDER = "order";
    private static final String TAG_STATUS = "status";
    private static final String TAG_VIEWED = "viewed";
    private static final String TAG_POSTED = "posted";
    private static final String TAG_PARENT_ID = "parent_id";
    private static final String TAG_PARENT_SLUG = "parent_slug";
    private static final String TAG_STATE = "state";

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
}
