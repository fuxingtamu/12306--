package com.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.railway.entity.Station;

import java.util.List;

/**
 * 车站服务接口
 */
public interface StationService extends IService<Station> {

    /**
     * 搜索车站
     */
    List<Station> searchStations(String keyword);

    /**
     * 获取热门车站
     */
    List<Station> getHotStations();
}