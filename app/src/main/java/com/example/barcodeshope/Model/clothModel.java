package com.example.barcodeshope.Model;

public class clothModel {

    String type,company,style,size,price,bcode,url,categoryy,quantity;

    public clothModel() {
    }

    public clothModel(String type, String company, String style, String size, String price, String bcode, String url, String categoryy, String quantity) {
        this.type = type;
        this.company = company;
        this.style = style;
        this.size = size;
        this.price = price;
        this.bcode = bcode;
        this.url = url;
        this.categoryy = categoryy;
        this.quantity = quantity;
    }

    public clothModel(String type, String company, String style, String size, String price, String bcode, String url) {
        this.type = type;
        this.company = company;
        this.style = style;
        this.size = size;
        this.price = price;
        this.bcode = bcode;
        this.url = url;
    }

    public clothModel(String type, String company, String style, String size, String price, String bcode, String url, String categoryy) {
        this.type = type;
        this.company = company;
        this.style = style;
        this.size = size;
        this.price = price;
        this.bcode = bcode;
        this.url = url;
        this.categoryy = categoryy;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBcode() {
        return bcode;
    }

    public void setBcode(String bcode) {
        this.bcode = bcode;
    }

    public String getCategoryy() {
        return categoryy;
    }

    public void setCategoryy(String categoryy) {
        this.categoryy = categoryy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String description) {
        this.style = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
