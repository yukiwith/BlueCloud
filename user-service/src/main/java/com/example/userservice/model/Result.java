package com.example.bolgwithcontents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;


    public static Result success(){ return new Result("200","",null);}

    public static Result success(Object data){ return new Result("200","",data);}

    public static Result success(String msg){ return new Result("200",msg,null);}
}
