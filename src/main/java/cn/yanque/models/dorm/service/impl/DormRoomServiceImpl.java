package cn.yanque.models.dorm.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.dorm.enums.DormBedStatusEnum;
import cn.yanque.models.dorm.mapper.DormBedMapper;
import cn.yanque.models.dorm.mapper.DormBuildingMapper;
import cn.yanque.models.dorm.mapper.DormRoomMapper;
import cn.yanque.models.dorm.pojo.entity.DormBedEntity;
import cn.yanque.models.dorm.pojo.entity.DormRoomEntity;
import cn.yanque.models.dorm.pojo.vo.req.DormRoomCreateReq;
import cn.yanque.models.dorm.pojo.vo.req.DormRoomPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormRoomUpdateReq;
import cn.yanque.models.dorm.pojo.vo.res.DormRoomRes;
import cn.yanque.models.dorm.service.DormRoomService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DormRoomServiceImpl implements DormRoomService {

    @Autowired
    private DormRoomMapper roomMapper;

    @Autowired
    private DormBuildingMapper buildingMapper;

    @Autowired
    private DormBedMapper bedMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRoom(DormRoomCreateReq req) {
        if (buildingMapper.selectById(req.getBuildingId()) == null) {
            throw BusinessException.DormBuildingNotExist;
        }

        DormRoomEntity room = new DormRoomEntity();
        room.setBuildingId(req.getBuildingId());
        room.setRoomNo(req.getRoomNo());
        room.setFloor(req.getFloor() == null ? 1 : req.getFloor());
        room.setCapacity(req.getCapacity());
        room.setRoomType(req.getRoomType());
        room.setStatus("ENABLED");
        room.setCreatedAt(new Date());
        room.setUpdatedAt(new Date());
        roomMapper.insert(room);

        if (Boolean.TRUE.equals(req.getAutoGenerateBeds()) && req.getCapacity() != null && req.getCapacity() > 0) {
            List<DormBedEntity> beds = new ArrayList<>();
            for (int i = 1; i <= req.getCapacity(); i++) {
                DormBedEntity bed = new DormBedEntity();
                bed.setRoomId(room.getId());
                bed.setBedNo(String.valueOf(i));
                bed.setStatus(DormBedStatusEnum.FREE.name());
                bed.setCreatedAt(new Date());
                bed.setUpdatedAt(new Date());
                beds.add(bed);
            }
            bedMapper.batchInsert(beds);
        }
        return room.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateRoom(DormRoomUpdateReq req) {
        DormRoomEntity room = new DormRoomEntity();
        room.setId(req.getId());
        room.setBuildingId(req.getBuildingId());
        room.setRoomNo(req.getRoomNo());
        room.setFloor(req.getFloor());
        room.setCapacity(req.getCapacity());
        room.setRoomType(req.getRoomType());
        room.setStatus(req.getStatus() == null ? "ENABLED" : req.getStatus());
        room.setUpdatedAt(new Date());
        int rows = roomMapper.updateById(room);
        if (rows == 0) {
            throw BusinessException.DormRoomNotExist;
        }
        return req.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteRoom(Long id) {
        if (bedMapper.countByRoomId(id) > 0) {
            throw BusinessException.DormRoomHasBed;
        }
        int rows = roomMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.DormRoomNotExist;
        }
        return id;
    }

    @Override
    public DormRoomRes getRoomById(Long id) {
        DormRoomEntity room = roomMapper.selectById(id);
        if (room == null) {
            throw BusinessException.DormRoomNotExist;
        }
        DormRoomRes res = new DormRoomRes();
        BeanUtils.copyProperties(room, res);
        return res;
    }

    @Override
    public PageResult<DormRoomRes> pageRoom(DormRoomPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        List<DormRoomEntity> list = roomMapper.selectPage(req.getBuildingId(), req.getKeyword());

        PageInfo<DormRoomEntity> pageInfo = new PageInfo<>(list);
        List<DormRoomRes> records = list.stream().map(room -> {
            DormRoomRes res = new DormRoomRes();
            BeanUtils.copyProperties(room, res);
            return res;
        }).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }
}
