package com.wms.watermonitorsystem;

public class User {
    private String imei;
    private String mac;

    public User(){}

    public String getUserImei(){
        return imei;
    }

    public void setUserImei(String imei){
        this.imei=imei;
    }

    public String getMac(){
        return mac;
    }

    public void setMac(String mac){
        this.mac=mac;
    }

}
