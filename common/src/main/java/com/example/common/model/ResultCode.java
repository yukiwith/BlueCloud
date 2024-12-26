package model;

public class ResultCode {
    // 是否执行成功
    boolean success;
    // 自定义状态码
    int code;
    // 提示信息
    String message;

    public ResultCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
