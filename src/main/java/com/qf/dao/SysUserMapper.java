package com.qf.dao;

import com.qf.dto.QueryDTO;
import com.qf.pojo.SysUser;
import com.qf.pojo.SysUserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysUserMapper {
    int countByExample(SysUserExample example);

    int deleteByExample(SysUserExample example);

    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    List<SysUser> selectByExample(SysUserExample example);

    SysUser selectByPrimaryKey(Long userId);

    int updateByExampleSelective(@Param("record") SysUser record, @Param("example") SysUserExample example);

    int updateByExample(@Param("record") SysUser record, @Param("example") SysUserExample example);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    List<SysUser> findByPage(QueryDTO queryDTO);

    List<Map<String,Object>> exportUser();

    public SysUser findByUsername(String username);
    int updatePassword(@Param("username") String username,@Param("password") String password);

    int updatePersonal(@Param("address")String address, @Param("phone")String phone, @Param("email")String email, @Param("id")String id);
}