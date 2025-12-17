package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Author beaker
 * @Date 2025/12/17 15:50
 * @Description 订单处理任务
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")
    public void executeTimeOutOrder() {
        log.info("定时处理超时待支付订单: {}", new Date());

        Orders ordersDB = Orders.builder()
                .status(Orders.PENDING_PAYMENT)
                .orderTime(LocalDateTime.now().plusMinutes(-15))
                .build();
        List<Orders> list = orderMapper.getByStatusAndOrderTime(ordersDB);

        if (list != null && !list.isEmpty()) {
            for (Orders orders : list) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时, 自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void executeDeliverOrder() {
        log.info("定时处理派送中的订单: {}", new Date());

        Orders ordersDB = Orders.builder()
                .orderTime(LocalDateTime.now().plusMinutes(-60))
                .status(Orders.DELIVERY_IN_PROGRESS)
                .build();
        List<Orders> list = orderMapper.getByStatusAndOrderTime(ordersDB);

        if (list != null && !list.isEmpty()) {
            for (Orders orders : list) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
