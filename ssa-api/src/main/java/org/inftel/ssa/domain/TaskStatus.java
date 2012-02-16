package org.inftel.ssa.domain;

/**
 * Posible estados de la tarea en función de su grado de avance.
 *
 * FIXME FIXME no puede cambiarse el orden del enumerado
 * http://duydo.com/effective-jpa-persist-an-enumerationeffectively/
 * 
 * @author jsba
 */
public enum TaskStatus {

  /**
   * Tarea por comenzar
   */
  TODO,
  /**
   * Tarea que se está ejecutando.
   */
  DOING,
  /**
   * Tarea ya terminada.
   */
  DONE
}