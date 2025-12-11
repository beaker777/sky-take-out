package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

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

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 删除套餐
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 根据id获取套餐
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 编辑套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 启用或禁用套餐
     * @param status
     */
    void startOrStop(Integer status, Long id);
}
