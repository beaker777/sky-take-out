package com.sky.service;

import com.sky.dto.SetmealDTO;

/**
 * @Author beaker
 * @Date 2025/12/11 20:19
 * @Description 套餐管理服务层接口
 */
public interface SetmealService {

    /**
     * 添加套餐
     * @param setmealDTO
     */
    void save(SetmealDTO setmealDTO);
}
