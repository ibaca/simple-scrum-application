/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jsbaes
 */
@Entity
@Table(name = "products")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Product p"),
	@NamedQuery(name = "Producto.findByProduct", query = "SELECT p FROM Product p WHERE p.id = :product"),
	@NamedQuery(name = "Producto.findByDescription", query = "SELECT p FROM Product p WHERE p.description = :description")})
public class Product extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Lob
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
	private Collection<Sprint> sprints;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlTransient
	public Collection<Sprint> getSprints() {
		return sprints;
	}

	public void setSprints(Collection<Sprint> sprints) {
		this.sprints = sprints;
	}
	
}
