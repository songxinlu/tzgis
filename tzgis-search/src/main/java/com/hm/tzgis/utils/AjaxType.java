package com.hm.tzgis.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjaxType {

    private int code;

    private boolean success;

    private String msg;

    private Object data;

    public static AjaxType success(){
        return new AjaxType(200, true, "success","");
    }

    public static AjaxType success(Object Data){
        return new AjaxType(200, true, "success",Data);
    }

    public static AjaxType error(String msg){
        return new AjaxType(500, false, msg, "");
    }

    public static AjaxType error(String msg, Object Data){
        return new AjaxType(500, false, msg, Data);
    }
}
