package org.inftel.ssa.datamining;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ibaca
 */
public interface DataminingData {

	Long getDataCount();

	Double getDataSum();

	BigDecimal getDataValue();

	String getName();

	Date getPeriodDate();

	DataminingDataPeriod getPeriodType();
	
}
