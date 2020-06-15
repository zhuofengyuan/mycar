package com.qf.service.impl;

import com.qf.dao.CartItemMapper;
import com.qf.dao.CartMapper;
import com.qf.pojo.*;
import com.qf.service.CartService;
import com.qf.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

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
    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    TransactionDefinition transactionDefinition;

    @Override
    public int insert(Cart record) {
        int i = 0;
        if(cartMapper.countByUserId(record.getUserid()) == 0){
            TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            cartMapper.insert(record);
            i = record.getId();
            dataSourceTransactionManager.commit(transactionStatus);
        } else {
            CartExample e = new CartExample();
            e.createCriteria().andUseridEqualTo(record.getUserid());
            Cart c = this.cartMapper.selectByExample(e).get(0);
            i = c.getId();
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
    public int updateQty(Integer id, Integer qty, Integer price) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setQty(qty);
        item.setGoodsTotalPrice(price);
        return cartItemMapper.updateByPrimaryKeySelective(item);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return cartItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByExample(CartItemExample example) {
        return cartItemMapper.deleteByExample(example);
    }
}
