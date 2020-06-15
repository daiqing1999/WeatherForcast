package com.test.weatherforcast.db;

import org.litepal.crud.DataSupport;
/**
 *用来存储省的实体类
 */
public class Province extends DataSupport {
    private int id;
    private String provinceName;//省名
    private int provinceCode;//天气代码

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
