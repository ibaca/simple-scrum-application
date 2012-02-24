package org.inftel.ssa.web.chart;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;
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
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean
@RequestScoped
public class StatisticsBean implements Serializable {

	@EJB
	private DataminingProcessor datamining;
	private CartesianChartModel sumRemainingTasksByUserForCurrentProjectModel;
	private CartesianChartModel taskModel;
	private CartesianChartModel countRemainingTasksByUserByProjectModel;
	private CartesianChartModel countRemainingTasksForCurrentProjectModel;
	private PieChartModel pieTaskModel;
	private CartesianChartModel storyPointBarModel;
	private int maxValueBarModel;
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

	/**
	 * Esfuerzo pendiente por usuario para el proyecto actual.
	 */
	private void createCountRemainingTasksForCurrentProjectModel() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));

		countRemainingTasksForCurrentProjectModel = new CartesianChartModel();
		Long projectId = getProjectManager().getCurrentProject().getId();
		String name = "task.by-project." + projectId + ".remaining";
		Map<Date, DataminingData> samples = datamining.findStatistics(name,
				DataminingDataPeriod.DAYLY, new Date(0), new Date());
		LineChartSeries series = new LineChartSeries("tareas pendientes");
		for (Date date : samples.keySet()) {
			series.set(df.format(date), samples.get(date).getDataCount());
		}
		countRemainingTasksForCurrentProjectModel.addSeries(series);
	}

	/**
	 * Esfuerzo pendiente por usuario para el proyecto actual.
	 */
	private void createSumRemainingTasksByUserForCurrentProjectModel() {
		sumRemainingTasksByUserForCurrentProjectModel = new CartesianChartModel();
		LineChartSeries stressSeries = new LineChartSeries();
		Map<Date, DataminingData> samples; // todos los datos por fecha
		Long idProject = getProjectManager().getCurrentProject().getId();
		User currentUser = userManager.getCurrentUser();
		//Sprint currentSprint = sprintManager.getCurrentSprint();
		Long idUser = currentUser.getId();
		//Long idSprint = currentSprint.getId();
		String nickname = currentUser.getNickname();
		stressSeries.setLabel(nickname);
		String name = "task.by-project." + idProject + ".by-user." + idUser + ".remaining";
		samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));
		for (Date date : samples.keySet()) {
			stressSeries.set(df.format(date), samples.get(date).getDataSum());
		}
		sumRemainingTasksByUserForCurrentProjectModel.addSeries(stressSeries);
	}

	private void createTaskModel() {
		taskModel = new CartesianChartModel();
		ChartSeries taskSeries = new ChartSeries();
		Map<Date, DataminingData> samples; // todos los datos por fecha
		Long idProject = getProjectManager().getCurrentProject().getId();
		User currentUser = userManager.getCurrentUser();
		//Sprint currentSprint = sprintManager.getCurrentSprint();
		Long idUser = currentUser.getId();
		//Long idSprint = currentSprint.getId();
		String nickname = currentUser.getNickname();
		taskSeries.setLabel(nickname);
		String name = "task.by-project." + idProject + ".by-user." + idUser + ".remaining";
		samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));
		for (Date date : samples.keySet()) {
			taskSeries.set(df.format(date), samples.get(date).getDataCount());
		}

		taskModel.addSeries(taskSeries);

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

		pieTaskModel.set("ToDo", todocount);
		pieTaskModel.set("Doing", doingcount);
		pieTaskModel.set("Done", donecount);
	}

	/**
	 * Numero de tareas(TODO o DOING) por usuario y por projecto
	 */
	private void createCountRemainingTasksByUserByProjectModel() {
		countRemainingTasksByUserByProjectModel = new CartesianChartModel();
		Map<Date, DataminingData> samples; // todos los datos por fecha
		Set<User> users = getProjectManager().getCurrentProject().getUsers();
		//Sprint currentSprint = sprintManager.getCurrentSprint();
		Long idProject = getProjectManager().getCurrentProject().getId();
		//long idSprint = currentSprint.getId();
		for (User user : users) {
			LineChartSeries series = new LineChartSeries();
			series.setLabel(user.getNickname());
			Long idUser = user.getId();
			String name = "task.by-project." + idProject + ".by-user." + idUser + ".remaining";
			samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));

			for (Date date : samples.keySet()) {
				series.set(df.format(date), samples.get(date).getDataCount());
			}

			countRemainingTasksByUserByProjectModel.addSeries(series);

		}
	}

	private void createStoryPointsModel() {
		List<Sprint> sprints = projectManager.getCurrentProject().getSprints();
		storyPointBarModel = new CartesianChartModel();
		ChartSeries chartSeries = new ChartSeries();
		chartSeries.setLabel("Story Point");
		maxValueBarModel = 0;
		for (Sprint sprint : sprints) {
			List<Task> tasks = sprint.getTasks();
			int storyPoint = 0;
			for (Task task : tasks) {
                                if(task.getEstimated()!=null){
                                    storyPoint += task.getEstimated();
                                }
			}
			if (storyPoint > maxValueBarModel) {
				maxValueBarModel = storyPoint;
			}
			chartSeries.set(sprint.getSummary(), storyPoint);
		}
		chartSeries.set("", 0);//Barra vacia
		storyPointBarModel.addSeries(chartSeries);
	}

	// --------------------------------------------------------------------------- Getters & Setters
	public CartesianChartModel getCountRemainingTasksForCurrentProjectModel() {
		createCountRemainingTasksForCurrentProjectModel();
		return countRemainingTasksForCurrentProjectModel;
	}

	/**
	 * Esfuerzo pendiente por usuario para el proyecto actual.
	 */
	public CartesianChartModel getSumRemainingTasksByUserForCurrentProject() {
		createSumRemainingTasksByUserForCurrentProjectModel();
		return sumRemainingTasksByUserForCurrentProjectModel;
	}

	/**
	 * Numero de tareas(TODO o DOING) por usuario y por projecto
	 */
	public CartesianChartModel getCountRemainingTasksByUserForCurrentProject() {
		createCountRemainingTasksByUserByProjectModel();
		return countRemainingTasksByUserByProjectModel;
	}

	public CartesianChartModel getTaskModel() {
		createTaskModel();
		return taskModel;
	}

	public PieChartModel getPieTaskModel() {
		createPieTaskModel();
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

	public CartesianChartModel getStoryPointBarModel() {
		return storyPointBarModel;
	}

	public void setStoryPointBarModel(CartesianChartModel storyPointBarModel) {
		this.storyPointBarModel = storyPointBarModel;
	}

	public int getMaxValueBarModel() {
                createStoryPointsModel();
		return maxValueBarModel;
	}

	public void setMaxValueBarModel(int maxValueBarModel) {
		this.maxValueBarModel = maxValueBarModel;
	}
}
