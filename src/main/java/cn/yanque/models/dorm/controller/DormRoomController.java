package cn.yanque.models.dorm.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.dorm.pojo.vo.req.DormRoomCreateReq;
import cn.yanque.models.dorm.pojo.vo.req.DormRoomPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormRoomUpdateReq;
import cn.yanque.models.dorm.pojo.vo.res.DormRoomRes;
import cn.yanque.models.dorm.service.DormRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 宿舍房间管理接口。
 */
@RestController
@RequestMapping("/api/dorm/room")
@Tag(name = "DormRoomController", description = "宿舍房间管理")
public class DormRoomController {

    @Autowired
    private DormRoomService dormRoomService;

    @PostMapping
    @Operation(description = "添加宿舍房间（可自动生成床位）")
    public ApiResponse<Long> addRoom(@Valid @RequestBody DormRoomCreateReq req) {
        return ApiResponse.success(dormRoomService.addRoom(req));
    }

    @PutMapping("{id}")
    @Operation(description = "修改宿舍房间")
    public ApiResponse<Long> updateRoom(@Parameter(description = "房间ID") @PathVariable Long id,
                                        @Valid @RequestBody DormRoomUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(dormRoomService.updateRoom(req));
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除宿舍房间")
    public ApiResponse<Long> deleteRoom(@Parameter(description = "房间ID") @PathVariable Long id) {
        return ApiResponse.success(dormRoomService.deleteRoom(id));
    }

    @GetMapping("{id}")
    @Operation(description = "根据ID查询宿舍房间")
    public ApiResponse<DormRoomRes> getRoomById(@Parameter(description = "房间ID") @PathVariable Long id) {
        return ApiResponse.success(dormRoomService.getRoomById(id));
    }

    @GetMapping
    @Operation(description = "分页查询宿舍房间")
    public ApiResponse<PageResult<DormRoomRes>> pageRoom(@Valid @ModelAttribute DormRoomPageReq req) {
        return ApiResponse.success(dormRoomService.pageRoom(req));
    }
}
