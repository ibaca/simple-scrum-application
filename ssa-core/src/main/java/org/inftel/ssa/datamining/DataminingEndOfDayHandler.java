package org.inftel.ssa.datamining;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    @PersistenceContext(unitName = "ssa-persistence-unit")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Generar estadisticas diarias de tiempo restante de tareas y número de
     * tareas pendientes por sprint y usuario
     *
     * @param eodEvent
     */
    public void sprintStatistics(@Observes DataminingEndOfDayEvent eodEvent) {
        Date from = eodEvent.getFromDate();
        Date to = eodEvent.getToDate();

        for (Sprint sprint : sprintFacade.findAll()) {

            for (User user : userFacade.findAll()) {
                String statName = "task.by-sprint." + sprint.getId() + ".by-user." + user.getId() + ".remaining";
                Double statSum = findHoursRemByUserAndSprint(sprint.getId(), user.getId());
                Long statCount = findTaskRemByUserAndSprint(sprint.getId(), user.getId());
                eodEvent.updateData(statName, statSum, statCount);
            }

            for (TaskStatus status : TaskStatus.values()) {
                Long statCount = countBySprintAndStatus(sprint.getId(), status);
                String statName = "task.by-sprint." + sprint.getId() + ".status." + status.name().toLowerCase();
                eodEvent.updateData(statName, null, statCount);
            }
        }
    }

    /**
     * Generar estadisticas diarias sobre el número de tareas que están en un
     * determinado estado por proyecto.
     *
     * @param eodEvent
     */
    public void projectStatistics(@Observes DataminingEndOfDayEvent eodEvent) {
        Date from = eodEvent.getFromDate();
        Date to = eodEvent.getToDate();

        for (Project project : projectFacade.findAll()) {
            for (TaskStatus status : TaskStatus.values()) {
                Long statCount = countByProjectAndStatus(project.getId(), status);
                String statName = "task.by-project." + project.getId() + ".status." + status.name().toLowerCase();
                eodEvent.updateData(statName, null, statCount);
            }
        }
    }

    /**
     * Generar estadisticas diarias sobre el número de tareas que están en un
     * determinado estado por usuario.
     *
     * @param eodEvent
     */
    public void userStatistics(@Observes DataminingEndOfDayEvent eodEvent) {
        Date from = eodEvent.getFromDate();
        Date to = eodEvent.getToDate();

        for (User user : userFacade.findAll()) {
            for (TaskStatus status : TaskStatus.values()) {
                Long statCount = countByUserAndStatus(user.getId(), status);
                String statName = "task.by-user." + user.getId() + ".status." + status.name().toLowerCase();
                eodEvent.updateData(statName, null, statCount);
            }
        }
    }

    /**
     * Consulta que devuelve las horas pendientes por completar de las tareas
     * por sprint y usuario.
     *
     * @param id_sprint Id del sprint
     * @param id_user Id del usuario
     */
    public Double findHoursRemByUserAndSprint(Long id_sprint, Long id_user) {

        return (Double) em.createQuery("SELECT SUM(t.toFinish) FROM Task t WHERE t.sprint.id = :idsprint AND t.user.id = :iduser AND t.toFinish > 0")
                 .setParameter("idsprint", id_sprint)
                 .setParameter("iduser", id_user)
                 .getSingleResult();
    }

   /**
     * Consulta que devuelve el número de tareas por completar
     * por sprint y usuario.
     *
     * @param id_sprint Id del sprint
     * @param id_user Id del usuario
     */
    public Long findTaskRemByUserAndSprint(Long id_sprint, Long id_user) {

        return (Long) em.createQuery("SELECT count(t) FROM Task t WHERE t.sprint.id = :idsprint AND t.user.id = :iduser AND t.toFinish > 0")
                .setParameter("idsprint", id_sprint)
                .setParameter("iduser", id_user)
                .getSingleResult();
    }

    /**
     * Consulta que devuelve el número de tareas con un estado determinado
     * por proyecto.
     *
     * @param id_project Id del proyecto
     * @param id_status Estado de la tarea
     */
    public Long countByProjectAndStatus(Long id_project, TaskStatus id_status) {

        return (Long) em.createQuery("SELECT count(t) FROM Task t WHERE t.project.id = :idproject AND t.status = :idstatus")
                .setParameter("idproject", id_project)
                .setParameter("idstatus", id_status)
                .getSingleResult();
    }

    /**
     * Consulta que devuelve el número de tareas con un estado determinado
     * por usuario.
     *
     * @param id_user Id del usuario
     * @param id_status Estado de la tarea
     */
    public Long countByUserAndStatus(Long id_user, TaskStatus id_status) {

        return (Long) em.createQuery("SELECT count(t) FROM Task t WHERE t.user.id = :iduser AND t.status = :idstatus")
                .setParameter("iduser", id_user)
                .setParameter("idstatus", id_status)
                .getSingleResult();
    }

    /**
     * Consulta que devuelve el número de tareas con un estado determinado
     * por sprint.
     *
     * @param id_sprint Id del sprint
     * @param id_status Estado de la tarea
     */
    public Long countBySprintAndStatus(Long id_sprint, TaskStatus id_status) {

        return (Long) em.createQuery("SELECT count(t) FROM Task t WHERE t.sprint.id = :idsprint AND t.status = :idstatus")
                .setParameter("idsprint", id_sprint)
                .setParameter("idstatus", id_status)
                .getSingleResult();
    }
}