/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.inftel.ssa.domain.User;

/**
 *
 * @author ibaca
 */
public class SsaAuthorizingRealm extends AuthorizingRealm {

	private final static Logger logger = Logger.getLogger(SsaAuthorizingRealm.class.getName());

	public SsaAuthorizingRealm() {
		setName("JpaRealm"); //This name must match the name in the User class's getPrincipals() method
	}

	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		logger.info("doGetAuthenticationInfo");
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		EntityManager em = null;
		logger.info("doGetAuthenticationInfo authcToken[user=" + token.getUsername() + ", pass=" + new String(token.getPassword()) + "]");
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("userName", token.getUsername());
			em = Persistence.createEntityManagerFactory("ssa-persistence-unit").createEntityManager();
			List<User> users = em.createQuery("select o from User o where o.email = '" + token.getUsername() + "'", User.class).getResultList();

			if (users.size() > 0) {
				User user = users.iterator().next();
				logger.info("doGetAuthenticationInfo database[user=" + user.getId() + ", pass=" + user.getPassword() + "]");
				return new SimpleAuthenticationInfo(user.getId(), user.getPassword(), getName());
			} else {
				return null;
			}
		} finally {
			em.close();
		}
	}

	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.info("doGetAuthorizationInfo");
		Long userId = (Long) principals.fromRealm(getName()).iterator().next();
		//User user = userService.find(userId);
		//if (user != null) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//for (Role role : user.getRoles()) {
		//    info.addRole(role.getName());
		//    info.addStringPermissions(role.getPermissions());
		//}
		return info;
		//} else {
		//    return null;
		//}
	}
}
