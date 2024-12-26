package model;

public class ExceptionCast {

    public CustomException cast(ResultCode code) {
        return new CustomException(code);
    }
}
