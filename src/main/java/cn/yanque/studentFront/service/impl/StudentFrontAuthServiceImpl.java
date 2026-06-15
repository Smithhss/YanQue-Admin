package cn.yanque.studentFront.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.jwt.JWTUtil;
import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.utils.RedisUtil;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.studentFront.pojo.res.StudentInfoRes;
import cn.yanque.studentFront.service.StudentFrontAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 学生前台登录态服务实现。
 */
@Service
public class StudentFrontAuthServiceImpl implements StudentFrontAuthService {

    private static final long TOKEN_EXPIRE_MILLIS = 1000 * 60 * 60;
    private static final Duration SIGN_SECRET_EXPIRE = Duration.ofMillis(TOKEN_EXPIRE_MILLIS);
    private static final String SIGN_SECRET_KEY_PREFIX = "yanque:student:sign:secret:";

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String createToken(StudentEntity student) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", student.getId());
        map.put("phone", student.getStudentPhone());
        map.put("student", true);
        map.put("expire_time", System.currentTimeMillis() + TOKEN_EXPIRE_MILLIS);
        return JWTUtil.createToken(map, sysConfigService.get(SysConfig.jwtSecret).getBytes());
    }

    @Override
    public String createSignSecret(StudentEntity student) {
        String signSecret = RandomUtil.randomString(48);
        redisUtil.set(SIGN_SECRET_KEY_PREFIX + student.getId(), signSecret, SIGN_SECRET_EXPIRE);
        return signSecret;
    }

    @Override
    public StudentInfoRes buildStudentInfo(StudentEntity student) {
        StudentInfoRes res = new StudentInfoRes();
        res.setName(student.getStudentName());
        res.setPhone(student.getStudentPhone());
        return res;
    }
}
