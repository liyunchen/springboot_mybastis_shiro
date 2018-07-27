package cn.chenlove.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.chenlove.mapper.ResourcesMapper;
import cn.chenlove.model.Resources;
import cn.chenlove.service.ResourcesService;
import tk.mybatis.mapper.entity.Example;
@Service("resourcesService")
public class ResourcesServiceImpl extends BaseService<Resources> implements ResourcesService  {
    @Resource
	private ResourcesMapper resourcesMapper;
	
	@Override
	public PageInfo<Resources> selectByPage(Resources resources, int start, int length) {
		int page = start/length+1;
		Example example = new Example(Resources.class);
		PageHelper.startPage(page, length);
		List<Resources> userList = selectByExample(example);
		return new PageInfo<>(userList);
	}

	@Override
	public List<Resources> queryAll() {
		// TODO Auto-generated method stub
		return resourcesMapper.queryAll();
	}

	@Override
	public List<Resources> loadUserResources(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return resourcesMapper.loadUserResources(map);
	}

	@Override
	public List<Resources> queryResourcesListWithSelected(Integer rid) {
		// TODO Auto-generated method stub
		return resourcesMapper.queryResourcesListWithSelected(rid);
	}

}
