package cn.yanque.models.teaching.duty.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyCreateReq;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyDateSaveReq;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyPageReq;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyUpdateReq;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyCreateRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDateRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDateSaveRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDeleteRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDetailRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyPageRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyUpdateRes;
import cn.yanque.models.teaching.duty.service.ClassDutyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 值班管理接口。
 */
@RestController
@RequestMapping("/api/classDuties")
@Tag(name = "ClassDutyController", description = "值班管理")
public class ClassDutyController {

    @Autowired
    private ClassDutyService classDutyService;

    /**
     * 新增值班。
     *
     * @param req 新增请求
     * @return 新增后的值班ID
     */
    @PostMapping
    @Operation(description = "新增值班")
    public ApiResponse<ClassDutyCreateRes> addDuty(@Valid @RequestBody ClassDutyCreateReq req) {
        return ApiResponse.success(classDutyService.addDuty(req));
    }

    /**
     * 修改值班。
     *
     * @param id  值班ID
     * @param req 修改请求
     * @return 被修改的值班ID
     */
    @PutMapping("{id}")
    @Operation(description = "修改值班")
    public ApiResponse<ClassDutyUpdateRes> updateDuty(@Parameter(description = "值班ID") @PathVariable Long id,
                                                     @Valid @RequestBody ClassDutyUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(classDutyService.updateDuty(req));
    }

    /**
     * 删除值班。
     *
     * @param id 值班ID
     * @return 被删除的值班ID
     */
    @DeleteMapping("{id}")
    @Operation(description = "删除值班")
    public ApiResponse<ClassDutyDeleteRes> deleteDuty(@Parameter(description = "值班ID") @PathVariable Long id) {
        return ApiResponse.success(classDutyService.deleteDuty(id));
    }

    /**
     * 查询值班详情。
     *
     * @param id 值班ID
     * @return 值班详情
     */
    @GetMapping("{id}")
    @Operation(description = "查询值班详情")
    public ApiResponse<ClassDutyDetailRes> getDutyById(@Parameter(description = "值班ID") @PathVariable Long id) {
        return ApiResponse.success(classDutyService.getDutyById(id));
    }

    /**
     * 分页查询值班。
     *
     * @param req 查询条件
     * @return 值班分页结果
     */
    @GetMapping
    @Operation(description = "分页查询值班")
    public ApiResponse<PageResult<ClassDutyPageRes>> pageDuty(@Valid @ModelAttribute ClassDutyPageReq req) {
        return ApiResponse.success(classDutyService.pageDuty(req));
    }

    /**
     * 按日期查询值班排班页面数据。
     *
     * @param dutyDate 值班日期
     * @return 当天排班数据
     */
    @GetMapping("date")
    @Operation(description = "按日期查询值班")
    public ApiResponse<ClassDutyDateRes> getDateDuty(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dutyDate) {
        return ApiResponse.success(classDutyService.getDateDuty(dutyDate));
    }

    /**
     * 按日期覆盖保存值班。
     *
     * @param req 保存请求
     * @return 保存数量
     */
    @PutMapping("date")
    @Operation(description = "按日期保存值班")
    public ApiResponse<ClassDutyDateSaveRes> saveDateDuty(@Valid @RequestBody ClassDutyDateSaveReq req) {
        return ApiResponse.success(classDutyService.saveDateDuty(req));
    }
}
