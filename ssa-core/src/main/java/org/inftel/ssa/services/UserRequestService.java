
package org.inftel.ssa.services;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.inftel.ssa.domain.User;
import org.inftel.ssa.domain.UserFacade;

@Stateless
@LocalBean
public class UserRequestService {

    private static final Logger log = Logger.getLogger(UserRequestService.class.getName());

    @EJB
    private UserFacade users;

    public UserRequestService() {
    }

    public Long countUsers() {
        return Integer.valueOf(users.count()).longValue();
    }

    public List<User> findAllUsers() {
        log.info("buscando todos los usuarios");
        return users.findAll();
    }

    public User findUser(Long id) {
        log.info("buscando usuario " + id);
        return users.find(id);
    }

    public List<User> findUserEntries(int firstResult, int maxResults) {
        log.info("buscanado usuarios first: " + firstResult + ", max: " + maxResults);
        return users.find(firstResult, maxResults, null, null, null);
    }

    public void persist(User instance) {
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
    }

    public void remove(User instance) {
        log.info("borrando usuario " + instance);
        users.remove(instance);
    }
}
