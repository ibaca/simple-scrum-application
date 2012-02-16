package org.inftel.ssa.datamining;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import org.inftel.ssa.domain.*;

@LocalBean
@Stateless
public class DataminingEndOfDayHandler {

	@EJB
	private SprintFacade sprintFacade;
	@EJB
	private UserFacade userFacade;
	@EJB
	private TaskFacade taskFacade;
	@EJB
	private ProjectFacade projectFacade;

	/**
	 * Generar estadisticas diarias de tiempo restante de tareas y número de tareas pendientes por
	 * sprint y usuario
	 *
	 * @param eodEvent
	 */
	public void sprintStatistics(@Observes DataminingEndOfDayEvent eodEvent) {
		Date from = eodEvent.getFromDate();
		Date to = eodEvent.getToDate();

		for (Sprint sprint : sprintFacade.findAll()) {

			for (User user : userFacade.findAll()) {
				String statName = "task.by-sprint." + sprint.getId() + ".by-user." + user.getId() + ".remaining";
				Double statSum = null; //taskFacade.findHoursRemByUserAndSprint(sprint.getId(), user.getId());
				Long statCount = null; //taskFacade.findTaskRemByUserAndSprint(sprint.getId(), user.getId());
				eodEvent.updateData(statName, statSum, statCount);
			}

			for (TaskStatus status : TaskStatus.values()) {
				Long statCount = null; // taskFacade.countBySprintAndStatus(sprint.getId(), status);
				String statName = "task.by-sprint." + sprint.getId() + ".status." + status.name().toLowerCase();
				eodEvent.updateData(statName, null, statCount);
			}
		}
	}

	/**
	 * Generar estadisticas diarias sobre el número de tareas que están en un determinado estado por
	 * proyecto.
	 *
	 * @param eodEvent
	 */
	public void projectStatistics(@Observes DataminingEndOfDayEvent eodEvent) {
		Date from = eodEvent.getFromDate();
		Date to = eodEvent.getToDate();

		for (Project project : projectFacade.findAll()) {
			for (TaskStatus status : TaskStatus.values()) {
				Long statCount = null; //taskFacade.countByProjectAndStatus(project.getId(), status);
				String statName = "task.by-project." + project.getId() + ".status." + status.name().toLowerCase();
				eodEvent.updateData(statName, null, statCount);
			}
		}
	}

	/**
	 * Generar estadisticas diarias sobre el número de tareas que están en un determinado estado por
	 * usuario.
	 *
	 * @param eodEvent
	 */
	public void userStatistics(@Observes DataminingEndOfDayEvent eodEvent) {
		Date from = eodEvent.getFromDate();
		Date to = eodEvent.getToDate();

		for (User user : userFacade.findAll()) {
			for (TaskStatus status : TaskStatus.values()) {
				Long statCount = null; //taskFacade.countByUserAndStatus(user.getId(), status);
				String statName = "task.by-user." + user.getId() + ".status." + status.name().toLowerCase();
				eodEvent.updateData(statName, null, statCount);
			}
		}
	}
}
