package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.Utils.AdminUtils;
import com.xxxx.server.config.security.component.JwtTokenUtil;
import com.xxxx.server.mapper.AdminMapper;
import com.xxxx.server.mapper.AdminRoleMapper;
import com.xxxx.server.mapper.RoleMapper;
import com.xxxx.server.pojo.*;
import com.xxxx.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-13
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private AdminMapper adminMapper;
    @Resource
    //@Lazy
    private UserDetailsService userDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHede;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    /**
     * 登入之后返回token
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        //拿到保存在session中保存的验证码
        String captcha =(String) request.getSession().getAttribute("captcha");
        //code不能为空，并且生成的验证码要与输入的验证码相同，忽略大小写
        if(StringUtils.isEmpty(code)||!captcha.equalsIgnoreCase(code)){
            return RespBean.error("验证码输入错误，请重新输入");
        }
        //登录
        Admin userDetails=(Admin)userDetailsService.loadUserByUsername(username);
        if(null==userDetails||!passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error("用户名或密码不正确");
        }
        if(!userDetails.isEnabled()){
            return RespBean.error("账号被禁用，请联系管理员！");
        }
        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken=new
                UsernamePasswordAuthenticationToken(userDetails,null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,String> tokenMap=new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHede);
        tokenMap.put("avatar",userDetails.getUserFace());

        return RespBean.success("登入成功",tokenMap);


    }

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUserName(String username) {
                                        //条件构造器
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).
                eq("enabled",true));
    }

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    /**
     * 获取所有操作员
     * @param keywords
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return adminMapper.getAllAdmins(AdminUtils.getCurrentAdmin().getId(),keywords);
    }

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateAdminRole(AdminRoleVo adminRoleVo) {
//        adminRoleMapper.delete(new QueryWrapper<AdminRole>()
//                .eq("adminId",adminId));
//        Integer result=adminRoleMapper.updateAdminRole(adminId,rids);
//        if(rids.length==result){
//            return RespBean.success("更新成功");
//        }
        Integer adminId=adminRoleVo.getAdminId();
        Integer[] rid=adminRoleVo.getRid();
        if (rid==null||rid.length==0){
            adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        }
        List<AdminRole> adminRoleList=adminRoleMapper.selectList(new QueryWrapper<AdminRole>().eq("adminId",adminId));

        List<Integer> ridListY=adminRoleList.stream().map(AdminRole::getRid).collect(Collectors.toList());
        List<Integer> ridListG= Arrays.asList(rid);
        try {

            ridListY.stream().filter(r->!ridListG.contains(r)).forEach(
                    r->{
                        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId).eq("rid",r));
                    }
            );
            ridListG.stream().filter(r->!ridListY.contains(r)).forEach(r->{
                AdminRole adminRole=new AdminRole();
                adminRole.setAdminId(adminId);
                adminRole.setRid(r);
                adminRoleMapper.insert(adminRole);
            });

            return RespBean.success("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("更新失败");
        }
    }

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin=adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        //判断旧密码是否正确
        if(encoder.matches(oldPass,admin.getPassword())){
            admin.setPassword(encoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if(1==result){
                return RespBean.success("跟新成功");
            }
        }
        return RespBean.error("更新失败");
    }
    @Override
    public PageUtil<Admin> getAllAdmins(Integer currentPage, Integer size, String name, String sort){
        Page<Admin> page=new Page<>(currentPage,size);
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>().like("name", name).
                or().like("phone",name);
        if(sort.equals("+id")){
            queryWrapper.orderByAsc("id");
        }else{
            queryWrapper.orderByDesc("id");
        }
        IPage<Admin> iPage=adminMapper.selectPage(page,queryWrapper);
        return new PageUtil<Admin>(iPage.getTotal(),iPage.getPages(),iPage.getRecords());
    }
}
