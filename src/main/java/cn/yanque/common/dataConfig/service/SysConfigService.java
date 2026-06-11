package cn.yanque.common.dataConfig.service;

import cn.hutool.core.convert.Convert;
import cn.yanque.common.api.PageResult;
import cn.yanque.common.dataConfig.entity.SysConfigEntity;
import cn.yanque.common.dataConfig.mapper.SysConfigMapper;
import cn.yanque.common.dataConfig.vo.req.SysConfigCreateReq;
import cn.yanque.common.dataConfig.vo.req.SysConfigPageReq;
import cn.yanque.common.dataConfig.vo.req.SysConfigUpdateReq;
import cn.yanque.common.dataConfig.vo.res.SysConfigCreateRes;
import cn.yanque.common.dataConfig.vo.res.SysConfigDeleteRes;
import cn.yanque.common.dataConfig.vo.res.SysConfigDetailRes;
import cn.yanque.common.dataConfig.vo.res.SysConfigPageRes;
import cn.yanque.common.dataConfig.vo.res.SysConfigUpdateRes;
import cn.yanque.common.exception.BusinessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class SysConfigService {

    private final Cache<String, Object> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.SECONDS)
            .maximumSize(10000).build();

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Transactional(rollbackFor = Exception.class)
    public SysConfigCreateRes addConfig(SysConfigCreateReq req) {
        SysConfigEntity sysConfigEntity = new SysConfigEntity();
        sysConfigEntity.setK(req.getK());
        sysConfigEntity.setV(req.getV());
        try {
            sysConfigMapper.insert(sysConfigEntity);
        } catch (DuplicateKeyException e) {
            throw BusinessException.ConfigExist;
        }

        cache.invalidate(req.getK());
        SysConfigCreateRes res = new SysConfigCreateRes();
        res.setId(sysConfigEntity.getId());
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    public SysConfigUpdateRes updateConfig(SysConfigUpdateReq req) {
        SysConfigEntity oldConfig = sysConfigMapper.selectById(req.getId());
        if (oldConfig == null) {
            throw BusinessException.ConfigNotExist;
        }

        SysConfigEntity sysConfigEntity = new SysConfigEntity();
        sysConfigEntity.setId(req.getId());
        sysConfigEntity.setK(req.getK());
        sysConfigEntity.setV(req.getV());

        int rows;
        try {
            rows = sysConfigMapper.updateById(sysConfigEntity);
        } catch (DuplicateKeyException e) {
            throw BusinessException.ConfigExist;
        }
        if (rows == 0) {
            throw BusinessException.ConfigNotExist;
        }

        cache.invalidate(oldConfig.getK());
        cache.invalidate(req.getK());
        SysConfigUpdateRes res = new SysConfigUpdateRes();
        res.setId(req.getId());
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    public SysConfigDeleteRes deleteConfig(Long id) {
        SysConfigEntity oldConfig = sysConfigMapper.selectById(id);
        if (oldConfig == null) {
            throw BusinessException.ConfigNotExist;
        }

        int rows = sysConfigMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.ConfigNotExist;
        }

        cache.invalidate(oldConfig.getK());
        SysConfigDeleteRes res = new SysConfigDeleteRes();
        res.setId(id);
        return res;
    }

    public SysConfigDetailRes getConfigById(Long id) {
        SysConfigEntity sysConfigEntity = sysConfigMapper.selectById(id);
        if (sysConfigEntity == null) {
            throw BusinessException.ConfigNotExist;
        }
        return buildConfigDetailRes(sysConfigEntity);
    }

    public PageResult<SysConfigPageRes> pageConfig(SysConfigPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<SysConfigEntity> list = sysConfigMapper.selectPage(req.getKeyword());
        PageInfo<SysConfigEntity> pageInfo = new PageInfo<>(list);
        List<SysConfigPageRes> records = list.stream().map(this::buildConfigPageRes).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    public <T> T get(SystemConfigItem<T> item) {
        Object value = cache.getIfPresent(item.getKey());
        if (value != null) {
            return Convert.convert(item.getClazz(), value);
        }

        SysConfigEntity sysConfigEntity = sysConfigMapper.selectByKey(item.getKey());
        if (sysConfigEntity != null && sysConfigEntity.getV() != null) {
            T configValue = Convert.convert(item.getClazz(), sysConfigEntity.getV());
            cache.put(item.getKey(), configValue);
            return configValue;
        }

        T defaultValue = Convert.convert(item.getClazz(), item.getDefaultValue());
        cache.put(item.getKey(), defaultValue);
        return defaultValue;
    }

    private SysConfigDetailRes buildConfigDetailRes(SysConfigEntity sysConfigEntity) {
        SysConfigDetailRes res = new SysConfigDetailRes();
        BeanUtils.copyProperties(sysConfigEntity, res);
        return res;
    }

    private SysConfigPageRes buildConfigPageRes(SysConfigEntity sysConfigEntity) {
        SysConfigPageRes res = new SysConfigPageRes();
        BeanUtils.copyProperties(sysConfigEntity, res);
        return res;
    }
}
