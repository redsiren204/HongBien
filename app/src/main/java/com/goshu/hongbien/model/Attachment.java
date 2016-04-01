package com.goshu.hongbien.model;

/**
 * Created by tamtv on 4/1/16.
 */
public class Attachment {

    public static final String TAG_TYPE = "type";
    public static final String TAG_SRC = "src";

    private String type;
    private String src;

    public Attachment() {

    }

    public Attachment(String type, String src) {
        this.type = type;
        this.src = src;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
