package org.inftel.ssa.domain;

import static javax.persistence.GenerationType.TABLE;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.Version;

/**
 * Entidad base para las entidades persistentes. Permite unificar el comportamiento y facilita el
 * desarrollo de los servicios DAO.<br>
 * 
 * @author ibaca
 * 
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    static final Logger log = Logger.getLogger(BaseEntity.class.getName());
    @Temporal(TIMESTAMP)
    private Date created;
    @Id
    @TableGenerator(name = "base_entity_generator", initialValue = 10000)
    @GeneratedValue(strategy = TABLE, generator = "base_entity_generator")
    private Long id;
    @Temporal(TIMESTAMP)
    private Date updated;
    @Version
    private Long version;

    public Date getCreated() {
        return created;
    }

    public Long getId() {
        return id;
    }

    public Date getUpdated() {
        return updated;
    }

    public Long getVersion() {
        return version;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
	
	public boolean isNew() {
        return (this.id == null);
    }

    @PrePersist
    void onCreate() {
        Date current = new Date();
        setCreated(current);
        setUpdated(current);
    }

    @PostPersist
    void onCreateLog() {
        // post para que se vea el identificador
        log.log(Level.INFO, "post persist {0}", toString());
    }

    @PreUpdate
    void onUpdate() {
        log.log(Level.INFO, "pre update {0}", toString());
        Date current = new Date();
        setUpdated(current);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(getClass().isAssignableFrom(object.getClass()))) {
            return false;
        }
        BaseEntity other = (BaseEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName().toLowerCase() + " [id=" + getId() + ", version="
                + getVersion() + "]";
    }
}
