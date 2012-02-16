package org.inftel.ssa.datamining;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import static org.inftel.ssa.datamining.DataminingDataPeriod.DAYLY;
import org.inftel.ssa.domain.*;

/**
 * Algunas estadisticas podrian generarse en el End Of Day, por ejemplo podrian registarse
 * diariamente el numero de alertas por tipo que se hay activas y que hay inactivas.
 *
 * @author agumpg
 */
@Stateless
@LocalBean
public class DataminingEndOfDay {

  @EJB
  private DataminingProcessorImpl statisticProcessor;

  /**
   * Calcula las estadisticas diarias para algunos valores. Y ademas, genera los historicos de
   * periodos superiores al diario. Por tanto, es importante que la cola de mensajes este vacia para
   * que todos esten procesados previamente. TODO comprobar que la cola esta vacia
   */
  @Schedule(minute = "0", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "2", dayOfWeek = "*")
  public void processDialyStatistics() {

    Calendar yesterday = Calendar.getInstance();
    yesterday.add(Calendar.DAY_OF_MONTH, -1);
    
    Date from = DAYLY.beginsAt(yesterday).getTime();
    Date to = DAYLY.endsAt(yesterday).getTime();

    generateEndOfDayStatistics(from, to);
    statisticProcessor.updatePeriodsForAllStatistics();
  }

  private void generateEndOfDayStatistics(Date from, Date to) {
    // Generar estadisticas diarias de tiempo restante de tareas y número de 
    // tareas pendientes por sprint y usuario
    
    /* Descomentar cuando estén implementados los servicios:
      for (Sprint sprint : SprintFacade.findAll()) {
          for (User user : UserFacade.findAll()) {
              String statName = "task." + sprint.getId() + "." + user.getId() + ".remaining";
              Double statSum = TaskFacade.findHoursRemByUserAndSprint(sprint.getId(), user.getId());
              Long statCount = TaskFacade.findTaskRemByUserAndSprint(sprint.getId(), user.getId());

              DataminingData data = new DataminingData(statName, from, statSum, statCount);
              statisticProcessor.updateDaylyStatistic(data);
          }
      }
    */
      
    // Generar estadisticas diarias sobre el número de tareas que están en un 
    // determinado estado por proyecto.
      
      /* Descomentar cuando estén implementados los servicios:
      for (Project project : ProjectFacade.findAll()) {
          for (TaskStatus status : TaskStatus.values()) {
              Long statCount = TaskFacade.countByProjectAndStatus(project.getId(), status);
              String statName = project.getId() + ".task.status." + status.name().toLowerCase();

              DataminingData data = new DataminingData(statName, from, null, statCount);
              statisticProcessor.updateDaylyStatistic(data);
          }

      }
  */
      
    // Generar estadisticas diarias sobre el número de tareas que están en un 
    // determinado estado por usuario.
      
    /* Descomentar cuando estén implementados los servicios:
      for (User user : UserFacade.findAll()) {
          for (TaskStatus status : TaskStatus.values()) {
              Long statCount = TaskFacade.countByUserAndStatus(user.getId(), status);
              String statName = user.getId() + ".task.status." + status.name().toLowerCase();

              DataminingData data = new DataminingData(statName, from, null, statCount);
              statisticProcessor.updateDaylyStatistic(data);
          }

      }
   */
      
    // Generar estadisticas diarias sobre el número de tareas que están en un 
    // determinado estado por sprint.
      
    /* Descomentar cuando estén implementados los servicios:
      for (Sprint sprint : SprintFacade.findAll()) {
          for (TaskStatus status : TaskStatus.values()) {
              Long statCount = TaskFacade.countBySprintAndStatus(sprint.getId(), status);
              String statName = sprint.getId() + ".task.status." + status.name().toLowerCase();

              DataminingData data = new DataminingData(statName, from, null, statCount);
              statisticProcessor.updateDaylyStatistic(data);
          }

      }
   */
   }
}
