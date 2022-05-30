package com.sabid.aspasn.DataModels;

public class Clients {
    private String name;
    private String address;
    private String mobile;

    public Clients(String name, String address, String mobile) {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }
}
