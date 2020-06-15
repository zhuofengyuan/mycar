package com.qf.service;

import com.qf.pojo.Cart;
import com.qf.pojo.CartItem;

import java.util.List;

public interface CartService {

    int insert(Cart record);

    int insertItem(CartItem record);

    List<CartItem> selectByCartId(Integer example);

    List<CartItem> selectByCartItemIds(List<Integer> ids);

    int updateQty(Integer id, Integer qty);
}
