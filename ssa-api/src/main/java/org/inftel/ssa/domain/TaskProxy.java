
package org.inftel.ssa.domain;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName(value = "org.inftel.ssa.domain.Task", locator = "org.inftel.ssa.locators.EntityLocator")
public interface TaskProxy extends EntityProxy {

    public abstract Long getId();

    public abstract Date getBeginDate();

    public abstract void setBeginDate(Date beginDate);

    public abstract Integer getBurned();

    public abstract void setBurned(Integer burned);

    // public abstract List<Comment> getComments();

    // public abstract void setComments(List<Comment> comments);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract Date getEndDate();

    public abstract void setEndDate(Date endDate);

    public abstract Integer getEstimated();

    public abstract void setEstimated(Integer estimated);

    public abstract Integer getPriority();

    public abstract void setPriority(Integer priority);

    public abstract ProjectProxy getProject();

    public abstract void setProject(ProjectProxy project);

    public abstract Integer getRemaining();

    public abstract void setRemaining(Integer remaining);

    // public abstract Sprint getSprint();

    // public abstract void setSprint(Sprint sprint);

    public abstract TaskStatus getStatus();

    public abstract void setStatus(TaskStatus status);

    public abstract String getSummary();

    public abstract void setSummary(String summary);

    public abstract UserProxy getUser();

    public abstract void setUser(UserProxy user);

    /** Id para capa de presentacion */
    EntityProxyId<ProjectProxy> stableId();

    /** Fecha ultima modificacion. */
    public abstract Date getUpdated();

    /** Fecha creacion. */
    public abstract Date getCreated();

}
