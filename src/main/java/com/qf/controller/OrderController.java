package com.qf.controller;

import com.qf.dao.CartMapper;
import com.qf.pojo.Cart;
import com.qf.pojo.CartExample;
import com.qf.pojo.CartItemExample;
import com.qf.pojo.SysUser;
import com.qf.service.CartService;
import com.qf.service.OrderService;
import com.qf.service.SysUserService;
import com.qf.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/order")
@Controller
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    CartService cartService;
    @Autowired
    CartMapper cartMapper;

    /**
     * 根据购物车新增订单
     * @return
     */
    @GetMapping("/add/cart")
    public @ResponseBody R addCart(@RequestParam("ids[]") List<Integer> ids, String username){
        this.orderService.insertByCartItem(ids, sysUserService.findByUsername(username));

        //清空购物车
        SysUser user = sysUserService.findByUsername(username);
        CartExample e = new CartExample();
        e.createCriteria().andUseridEqualTo(Integer.parseInt(user.getUserId().toString()));
        Cart c = this.cartMapper.selectByExample(e).get(0);
        CartItemExample eX = new CartItemExample();
        eX.createCriteria().andCartIdEqualTo(c.getId());
        this.cartService.deleteByExample(eX);
        return R.ok();
    }

    /**
     * 根据商品新增订单
     * @param ids
     * @param username
     * @return
     */
    @GetMapping("/add/product")
    public @ResponseBody R addCart(Integer ids, Integer qty, String username){
        this.orderService.insertByProduct(ids, qty, sysUserService.findByUsername(username));
        return R.ok();
    }

    /**
     * 获取所有订单
     * @param username
     * @return
     */
    @GetMapping("/list")
    public @ResponseBody R list(String username){
        return R.ok().put("list", this.orderService.selectAll(sysUserService.findByUsername(username)));
    }
}

