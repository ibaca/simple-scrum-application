
package org.inftel.ssa.services;

import java.util.Date;
import java.util.List;

import org.inftel.ssa.domain.ProjectProxy;
import org.inftel.ssa.domain.TaskProxy;
import org.inftel.ssa.domain.UserProxy;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

/**
 * Permite obtener y gestionar los recursos del sistema.
 * 
 * @author ibaca
 */
@ServiceName(value = "org.inftel.ssa.services.SsaRequestService", locator = "org.inftel.ssa.locators.BeanLocator")
public interface SsaRequestContext extends RequestContext {

    // Users

    Request<Long> countUsers();

    Request<UserProxy> findUserById(Long id);

    Request<UserProxy> findUserByEmail(String email);

    Request<List<UserProxy>> findAllUsers();

    Request<List<UserProxy>> findUserEntries(int firstResult, int maxResults);

    // Projects

    Request<Long> countProjects();

    Request<List<ProjectProxy>> findAllProjects();

    Request<List<ProjectProxy>> findProjectEntries(int firstResult, int maxResults);

    Request<List<ProjectProxy>> findProjectsSince(Date date);

    // Tasks

    /**
     * Tareas asociadas al proyecto actualizadas posteriormente a la fecha
     * pasada. La fecha pasada puede ser nula, devolviendose todas las tareas.
     */
    Request<List<TaskProxy>> findTasksByProjectSince(Long projectId, Date since);

}
