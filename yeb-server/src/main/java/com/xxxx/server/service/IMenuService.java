package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-13
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据用户id查询菜单列表
     * @return
     */
    List<Menu> getMenusByAdminId();

    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenuWithRole();

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> getAllMenus();

    List<Menu> getMenuWithRole(Integer rid);

    RespBean deleteMenu(Integer id);

    List<String> getUrl(Integer id);
}
