package com.test.weatherforcast.gson;

import com.google.gson.annotations.SerializedName;
/**
 * 用来存储天气数据更新时间的实体类
 */
public class Update {
        @SerializedName("loc")
        public String updateTime;
}
