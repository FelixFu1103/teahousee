package com.cmpe277.onlinemilktea.Model;

public class TokenModel {
    private String phone, name, token;

    public TokenModel() {
    }

    public TokenModel(String phone, String name, String token) {
        this.phone = phone;
        this.name = name;
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
