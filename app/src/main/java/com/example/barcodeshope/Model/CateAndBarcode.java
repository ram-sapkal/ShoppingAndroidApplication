package com.example.barcodeshope.Model;

public class CateAndBarcode {
    String barcode,cate;

    public CateAndBarcode(String barcode, String cate) {
        this.barcode = barcode;
        this.cate = cate;
    }

    public CateAndBarcode() {
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }
}
