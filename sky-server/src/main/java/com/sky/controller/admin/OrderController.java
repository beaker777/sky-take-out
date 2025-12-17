package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author beaker
 * @Date 2025/12/16 22:15
 * @Description 订单管理接口
 */
@RestController("管理端订单管理接口")
@RequestMapping("/admin/order")
@Api(tags = "订单管理接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 条件搜索订单信息
     *
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("条件搜索订单信息")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("条件搜索订单信息: {}", ordersPageQueryDTO);

        PageResult page = orderService.conditionSearch(ordersPageQueryDTO);

        return Result.success(page);
    }

    /**
     * 各状态订单数量统计
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("各状态订单数量统计")
    public Result<OrderStatisticsVO> statistics() {
        log.info("各状态订单数量统计");

        OrderStatisticsVO orderStatisticsVO = orderService.statistics();

        return Result.success(orderStatisticsVO);
    }

    /**
     * 查看订单信息
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("查看订单信息")
    public Result<OrderVO> getById(@PathVariable Long id) {
        log.info("查看订单信息: {}", id);

        OrderVO orderVO = orderService.getById(id);

        return Result.success(orderVO);
    }

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单: {}", ordersConfirmDTO);

        orderService.confirm(ordersConfirmDTO);

        return Result.success();
    }

    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        log.info("拒单: {}", ordersRejectionDTO);

        orderService.rejection(ordersRejectionDTO);

        return Result.success();
    }

    /**
     * 管理端取消订单
     *
     * @param ordersCancelDTO
     * @return
     * @throws Exception
     */
    @PutMapping("/cancel")
    @ApiOperation("管理端取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("管理端取消订单: {}", ordersCancelDTO);

        orderService.adminCancelById(ordersCancelDTO);

        return Result.success();
    }

    /**
     * 派送
     *
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送")
    public Result delivery(@PathVariable Long id) {
        log.info("派送: {}", id);

        orderService.delivery(id);

        return Result.success();
    }

    /**
     * 完成订单
     *
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable Long id) {
        log.info("完成订单: {}", id);

        orderService.complete(id);

        return Result.success();
    }
}
