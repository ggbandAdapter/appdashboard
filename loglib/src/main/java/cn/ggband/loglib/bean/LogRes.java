package cn.ggband.loglib.bean;

public class LogRes<T> {
    private int code;
    private String message;
    private T data;

    public LogRes() {
    }

    public LogRes(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static LogRes error(String message) {
        return new LogRes<Object>(500, message, null);
    }

    public boolean isOk() {
        return code == 1000;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
