package org.inftel.ssa.services;

import javax.ejb.Stateless;
import org.inftel.ssa.domain.User;

/**
 *
 * @author ibaca
 */
@Stateless
public class SessionServiceImpl implements SessionService {

	@Override
	public User currentUser() {
		//TODO sistema de autenticacion
		User current = new User();
		current.setId(1l);
		current.setEmail("test.user@mail.com");
		current.setNickname("test-user");
		current.setPassword("test-password");
		return current;
	}

	
	
}
