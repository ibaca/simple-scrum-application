package org.inftel.ssa.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsbaes
 */
@Entity
@Table(name = "sprints")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Sprint.findAll", query = "SELECT s FROM Sprint s"),
	@NamedQuery(name = "Sprint.findById", query = "SELECT s FROM Sprint s WHERE s.id = :id")})
public class Sprint extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String summary;
	private String description;
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	@OneToMany(mappedBy = "sprint")
	private List<Task> tasks;
	@ManyToOne
	private Project project;
	@ManyToOne
	private Product product;

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<Task> getTasks() {
		return (tasks == null) ? tasks = new ArrayList<Task>(0) : tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
