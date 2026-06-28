package cn.yanque.models.dorm.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.enums.EnableStatusEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.dorm.mapper.DormBuildingMapper;
import cn.yanque.models.dorm.mapper.DormRoomMapper;
import cn.yanque.models.dorm.pojo.entity.DormBuildingEntity;
import cn.yanque.models.dorm.pojo.vo.req.DormBuildingCreateReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBuildingPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBuildingUpdateReq;
import cn.yanque.models.dorm.pojo.vo.res.DormBuildingRes;
import cn.yanque.models.dorm.service.DormBuildingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DormBuildingServiceImpl implements DormBuildingService {

    @Autowired
    private DormBuildingMapper buildingMapper;

    @Autowired
    private DormRoomMapper roomMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addBuilding(DormBuildingCreateReq req) {
        DormBuildingEntity building = new DormBuildingEntity();
        building.setCampusId(req.getCampusId());
        building.setBuildingName(req.getBuildingName());
        building.setGenderType(req.getGenderType());
        building.setManagerName(req.getManagerName());
        building.setManagerPhone(req.getManagerPhone());
        building.setStatus(EnableStatusEnum.ENABLED.name());
        building.setCreatedAt(new Date());
        building.setUpdatedAt(new Date());
        buildingMapper.insert(building);
        return building.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateBuilding(DormBuildingUpdateReq req) {
        DormBuildingEntity building = new DormBuildingEntity();
        building.setId(req.getId());
        building.setCampusId(req.getCampusId());
        building.setBuildingName(req.getBuildingName());
        building.setGenderType(req.getGenderType());
        building.setManagerName(req.getManagerName());
        building.setManagerPhone(req.getManagerPhone());
        building.setStatus(req.getStatus() == null ? EnableStatusEnum.ENABLED.name() : req.getStatus());
        building.setUpdatedAt(new Date());
        int rows = buildingMapper.updateById(building);
        if (rows == 0) {
            throw BusinessException.DormBuildingNotExist;
        }
        return req.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteBuilding(Long id) {
        if (roomMapper.countByBuildingId(id) > 0) {
            throw BusinessException.DormBuildingHasRoom;
        }
        int rows = buildingMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.DormBuildingNotExist;
        }
        return id;
    }

    @Override
    public DormBuildingRes getBuildingById(Long id) {
        DormBuildingEntity building = buildingMapper.selectById(id);
        if (building == null) {
            throw BusinessException.DormBuildingNotExist;
        }
        DormBuildingRes res = new DormBuildingRes();
        BeanUtils.copyProperties(building, res);
        return res;
    }

    @Override
    public PageResult<DormBuildingRes> pageBuilding(DormBuildingPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        List<DormBuildingEntity> list = buildingMapper.selectPage(req.getKeyword(), req.getCampusId());

        PageInfo<DormBuildingEntity> pageInfo = new PageInfo<>(list);
        List<DormBuildingRes> records = list.stream().map(building -> {
            DormBuildingRes res = new DormBuildingRes();
            BeanUtils.copyProperties(building, res);
            return res;
        }).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }
}
