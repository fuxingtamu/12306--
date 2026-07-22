package com.railway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.railway.entity.Passenger;
import org.apache.ibatis.annotations.Mapper;

/**
 * 旅客Mapper
 */
@Mapper
public interface PassengerMapper extends BaseMapper<Passenger> {
}