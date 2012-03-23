
package org.inftel.ssa.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName(value = "org.inftel.ssa.domain.User", locator = "org.inftel.ssa.locators.EntityLocator")
public interface UserProxy extends EntityProxy {

    public abstract Long getId();

    public abstract String getCompany();

    public abstract void setCompany(String company);

    public abstract Set<ProjectProxy> getProjects();

    public abstract void setProjects(Set<ProjectProxy> projects);

    public abstract String getNickname();

    public abstract void setNickname(String nickname);

    public abstract String getFullName();

    public abstract void setFullName(String fullname);

    public abstract String getEmail();

    public abstract void setEmail(String email);

    public abstract String getPassword();

    public abstract void setPassword(String password);

    public abstract List<TaskProxy> getTasks();

    public abstract void setTasks(List<TaskProxy> tasks);

    public abstract String getUserRole();

    public abstract void setUserRole(String role);

    /** Id para capa de presentacion */
    EntityProxyId<ProjectProxy> stableId();

    /** Fecha ultima modificacion. */
    public abstract Date getUpdated();

    /** Fecha creacion. */
    public abstract Date getCreated();

}
