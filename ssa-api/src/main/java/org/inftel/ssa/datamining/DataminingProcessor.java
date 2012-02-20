package org.inftel.ssa.datamining;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;


/**
 * Permite registrar valores estadisticos de forma asincrona.
 * 
 * @author ibaca
 */
@Local
public interface DataminingProcessor {

    /**
     * Envia a procesar una estadistica. Por ejemplo, si se quiere almacenar el tiempo de procesado
     * de los mensajes recibidos para 10 minsajes alavez:
     * <ul>
     * <li>name: 'system.messagesRecived.processTime'
     * <li>date: new Date()
     * <li>accumulated: 300 (si la muestra duro 300milisegundos en procesarse)
     * </ul>
     * 
     * Este metodo registra una única muestra, si se quiere registrar varias muestras simultaneas
     * debe llamarse al metodo
     * {@link DataminingProcessor#queueStatistic(String, Calendar, int, double)}.
     * 
     * @param name
     *            del valor estadistico en nomenclatura similar a paquetes java
     * @param date
     *            fecha asociada al valor estadistico
     * @param accumulated
     *            el valor acumulado que representa la muestra
     */
    public void process(String name, Date date, Double accumulated, Long samples);

    /**
     * Envia a procesar una estadistica tipo acumulado. Por ejemplo, si se quiere almacenar el
     * tiempo de procesado de los mensajes recibidos:
     * <ul>
     * <li>name: 'system.messagesRecived.processTime'
     * <li>date: new Date()
     * <li>accumulated: 300 (si la muestra duro 300milisegundos en procesarse)
     * </ul>
     * 
     * Este metodo registra una única muestra, si se quiere registrar varias muestras simultaneas
     * debe llamarse al metodo
     * {@link DataminingProcessor#queueStatistic(String, Calendar, int, double)}.
     * 
     * @param name
     *            del valor estadistico en nomenclatura similar a paquetes java
     * @param date
     *            fecha asociada al valor estadistico
     * @param accumulated
     *            el valor acumulado que representa la muestra
     */
    public void process(String name, Date date, double accumulated);

    /**
     * Envia a procesar una estadistica tipo contador. Por ejemplo, si se quiere almacenar el numero
     * de mensajes los valores podrian ser:
     * <ul>
     * <li>name: 'system.messagesRecived.count'
     * <li>date: new Date()
     * <li>samples: 5 (si se quieren registrar 5 mensajes recibidos en la fecha <code>date</code>)
     * </ul>
     * 
     * @param name
     *            del valor estadistico en nomenclatura similar a paquetes java
     * @param date
     *            fecha asociada al valor estadistico
     * @param samples
     *            numero de muestras que contienen se quieren registrar
     */
    public void process(String name, Date date, long samples);

    /**
     * Envia a procesar una estadistica tipo contador. Por ejemplo, si se quiere almacenar el numero
     * de mensajes los valores podrian ser:
     * <ul>
     * <li>name: 'system.messagesRecived.count'
     * <li>date: new Date()
     * <li>samples: 5 (si se quieren registrar 5 mensajes recibidos en la fecha <code>date</code>)
     * </ul>
     * 
     * @param name
     *            del valor estadistico en nomenclatura similar a paquetes java
     * @param date
     *            fecha asociada al valor estadistico
     */
    public void process(String name, Date date);

    /**
     * Obtiene valores estadisticos.
     * 
     * @param name
     *            nombre estadistico
     * @param period
     *            tipo de periodo que se quiere buscar
     * @param fromDate
     *            inicio de fecha de las muestras que se quieren obtener
     * @param toDate
     *            fin de fecha de las muestras que se quieren obtener
     * @return mapa donde las claves son los nombre completos estadisticos y los enteros son el
     *         valor para la muestra y periodo concretoF
     */
    public Map<Date, DataminingData> findStatistics(String name, DataminingDataPeriod period, 
			Date fromDate, Date toDate);

    /**
     * Devuelve todos los nombres estadisticos que coincidan con el token pasado.
     * 
     * @param startWith
     *            comienzo del nombre estadistico que se quiere buscar
     * @return lista de los nombres que coinciden
     */
    public List<String> findStatisticsNames(String startWith);
}
