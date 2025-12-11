package com.sky.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author beaker
 * @Date 2025/12/11 16:39
 * @Description TODO
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 根据dishId查询id
     * @param id
     * @return
     */
    Long getSetmealIdByDishId(Long id);
}
