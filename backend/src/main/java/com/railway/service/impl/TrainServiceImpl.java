package com.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.railway.dto.TrainQueryDTO;
import com.railway.entity.SeatType;
import com.railway.entity.Station;
import com.railway.entity.Train;
import com.railway.entity.TrainStop;
import com.railway.mapper.SeatTypeMapper;
import com.railway.mapper.StationMapper;
import com.railway.mapper.TrainMapper;
import com.railway.mapper.TrainStopMapper;
import com.railway.service.TrainService;
import com.railway.vo.TrainVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 车次服务实现
 */
@Service
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train> implements TrainService {

    private final StationMapper stationMapper;
    private final TrainStopMapper trainStopMapper;
    private final SeatTypeMapper seatTypeMapper;

    public TrainServiceImpl(StationMapper stationMapper, TrainStopMapper trainStopMapper, SeatTypeMapper seatTypeMapper) {
        this.stationMapper = stationMapper;
        this.trainStopMapper = trainStopMapper;
        this.seatTypeMapper = seatTypeMapper;
    }

    @Override
    public List<TrainVO> queryTrains(TrainQueryDTO queryDTO) {
        // 查询出发站和到达站
        Station startStation = findStation(queryDTO.getStartStation());
        Station endStation = findStation(queryDTO.getEndStation());

        if (startStation == null) {
            throw new RuntimeException("出发站不存在");
        }
        if (endStation == null) {
            throw new RuntimeException("到达站不存在");
        }

        // 查询经过出发站的所有经停记录
        List<TrainStop> startStationStops = trainStopMapper.selectList(
                new LambdaQueryWrapper<TrainStop>()
                        .eq(TrainStop::getStationName, startStation.getStationName())
        );

        // 查询经过到达站的所有经停记录
        List<TrainStop> endStationStops = trainStopMapper.selectList(
                new LambdaQueryWrapper<TrainStop>()
                        .eq(TrainStop::getStationName, endStation.getStationName())
        );

        // 用 train_code 找出同时经过这两个站的车次
        Set<String> trainCodes = startStationStops.stream()
                .map(TrainStop::getTrainCode)
                .filter(code -> endStationStops.stream().anyMatch(s -> s.getTrainCode().equals(code)))
                .collect(Collectors.toSet());

        // 查询这些车次的信息
        List<Train> trains;
        if (trainCodes.isEmpty()) {
            trains = new ArrayList<>();
        } else {
            trains = this.lambdaQuery()
                    .eq(Train::getStatus, 1)
                    .in(Train::getTrainCode, trainCodes)
                    .list();

            // 过滤：只保留出发站在到达站之前的车次
            Map<String, Integer> startOrders = startStationStops.stream()
                    .collect(Collectors.toMap(TrainStop::getTrainCode, TrainStop::getStopOrder, (a, b) -> a));
            Map<String, Integer> endOrders = endStationStops.stream()
                    .collect(Collectors.toMap(TrainStop::getTrainCode, TrainStop::getStopOrder, (a, b) -> a));

            trains = trains.stream()
                    .filter(train -> {
                        Integer startOrder = startOrders.get(train.getTrainCode());
                        Integer endOrder = endOrders.get(train.getTrainCode());
                        return startOrder != null && endOrder != null && startOrder < endOrder;
                    })
                    .collect(Collectors.toList());
        }

        // 车次类型筛选
        if (queryDTO.getTrainType() != null) {
            trains = trains.stream()
                    .filter(t -> t.getTrainType().equals(queryDTO.getTrainType()))
                    .collect(Collectors.toList());
        }

        if (queryDTO.getOnlyHighSpeed() != null && queryDTO.getOnlyHighSpeed()) {
            trains = trains.stream()
                    .filter(t -> t.getTrainType() <= 3) // G、D、C为高铁
                    .collect(Collectors.toList());
        }

        // 转换为VO并生成余票信息
        List<TrainVO> trainVOs = trains.stream()
                .map(train -> convertToVO(train, queryDTO.getTravelDate()))
                .collect(Collectors.toList());

        // 排序
        if (queryDTO.getSortType() != null) {
            switch (queryDTO.getSortType()) {
                case 1: // 按时间排序
                    trainVOs.sort(Comparator.comparing(TrainVO::getStartTime));
                    break;
                case 2: // 按价格排序（最低票价）
                    trainVOs.sort(Comparator.comparing(t -> getMinPrice(t.getSeats())));
                    break;
                case 3: // 按耗时排序
                    trainVOs.sort(Comparator.comparing(TrainVO::getTotalTime));
                    break;
                default:
                    trainVOs.sort(Comparator.comparing(TrainVO::getStartTime));
            }
        }

        return trainVOs;
    }

    @Override
    public TrainVO getTrainDetail(Long trainId, String travelDate) {
        Train train = this.getById(trainId);
        if (train == null) {
            throw new RuntimeException("车次不存在");
        }

        TrainVO trainVO = convertToVO(train, LocalDate.parse(travelDate));

        // 获取经停站信息
        List<TrainStop> stops = trainStopMapper.selectList(
                new LambdaQueryWrapper<TrainStop>()
                        .eq(TrainStop::getTrainId, trainId)
                        .orderByAsc(TrainStop::getStopOrder)
        );

        List<TrainVO.StopVO> stopVOs = stops.stream()
                .map(stop -> {
                    TrainVO.StopVO stopVO = new TrainVO.StopVO();
                    stopVO.setStationName(stop.getStationName());
                    stopVO.setStopOrder(stop.getStopOrder());
                    stopVO.setArriveTime(stop.getArriveTime());
                    stopVO.setDepartTime(stop.getDepartTime());
                    stopVO.setStopDuration(stop.getStopDuration());
                    stopVO.setIsStart(stop.getIsStart() == 1);
                    stopVO.setIsEnd(stop.getIsEnd() == 1);
                    return stopVO;
                })
                .collect(Collectors.toList());

        trainVO.setStops(stopVOs);
        return trainVO;
    }

    @Override
    public TrainVO queryByTrainCode(String trainCode) {
        Train train = this.lambdaQuery()
                .eq(Train::getTrainCode, trainCode)
                .eq(Train::getStatus, 1)
                .one();

        if (train == null) {
            throw new RuntimeException("车次不存在");
        }

        return convertToVO(train, LocalDate.now());
    }

    /**
     * 转换为VO
     */
    private TrainVO convertToVO(Train train, LocalDate travelDate) {
        TrainVO vo = new TrainVO();
        vo.setTrainId(train.getId());
        vo.setTrainCode(train.getTrainCode());
        vo.setTrainType(train.getTrainType());
        vo.setTrainTypeName(getTrainTypeName(train.getTrainType()));
        vo.setStartStationName(train.getStartStationName());
        vo.setEndStationName(train.getEndStationName());
        vo.setStartTime(train.getStartTime());
        vo.setEndTime(train.getEndTime());
        vo.setTotalTime(train.getTotalTime());
        vo.setTotalTimeText(formatTotalTime(train.getTotalTime()));
        vo.setTravelDate(travelDate);
        vo.setHasSeatSelection(train.getHasSeatSelection() == 1);

        // 生成座位信息（模拟）
        List<TrainVO.SeatVO> seats = generateSeatInfo(train);
        vo.setSeats(seats);

        return vo;
    }

    /**
     * 查找车站
     */
    private Station findStation(String stationName) {
        return stationMapper.selectOne(
                new LambdaQueryWrapper<Station>()
                        .eq(Station::getStatus, 1)
                        .and(wrapper -> wrapper
                                .eq(Station::getStationName, stationName)
                                .or()
                                .eq(Station::getStationCode, stationName.toUpperCase())
                        )
        );
    }

    /**
     * 生成座位信息（模拟）
     */
    private List<TrainVO.SeatVO> generateSeatInfo(Train train) {
        List<SeatType> seatTypes = seatTypeMapper.selectList(null);
        List<TrainVO.SeatVO> seats = new ArrayList<>();

        for (SeatType seatType : seatTypes) {
            // 根据车次类型决定座位类型
            if (train.getTrainType() <= 3) { // 高铁动车
                if (!seatType.getSeatTypeCode().equals("SWZ") &&
                    !seatType.getSeatTypeCode().equals("ZY") &&
                    !seatType.getSeatTypeCode().equals("ZE") &&
                    !seatType.getSeatTypeCode().equals("WZ")) {
                    continue;
                }
            } else { // 普速列车
                if (seatType.getSeatTypeCode().equals("SWZ") ||
                    seatType.getSeatTypeCode().equals("ZY") ||
                    seatType.getSeatTypeCode().equals("ZE")) {
                    continue;
                }
            }

            TrainVO.SeatVO seatVO = new TrainVO.SeatVO();
            seatVO.setSeatTypeId(seatType.getId());
            seatVO.setSeatTypeCode(seatType.getSeatTypeCode());
            seatVO.setSeatTypeName(seatType.getSeatTypeName());

            // 模拟座位数（高铁通常100-500个座位）
            int baseSeats = train.getTrainType() <= 3 ? 200 : 100;
            int randomSold = (int) (Math.random() * baseSeats * 0.8); // 随机已售数量

            seatVO.setTotalSeats(baseSeats);
            seatVO.setAvailableSeats(baseSeats - randomSold);
            seatVO.setSoldSeats(randomSold);

            // 计算票价（基于距离和座位类型）
            BigDecimal price = calculatePrice(train.getTotalDistance(), seatType);
            seatVO.setPrice(price);

            seatVO.setHasTicket(seatVO.getAvailableSeats() > 0);
            seatVO.setCanWaiting(seatVO.getAvailableSeats() == 0 && seatVO.getSoldSeats() < seatVO.getTotalSeats());

            seats.add(seatVO);
        }

        return seats;
    }

    /**
     * 计算票价
     */
    private BigDecimal calculatePrice(Integer distance, SeatType seatType) {
        if (distance == null) {
            distance = 500;
        }
        // 基础票价：每公里0.1元 * 基础价格系数
        double basePrice = distance * 0.1 * seatType.getBasePrice().doubleValue() / 40;
        return BigDecimal.valueOf(Math.round(basePrice * 100) / 100.0);
    }

    /**
     * 获取车次类型名称
     */
    private String getTrainTypeName(Integer trainType) {
        switch (trainType) {
            case 1: return "高铁";
            case 2: return "动车";
            case 3: return "城际";
            case 4: return "直达";
            case 5: return "特快";
            case 6: return "快速";
            case 7: return "普快";
            default: return "未知";
        }
    }

    /**
     * 格式化总耗时
     */
    private String formatTotalTime(Integer minutes) {
        if (minutes == null) {
            return "";
        }
        int hours = minutes / 60;
        int mins = minutes % 60;
        if (hours > 0) {
            return hours + "小时" + (mins > 0 ? mins + "分钟" : "");
        }
        return mins + "分钟";
    }

    /**
     * 获取最低票价
     */
    private BigDecimal getMinPrice(List<TrainVO.SeatVO> seats) {
        if (seats == null || seats.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return seats.stream()
                .filter(TrainVO.SeatVO::getHasTicket)
                .map(TrainVO.SeatVO::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}