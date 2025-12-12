package com.sky.controller.user;

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author beaker
 * @Date 2025/12/12 19:09
 * @Description 店铺管理接口
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "用户商铺管理")
@Slf4j
public class ShopController {

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus() {
        log.info("获取店铺营业状态");

        Integer status = (Integer) redisTemplate.opsForValue().get(RedisConstant.SHOP_STATUS);

        return Result.success(status);
    }
}
