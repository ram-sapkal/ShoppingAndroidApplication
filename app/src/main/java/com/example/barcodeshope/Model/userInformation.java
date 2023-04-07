package com.example.barcodeshope.Model;

public class userInformation {
    String firstName="NA";
    String lastName="NA";
    String mobileNumber="NA";
    String address="NA";
    //String isVerified="NA";

    public userInformation() {
    }

/*    public userInformation(String mobileNumber, String isVerified) {
        this.mobileNumber = mobileNumber;
        this.isVerified = isVerified;
    }*/

    public userInformation(String firstName, String lastName, String mobileNumber, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.address = address;
    }

/*    public userInformation(String firstName, String lastName, String mobileNumber, String address, String isVerified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.isVerified = isVerified;
    }*/


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
/*
    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }*/
}
