package com.wms.watermonitorsystem;

public class User {
    private String imei;
    private String mac;
    private String reservoir;

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

    public String getReservoir(){return reservoir;}

    public void setReservoir(String reservoir){this.reservoir=reservoir;}

}
