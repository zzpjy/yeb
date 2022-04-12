package com.xxxx.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.server.pojo.PageUtil;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @ApiOperation("分页显示角色列表")
    @GetMapping("/pageRoleList")
    public PageUtil<Role> pageRoleList(@RequestParam(defaultValue = "1")
                                                   Integer currPageNo,
                                       @RequestParam(defaultValue = "4")
                                                   Integer pageSize,
                                       @RequestParam(required = false)String roleName,
                                       String sort){
        QueryWrapper<Role> queryWrapper=new QueryWrapper<Role>()
                .like("name",roleName).or()
                .like("nameZh",roleName);
        if(sort.equals("+id")){
            queryWrapper.orderByAsc("id");
        }else{
            queryWrapper.orderByDesc("id");
        }
        Page<Role> page=new Page<>(currPageNo,pageSize);
        PageUtil<Role> pageUtil=roleService.pageRoleList(page,queryWrapper);
        return pageUtil;
    }



}
