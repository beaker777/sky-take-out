package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author beaker
 * @Date 2025/12/11 14:01
 * @Description 菜品管理接口实现
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品和对应口味
     *
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        // 在菜品表中插入数据
        Dish dish = Dish.builder()
                .categoryId(dishDTO.getCategoryId())
                .description(dishDTO.getDescription())
                .image(dishDTO.getImage())
                .name(dishDTO.getName())
                .price(dishDTO.getPrice())
                .build();
        dishMapper.insert(dish);

        // 获取菜品id
        Long id = dish.getId();

        // 在口味表中插入数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 判断是否需要添加口味
        if (flavors == null || flavors.isEmpty()) return;
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
            dishFlavorMapper.insert(flavor);
        }
    }

    /**
     * 分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }
}
