package com.test.weatherforcast.db;

import org.litepal.crud.DataSupport;
/**
 *用来存储区县城市的实体类
 */
public class County extends DataSupport {
    private int id;
    private String countyName;//区县名
    private String weatherId;//天气代码
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
