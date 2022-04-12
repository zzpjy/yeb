package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.MenuMapper;
import com.xxxx.server.mapper.MenuRoleMapper;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.MenuRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IMenuRoleService;
import com.xxxx.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private IMenuService menuService;

    /**
     * 更新角色菜单
     *
     * @param rid
     * @param mids
     * @return
     */
//    @Override
//    //事务的注解
//    @Transactional
//    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
//        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
//        if(mids==null||0==mids.length){
//            return RespBean.success("更新成功!");
//        }
//        Integer result=menuRoleMapper.insertRecord(rid,mids);
//        if(result==mids.length){
//            return RespBean.success("更新成功");
//        }
//        return RespBean.error("更新失败");
//    }
    @Override
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        if (mids == null || mids.length == 0) {
            menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
            return RespBean.success("更新成功");
        }

        List<MenuRole> menuRoleList = menuRoleMapper.selectList(new QueryWrapper<MenuRole>().eq("rid", rid));

        List<Integer> hMids = menuRoleList.stream().map(MenuRole::getMid).collect(Collectors.toList());
        List<Integer> qMids = Arrays.asList(mids);

        if (hMids == null || hMids.size() == 0) {
            Integer result = menuRoleMapper.insertRecord(rid, mids);
            if (result == mids.length) {
                return RespBean.success("更新成功");
            }
        }

//        if (hMids.retainAll(qMids)) {
//            return RespBean.success("更新成功");
//        }

        mids = qMids.stream().filter(mid -> !hMids.contains(mid)).toArray(Integer[]::new);
        Integer[] deleteMids = hMids.stream().filter(mid -> !qMids.contains(mid)).toArray(Integer[]::new);
        Integer result = 0;
        if (mids != null && mids.length > 0) {
            result = menuRoleMapper.insertRecord(rid, mids);
        }
        if (deleteMids != null && deleteMids.length > 0) {
            result += menuRoleMapper.deleteRecord(rid, deleteMids);
        }

        if (result != (mids.length + deleteMids.length)) {
            return RespBean.error("更新失败");
        }

        return RespBean.success("更新成功");

    }

    @Override
    @Transactional
    public RespBean deleteeMenuRole(Integer rid, Integer mid) {
        List<Integer> menuList = listIds(mid);
        List<Menu> list = null;
        List<Integer> listId = new ArrayList<>();
        listId.add(mid);
        List<Integer> listMidS=menuRoleMapper.selectList(new QueryWrapper<MenuRole>()
                .eq("rid",rid)).stream().map(MenuRole::getMid)
                .collect(Collectors.toList());
        try {
            if (menuList != null && menuList.size() != 0) {
                listIdAdd(listId, menuList);
            }

//            if(menuList!=null&&menuList.size()!=0){
//                menuList.stream().map(Menu::getId).forEach(id->{
//                    menuRoleMapper.delete(new QueryWrapper<MenuRole>()
//                            .eq("rid",rid).eq("mid",id));
//                });
//            }
            listId.stream().filter(m->listMidS.contains(m)).forEach(id->{
                menuRoleMapper.delete(new QueryWrapper<MenuRole>()
                        .eq("rid", rid).eq("mid", mid));
            });

            list = menuService.getMenuWithRole(rid);
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("删除权限失败");
        }
        return RespBean.success("删除权限成功", list);
    }

    private List<Integer> listIds(Integer mid) {
        return menuMapper.selectList(new QueryWrapper<Menu>().eq("parentId", mid)).stream()
                .map(Menu::getId).collect(Collectors.toList());
    }

    private void listIdAdd(List<Integer> listId, List<Integer> menuId) {

            menuId.stream().forEach(id->{
                List<Integer> childrenId=listIds(id);
                listId.add(id);
                if (menuId != null && menuId.size() != 0) {
                    listIdAdd(listId,childrenId);
                }
            });

    }

}
