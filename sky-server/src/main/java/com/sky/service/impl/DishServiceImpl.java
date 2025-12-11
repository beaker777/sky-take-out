package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    SetmealDishMapper setmealDishMapper;

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

    /**
     * 删除菜品
     *
     * @param ids
     */
    @Override
    @Transactional
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);

            // 判断是否正在销售中
            if (dish.getStatus() == StatusConstant.ENABLE) {
                // 正在销售中, 不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }

            Long setmealId = setmealDishMapper.getSetmealIdByDishId(id);
            // 判断菜品是否关联在套餐中
            if (setmealId != null) {
                // 关联了套餐, 不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }

            // 删除菜品
            dishMapper.delete(id);

            // 删除菜品关联的口味
            dishFlavorMapper.deleteByDishId(id);
        }
    }

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        // 获取菜品数据
        Dish dish = dishMapper.getById(id);

        // 获取口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 更新菜品
     *
     * @param dishDTO
     */
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = Dish.builder()
                .id(dishDTO.getId())
                .categoryId(dishDTO.getCategoryId())
                .description(dishDTO.getDescription())
                .image(dishDTO.getImage())
                .name(dishDTO.getName())
                .price(dishDTO.getPrice())
                .status(dishDTO.getStatus())
                .build();
        dishMapper.update(dish);

        // 删除原本口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDTO.getId());
            dishFlavorMapper.insert(flavor);
        }
    }

    /**
     * 根据分类查询菜品
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Integer categoryId) {
        return dishMapper.list(categoryId);
    }

    /**
     * 启用或禁用菜品
     *
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();

        dishMapper.update(dish);
    }
}
