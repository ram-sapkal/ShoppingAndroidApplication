package com.example.barcodeshope.Model;

public class orderInfo {
    String firstName,lastName,mobileNumber,address,pcode,bcode,paymentId,OrderId,status;

    public orderInfo() {
    }

    public orderInfo(String firstName, String lastName, String mobileNumber, String address,
                     String pcode, String bcode, String paymentId, String orderId, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.pcode = pcode;
        this.bcode = bcode;
        this.paymentId = paymentId;
        OrderId = orderId;
        this.status = status;
    }

    public orderInfo(String firstName, String lastName, String mobileNumber, String address,
                     String pcode, String bcode, String paymentId, String orderId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.pcode = pcode;
        this.bcode = bcode;
        this.paymentId = paymentId;
        OrderId = orderId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getBcode() {
        return bcode;
    }

    public void setBcode(String bcode) {
        this.bcode = bcode;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
