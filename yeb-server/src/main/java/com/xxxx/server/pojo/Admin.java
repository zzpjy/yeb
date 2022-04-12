package com.xxxx.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xxxx.server.config.CustomAuthorityDeserializer;
import com.xxxx.server.config.security.component.CustomUrlDecisionManager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_admin")
@ApiModel(value="Admin对象", description="")
public class Admin implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "住宅电话")
    private String telephone;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "是否启用")
    @Getter(AccessLevel.NONE)
    private Boolean enabled;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户头像")
    private String userFace;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "角色")
    @TableField(exist = false)
    private List<Role> roles;


    @Override
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> authorities=roles
//                .stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//        return authorities;
        Collection<GrantedAuthority> authorities = new HashSet<>();
        // 获取当前登录用户所拥有的权限列表(以角色名列表作为权限列表)
        if (!CollectionUtils.isEmpty(roles)) {
            for (Role role : roles) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                // 将当前角色名作为权限(添加角色权限)
                authorities.add(authority);
                if (!CollectionUtils.isEmpty(role.getChildren())) {
                    for (Menu menu : role.getChildren()) {
                        // 将当前角色所拥有菜单权限URL(添加菜单权限)
                        authority = new SimpleGrantedAuthority(menu.getUrl());
                        authorities.add(authority);
                    }
                }
            }
        }
        return authorities;

    }




    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
