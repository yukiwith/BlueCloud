package model;

public class CustomException extends RuntimeException {

    ResultCode code;

    public CustomException(ResultCode code) {
        this.code = code;
    }

    public ResultCode getResultCode() {
        return code;
    }
}
