
package org.inftel.ssa.services;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.inftel.ssa.domain.Project;
import org.inftel.ssa.domain.ProjectFacade;
import org.inftel.ssa.domain.Task;
import org.inftel.ssa.domain.TaskFacade;
import org.inftel.ssa.domain.User;
import org.inftel.ssa.domain.UserFacade;

@Stateless
@LocalBean
public class SsaRequestService {

    private static final Logger log = Logger.getLogger(SsaRequestService.class.getName());

    @EJB
    private UserFacade users;

    @EJB
    private ProjectFacade projects;

    @EJB
    private TaskFacade tasks;

    @PersistenceContext(unitName = "ssa-persistence-unit")
    private EntityManager em;

    public SsaRequestService() {
    }

    public Long countUsers() {
        return Integer.valueOf(users.count()).longValue();
    }

    public Long countProjects() {
        return Integer.valueOf(projects.count()).longValue();
    }

    public List<User> findAllUsers() {
        log.info("buscando todos los usuarios");
        return users.findAll();
    }

    public List<Project> findAllProjects() {
        log.info("buscando todos los proyectos");
        return projects.findAll();
    }

    public User findUserById(Long id) {
        log.info("buscando usuario " + id);
        return users.find(id);
    }

    public User findUserByEmail(String email) {
        if (email == null || email.trim().length() == 0) {
            return null;
        }
        return users.findByEmail(email);
    }

    public Project findProject(Long id) {
        log.info("buscando proyecto " + id);
        return projects.find(id);
    }

    public List<User> findUserEntries(int firstResult, int maxResults) {
        log.info("buscanado usuarios first: " + firstResult + ", max: " + maxResults);
        return users.find(firstResult, maxResults, null, null, null);
    }

    public List<Project> findProjectEntries(int firstResult, int maxResults) {
        log.info("buscanado proyectos first: " + firstResult + ", max: " + maxResults);
        return projects.find(firstResult, maxResults, null, null, null);
    }

    // TODO es necesario q sea by user since, sino no sirve!
    public List<Project> findProjectsSince(Date date) {
        log.info("buscanado proyectos desde: " + date);
        return projects.findSince(date);
    }

    public List<Task> findTasksByProjectSince(Long projectId, Date since) {
        return tasks.findByProjectSince(projectId, since);
    }

    public User persistUser(User instance) {
        log.info("creando usuario " + instance);
        if (instance == null) {
            throw new NullPointerException();
        }
        if (instance.getId() != null) {
            throw new RuntimeException("Id debe ser nulo, es decir, usuario nuevo");
        }
        if (instance.getEmail() == null || instance.getEmail().trim().length() == 0) {
            throw new RuntimeException("Un usuario debe crearse con email valido");
        }
        users.create(instance);
        return instance;
    }

    public Project persistProject(Project instance) {
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
        return instance;
    }

    public Task persistTask(Task instance) {
        log.info("creando proyecto " + instance);
        if (instance == null) {
            throw new NullPointerException();
        }
        if (instance.getId() != null) {
            throw new RuntimeException("Id debe ser nulo, es decir, proyecto nuevo");
        }
        tasks.create(instance);
        return instance;
    }

    public Object save(Object instance) {
        log.info("guardando entidad " + instance);
        em.merge(instance);
        return instance;
    }

    public void remove(User instance) {
        log.info("borrando usuario " + instance);
        users.remove(instance);
    }

    public void remove(Project instance) {
        log.info("borrando proyecto " + instance);
        projects.remove(instance);
    }

}
