package org.inftel.ssa.datamining;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Se encarga de procesar la cola de estadistiacs e ir acutalizando la tabla de
 * estadistica. El pool de beans esta configurado para tener un maximo de 1, por
 * lo tanto no hay problema de actualizar las filas de la tabla. Ademas, el
 * MessageDriven es no transaccional, y se recomienda usar acceso a las tablas
 * no transaccional.
 */
@MessageDriven(mappedName = "jms/datamining", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class DataminingListener implements MessageListener {

    private static final Logger logger = getLogger(DataminingListener.class.getName());
    
    @EJB
    private DataminingProcessorImpl statisticProcessor;

    public DataminingListener() {
    }

    /** Delega el procesado de los mensajes a DataminingProcessorImpl. */
    @Override
    public void onMessage(Message message) {
        try {
            Object content = ((ObjectMessage) message).getObject();
            if (content instanceof DataminingData) {
                logger.log(FINE, "mensaje recibido: " + content);
                statisticProcessor.updateDaylyStatistic((DataminingData) content);
            } else {
                logger.log(WARNING, "mensaje recibido de tipo desconocido: " + content);
            }
        } catch (JMSException ex) {
            Logger.getLogger(DataminingListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    public void flush() {
        // Se podrian acumular las estadisticas en queues en memoria, de forma q se acumulasen por tipo
        // y solo cada cierto tiempo se volcasen a la base de datos, en esta situacion de tener mensajes
        // en memoria, deberia asegurarse en este metodo PreDestroy que las colas de memoria estan vacia
        // y en caso contrario escribirlas en la tabla.
    }
}
