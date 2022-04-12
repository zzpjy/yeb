package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.Utils.AdminUtils;
import com.xxxx.server.mapper.MenuMapper;
import com.xxxx.server.mapper.MenuRoleMapper;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.MenuRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-13
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Override
    @Transactional
    public RespBean deleteMenu(Integer id) {
        List<Menu> list = menuMapper.selectList(null);
        List<Integer> listId = new ArrayList<>();
        listId.add(id);
        listIdAdd(list, listId, id);

        List<Integer> menuRoleList =
                menuRoleMapper.selectList(null).stream().map(MenuRole::getMid).collect(Collectors.toList());
        Collections.reverse(listId);
        try {
            listId.forEach(ids -> {
                if (menuRoleList.contains(ids)) {
                    menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("mid", ids));
                }
                menuMapper.deleteById(ids);
            });
            return RespBean.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("删除失败");
        }

    }

    @Override
    public List<String> getUrl(Integer id) {
        return menuMapper.getUrl(id);
    }


    private List<Integer> listIds(List<Menu> list, Integer mid) {
        return list.stream().filter(menu -> menu.getParentId() == mid)
                .map(Menu::getId).collect(Collectors.toList());
    }

    private void listIdAdd(List<Menu> list, List<Integer> listId, Integer menuId) {
        List<Integer> childrenId = listIds(list, menuId);
        if (childrenId != null && childrenId.size() != 0) {
            childrenId.stream().forEach(id -> {
                listId.add(id);
                listIdAdd(list, listId, id);

            });
        }

    }


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    /**
//     * 根据用户id查询菜单列表
//     * @return
//     */
//    @Override
//    public List<Menu> getMenusByAdminId() {
//        Integer adminId = AdminUtils.getCurrentAdmin().getId();
//        ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
//        List<Menu> menus=(List<Menu>)valueOperations.get("menu_" + adminId);
//        //如果为空，去数据库获取数据
//        if(CollectionUtils.isEmpty(menus)){
//            menus=menuMapper.getMenusByAdminId(adminId);
//            //将数据设置到Redis中
//            valueOperations.set("menu_"+adminId,menus);
//        }
//        return menus;
//    }


    /**
     * 根据用户id查询菜单列表
     *
     * @return
     */
    @Override
    public List<Menu> getMenusByAdminId() {
        Integer adminId = AdminUtils.getCurrentAdmin().getId();
//        ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
//        List<Menu> menus=(List<Menu>)valueOperations.get("menu_" + adminId);
//        //如果为空，去数据库获取数据
//        if(CollectionUtils.isEmpty(menus)){
//            menus=menuMapper.getMenusByAdminId(adminId);
//            //将数据设置到Redis中
//            valueOperations.set("menu_"+adminId,menus);
//        }
        List<Menu> menus = menuMapper.getMenusByAdminIdEj(adminId);

        List<Menu> menusYi = menus.stream().filter(menu -> menu.getMenuLevel() == 1).collect(Collectors.toList());

//        menusYi.stream().forEach(menuf -> {
//            List<Menu> childrenList= menus.stream().filter(menu -> menu.getParentId()==menuf.getId()).collect
//            (Collectors.toList())
//            if(childrenList!=null){
//                children(menuf,childrenList);
//            }
//        });
        List<Menu> list = children(menus, menusYi);

        return list;
    }


    public List<Menu> children(List<Menu> menus, List<Menu> childrenList) {
        childrenList.stream().forEach(menuf -> {
            List<Menu> childrenList2 =
                    menus.stream().filter(menu -> menu.getParentId() == menuf.getId()).collect(Collectors.toList());
            if (childrenList != null) {
                menuf.setChildren(childrenList2);
                children(menus, childrenList2);
            }
        });
        return childrenList;
    }

    /**
     * 根据角色获取菜单列表
     *
     * @return
     */
    @Override
    public List<Menu> getMenuWithRole() {
        return menuMapper.getMenuWithRole();
    }

    /**
     * 查询所有菜单
     *
     * @return
     */
    @Override
    public List<Menu> getAllMenus() {
        List<Menu> list = menuMapper.selectList(null);
        List<Menu> list1 = list.stream().filter(l -> l.getMenuLevel() == 1).collect(Collectors.toList());

        return children(list, list1);
    }

    @Override
    public List<Menu> getMenuWithRole(Integer rid) {
        List<Menu> list = menuMapper.getMenuWithRoleId(rid);
        List<Menu> list1 = list.stream().filter(menu -> menu.getMenuLevel() == 1).collect(Collectors.toList());
        List<Menu> menuList = children(list, list1);
        return menuList;
    }
}
