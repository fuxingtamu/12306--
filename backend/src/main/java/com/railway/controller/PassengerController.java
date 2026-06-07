package com.railway.controller;

import com.railway.common.Result;
import com.railway.config.UserPrincipal;
import com.railway.dto.PassengerDTO;
import com.railway.entity.Passenger;
import com.railway.service.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 旅客管理控制器
 */
@Tag(name = "旅客管理接口", description = "常用联系人管理相关接口")
@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * 获取旅客列表
     */
    @Operation(summary = "获取旅客列表")
    @GetMapping
    public Result<List<Passenger>> getPassengerList() {
        try {
            Long userId = getCurrentUserId();
            List<Passenger> passengers = passengerService.getPassengerList(userId);
            return Result.success(passengers);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 添加旅客
     */
    @Operation(summary = "添加旅客")
    @PostMapping
    public Result<Passenger> addPassenger(@Valid @RequestBody PassengerDTO passengerDTO) {
        try {
            Long userId = getCurrentUserId();
            Passenger passenger = passengerService.addPassenger(userId, passengerDTO);
            return Result.success("添加成功", passenger);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 更新旅客
     */
    @Operation(summary = "更新旅客")
    @PutMapping("/{id}")
    public Result<Passenger> updatePassenger(@PathVariable Long id, @Valid @RequestBody PassengerDTO passengerDTO) {
        try {
            Long userId = getCurrentUserId();
            Passenger passenger = passengerService.updatePassenger(userId, id, passengerDTO);
            return Result.success("更新成功", passenger);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 删除旅客
     */
    @Operation(summary = "删除旅客")
    @DeleteMapping("/{id}")
    public Result<Void> deletePassenger(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            passengerService.deletePassenger(userId, id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取旅客详情
     */
    @Operation(summary = "获取旅客详情")
    @GetMapping("/{id}")
    public Result<Passenger> getPassengerDetail(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            Passenger passenger = passengerService.getPassengerDetail(userId, id);
            return Result.success(passenger);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 设置默认旅客
     */
    @Operation(summary = "设置默认旅客")
    @PutMapping("/{id}/default")
    public Result<Void> setDefaultPassenger(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            passengerService.setDefaultPassenger(userId, id);
            return Result.success("设置成功", null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUserId();
    }
}
