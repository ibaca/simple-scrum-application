package org.inftel.ssa.services;

import javax.ejb.Local;
import org.inftel.ssa.domain.User;

/**
 *
 * @author ibaca
 */
@Local
public interface SessionService {

	User currentUser();
	
}
