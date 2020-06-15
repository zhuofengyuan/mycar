package com.qf.service.impl;

import com.qf.dao.CartItemMapper;
import com.qf.dao.CartMapper;
import com.qf.pojo.*;
import com.qf.service.CartService;
import com.qf.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartMapper cartMapper;
    @Autowired
    CartItemMapper cartItemMapper;
    @Autowired
    SysUserService sysUserService;

    @Override
    public int insert(Cart record) {
        int i = 0;
        if(cartMapper.countByUserId(record.getUserid()) == 0){
            i = cartMapper.insert(record);
        }
        return i;
    }

    @Override
    public int insertItem(CartItem record) {
        SysUser u = sysUserService.findByUsername(record.getUsername());
        CartExample e = new CartExample();
        e.createCriteria().andUseridEqualTo(Integer.parseInt(u.getUserId().toString()));
        Cart c = this.cartMapper.selectByExample(e).get(0);
        record.setCartId(c.getId());
        record.setCreateTime(new Date());
        return cartItemMapper.insert(record);
    }

    @Override
    public List<CartItem> selectByCartId(Integer cartId) {
        CartItemExample example = new CartItemExample();
        example.createCriteria().andCartIdEqualTo(cartId);
        example.setOrderByClause("id");
        return cartItemMapper.selectByExample(example);
    }

    @Override
    public List<CartItem> selectByCartItemIds(List<Integer> ids) {
        CartItemExample example = new CartItemExample();
        example.createCriteria().andIdIn(ids);
        example.setOrderByClause("id");
        return cartItemMapper.selectByExample(example);
    }

    @Override
    public int updateQty(Integer id, Integer qty) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setQty(qty);
        return cartItemMapper.updateByPrimaryKeySelective(item);
    }
}
