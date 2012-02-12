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
    // Generar estadisticas diarias de tipo de alertas recibidas ayer
//        for (AlertType type : AlertType.values()) {
//            Long statCount = alertFacade.countByType(type, from, to);
//            String statName = "alert.type." + type.name().toLowerCase();
//
//            DataminingData data = new DataminingData(statName, from, null, statCount);
//            statisticProcessor.updateDaylyStatistic(data);
//        }
//
//        // Generar estadisticas diarias de tipo de afectados registrados en el sistema
//        for (AffectedType type : AffectedType.values()) {
//            Long statCount = affectedFacade.countByType(type);
//            String statName = "affected.type." + type.name().toLowerCase();
//
//            DataminingData data = new DataminingData(statName, from, null, statCount);
//            statisticProcessor.updateDaylyStatistic(data);
//        }
  }
  
}
