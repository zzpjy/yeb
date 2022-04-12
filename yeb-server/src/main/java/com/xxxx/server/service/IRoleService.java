package com.xxxx.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.PageUtil;
import com.xxxx.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-13
 */
public interface IRoleService extends IService<Role> {


    PageUtil<Role> pageRoleList(Page<Role> page, QueryWrapper<Role> queryWrapper);

    List<Role> getRoleList(Integer id);
}
