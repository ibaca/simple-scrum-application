package org.inftel.ssa.web.statistics;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.inftel.ssa.datamining.DataminingData;
import org.inftel.ssa.datamining.DataminingDataPeriod;
import org.inftel.ssa.datamining.DataminingProcessor;
import org.inftel.ssa.domain.Sprint;
import org.inftel.ssa.domain.Task;
import org.inftel.ssa.domain.User;
import org.inftel.ssa.web.ProjectManager;
import org.inftel.ssa.web.SprintManager;
import org.inftel.ssa.web.UserManager;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean
@RequestScoped
public class StatisticsBean implements Serializable {

    @EJB
    private DataminingProcessor datamining;
    private CartesianChartModel stressModel;
    private CartesianChartModel taskModel;
    private CartesianChartModel individualModel;
    private PieChartModel pieTaskModel;
    @ManagedProperty(value = "#{projectManager}")
    private ProjectManager projectManager;
    @ManagedProperty(value = "#{sprintManager}")
    private SprintManager sprintManager;
    @ManagedProperty(value = "#{userManager}")
    private UserManager userManager;

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public StatisticsBean() {
    }
    
  @PostConstruct
    private void init(){
        createStressModel();
        createTaskModel();
        createPieTaskModel();
        //createIndividualModel();
    }

    public CartesianChartModel getStressModel() {
        return stressModel;
    }

    public CartesianChartModel getIndividualModel() {
        return individualModel;
    }

    public CartesianChartModel getTaskModel() {
        return taskModel;
    }

    public PieChartModel getPieTaskModel() {
        return pieTaskModel;
    }
    
    public SprintManager getSprintManager() {
        return sprintManager;
    }

    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    private void createStressModel() {
        stressModel = new CartesianChartModel();
        LineChartSeries stressSeries = new LineChartSeries();
        Map<Date, DataminingData> samples; // todos los datos por fecha
        User currentUser = userManager.getCurrentUser();
        Sprint currentSprint = sprintManager.getCurrentSprint();
        Long idUser = currentUser.getId();
        Long idSprint = currentSprint.getId();
        String nickname = currentUser.getNickname();
        stressSeries.setLabel(nickname); 

        String name = "task.by-project."+idUser+"."+idSprint+".remaining"; 
        samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));

        for (Date date : samples.keySet()) {
            stressSeries.set(df.format(date), samples.get(date).getDataSum());
        }

        stressModel.addSeries(stressSeries);

    }

    private void createTaskModel() {

        taskModel = new CartesianChartModel();
        LineChartSeries taskSeries = new LineChartSeries();
        Map<Date, DataminingData> samples; // todos los datos por fecha
        User currentUser = userManager.getCurrentUser();
        Sprint currentSprint = sprintManager.getCurrentSprint();
        Long idSprint = currentSprint.getId();
        Long idUser = currentUser.getId();
        String nickname = currentUser.getNickname();
        taskSeries.setLabel(nickname); 

        String name = "task.by-project."+idUser+"."+idSprint+".remaining"; 
        samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));

        for (Date date : samples.keySet()) {
            taskSeries.set(df.format(date), samples.get(date).getDataCount());
        }

        stressModel.addSeries(taskSeries);

    }

    private void createPieTaskModel() {
        pieTaskModel = new PieChartModel();
        int todocount = 0, doingcount = 0, donecount = 0;
        List<Task> tasks = getProjectManager().getCurrentProject().getTasks();
        for (Task task : tasks) {
            switch (task.getStatus()) {
                case TODO:
                    todocount++;
                    break;
                case DOING:
                    doingcount++;
                    break;
                case DONE:
                    donecount++;
                    break;
            }
        }

        pieTaskModel.set("To Do", todocount);
        pieTaskModel.set("Doing", doingcount);
        pieTaskModel.set("Done", donecount);
    }

    private void createIndividualModel() {

        individualModel = new CartesianChartModel();
        LineChartSeries series = new LineChartSeries();
        Set<User> users = getProjectManager().getCurrentProject().getUsers();
        Sprint currentSprint = sprintManager.getCurrentSprint();
        long idSprint = currentSprint.getId();
        for (User user : users) {
            series.setLabel(user.getNickname());
            Long idUser = user.getId();

            Map<Date, DataminingData> samples; // todos los datos por fecha
            String name = "task." + idUser + "." + idSprint + ".remaining";
            samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));

            for (Date date : samples.keySet()) {
                series.set(df.format(date), samples.get(date).getDataCount());
            } 

            individualModel.addSeries(series);

        }
    }

    
}
