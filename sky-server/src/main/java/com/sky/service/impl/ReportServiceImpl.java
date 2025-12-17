package com.sky.service.impl;

import com.sky.constant.SplitConstant;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @Author beaker
 * @Date 2025/12/17 20:06
 * @Description 数据统计服务层实现类
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        // 存放 begin 到 end 的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.equals(end)) {
            // 将日期加入 list 之后 +1
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        dateList.add(end);

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.sumByTime(map);
            if (turnover == null) turnover = 0.0;
            turnoverList.add(turnover);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, SplitConstant.SPLIT))
                .turnoverList(StringUtils.join(turnoverList, SplitConstant.SPLIT))
                .build();
    }

    /**
     * 用户统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        // 存放 begin 到 end 的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.equals(end)) {
            // 将日期加入 list 之后 +1
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        dateList.add(end);

        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);

            Integer newUser = userMapper.sumNewUser(map);
            Integer totalUser = userMapper.sumTotalUser(map);

            if (newUser == null) newUser = 0;
            if (totalUser == null) totalUser = 0;
            newUserList.add(newUser);
            totalUserList.add(totalUser);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, SplitConstant.SPLIT))
                .newUserList(StringUtils.join(newUserList, SplitConstant.SPLIT))
                .totalUserList(StringUtils.join(totalUserList, SplitConstant.SPLIT))
                .build();
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO orderStatistics(LocalDate begin, LocalDate end) {
        // 存放 begin 到 end 的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.equals(end)) {
            // 将日期加入 list 之后 +1
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        dateList.add(end);

        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        Integer totalOrderCount = 0;
        Integer totalValidOrderCount = 0;
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);

            Integer orderCount = orderMapper.countOrder(map);
            Integer validOrderCount = orderMapper.countValidOrder(map);

            if (orderCount == null) orderCount = 0;
            if (validOrderCount == null) validOrderCount = 0;
            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);

            totalOrderCount += orderCount;
            totalValidOrderCount += validOrderCount;
        }

        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) orderCompletionRate = totalValidOrderCount.doubleValue() / totalOrderCount;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, SplitConstant.SPLIT))
                .orderCountList(StringUtils.join(orderCountList, SplitConstant.SPLIT))
                .totalOrderCount(totalOrderCount)
                .validOrderCountList(StringUtils.join(validOrderCountList, SplitConstant.SPLIT))
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 销售前10名
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getTop10(beginTime, endTime);

        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        for (GoodsSalesDTO goodsSalesDTO : goodsSalesDTOList) {
            nameList.add(goodsSalesDTO.getName());
            numberList.add(goodsSalesDTO.getNumber());
        }

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, SplitConstant.SPLIT))
                .numberList(StringUtils.join(numberList, SplitConstant.SPLIT))
                .build();
    }
}
