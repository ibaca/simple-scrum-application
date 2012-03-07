
package org.inftel.ssa.datamining;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.logging.Level.FINE;
import static java.util.logging.Logger.getLogger;
import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import static javax.ejb.LockType.WRITE;

import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.Singleton;

/**
 * Se encarga de procesar la cola de estadistiacs e ir acutalizando la tabla de
 * estadistica. El pool de beans esta configurado para tener un maximo de 1, por
 * lo tanto no hay problema de actualizar las filas de la tabla. Ademas, el
 * MessageDriven es no transaccional, y se recomienda usar acceso a las tablas
 * no transaccional.
 */
@Singleton
@ConcurrencyManagement(CONTAINER)
public class DataminingListener {

    private static final Logger logger = getLogger(DataminingListener.class.getName());

    @EJB
    private DataminingProcessorImpl statisticProcessor;

    /** Delega el procesado de los mensajes a DataminingProcessorImpl. */
    @Lock(WRITE)
    @AccessTimeout(value = 5, unit = MINUTES)
    public void onMessage(DataminingDataEntity content) {
        logger.log(FINE, "mensaje recibido: " + content);
        statisticProcessor.updateDaylyStatistic(content);
    }

    @PreDestroy
    public void flush() {
        /*
         * Se podrian acumular las estadisticas en queues en memoria, de forma q
         * se acumulasen por tipo y solo cada cierto tiempo se volcasen a la
         * base de datos, en esta situacion de tener mensajes en memoria,
         * deberia asegurarse en este metodo PreDestroy que las colas de memoria
         * estan vacia y en caso contrario escribirlas en la tabla.
         */
    }
}
