package com.test.weatherforcast.gson;

import com.google.gson.annotations.SerializedName;
/**
 *用来存储生活指数的实体类
 */

public class Lifestyle{
    @SerializedName("type")
    public String Comfort;

    @SerializedName("brf")
    public String brief;

    @SerializedName("txt")
    public String suggestion;
}
