package com.test.weatherforcast.gson;


import com.google.gson.annotations.SerializedName;


/**
 *用来存储24小时天气数据的实体类
 */
public class Hourly {
    public String time;
    @SerializedName("cond_code")
    public int cond_code;
    public String cond_txt;

    @SerializedName("tmp")
    public String tmp;

}
