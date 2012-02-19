package org.inftel.ssa.services;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.inftel.ssa.domain.User;
import org.inftel.ssa.domain.UserFacade;

/**
 *
 * @author ibaca
 */
@Stateless
public class SessionServiceImpl implements SessionService {
	
	@EJB
	private UserFacade users;
	
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
	
	@Override
	public void saveUser(User task) {
		if (task.isNew()) {
			users.create(task);
		} else {
			users.edit(task);
		}
	}

	@Override
	public User findUser(Object id) {
		return users.find(id);
	}

	
	
}
