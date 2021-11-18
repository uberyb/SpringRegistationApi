package com.example.restservice.register.model;
import java.util.Map;
public class ApiResponse {
    private String msg;
    private Map data;

    public ApiResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ApiResponse setData(Map data) {
        this.data = data;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Map getData() {
        return data;
    }


}