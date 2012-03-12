
package org.inftel.ssa.services;

import java.util.List;

import org.inftel.ssa.domain.UserProxy;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

/**
 * Permite obtener y gestionar los usuarios registrados en el sistema.
 * 
 * @author ibaca
 */
@ServiceName(value = "org.inftel.ssa.services.UserRequestService", locator = "org.inftel.ssa.locators.BeanLocator")
public interface UserRequest extends RequestContext {

    Request<Long> countUsers();

    Request<List<UserProxy>> findAllUsers();

    Request<List<UserProxy>> findUserEntries(int firstResult, int maxResults);

}
