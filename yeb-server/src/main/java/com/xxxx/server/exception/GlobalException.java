/*
package com.xxxx.server.exception;

import com.xxxx.server.pojo.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

*/
/**
 * 全局异常处理
 *//*

//表示控制器的一个增强类，如果发生异常并且符合自定义的拦截异常就会被拦截
@RestControllerAdvice
public class GlobalException {

    //捕捉异常，捕捉SQL所有异常
    @ExceptionHandler(SQLException.class)
    public RespBean mySqlException(SQLException e){
        if(e instanceof SQLIntegrityConstraintViolationException){
            return RespBean.error("该数据有关联数据，操作失败"+e);
        }
        return RespBean.error("数据库异常，操作失败！"+e);
    }

}
*/
