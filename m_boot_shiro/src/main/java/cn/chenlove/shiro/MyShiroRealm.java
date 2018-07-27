package cn.chenlove.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ByteSource;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import cn.chenlove.model.Resources;
import cn.chenlove.model.User;
import cn.chenlove.service.ResourcesService;
import cn.chenlove.service.UserService;


public class MyShiroRealm extends AuthorizingRealm{
    
	@Resource
	private UserService userService;
	
    @Resource
    private ResourcesService resourcesService;
    
    @Resource
    private RedisSessionDAO redisSessionDAO;
	
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userid", user.getId());
		List<Resources> resourcesList = resourcesService.loadUserResources(map);
		
		SimpleAuthorizationInfo info =new SimpleAuthorizationInfo();
		for(Resources resources: resourcesList) {
			info.addStringPermission(resources.getResurl());
		}
		return info;
	}
    //认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String)token.getPrincipal();
		User user = userService.selectByUsername(username);
		if(user==null)
			throw new UnknownAccountException();
		if(0==user.getEnable()) {
			throw new LockedAccountException(); // 帐号锁定
		}
		SimpleAuthenticationInfo  authenticationInfo = new SimpleAuthenticationInfo(
		  user,
		  user.getPassword(),
		  ByteSource.Util.bytes(username),
		  getName()
		);
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute("userSession", user);
		session.setAttribute("userSessionId", user.getId());
		return authenticationInfo;
	}
	
	public void clearUserAuthByUserId(List<Integer> userIds) {
		if(null==userIds || userIds.size()==0)
			return ;
		Collection<Session> sessions = redisSessionDAO.getActiveSessions();
		List<SimplePrincipalCollection> list = new ArrayList<SimplePrincipalCollection>();
		for(Session session: sessions) {
			Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
		    if(null!=obj && obj instanceof SimplePrincipalCollection) {
		    	SimplePrincipalCollection spc =(SimplePrincipalCollection)obj;
		    	obj = spc.getPrimaryPrincipal();
		    	if(null!=obj && obj instanceof User) {
		    		User user = (User)obj;
		    		System.out.println("user:"+user);
		    		if(null!=user && userIds.contains(user.getId())) {
		    			list.add(spc);
		    		}
		    	}
		    }
		
		}
		RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
		MyShiroRealm realm = (MyShiroRealm) securityManager.getRealms().iterator().next();
	    for(SimplePrincipalCollection simplePrincipalCollection:list) {
	    	realm.clearCachedAuthorizationInfo(simplePrincipalCollection);
	    }
	}
	
	
	

}
