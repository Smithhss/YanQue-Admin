package cn.yanque.models.dorm.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.dorm.enums.DormAssignmentStatusEnum;
import cn.yanque.models.dorm.enums.DormBedStatusEnum;
import cn.yanque.models.dorm.mapper.DormAssignmentMapper;
import cn.yanque.models.dorm.mapper.DormBedMapper;
import cn.yanque.models.dorm.mapper.DormBuildingMapper;
import cn.yanque.models.dorm.mapper.DormRoomMapper;
import cn.yanque.models.dorm.pojo.entity.DormAssignmentEntity;
import cn.yanque.models.dorm.pojo.entity.DormBedEntity;
import cn.yanque.models.dorm.pojo.entity.DormBuildingEntity;
import cn.yanque.models.dorm.pojo.entity.DormRoomEntity;
import cn.yanque.models.dorm.pojo.vo.req.DormAssignReq;
import cn.yanque.models.dorm.pojo.vo.req.DormAssignmentPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormTransferReq;
import cn.yanque.models.dorm.pojo.vo.res.DormAssignmentRes;
import cn.yanque.models.dorm.service.DormAssignmentService;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DormAssignmentServiceImpl implements DormAssignmentService {

    private static final String TEACHING_MODE_OFFLINE = "OFFLINE";

    @Autowired
    private DormAssignmentMapper assignmentMapper;

    @Autowired
    private DormBedMapper bedMapper;

    @Autowired
    private DormRoomMapper roomMapper;

    @Autowired
    private DormBuildingMapper buildingMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long assign(DormAssignReq req, Long operatorId) {
        StudentEntity student = studentMapper.selectById(req.getStudentId());
        validateStudentCanLive(student);

        DormBedEntity bed = loadFreeBed(req.getBedId());
        DormBuildingEntity building = loadBuildingByBed(bed, student.getGender());

        if (assignmentMapper.selectLivingByStudentId(req.getStudentId()) != null) {
            throw BusinessException.DormStudentAlreadyAssigned;
        }

        Long assignmentId = createLivingAssignment(student.getId(), bed, building, req.getCheckInDate(), req.getRemark(), operatorId);
        bedMapper.updateStatusAndStudent(bed.getId(), DormBedStatusEnum.OCCUPIED.name(), student.getId());
        return assignmentId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long checkout(Long assignmentId) {
        DormAssignmentEntity assignment = assignmentMapper.selectById(assignmentId);
        if (assignment == null) {
            throw BusinessException.DormAssignmentNotExist;
        }
        if (!DormAssignmentStatusEnum.LIVING.name().equals(assignment.getStatus())) {
            throw BusinessException.DormAssignmentNotLiving;
        }
        assignmentMapper.checkout(assignmentId, new Date());
        bedMapper.updateStatusAndStudent(assignment.getBedId(), DormBedStatusEnum.FREE.name(), null);
        return assignmentId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long transfer(DormTransferReq req, Long operatorId) {
        DormAssignmentEntity living = assignmentMapper.selectLivingByStudentId(req.getStudentId());
        if (living == null) {
            throw BusinessException.DormStudentNotLiving;
        }
        StudentEntity student = studentMapper.selectById(req.getStudentId());
        validateStudentCanLive(student);

        DormBedEntity newBed = loadFreeBed(req.getNewBedId());
        DormBuildingEntity building = loadBuildingByBed(newBed, student.getGender());

        // 退旧床
        assignmentMapper.checkout(living.getId(), new Date());
        bedMapper.updateStatusAndStudent(living.getBedId(), DormBedStatusEnum.FREE.name(), null);

        // 入新床
        Long assignmentId = createLivingAssignment(student.getId(), newBed, building, new Date(), req.getRemark(), operatorId);
        bedMapper.updateStatusAndStudent(newBed.getId(), DormBedStatusEnum.OCCUPIED.name(), student.getId());
        return assignmentId;
    }

    @Override
    public PageResult<DormAssignmentRes> pageAssignment(DormAssignmentPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        List<DormAssignmentRes> list = assignmentMapper.selectPage(req.getBuildingId(), req.getStatus(), req.getKeyword());

        PageInfo<DormAssignmentRes> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, list);
    }

    /** 宿舍只接收线下且已完善性别的学生。 */
    private void validateStudentCanLive(StudentEntity student) {
        if (student == null) {
            throw BusinessException.DormStudentNotExist;
        }
        if (!TEACHING_MODE_OFFLINE.equals(student.getTeachingMode())) {
            throw BusinessException.DormStudentNotOffline;
        }
        if (student.getGender() == null || student.getGender().isEmpty()) {
            throw BusinessException.DormStudentGenderUnknown;
        }
    }

    /** 校验并加载空闲床位。 */
    private DormBedEntity loadFreeBed(Long bedId) {
        DormBedEntity bed = bedMapper.selectById(bedId);
        if (bed == null) {
            throw BusinessException.DormBedNotExist;
        }
        if (!DormBedStatusEnum.FREE.name().equals(bed.getStatus())) {
            throw BusinessException.DormBedNotFree;
        }
        return bed;
    }

    /** 加载床位所属楼栋并校验性别隔离。 */
    private DormBuildingEntity loadBuildingByBed(DormBedEntity bed, String studentGender) {
        DormRoomEntity room = roomMapper.selectById(bed.getRoomId());
        if (room == null) {
            throw BusinessException.DormRoomNotExist;
        }
        DormBuildingEntity building = buildingMapper.selectById(room.getBuildingId());
        if (building == null) {
            throw BusinessException.DormBuildingNotExist;
        }
        if (!building.getGenderType().equals(studentGender)) {
            throw BusinessException.DormGenderMismatch;
        }
        return building;
    }

    /** 创建在住记录。 */
    private Long createLivingAssignment(Long studentId, DormBedEntity bed, DormBuildingEntity building,
                                        Date checkInDate, String remark, Long operatorId) {
        DormAssignmentEntity assignment = new DormAssignmentEntity();
        assignment.setStudentId(studentId);
        assignment.setBedId(bed.getId());
        assignment.setRoomId(bed.getRoomId());
        assignment.setBuildingId(building.getId());
        assignment.setCheckInDate(checkInDate == null ? new Date() : checkInDate);
        assignment.setStatus(DormAssignmentStatusEnum.LIVING.name());
        assignment.setAssignedBy(operatorId);
        assignment.setRemark(remark);
        assignment.setCreatedAt(new Date());
        assignment.setUpdatedAt(new Date());
        assignmentMapper.insert(assignment);
        return assignment.getId();
    }
}
