package cn.yanque.models.dorm.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.dorm.pojo.vo.req.DormRoomCreateReq;
import cn.yanque.models.dorm.pojo.vo.req.DormRoomPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormRoomUpdateReq;
import cn.yanque.models.dorm.pojo.vo.res.DormRoomRes;

/**
 * 宿舍房间业务接口。
 */
public interface DormRoomService {

    /** 新增房间（可按容量自动生成床位），返回新房间ID。 */
    Long addRoom(DormRoomCreateReq req);

    /** 修改房间，返回房间ID。 */
    Long updateRoom(DormRoomUpdateReq req);

    /** 删除房间（房间下存在床位时不允许删除），返回房间ID。 */
    Long deleteRoom(Long id);

    /** 查询房间详情。 */
    DormRoomRes getRoomById(Long id);

    /** 分页查询房间。 */
    PageResult<DormRoomRes> pageRoom(DormRoomPageReq req);
}
