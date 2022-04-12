package com.xxxx.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.MenuRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.service.IMenuRoleService;
import com.xxxx.server.service.IMenuService;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/system/cfg")
public class MenuController {

    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "通过用户id查询菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(){
        return menuService.getMenusByAdminId();
    }

    @ApiOperation(value = "添加菜单")
    @PostMapping("/saveMenu")
    public RespBean insertMenu(@RequestBody Menu menu){
        if(menuService.save(menu)){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/deleteMenu/{id}")
    public RespBean deleteMenu(@PathVariable Integer id){
        return menuService.deleteMenu(id);
    }

    @ApiOperation("修改菜单")
    @PutMapping("/updateMenu")
    public RespBean updateMenu(@RequestBody Menu menu){
        if(menuService.updateById(menu)){
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }

    @ApiOperation("查询当前菜单信息")
    @GetMapping("/getMenuId/{id}")
    public Menu getMenuId(@PathVariable Integer id){
        return menuService.list(new QueryWrapper<Menu>().eq("id",id)).get(0);
    }

}
