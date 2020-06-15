package com.qf.controller;

import com.qf.pojo.Cart;
import com.qf.pojo.CartItem;
import com.qf.service.CartService;
import com.qf.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/cart")
@Controller
public class CartController {

    @Autowired
    CartService cartService;

    /**
     * 新增购物车
     * @param record
     * @return
     */
    @GetMapping("/add")
    public @ResponseBody R addCart(Cart record){
        cartService.insert(record);
        return R.ok();
    }

    /**
     * 添加到购物车
     * @param record
     * @return
     */
    @GetMapping("/add/item")
    public @ResponseBody R addCart(CartItem record){
        cartService.insertItem(record);
        return R.ok();
    }

    /**
     * 获取所有的购物车项
     * @param record
     * @return
     */
    @GetMapping("/list")
    public @ResponseBody R itemList(Integer record){
        return R.ok().put("data", cartService.selectByCartId(record));
    }

    /**
     * 更新购物车数量
     * @param id
     * @param qty
     * @return
     */
    @GetMapping("/update/qty")
    public @ResponseBody R updateQty(Integer id, Integer qty){
        cartService.updateQty(id, qty);
        return R.ok();
    }
}
