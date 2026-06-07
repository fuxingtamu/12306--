package com.railway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.railway.entity.Station;
import com.railway.mapper.StationMapper;
import com.railway.service.StationService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 车站服务实现
 */
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements StationService {

    // 热门车站列表
    private static final List<String> HOT_STATION_CODES = Arrays.asList(
            "BJP", "BJN", "AOH", "SHH", "SHN", "GZQ", "GZN", "SZQ", "SNW",
            "WHN", "NJH", "NJN", "HZH", "HGH", "XAY", "XAE", "ZZF", "ZZD",
            "CSQ", "CWQ", "SYT", "QDK", "JNK", "XMK", "FZS", "SZH", "WXH"
    );

    @Override
    public List<Station> searchStations(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        final String searchKeyword = keyword.trim();

        return this.lambdaQuery()
                .eq(Station::getStatus, 1)
                .and(wrapper -> wrapper
                        .like(Station::getStationName, searchKeyword)
                        .or()
                        .like(Station::getStationPinyin, searchKeyword)
                        .or()
                        .like(Station::getStationShortPinyin, searchKeyword)
                        .or()
                        .like(Station::getCityName, searchKeyword)
                        .or()
                        .eq(Station::getStationCode, searchKeyword.toUpperCase())
                )
                .last("LIMIT 20")
                .list();
    }

    @Override
    public List<Station> getHotStations() {
        return this.lambdaQuery()
                .eq(Station::getStatus, 1)
                .in(Station::getStationCode, HOT_STATION_CODES)
                .list();
    }
}