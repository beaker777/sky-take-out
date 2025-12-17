package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author beaker
 * @Date 2025/12/16 15:43
 * @Description 订单管理持久层
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 历史订单查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询订单详情
     * @param id
     */
    Orders getById(Long id);

    /**
     * 统计各状态订单数量
     * @param status
     */
    Integer countByStatus(Integer status);

    /**
     * 根据状态和时间查询订单
     * @param orders
     * @return
     */
    List<Orders> getByStatusAndOrderTime(Orders orders);

    /**
     * 根据时间查询营业额
     * @param map
     * @return
     */
    Double sumByTime(Map<String, Object> map);

    /**
     * 查询订单数
     * @param map
     * @return
     */
    Integer countOrder(Map<String, Object> map);

    /**
     * 查询有效订单数
     * @param map
     * @return
     */
    Integer countValidOrder(Map<String, Object> map);

    /**
     * 查询排名前10的商品
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getTop10(LocalDateTime begin, LocalDateTime end);
}
