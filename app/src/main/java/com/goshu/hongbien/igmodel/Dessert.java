package com.goshu.hongbien.igmodel;

import com.instagram.common.json.annotation.JsonField;
import com.instagram.common.json.annotation.JsonType;

@JsonType
public class Dessert {
    @JsonField(fieldName="type")
    String type;

    @JsonField(fieldName="rating")
    float rating;
}
