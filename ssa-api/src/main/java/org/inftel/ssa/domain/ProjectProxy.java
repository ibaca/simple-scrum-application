
package org.inftel.ssa.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName(value = "org.inftel.ssa.domain.Project", locator = "org.inftel.ssa.locators.EntityLocator")
public interface ProjectProxy extends EntityProxy {

    public abstract String getSummary();

    public abstract void setSummary(String summary);

    public abstract Set<String> getLabels();

    public abstract void setLabels(Set<String> labels);

    public abstract String getLicense();

    public abstract void setLicense(String license);

    public abstract Date getClosed();

    public abstract void setClosed(Date closed);

    public abstract String getCompany();

    public abstract void setCompany(String company);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    // public abstract Map<String, String> getLinks();

    // public abstract void setLinks(Map<String, String> links);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Date getOpened();

    public abstract void setOpened(Date opened);

    // public abstract List<Sprint> getSprints();

    // public abstract void setSprints(List<Sprint> sprints);

    public abstract Date getStarted();

    public abstract void setStarted(Date started);

    public abstract List<TaskProxy> getTasks();

    public abstract void setTasks(List<TaskProxy> tasks);

    public abstract Set<UserProxy> getUsers();

    public abstract void setUsers(Set<UserProxy> users);

    /** Id para capa de presentacion */
    EntityProxyId<ProjectProxy> stableId();

    /** Fecha ultima modificacion. */
    public abstract Date getUpdated();

    /** Fecha creacion. */
    public abstract Date getCreated();

}
