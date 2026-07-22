package com.railway.controller;

import com.railway.common.Result;
import com.railway.dto.TrainQueryDTO;
import com.railway.service.TrainService;
import com.railway.vo.TrainVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车次查询控制器
 */
@Tag(name = "车次查询接口", description = "车次、车票查询相关接口")
@RestController
@RequestMapping("/trains")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    /**
     * 查询车次列表
     */
    @Operation(summary = "查询车次列表")
    @PostMapping("/query")
    public Result<List<TrainVO>> queryTrains(@Valid @RequestBody TrainQueryDTO queryDTO) {
        try {
            List<TrainVO> trains = trainService.queryTrains(queryDTO);
            return Result.success(trains);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取车次详情
     */
    @Operation(summary = "获取车次详情")
    @GetMapping("/{trainId}")
    public Result<TrainVO> getTrainDetail(@PathVariable Long trainId, @RequestParam String travelDate) {
        try {
            TrainVO train = trainService.getTrainDetail(trainId, travelDate);
            return Result.success(train);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 根据车次号查询
     */
    @Operation(summary = "根据车次号查询")
    @GetMapping("/code/{trainCode}")
    public Result<TrainVO> queryByTrainCode(@PathVariable String trainCode) {
        try {
            TrainVO train = trainService.queryByTrainCode(trainCode);
            return Result.success(train);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}