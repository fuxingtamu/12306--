package com.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.railway.dto.TrainQueryDTO;
import com.railway.entity.Train;
import com.railway.vo.TrainVO;

import java.util.List;

/**
 * 车次服务接口
 */
public interface TrainService extends IService<Train> {

    /**
     * 查询车次列表
     */
    List<TrainVO> queryTrains(TrainQueryDTO queryDTO);

    /**
     * 获取车次详情
     */
    TrainVO getTrainDetail(Long trainId, String travelDate);

    /**
     * 根据车次号查询
     */
    TrainVO queryByTrainCode(String trainCode);
}