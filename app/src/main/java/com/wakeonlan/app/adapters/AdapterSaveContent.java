package com.wakeonlan.app.adapters;

public class AdapterSaveContent {
    private String mac;
    private String name;
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AdapterSaveContent(){

    }

    public AdapterSaveContent(String mac, String name){
        setMac(mac);
        setName(name);
    }
}
