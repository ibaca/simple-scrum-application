package org.inftel.ssa.datamining;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * Periodo del dato estadistico. Establece el intervalo de tiempo que representa cada dato
 * estadistico, pro ejemplo, si el periodo fuese emnsual significa que los valores estadisticos
 * almacenados corresponderían al total acumulado de ese mes.
 * 
 * @author ibaca
 */
public enum DataminingDataPeriod {
    /**
     * Periodo estadistico para intervalo diario.
     */
    DAYLY {
        @Override
        protected int getField() {
            return DAY_OF_MONTH;
        }
    },
    /**
     * Periodo estadistico para intervalo mensual.
     */
    MONTHLY {
        @Override
        protected int getField() {
            return MONTH;
        }
    },
    /**
     * Periodo estadistico para intervalo anual.
     */
    ANNUAL {
        @Override
        protected int getField() {
            return YEAR;
        }
    };

    protected abstract int getField();

    /** Devuelve la fecha que representa el inicio del periodo. */
    public Calendar endsAt(Calendar of) {
        return DateUtils.ceiling(of, getField());
    }

    /** Devuelve la fecha que representa el inicio del periodo. */
    public Date endsAt(Date of) {
        return DateUtils.ceiling(of, getField());
    }

    /** Devuelve la fecha que representa el final del periodo. */
    public Calendar beginsAt(Calendar of) {
        return DateUtils.truncate(of, getField());
    }

    /** Devuelve la fecha que representa el final del periodo. */
    public Date beginsAt(Date of) {
        return DateUtils.truncate(of, getField());
    }

}