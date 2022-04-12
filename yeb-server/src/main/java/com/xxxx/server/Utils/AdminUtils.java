package com.xxxx.server.Utils;

import com.xxxx.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 操作员工工具类
 */
public class AdminUtils {

    public static Admin getCurrentAdmin(){
        //获取当前操作员
        return (Admin) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
    }

}
