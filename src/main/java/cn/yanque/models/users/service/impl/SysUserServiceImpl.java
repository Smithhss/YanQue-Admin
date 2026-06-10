package cn.yanque.models.users.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.jwt.JWTUtil;
import cn.yanque.common.api.PageResult;
import cn.yanque.common.enums.ActiveEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.users.mapper.SysPermissionMapper;
import cn.yanque.models.users.mapper.SysRoleMapper;
import cn.yanque.models.users.mapper.SysUserMapper;
import cn.yanque.models.users.pojo.entity.SysPermissionEntity;
import cn.yanque.models.users.pojo.entity.SysRoleEntity;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import cn.yanque.models.users.pojo.vo.bo.QueryPermissionBo;
import cn.yanque.models.users.pojo.vo.bo.QueryUserBo;
import cn.yanque.models.users.pojo.vo.req.*;
import cn.yanque.models.users.pojo.vo.res.*;
import cn.yanque.models.users.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class SysUserServiceImpl implements SysUserService {


    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCreateRes addUser(UserCreateReq req) {

        SysUserEntity user = new SysUserEntity();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setNickname(req.getNickname());
        user.setRealName(req.getRealName());
        user.setPhone(req.getPhone());
        user.setEmail(req.getEmail());
        user.setStatus(req.getStatus());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        try {
            sysUserMapper.insert(user);
        } catch (DuplicateKeyException e) {
            throw BusinessException.UserExist;
        }

        UserCreateRes res = new UserCreateRes();
        res.setId(user.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserUpdateRes updateUser(UserUpdateReq req) {
        SysUserEntity user = new SysUserEntity();
        user.setId(req.getId());
        user.setNickname(req.getNickname());
        user.setRealName(req.getRealName());
        user.setPhone(req.getPhone());
        user.setEmail(req.getEmail());
        user.setUnionId(req.getUnionId());
        user.setStatus(req.getStatus());
        user.setUpdatedAt(new Date());

        int rows;
        try {
            rows = sysUserMapper.updateById(user);
        } catch (DuplicateKeyException e) {
            throw BusinessException.UserExist;
        }
        if (rows == 0) {
            throw BusinessException.UserNotExist;
        }
        UserUpdateRes res = new UserUpdateRes();
        res.setId(req.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDeleteRes deleteUser(Long id) {
        sysUserMapper.deleteUserRoles(id);
        int rows = sysUserMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.UserNotExist;
        }
        UserDeleteRes res = new UserDeleteRes();
        res.setId(id);
        return res;
    }

    @Override
    public UserDetailRes getUserById(Long id) {
        SysUserEntity user = sysUserMapper.selectById(id);
        if (user == null) {
            throw BusinessException.UserNotExist;
        }
        return buildUserDetailRes(user);
    }

    @Override
    public PageResult<UserPageRes> pageUser(UserPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<SysUserEntity> list = sysUserMapper.selectPage(req.getKeyword(), req.getStatus(), req.getRoleCode());
        PageInfo<SysUserEntity> pageInfo = new PageInfo<>(list);
        List<UserPageRes> records = list.stream().map(this::buildUserPageRes).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRoleAssignRes assignUserRoles(Long userId, UserRoleAssignReq req) {
        SysUserEntity user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.UserNotExist;
        }

        sysUserMapper.deleteUserRoles(userId);
        if (!req.getRoleIds().isEmpty()) {
            sysUserMapper.insertUserRoles(userId, req.getRoleIds());
        }

        UserRoleAssignRes res = new UserRoleAssignRes();
        res.setUserId(userId);
        res.setRoleIds(req.getRoleIds());
        return res;
    }

    @Override
    public LoginRes LoginReq(LoginReq req) {

        LoginRes loginRes = new LoginRes();

        //1.查询用户
        SysUserEntity sysUserEntity = sysUserMapper.selectByUsername(req.getUsername());

        //2.判断是否存在
        if (sysUserEntity == null || !sysUserEntity.getStatus().equals(ActiveEnum.ACTIVE.name())) {
            throw BusinessException.UserNotExist.newInstance("用户不存在或已失效");
        }

        // 判断密码
        if (!req.getPassword().equals(sysUserEntity.getPassword())) {
            throw BusinessException.PasswordError;
        }

        UserDetailRes userDetailRes = new UserDetailRes();
        BeanUtils.copyProperties(sysUserEntity, userDetailRes);
        loginRes.setUserDetailRes(userDetailRes);

        //3.生成token
        String token = createToken(sysUserEntity);
        loginRes.setToken(token);

        //4.查询角色权限
        List<Long> roleIds = sysUserMapper.selectRoleIdsByUserId(sysUserEntity.getId());

        if (CollectionUtil.isEmpty(roleIds)) {
            return loginRes;
        }

        QueryUserBo queryUserBo = new QueryUserBo();
        queryUserBo.setIds(roleIds);
        List<SysRoleEntity> sysRoleEntityList = sysRoleMapper.selectList(queryUserBo);
        List<RoleDetailRes> roleDetailResList = sysRoleEntityList.stream().map(sysRoleEntity -> {
            RoleDetailRes roleDetailRes = new RoleDetailRes();
            BeanUtils.copyProperties(sysRoleEntity, roleDetailRes);
            return roleDetailRes;
        }).toList();
        loginRes.setRoleDetailResList(roleDetailResList);


        List<Long> permissionIds = sysRoleMapper.selectPermissionIdsByRoleId(roleIds);
        permissionIds = permissionIds.stream().distinct().toList();

        if (CollectionUtil.isEmpty(permissionIds)) {
            return loginRes;
        }

        QueryPermissionBo queryPermissionBo = new QueryPermissionBo();
        queryPermissionBo.setIds(permissionIds);
        List<SysPermissionEntity> sysPermissionEntityList = sysPermissionMapper.selectList(queryPermissionBo);


        List<PermissionDetailRes> permissionDetailResList = sysPermissionEntityList.stream().map(sysPermissionEntity -> {
            PermissionDetailRes permissionDetailRes = new PermissionDetailRes();
            BeanUtils.copyProperties(sysPermissionEntity, permissionDetailRes);
            return permissionDetailRes;
        }).toList();

        loginRes.setPermissionDetailResList(permissionDetailResList);
        //5.组装信息返回
        return loginRes;
    }

    private String createToken(SysUserEntity sysUserEntity) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", sysUserEntity.getId());
        map.put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60);
        return JWTUtil.createToken(map, "1234".getBytes());
    }

    private UserDetailRes buildUserDetailRes(SysUserEntity user) {
        UserDetailRes res = new UserDetailRes();
        BeanUtils.copyProperties(user, res);
        return res;
    }

    private UserPageRes buildUserPageRes(SysUserEntity user) {
        UserPageRes res = new UserPageRes();
        BeanUtils.copyProperties(user, res);
        return res;
    }
}
