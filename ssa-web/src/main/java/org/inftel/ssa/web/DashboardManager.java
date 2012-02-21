/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web;

import java.util.List;
import java.util.logging.Logger;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import org.inftel.ssa.domain.Task;
import org.inftel.ssa.domain.TaskStatus;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.panel.Panel;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

@ManagedBean
@SessionScoped
public class DashboardManager {

    private final static Logger logger = Logger.getLogger(TaskManager.class.getName());
    @ManagedProperty(value = "#{sprintManager}")
    private SprintManager sprintManager;
    @ManagedProperty(value = "#{projectManager}")
    private ProjectManager projectManager;
    private int columnCount;
    private Dashboard dashboard;
    private DashboardModel dashboardModel;

    public DashboardManager() {
    }

    public void init() {
        columnCount = 3;
        FacesContext fc = FacesContext.getCurrentInstance();
        Application application = fc.getApplication();

        dashboard = (Dashboard) application.createComponent(fc, "org.primefaces.component.Dashboard", "org.primefaces.component.DashboardRenderer");
        dashboard.setId("dashboard");

        dashboardModel = new DefaultDashboardModel();

        DashboardColumn toDo = new DefaultDashboardColumn();
        DashboardColumn inProgress = new DefaultDashboardColumn();
        DashboardColumn done = new DefaultDashboardColumn();


        dashboardModel.addColumn(toDo);
        dashboardModel.addColumn(inProgress);
        dashboardModel.addColumn(done);

        dashboard.setModel(dashboardModel);

        List<Task> tasks = projectManager.getCurrentProject().getTasks();
        //Estas tareas tendrian que estar filtradas por el currenteSprint
        for (Task task : tasks) {
            Panel panel = (Panel) application.createComponent(fc, "org.primefaces.component.Panel", "org.primefaces.component.PanelRenderer");
            //Al establecer el id me daba error sino ponia al menos una cadena de texto. Raro, la verdad
            panel.setId("id_" + task.getId().toString());
            panel.setHeader("Priority: " + task.getPriority().toString());
            dashboard.getChildren().add(panel);
            DashboardColumn column = dashboardModel.getColumn(task.getStatus().ordinal());
            column.addWidget(panel.getId());
            HtmlOutputText text = new HtmlOutputText();
            text.setValue(task.getDescription());
            panel.getChildren().add(text);

        }
    }

    public Dashboard getDashboard() {
        init();
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    private int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public SprintManager getSprintManager() {
        return sprintManager;
    }

    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }

    public void handleReorder(DashboardReorderEvent event) {
        String widgetId = event.getWidgetId();
        int columnIndex = event.getColumnIndex();
        //TODO actualizar las tareas
        logger.info("Widget movido: "+ widgetId);
        logger.info("Columna destino: " + columnIndex);
        
    }
}
