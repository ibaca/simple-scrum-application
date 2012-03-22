
package org.inftel.ssa.services;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.inftel.ssa.domain.Project;
import org.inftel.ssa.domain.ProjectFacade;

@Stateless
@LocalBean
public class ProjectRequestService {

    private static final Logger log = Logger.getLogger(ProjectRequestService.class.getName());

    @EJB
    private ProjectFacade projects;

    public ProjectRequestService() {
    }

    public Long countUsers() {
        return Integer.valueOf(projects.count()).longValue();
    }

    public List<Project> findAllProjects() {
        log.info("buscando todos los proyectos");
        return projects.findAll();
    }

    public Project findProject(Long id) {
        log.info("buscando proyecto " + id);
        return projects.find(id);
    }

    public List<Project> findProjectEntries(int firstResult, int maxResults) {
        log.info("buscanado proyectos first: " + firstResult + ", max: " + maxResults);
        return projects.find(firstResult, maxResults, null, null, null);
    }

    public List<Project> findProjectsSince(Date date) {
        log.info("buscanado proyectos desde: " + date);
        return projects.findSince(date);
    }

    public void persist(Project instance) {
        log.info("creando proyecto " + instance);
        if (instance == null) {
            throw new NullPointerException();
        }
        if (instance.getId() != null) {
            throw new RuntimeException("Id debe ser nulo, es decir, proyecto nuevo");
        }
        if (instance.getName() == null || instance.getName().trim().length() == 0) {
            throw new RuntimeException("Un proyecto debe crearse con nombre valido");
        }
        projects.create(instance);
    }

    public void remove(Project instance) {
        log.info("borrando proyecto " + instance);
        projects.remove(instance);
    }
}
