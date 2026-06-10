package cn.yanque.models.users.service.impl;

import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.users.mapper.SysUserMapper;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import cn.yanque.models.users.pojo.vo.req.UserCreateReq;
import cn.yanque.models.users.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SysUserServiceImpl implements SysUserService {


    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public void addUser(UserCreateReq req) {

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


    }
}
