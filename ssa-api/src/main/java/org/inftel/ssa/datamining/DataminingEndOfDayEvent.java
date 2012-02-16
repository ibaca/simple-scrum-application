/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.datamining;

import java.util.Date;

/**
 *
 * @author ibaca
 */
public final class DataminingEndOfDayEvent {

	/**
	 * Delega la actualizacion al procesador de dataminin, solo uso interno dataminig
	 */
	private UpdateDataHelper updateDataHelper;
	/**
	 * Fecha de inicio desde la que deben procesarse los datos
	 */
	private Date fromDate;
	/**
	 * Fecha de fin desde la que deben procesarse los datos
	 */
	private Date toDate;

	public DataminingEndOfDayEvent() {
	}

	/**
	 * Actualiza los datos en el intervalo fromDate() a toDate()
	 */
	public void updateData(String statName, Double statSum, Long statCount) {
		updateDataHelper.updateDaily(statName, getFromDate(), statSum, statCount);
	}

	void setUpdateDataHelper(UpdateDataHelper updateDataHelper) {
		this.updateDataHelper = updateDataHelper;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * Mantiene una referencia con el EJB que hace la persistencia de las estadisticas.
	 *
	 * FIXME Pasar referencias de los ejbs entre metodos es incorrecto en la arquitectura JEE.
	 */
	static interface UpdateDataHelper {

		void updateDaily(String statName, Date from, Double statSum, Long statCount);
	}
}
