package com.idme.minibom.Result;

import java.util.HashMap;

public class Result extends HashMap<String, Object> {
    public static final String CODE = "code";

    public static final String MSG = "msg";

    public static final String DATA = "data";

    public Result(){

    }

    public Result(int code,String msg){
        super.put(CODE,code);
        super.put(MSG,msg);
    }

    public Result(int code,String msg,Object data){
        super.put(CODE,code);
        super.put(MSG,msg);
        if(data != null)
            super.put(DATA,data);
    }

    public static Result success(String msg,Object data){
        return new Result(200,msg,data);
    }

    public static Result success(){
        return success("操作成功");
    }

    public static Result success(String msg){
        return success(msg,null);
    }

    public static Result success(Object data){
        return success("操作成功",data);
    }

    public static Result error(int code,String msg){
        return new Result(code,msg);
    }

    public static Result error(String msg){
        return error(500,msg);
    }

    @Override
    public Result put(String key,Object value){
        super.put(key,value);
        return this;
    }

}
