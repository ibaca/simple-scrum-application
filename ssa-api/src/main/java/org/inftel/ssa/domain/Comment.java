package org.inftel.ssa.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidad Comment: Conjunto de comentarios asociados a las tareas, que permiten
 * además llevar un histórico del desarrollo de las mismas.
 *
 * @author jsbaes
 */
@Entity
@Table(name = "comment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comment.findAll", query = "SELECT c FROM Comment c"),
    @NamedQuery(name = "Comment.findByIdcomment", query = "SELECT c FROM Comment c WHERE c.id = :idcomment"),
    @NamedQuery(name = "Comment.findByDescription", query = "SELECT c FROM Comment c WHERE c.description = :description")})
public class Comment extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String description;
    //@JoinColumn(name = "IDTASK", referencedColumnName = "IDTASK")
    @ManyToOne
    private Task idtask;

    /**
     * Descripción del comentario
     *
     * @return texto del comentario
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece el texto de un comentario
     *
     * @param descripcion Texto del comentario
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Tarea a la que se asocia un comentario
     *
     * @return tarea asociada
     */
    public Task getIdtask() {
        return idtask;
    }

    /**
     * Asocia una tarea al comentario
     *
     * @param idtask Tarea asociada al comentario
     */
    public void setIdsprint(Task idtask) {
        this.idtask = idtask;
    }
}
