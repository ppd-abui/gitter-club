package gitter.server.common;

/*
* 返回数据格式：
* {
*   code:int,
*   data:{},
*   msg:String
* }
*
* define code:
* success:200
* failed:500
* */


public class Result<T> {
    private int code;
    private T data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result() {
    }

    public Result (int code, T data, String msg){
        this.code = code;
        this.data=data;
        this.msg=msg;
    }
}
