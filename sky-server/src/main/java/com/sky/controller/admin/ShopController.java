package com.sky.controller.admin;

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author beaker
 * @Date 2025/12/12 19:01
 * @Description 商铺管理接口
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "商家商铺管理")
@Slf4j
public class ShopController {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 设置店铺营业状态
     *
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态: {}", status);

        redisTemplate.opsForValue().set(RedisConstant.SHOP_STATUS, status);

        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus() {
        log.info("获取店铺营业状态");

        Integer status = (Integer) redisTemplate.opsForValue().get(RedisConstant.SHOP_STATUS);

        return Result.success(status);
    }
}
