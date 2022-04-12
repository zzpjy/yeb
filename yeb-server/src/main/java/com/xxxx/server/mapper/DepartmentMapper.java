package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-13
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 获取所有部门
     * @return
     * @param
     */
    List<Department> getAllDepartments(Integer id);

    /**
     * 添加部门
     * @param department
     */
    void addDep(Department department);

    /**
     * 删除部门
     * @param dep
     */
    void deleteDep(Department dep);
}
