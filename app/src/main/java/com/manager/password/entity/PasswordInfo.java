package com.manager.password.entity;

public class PasswordInfo {
    int _id;
    String websiteName;
    String username;
    byte[] password;
    byte[] iv;

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public PasswordInfo(int _id, String websiteName, byte[] password,byte[] iv,String username) {
        this._id = _id;
        this.websiteName = websiteName;
        this.password = password;
        this.iv = iv;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PasswordInfo() {

    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public  byte[] getPassword() {
        return password;
    }

    public void setPassword( byte[] password) {
        this.password = password;
    }
}
