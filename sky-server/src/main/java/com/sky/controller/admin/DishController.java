package com.sky.controller.admin;

import com.sky.constant.RedisConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @Author beaker
 * @Date 2025/12/11 13:54
 * @Description 菜品管理接口
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品: {}", dishDTO);

        dishService.saveWithFlavor(dishDTO);

        // 新增菜品后修改缓存数据
        redisTemplate.delete(RedisConstant.DISH + dishDTO.getCategoryId());

        return Result.success();
    }

    /**
     * 分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询: {}", dishPageQueryDTO);

        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("删除菜品: {}", ids);

        dishService.delete(ids);

        // 清空缓存
        Set keys = redisTemplate.keys(RedisConstant.DISH + "*");
        redisTemplate.delete(keys);

        return Result.success();
    }

    /**
     * 根据id获取菜品数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id获取菜品数据")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id获取菜品数据: {}", id);

        DishVO dishVO = dishService.getById(id);

        return Result.success(dishVO);
    }

    /**
     * 编辑菜品
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("编辑菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("编辑菜品: {}", dishDTO);

        dishService.update(dishDTO);

        // 清空缓存
        Set keys = redisTemplate.keys(RedisConstant.DISH + "*");
        redisTemplate.delete(keys);

        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(@RequestParam Integer categoryId) {
        log.info("根据分类id查询菜品: {}", categoryId);

        List<Dish> dishes = dishService.list(categoryId);

        return Result.success(dishes);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用或禁用菜品")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用或禁用菜品: {}, {}", status, id);

        dishService.startOrStop(status, id);

        // 清空缓存
        Set keys = redisTemplate.keys(RedisConstant.DISH + "*");
        redisTemplate.delete(keys);

        return Result.success();
    }

    
}
