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

	@Override
	public User findByEmail(String email) {
		return users.findByEmail(email);
	}
	
	

	
	
}
