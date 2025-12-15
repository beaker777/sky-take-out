package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author beaker
 * @Date 2025/12/15 21:27
 * @Description 购物车管理服务层实现类
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        // 判断添加的菜品或套餐是否已经在购物车中
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .setmealId(shoppingCartDTO.getSetmealId())
                .dishId(shoppingCartDTO.getDishId())
                .dishFlavor(shoppingCartDTO.getDishFlavor())
                .build();
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);

        // 在购物车中, 更新 (数量 +1)
        if (shoppingCarts != null && !shoppingCarts.isEmpty()) {
            // 查询到的列表中的第一个数据就是对应的购物车记录
            shoppingCart = shoppingCarts.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);

            shoppingCartMapper.updateNumberById(shoppingCart);
            return;
        }

        // 不在购物车中, 插入
        // 先判断添加的是菜品还是套餐
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        if (setmealId != null) {
            // 添加套餐
            Setmeal setmeal = setmealMapper.getById(setmealId);
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setAmount(setmeal.getPrice());
            shoppingCart.setNumber(1);
        } else {
            // 添加菜品
            Dish dish = dishMapper.getById(dishId);
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setAmount(dish.getPrice());
            shoppingCart.setNumber(1);
        }

        // 插入数据
        shoppingCartMapper.insert(shoppingCart);
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build();

        return shoppingCartMapper.list(shoppingCart);
    }

    /**
     * 购物车菜品数量减1
     *
     * @param shoppingCartDTO
     */
    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        // 查询出被执行扣减操作的购物车
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .setmealId(shoppingCartDTO.getSetmealId())
                .dishId(shoppingCartDTO.getDishId())
                .dishFlavor(shoppingCartDTO.getDishFlavor())
                .build();
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
        shoppingCart = shoppingCarts.get(0);

        // 判断扣减只后数量是否为0
        shoppingCart.setNumber(shoppingCart.getNumber() - 1);
        if (shoppingCart.getNumber() == 0) {
            // 扣减后数量为0, 删除对应购物车
            shoppingCartMapper.delete(shoppingCart);
        } else {
            // 扣减后不为0, 更新数据
            shoppingCartMapper.updateNumberById(shoppingCart);
        }
    }

    /**
     * 清空购物车
     *
     */
    @Override
    public void clean() {
        shoppingCartMapper.cleanByUserId(BaseContext.getCurrentId());
    }
}
