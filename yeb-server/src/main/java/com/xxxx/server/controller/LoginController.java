package com.xxxx.server.controller;

import com.xxxx.server.pojo.*;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.service.IMenuService;
import com.xxxx.server.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "LoginController")
@RestController
public class LoginController {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "登入之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam,
                          HttpServletRequest request){
        return adminService.login(adminLoginParam.getUsername(),
                adminLoginParam.getPassword(),adminLoginParam.getCode(),request);
    }

    @ApiOperation(value="获取当前登录用户的信息")
    @GetMapping("/admin/info")
    public Map<String,Object> getAdminInfo(Principal principal){
        if(null==principal){
            return null;
        }
        String username=principal.getName();
        Admin admin=adminService.getAdminByUserName(username);
        admin.setPassword(null);
        admin.setRoles(roleService.getRoleList(admin.getId()));
        List<String> permissions=menuService.getUrl(admin.getId());
        Map<String,Object> map=new HashMap<>();
        map.put("roles",admin.getRoles());
        map.put("permissions",permissions);
        map.put("admin",admin);
        return map;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功！");
    }

}
