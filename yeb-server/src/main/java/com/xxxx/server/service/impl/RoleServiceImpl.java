package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.RoleMapper;
import com.xxxx.server.pojo.AdminRole;
import com.xxxx.server.pojo.PageUtil;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IAdminRoleService;
import com.xxxx.server.service.IMenuRoleService;
import com.xxxx.server.service.IMenuService;
import com.xxxx.server.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {


    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IAdminRoleService adminRoleService;

    @Override
    public PageUtil<Role> pageRoleList(Page<Role> page, QueryWrapper<Role> queryWrapper) {

        IPage<Role> iPage = roleMapper.selectPage(page, queryWrapper);

        List<Role> roleList = iPage.getRecords();

        roleList.stream().forEach(r->{
            r.setChildren(menuService.getMenuWithRole(r.getId()));
        });

        return new PageUtil<>(iPage.getTotal(),iPage.getPages(),roleList);
    }

    @Override
    public List<Role> getRoleList(Integer id) {
//        List<Integer> roleList=adminRoleService.list(new QueryWrapper<AdminRole>()
//                .eq("adminId",id)).stream().map(AdminRole::getRid).collect(Collectors.toList());
        List<Role> roleList=roleMapper.getRoles(id);
        roleList.stream().forEach(r->{
            r.setChildren(menuService.getMenuWithRole(r.getId()));
        });
        return roleList;
    }


}
