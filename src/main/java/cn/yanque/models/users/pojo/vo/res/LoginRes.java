package cn.yanque.models.users.pojo.vo.res;

import lombok.Data;

import java.util.List;

@Data
public class LoginRes {

    private String token;

    private UserDetailRes userDetailRes;

    private List<PermissionDetailRes> permissionDetailResList;

    private List<RoleDetailRes> roleDetailResList;
}
