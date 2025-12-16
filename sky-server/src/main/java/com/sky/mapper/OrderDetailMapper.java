package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author beaker
 * @Date 2025/12/16 15:47
 * @Description 订单细节持久层
 */
@Mapper
public interface OrderDetailMapper {

    /**
     * 插入订单细节数据
     * @param orderDetail
     */
    void insert(OrderDetail orderDetail);
}
