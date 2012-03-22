
package org.inftel.ssa.services;

import java.util.Date;
import java.util.List;

import org.inftel.ssa.domain.ProjectProxy;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

/**
 * Permite obtener y gestionar los usuarios registrados en el sistema.
 * 
 * @author ibaca
 */
@ServiceName(value = "org.inftel.ssa.services.ProjectRequestService", locator = "org.inftel.ssa.locators.BeanLocator")
public interface ProjectRequest extends RequestContext {

    Request<Long> countUsers();

    Request<List<ProjectProxy>> findAllProjects();

    Request<List<ProjectProxy>> findProjectEntries(int firstResult, int maxResults);

    Request<List<ProjectProxy>> findProjectsSince(Date date);

}
