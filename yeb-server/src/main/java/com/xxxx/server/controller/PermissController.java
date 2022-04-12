package com.xxxx.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.server.pojo.*;
import com.xxxx.server.service.IAdminRoleService;
import com.xxxx.server.service.IMenuRoleService;
import com.xxxx.server.service.IMenuService;
import com.xxxx.server.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissController {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;
    @Autowired
    private IAdminRoleService adminRoleService;

    @ApiOperation(value = "获取角色所有信息")
    @GetMapping("/")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value="添加角色")
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role){
        if(!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }
        if(roleService.save(role)){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "更新角色")
    @PutMapping("/")
    public RespBean updateRoley(@RequestBody Role role){
        if(!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }
        if (roleService.updateById(role)) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRole(@PathVariable Integer rid){
        try {
            adminRoleService.remove(new QueryWrapper<AdminRole>().eq("rid",rid));
            menuRoleService.remove(new QueryWrapper<MenuRole>().eq("rid",rid));
            if(roleService.removeById(rid)){
                return RespBean.success("删除成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("删除失败!");
        }
        return RespBean.error("删除失败!");
    }

    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色id查询菜单id")
    @GetMapping("/mid/{rid}")
    public Integer[] getMidByRid(@PathVariable Integer rid){
        return menuRoleService.list(new QueryWrapper<MenuRole>()
                .eq("rid",rid)).stream().map(MenuRole::getMid)
                .toArray(Integer[]::new);
    }

    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/menuRoleId")
    public RespBean updateMenuRole(@RequestBody RoleMenuVo roleMenuVo){
        Integer rid=roleMenuVo.getRid();
        Integer[] mids=roleMenuVo.getMids();
        return menuRoleService.updateMenuRole(rid,mids);
    }

    @ApiOperation("通过角色id和权限，删除这个角色id所对应的权限")
    @DeleteMapping("/role/delMenuByRoleId/{rid}/{mid}")
    @PreAuthorize("hasRole('admin')")
    public RespBean deleteMenuRole(@PathVariable Integer rid,
                                   @PathVariable Integer mid){

        return menuRoleService.deleteeMenuRole(rid,mid);
    }

    @ApiOperation("根据id查询角色详情信息")
    @GetMapping("/role/getById/{rid}")
    public Role getById(@PathVariable Integer rid){
        return roleService.list(new QueryWrapper<Role>().eq("id",rid)).get(0);
    }




}
