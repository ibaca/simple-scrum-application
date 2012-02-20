package org.inftel.ssa.datamining;

import static java.util.logging.Level.INFO;
import static javax.persistence.GenerationType.TABLE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.*;

/**
 * Los valores estadisticos quedan definidos por dos atributos, sum y count. Sum indica el sumatorio
 * de los valores en el intervalo que representa la instancia. Count representa el numero de
 * muestras usadas en el intervalo.
 *
 * Existen valores que solo usaran uno de los atributos, por ejemplo, si se guarda una estadistica
 * de mensajes procesados se registrara únicamente el count, que represnta el numero de menjsajes,
 * dejando sum igual a null.
 *
 * Se ha creado un metodo de conveniencia getDataValue, que permite obtener el valor estadistico
 * independientemente de si se trata de un contador o un sumatorio.
 *
 * @author agumpg
 */
@Entity
@Table(name = "datamining")
public class DataminingDataEntity implements Serializable, DataminingData {

	private final static Logger logger = Logger.getLogger(DataminingDataEntity.class.getName());
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(nullable = false)
	private DataminingDataPeriod periodType;
	@Temporal(TemporalType.TIMESTAMP)
	private Date periodDate;
	private Double dataSum;
	private Long dataCount;

	public DataminingDataEntity() {
	}

	public DataminingDataEntity(String name, DataminingDataPeriod period, Date date, Long count) {
		this(name, period, date, null, count);
	}

	public DataminingDataEntity(String name, DataminingDataPeriod period, Date date, Double sum) {
		this(name, period, date, sum, null);
	}

	/**
	 * Este metodo crea estadisticas temporales, ya que el periodo es un campo obligatorio.
	 */
	public DataminingDataEntity(String name, Date date, Double sum, Long count) {
		this(name, null, date, sum, count);
	}

	public DataminingDataEntity(String name, DataminingDataPeriod period, Date date, Double sum, Long count) {
		this.name = name;
		this.periodType = period;
		this.periodDate = date;
		this.dataSum = sum;
		this.dataCount = count; // Salta la restriccion de valores negativos o nulos
	}

	@Override
	public BigDecimal getDataValue() {
		if (dataSum == null) {
			// Si sum es null se devuelve el valor del contador
			return new BigDecimal(getDataCount());
		} else if (dataCount == null) {
			return new BigDecimal(getDataSum());
		} else {
			// Si no, se devolvera el valor de sum divido entre el contador
			return new BigDecimal(getDataSum()).divide(new BigDecimal(getDataCount()));
		}
	}

	@Override
	public Long getDataCount() {
		return (dataCount == null) ? 0l : dataCount;
	}

	public void setDataCount(Long dataCount) {
		if (dataCount == null || dataCount < 0) {
			// Si dataCount == 0 podria dar una excepcion al llamar a getDataValue (division por 0)
			throw new IllegalArgumentException("el contador debe ser valor entero mayor que 0");
		}
		this.dataCount = dataCount;
	}

	@Override
	public Date getPeriodDate() {
		return periodDate;
	}

	public void setPeriodDate(Date periodDate) {
		this.periodDate = periodDate;
	}

	@Override
	public DataminingDataPeriod getPeriodType() {
		return periodType;
	}

	public void setPeriodType(DataminingDataPeriod periodType) {
		this.periodType = periodType;
	}

	@Override
	public Double getDataSum() {
		return (dataSum == null) ? 0d : dataSum;
	}

	public void setDataSum(Double dataSum) {
		this.dataSum = dataSum;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		if (!(object instanceof DataminingDataEntity)) {
			return false;
		}
		DataminingDataEntity other = (DataminingDataEntity) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer("StatisticsData[");
		buff.append(" id=").append(id);
		buff.append(", name=").append(name);
		buff.append(", period=").append(periodType);
		buff.append(", date=").append(periodDate);
		buff.append(", count=").append(dataCount);
		buff.append(", sum=").append(dataSum);
		buff.append(" ]");
		return buff.toString();
	}

	@PostPersist
	void onCreateLog() {
		logger.log(INFO, "post persist {0}", toString());
	}

	@PreUpdate
	void onUpdate() {
		logger.log(INFO, "pre update {0}", toString());
	}
}
