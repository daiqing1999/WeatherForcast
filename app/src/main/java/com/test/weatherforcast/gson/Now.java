package com.test.weatherforcast.gson;

import com.google.gson.annotations.SerializedName;
/**
 *用来存储实时天气数据的实体类
 */
public class Now {
    @SerializedName("cond_code")
    public int weatherCondCode;

    @SerializedName("cond_txt")
    public String weatherCond;

    @SerializedName("tmp")
    public String nowTemperature;

    @SerializedName("wind_dir")
    public String windDirection;

    @SerializedName("wind_sc")
    public String windLevel;

    @SerializedName("wind_spd")
    public String windSpeed;
}
