package com.xxxx.server.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ApiModel(value="用于接收分配角色时的参数", description="")
public class RoleMenuVo {
    private Integer rid;
    private Integer[] mids;

}

