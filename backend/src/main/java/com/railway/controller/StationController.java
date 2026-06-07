package com.railway.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.railway.common.Result;
import com.railway.entity.Station;
import com.railway.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车站控制器
 */
@Tag(name = "车站接口", description = "车站查询相关接口")
@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    /**
     * 搜索车站
     */
    @Operation(summary = "搜索车站")
    @GetMapping("/search")
    public Result<List<Station>> searchStations(@RequestParam String keyword) {
        try {
            List<Station> stations = stationService.searchStations(keyword);
            return Result.success(stations);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取热门车站
     */
    @Operation(summary = "获取热门车站")
    @GetMapping("/hot")
    public Result<List<Station>> getHotStations() {
        try {
            List<Station> stations = stationService.getHotStations();
            return Result.success(stations);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取车站详情
     */
    @Operation(summary = "获取车站详情")
    @GetMapping("/{id}")
    public Result<Station> getStationDetail(@PathVariable Long id) {
        try {
            Station station = stationService.getById(id);
            return Result.success(station);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取所有车站
     */
    @Operation(summary = "获取所有车站")
    @GetMapping("/all")
    public Result<List<Station>> getAllStations() {
        try {
            List<Station> stations = stationService.lambdaQuery()
                    .eq(Station::getStatus, 1)
                    .list();
            return Result.success(stations);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}