package com.xxxx.server.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.server.pojo.*;
import com.xxxx.server.service.IAdminRoleService;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/system/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IAdminRoleService adminRoleService;

    @ApiOperation("获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmins(String keywords){
        return adminService.getAllAdmins(keywords);
    }

    @ApiOperation("显示管理员分页列表信息")
    @GetMapping("/list")
    public RespBean getPageUserList(@RequestParam(defaultValue = "1")
                                                Integer currPageNo,
                                    @RequestParam(defaultValue = "4")
                                                Integer pageSize,
                                    @RequestParam(required = false)String name,
                                    String sort){
        PageUtil<Admin> data= adminService.getAllAdmins(currPageNo,pageSize,name,sort);
        data.getList().stream().map(Admin::getName).forEach(System.out::println);
        return RespBean.success(null,data);
    }


    @ApiOperation(value = "更新操作员")
    @PutMapping("/")
    public RespBean updateAdmin(@RequestBody Admin admin){
        if(adminService.updateById(admin)){
            return RespBean.success("更新成功");

        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteAdmin(@PathVariable Integer id){
        if(adminService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "更新操作员角色")
    @PutMapping("/role")
    public RespBean updateAdminRole(@RequestBody AdminRoleVo adminRoleVo){
        return adminService.updateAdminRole(adminRoleVo);
    }

    @ApiOperation("添加操作员")
    @PostMapping("/")
    public RespBean insertAdmin(@RequestBody Admin admin){
        if(adminService.save(admin)){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation("通过用户id获取该操作所拥有的角色列表")
    @GetMapping("/adminRoleList/{id}")
    public List<AdminRole> getAdminRoleList(@PathVariable Integer id){
        return adminRoleService.list(new QueryWrapper<AdminRole>().eq("adminId",id));
    }

    @ApiOperation("根据操作员id查询操作员详情对象信息")
    @GetMapping("/getById/{id}")
    public Admin findByAdminId(@PathVariable Integer id){
        return adminService.list(new QueryWrapper<Admin>()
                .eq("id",id)).get(0);
    }

}
