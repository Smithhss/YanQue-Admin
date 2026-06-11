package cn.yanque.models.teaching.campus.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.teaching.campus.mapper.CampusMapper;
import cn.yanque.models.teaching.campus.pojo.entity.CampusEntity;
import cn.yanque.models.teaching.campus.pojo.vo.req.CampusCreateReq;
import cn.yanque.models.teaching.campus.pojo.vo.req.CampusPageReq;
import cn.yanque.models.teaching.campus.pojo.vo.req.CampusUpdateReq;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusCreateRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusDeleteRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusDetailRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusPageRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusUpdateRes;
import cn.yanque.models.teaching.campus.service.CampusService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CampusServiceImpl implements CampusService {

    @Autowired
    private CampusMapper campusMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CampusCreateRes addCampus(CampusCreateReq req) {
        CampusEntity campus = new CampusEntity();
        campus.setCampusLocation(req.getCampusLocation());
        campus.setManagerName(req.getManagerName());
        campus.setManagerPhone(req.getManagerPhone());
        campus.setCreatedAt(new Date());
        campus.setUpdatedAt(new Date());
        campusMapper.insert(campus);

        CampusCreateRes res = new CampusCreateRes();
        res.setId(campus.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CampusUpdateRes updateCampus(CampusUpdateReq req) {
        CampusEntity campus = new CampusEntity();
        campus.setId(req.getId());
        campus.setCampusLocation(req.getCampusLocation());
        campus.setManagerName(req.getManagerName());
        campus.setManagerPhone(req.getManagerPhone());
        campus.setUpdatedAt(new Date());

        int rows = campusMapper.updateById(campus);
        if (rows == 0) {
            throw BusinessException.CampusNotExist;
        }

        CampusUpdateRes res = new CampusUpdateRes();
        res.setId(req.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CampusDeleteRes deleteCampus(Long id) {
        int rows = campusMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.CampusNotExist;
        }

        CampusDeleteRes res = new CampusDeleteRes();
        res.setId(id);
        return res;
    }

    @Override
    public CampusDetailRes getCampusById(Long id) {
        CampusEntity campus = campusMapper.selectById(id);
        if (campus == null) {
            throw BusinessException.CampusNotExist;
        }
        return buildCampusDetailRes(campus);
    }

    @Override
    public PageResult<CampusPageRes> pageCampus(CampusPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<CampusEntity> list = campusMapper.selectPage(req.getKeyword());
        PageInfo<CampusEntity> pageInfo = new PageInfo<>(list);
        List<CampusPageRes> records = list.stream().map(this::buildCampusPageRes).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    private CampusDetailRes buildCampusDetailRes(CampusEntity campus) {
        CampusDetailRes res = new CampusDetailRes();
        BeanUtils.copyProperties(campus, res);
        return res;
    }

    private CampusPageRes buildCampusPageRes(CampusEntity campus) {
        CampusPageRes res = new CampusPageRes();
        BeanUtils.copyProperties(campus, res);
        return res;
    }
}
