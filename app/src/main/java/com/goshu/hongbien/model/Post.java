package com.goshu.hongbien.model;

import java.util.ArrayList;

/**
 * Created by tamtv on 3/23/2016.
 */
public class Post {

    public static final String TAG_POST_ID = "post_id";
    public static final String TAG_CONTENT = "content";
    public static final String TAG_ATTACHMENTS = "attachments";
    public static final String TAG_CREATED = "created";
    public static final String TAG_UPDATED = "updated";
    public static final String TAG_STATUS = "status";
    public static final String TAG_VIEWED = "viewed";
    public static final String TAG_LIKED = "liked";
    public static final String TAG_UNlIKED = "unliked";
    public static final String TAG_COMMENTED = "commented";
    public static final String TAG_CATEGORY_ID = "category_id";
    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_IS_ANONYMOUS = "is_anonymous";
    public static final String TAG_SHARED = "shared";
    public static final String TAG_IS_TOP = "is_top";
    public static final String TAG_IS_GHIM = "is_ghim";
    public static final String TAG_TOP_LIKE = "top_like";
    public static final String TAG_CATEGORY_NAME = "category_name";
    public static final String TAG_CATEGORY_SLUG = "category_slug";
    public static final String TAG_CATEGORY_PARENT_SLUG = "category_parent_slug";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_DISPLAY_NAME = "display_name";
    public static final String TAG_AVATAR = "avatar";
    public static final String TAG_IS_LIKED = "is_liked";
    public static final String TAG_IS_SHARED = "is_shared";

    private int postId;
    private String content;
    private ArrayList<Attachment> attachments;
    private String updated;
    private int liked;
    private int commented;
    private int shared;
    private String displayName;
    private String avatar;

    public Post() {

    }

    public Post(int postId, String content, ArrayList<Attachment> attachments, String updated, int liked, int commented, int shared, String displayName, String avatar) {
        this.postId = postId;
        this.content = content;
        this.attachments = attachments;
        this.updated = updated;
        this.liked = liked;
        this.commented = commented;
        this.shared = shared;
        this.displayName = displayName;
        this.avatar = avatar;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getCommented() {
        return commented;
    }

    public void setCommented(int commented) {
        this.commented = commented;
    }

    public int getShared() {
        return shared;
    }

    public void setShared(int shared) {
        this.shared = shared;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
