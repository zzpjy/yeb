package com.xxxx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共放回对象
 */
@Data
@NoArgsConstructor//无参构造
@AllArgsConstructor//全参构造
public class RespBean {

    private Integer code;//状态码
    private String message;//提示信息
    private Object obj;//对象数据

    /**
     * 成功放回结果
     * @param message
     * @return
     */
    public static RespBean success(String message){
        return new RespBean(200,message,null);
    }

    /**
     * 成功放回结果带返回对象
     * @param message
     * @param obj
     * @return
     */
    public static RespBean success(String message,Object obj){
        return new RespBean(200,message,obj);
    }

    /**
     * 失败返回结果
     * @param message
     * @return
     */
    public static RespBean error(String message){
        return new RespBean(500,message,null);
    }

    /**
     * 失败放回结果带参数
     * @param message
     * @param obj
     * @return
     */
    public static RespBean error(String message,Object obj){
        return new RespBean(500,message,obj);
    }
}
