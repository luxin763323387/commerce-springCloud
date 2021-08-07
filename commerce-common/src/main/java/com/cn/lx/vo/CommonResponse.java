package com.cn.lx.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * {
 *     "code" : 0,
 *     "message : "",
 *     "data":{}
 * }
 * @author StevenLu
 * @date 2021/7/25 下午11:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> implements Serializable {

    private static final Integer SUCCESS = 0;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 信息
     */
    private String message;

    /**
     * 类型
     */
    private T date;


    public CommonResponse (Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<T>(data);
    }


    private CommonResponse(T data){
        this.code = SUCCESS;
        this.message = "";
        this.date = data;
    }

    public  boolean isSuccess(){
        return SUCCESS.equals(this.code);
    }



}
