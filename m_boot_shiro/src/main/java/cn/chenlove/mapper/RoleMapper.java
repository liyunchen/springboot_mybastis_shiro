package cn.chenlove.mapper;

import java.util.List;

import cn.chenlove.model.Role;
import cn.chenlove.util.MyMapper;

public interface RoleMapper extends MyMapper<Role> {
     public List<Role> queryRoleListWithSelected(Integer id);
}
