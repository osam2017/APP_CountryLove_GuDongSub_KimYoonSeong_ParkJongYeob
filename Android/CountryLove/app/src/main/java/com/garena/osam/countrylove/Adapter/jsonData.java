package com.garena.osam.countrylove.Adapter;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017-10-17.
 */

public class jsonData {
    private int id;            private String company;
    private Double latitude;  private Double longitude;
    private String area;       private String classification;
    private String info;       private String date;
    private String detail_address;
    private String detail_info;
    private String company_info;
    private String company_tel;
    private int distance;     private Bitmap image;

    public String getDetail_address() {
        return detail_address;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }

    public String getDetail_info() {
        return detail_info;
    }

    public void setDetail_info(String detail_info) {
        this.detail_info = detail_info;
    }

    public String getCompany_info() {
        return company_info;
    }

    public void setCompany_info(String company_info) {
        this.company_info = company_info;
    }

    public String getCompany_tel() {
        return company_tel;
    }

    public void setCompany_tel(String company_tel) {
        this.company_tel = company_tel;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getArea() {
        return area;
    }

    public String getClassification() {
        return classification;
    }

    public String getInfo() {
        return info;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public jsonData(int id, String company, Double latitude, Double longitude, String area, String classification, String info, String date, String detail_address, String detail_info, String company_info, String company_tel, int distance, Bitmap image) {
        this.id = id;
        this.company = company;
        this.latitude = latitude;
        this.longitude = longitude;
        this.area = area;
        this.classification = classification;
        this.info = info;
        this.date = date;
        this.detail_address = detail_address;
        this.detail_info = detail_info;
        this.company_info = company_info;
        this.company_tel = company_tel;
        this.distance = distance;
        this.image = image;
    }
}
