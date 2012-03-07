
package org.inftel.ssa.datamining;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static org.inftel.ssa.datamining.DataminingDataPeriod.ANNUAL;
import static org.inftel.ssa.datamining.DataminingDataPeriod.DAYLY;
import static org.inftel.ssa.datamining.DataminingDataPeriod.MONTHLY;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Debe sacar la informacion necesaria a traves de los metodos expuestos en el
 * interfaz remoto, y poner en la cola de estadisticas los valores concretos que
 * se quieran agregar. La decisión de extraer las estadisticas de la entidad
 * concreta (alert, interventio, etc) podria hacerse cuando se lee de la cola.
 * 
 * @author agumpg
 */
@Stateless(name = "DataminingProcessor")
@LocalBean
public class DataminingProcessorImpl implements DataminingProcessor {

    private final static Logger logger = getLogger(DataminingProcessorImpl.class.getName());

    @EJB
    private DataminingDataFacade dataminingDataFacade;

    @EJB
    private DataminingListener dataminingListener;

    @Override
    public void process(String name, Date date) {
        process(name, date, 1);
    }

    @Override
    public void process(String name, Date date, long samples) {
        process(name, date, null, samples);
    }

    @Override
    public void process(String name, Date date, double accumulated) {
        process(name, date, accumulated, 1l);
    }

    @Override
    public void process(String name, Date date, Double accumulated, Long samples) {
        dataminingListener.onMessage(new DataminingDataEntity(name, date, accumulated, samples));
    }

    /**
     * Actualización de los diarios, mensuales y anuales en la tabla estadística
     * 
     * @param name nombre de la etiqueta de alerta a procesar
     * @param date fecha de procesamiento
     * @param value valor del acumulado
     */
    public void updatePeriodsForAllStatistics() {
        // String name, Calendar date, Long value
        // Se calcula el dia de hoy para comparar
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(DAY_OF_MONTH, -1);

        for (String name : dataminingDataFacade.findStatisticsNames("")) {
            // Si ayer fue un mes diferente
            if (today.get(MONTH) != yesterday.get(MONTH)) {
                logger.log(INFO, "actualizando perido mensual para estadistica {0}",
                        new Object[] {
                            name
                        });
                Date from = ANNUAL.beginsAt(yesterday).getTime();
                Date to = ANNUAL.endsAt(yesterday).getTime();
                Map<String, BigDecimal> x = dataminingDataFacade
                        .sumStatictics(name, DAYLY, to, from);
                Long count = x.get("count").longValue();
                Double sum = x.get("sum").doubleValue();
                saveStatisticData(name, MONTHLY, yesterday, count, sum);
            }

            // Si se ha producido un cambio de año actualizamos históricos
            // anuales
            if (today.get(YEAR) != yesterday.get(YEAR)) {
                logger.log(INFO, "actualizando perido anual para estadistica {0}",
                        new Object[] {
                            name
                        });
                Date from = ANNUAL.beginsAt(yesterday).getTime();
                Date to = ANNUAL.endsAt(yesterday).getTime();
                Map<String, BigDecimal> x = dataminingDataFacade.sumStatictics(name, MONTHLY, to,
                        from);
                Long count = x.get("count").longValue();
                Double sum = x.get("sum").doubleValue();
                saveStatisticData(name, ANNUAL, yesterday, count, sum);
            }
        }
    }

    /**
     * Se encarga de actualizar los valores estadisticos para el periodo minimo.
     * Actualmente el periodo minimo es diario. Los periodos superiores, como
     * mensual o anual, se van actualizando cuando de forma independiente.
     */
    public void updateDaylyStatistic(DataminingDataEntity dataOrigin) {
        // Actualmente el nivel minimo de periodo es DAILY
        Date processDate = DAYLY.beginsAt(dataOrigin.getPeriodDate());
        DataminingDataEntity data = dataminingDataFacade.findByDate(dataOrigin.getName(),
                processDate);

        // ¿Hay datos de hoy?
        if (data == null) {
            // No existe ninguna entrada
            data = new DataminingDataEntity();
            data.setName(dataOrigin.getName());
            data.setPeriodType(DAYLY);
            data.setPeriodDate(processDate);
            data.setDataCount(dataOrigin.getDataCount());
            data.setDataSum(dataOrigin.getDataSum());
            dataminingDataFacade.create(data);
        } else {
            // Ya existe una entrada en DataminingDataEntity y lo actualizamos
            data.setPeriodType(DAYLY); // FIXME no deberia ser necesario
            data.setDataCount(data.getDataCount() + dataOrigin.getDataCount());
            data.setDataSum(data.getDataSum() + dataOrigin.getDataSum());
            // TODO esto no debe fallar, pero si un tipo de estadistica pasa de
            // ser tipo
            // sum a tipo count podria dar un nullpointer porque getDataSum de
            // null y viceversa
        }
    }

    /**
     * Almacena en la tabla de estadísticas una estadística de acumulado
     * 
     * @param name nombre asociado a la etiqueta de la alerta
     * @param period DIARY, MONTHLY, ANNUAL
     * @param date fecha de la alerta
     * @param count valor asociado a la alerta
     */
    private void saveStatisticData(String name, DataminingDataPeriod period, Calendar date,
            Long count, Double sum) {
        DataminingDataEntity data = new DataminingDataEntity();
        data.setName(name);
        data.setPeriodType(period);
        data.setPeriodDate(period.beginsAt(date).getTime());
        data.setDataCount(count);
        data.setDataSum(sum);
        dataminingDataFacade.create(data);
    }

    @Override
    public Map<Date, DataminingData> findStatistics(String name, DataminingDataPeriod period,
            Date fromDate,
            Date toDate) {
        logger.log(INFO, "consultando estadisticas {0} para periodo {1} y fechas entre {2} y {3}",
                new Object[] {
                        name, period, fromDate, toDate
                });
        // Delegate to DataminingDataFacade
        return dataminingDataFacade.findStatistics(name, period, fromDate, toDate);
    }

    @Override
    public List<String> findStatisticsNames(String startWith) {
        return dataminingDataFacade.findStatisticsNames(startWith);
    }
}
