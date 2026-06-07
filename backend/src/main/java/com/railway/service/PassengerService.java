package com.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.railway.dto.PassengerDTO;
import com.railway.entity.Passenger;

import java.util.List;

/**
 * 旅客服务接口
 */
public interface PassengerService extends IService<Passenger> {

    /**
     * 获取用户的旅客列表
     */
    List<Passenger> getPassengerList(Long userId);

    /**
     * 添加旅客
     */
    Passenger addPassenger(Long userId, PassengerDTO passengerDTO);

    /**
     * 更新旅客
     */
    Passenger updatePassenger(Long userId, Long passengerId, PassengerDTO passengerDTO);

    /**
     * 删除旅客
     */
    void deletePassenger(Long userId, Long passengerId);

    /**
     * 获取旅客详情
     */
    Passenger getPassengerDetail(Long userId, Long passengerId);

    /**
     * 设置默认旅客
     */
    void setDefaultPassenger(Long userId, Long passengerId);
}
