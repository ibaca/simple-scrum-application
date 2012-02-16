package org.inftel.ssa.datamining;

import java.util.*;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
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
	@Inject
	private Event<DataminingEndOfDayEvent> eodEventTrigger;

	/**
	 * Calcula las estadisticas diarias para algunos valores. Y ademas, genera los historicos de
	 * periodos superiores al diario. Por tanto, es importante que la cola de mensajes este vacia
	 * para que todos esten procesados previamente. TODO comprobar que la cola esta vacia
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

	private void updateDaily(String statName, Date from, Double statSum, Long statCount) {
		DataminingData data = new DataminingData(statName, from, statSum, statCount);
		statisticProcessor.updateDaylyStatistic(data);
	}

	private void generateEndOfDayStatistics(Date from, Date to) {
		DataminingEndOfDayEvent eodEvent = new DataminingEndOfDayEvent();
		eodEvent.setUpdateDataHelper(new DataminingEndOfDayEvent.UpdateDataHelper() {

			@Override
			public void updateDaily(String statName, Date from, Double statSum, Long statCount) {
				DataminingEndOfDay.this.updateDaily(statName, from, statSum, statCount);
			}
		});
		eodEvent.setFromDate(from);
		eodEvent.setToDate(to);
		eodEventTrigger.fire(eodEvent);
	}
}
