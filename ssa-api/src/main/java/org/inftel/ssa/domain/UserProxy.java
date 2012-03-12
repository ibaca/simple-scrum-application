
package org.inftel.ssa.domain;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName(value = "org.inftel.ssa.domain.User", locator = "org.inftel.ssa.mobile.locators.EntityLocator")
public interface UserProxy extends EntityProxy {

    public abstract String getCompany();

    public abstract void setCompany(String company);

    // public abstract Set<Project> getProjects();

    // public abstract void setProjects(Set<Project> projects);

    public abstract String getNickname();

    public abstract void setNickname(String nickname);

    public abstract String getFullName();

    public abstract void setFullName(String fullname);

    public abstract String getEmail();

    public abstract void setEmail(String email);

    public abstract String getPassword();

    public abstract void setPassword(String password);

    // public abstract List<Task> getTasks();

    // public abstract void setTasks(List<Task> tasks);

    public abstract String getUserRole();

    public abstract void setUserRole(String role);

}
