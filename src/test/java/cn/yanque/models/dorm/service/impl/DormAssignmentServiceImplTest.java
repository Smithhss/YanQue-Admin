package cn.yanque.models.dorm.service.impl;

import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.dorm.mapper.DormAssignmentMapper;
import cn.yanque.models.dorm.mapper.DormBedMapper;
import cn.yanque.models.dorm.mapper.DormBuildingMapper;
import cn.yanque.models.dorm.mapper.DormRoomMapper;
import cn.yanque.models.dorm.pojo.vo.req.DormAssignReq;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DormAssignmentServiceImplTest {

    @Mock
    private DormAssignmentMapper assignmentMapper;

    @Mock
    private DormBedMapper bedMapper;

    @Mock
    private DormRoomMapper roomMapper;

    @Mock
    private DormBuildingMapper buildingMapper;

    @Mock
    private StudentMapper studentMapper;

    private DormAssignmentServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new DormAssignmentServiceImpl();
        ReflectionTestUtils.setField(service, "assignmentMapper", assignmentMapper);
        ReflectionTestUtils.setField(service, "bedMapper", bedMapper);
        ReflectionTestUtils.setField(service, "roomMapper", roomMapper);
        ReflectionTestUtils.setField(service, "buildingMapper", buildingMapper);
        ReflectionTestUtils.setField(service, "studentMapper", studentMapper);
    }

    @Test
    void assignRejectsOnlineStudentBeforeBedLookup() {
        DormAssignReq req = new DormAssignReq();
        req.setStudentId(1L);
        req.setBedId(2L);

        StudentEntity student = new StudentEntity();
        student.setId(1L);
        student.setTeachingMode("ONLINE");
        student.setGender("MALE");
        when(studentMapper.selectById(1L)).thenReturn(student);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.assign(req, 99L));

        assertEquals(BusinessException.DormStudentNotOffline.getCode(), ex.getCode());
        verifyNoInteractions(bedMapper, roomMapper, buildingMapper, assignmentMapper);
    }
}
