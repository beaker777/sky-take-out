package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * @Author beaker
 * @Date 2025/12/11 14:00
 * @Description 菜品管理服务层接口
 */
public interface DishService {

    /**
     * 新增菜品和对应口味
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
}
