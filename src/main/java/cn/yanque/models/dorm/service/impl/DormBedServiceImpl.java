package cn.yanque.models.dorm.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.dorm.enums.DormBedStatusEnum;
import cn.yanque.models.dorm.mapper.DormBedMapper;
import cn.yanque.models.dorm.mapper.DormRoomMapper;
import cn.yanque.models.dorm.pojo.entity.DormBedEntity;
import cn.yanque.models.dorm.pojo.vo.req.DormBedCreateReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBedPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBedUpdateReq;
import cn.yanque.models.dorm.pojo.vo.res.DormBedRes;
import cn.yanque.models.dorm.service.DormBedService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DormBedServiceImpl implements DormBedService {

    @Autowired
    private DormBedMapper bedMapper;

    @Autowired
    private DormRoomMapper roomMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addBed(DormBedCreateReq req) {
        if (roomMapper.selectById(req.getRoomId()) == null) {
            throw BusinessException.DormRoomNotExist;
        }
        DormBedEntity bed = new DormBedEntity();
        bed.setRoomId(req.getRoomId());
        bed.setBedNo(req.getBedNo());
        bed.setStatus(DormBedStatusEnum.FREE.name());
        bed.setCurrentStudentId(null);
        bed.setCreatedAt(new Date());
        bed.setUpdatedAt(new Date());
        bedMapper.insert(bed);
        return bed.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateBed(DormBedUpdateReq req) {
        DormBedEntity existing = bedMapper.selectById(req.getId());
        if (existing == null) {
            throw BusinessException.DormBedNotExist;
        }
        if (DormBedStatusEnum.OCCUPIED.name().equals(existing.getStatus())) {
            throw BusinessException.DormBedOccupied;
        }
        existing.setBedNo(req.getBedNo());
        existing.setStatus(req.getStatus() == null ? DormBedStatusEnum.FREE.name() : req.getStatus());
        existing.setCurrentStudentId(null);
        existing.setUpdatedAt(new Date());
        int rows = bedMapper.updateById(existing);
        if (rows == 0) {
            throw BusinessException.DormBedNotExist;
        }
        return req.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteBed(Long id) {
        DormBedEntity existing = bedMapper.selectById(id);
        if (existing == null) {
            throw BusinessException.DormBedNotExist;
        }
        if (DormBedStatusEnum.OCCUPIED.name().equals(existing.getStatus())) {
            throw BusinessException.DormBedOccupied;
        }
        int rows = bedMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.DormBedNotExist;
        }
        return id;
    }

    @Override
    public DormBedRes getBedById(Long id) {
        DormBedEntity bed = bedMapper.selectById(id);
        if (bed == null) {
            throw BusinessException.DormBedNotExist;
        }
        DormBedRes res = new DormBedRes();
        BeanUtils.copyProperties(bed, res);
        return res;
    }

    @Override
    public PageResult<DormBedRes> pageBed(DormBedPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        List<DormBedEntity> list = bedMapper.selectPage(req.getRoomId(), req.getStatus());

        PageInfo<DormBedEntity> pageInfo = new PageInfo<>(list);
        List<DormBedRes> records = list.stream().map(bed -> {
            DormBedRes res = new DormBedRes();
            BeanUtils.copyProperties(bed, res);
            return res;
        }).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }
}
