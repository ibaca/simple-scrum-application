/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.domain;

import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jsbaes
 */
@Entity
@Table(name = "producto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Producto.findByIdproduct", query = "SELECT p FROM Product p WHERE p.id = :idproduct"),
    @NamedQuery(name = "Producto.findByDescripcion", query = "SELECT p FROM Product p WHERE p.descripcion = :descripcion")})
public class Product extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idproduct")
    private Collection<Sprint> sprintCollection;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Sprint> getSprintCollection() {
        return sprintCollection;
    }

    public void setSprintCollection(Collection<Sprint> sprintCollection) {
        this.sprintCollection = sprintCollection;
    }
}
