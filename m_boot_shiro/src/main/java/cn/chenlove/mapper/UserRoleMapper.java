package cn.chenlove.mapper;

import java.util.List;

import cn.chenlove.model.UserRole;
import cn.chenlove.util.MyMapper;

public interface UserRoleMapper extends MyMapper<UserRole>{
    public List<Integer> findUserIdByRoleId(Integer roleId);
}
