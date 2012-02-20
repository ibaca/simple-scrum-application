package org.inftel.ssa.web.estadisticas;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.inftel.ssa.datamining.DataminingData;
import org.inftel.ssa.datamining.DataminingDataPeriod;
import org.inftel.ssa.datamining.DataminingProcessor;
import org.inftel.ssa.domain.Project;
import org.inftel.ssa.domain.Sprint;
import org.inftel.ssa.domain.User;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean
@RequestScoped
public class EstadisBean implements Serializable {

    @EJB
    private DataminingProcessor datamining;
    private CartesianChartModel esfuerzoModel;
    private CartesianChartModel tareasModel;
    private CartesianChartModel individualModel;

    public EstadisBean() {
        createEsfuerzoModel();
        createTareasModel();
       // createIndividualModel();
    }

    public CartesianChartModel getEsfuerzoModel() {
        return esfuerzoModel;
    }

    public CartesianChartModel getIndividualModel() {
        return individualModel;
    }

    public CartesianChartModel getTareasModel() {
        return tareasModel;
    }

    private void createEsfuerzoModel() {
        esfuerzoModel = new CartesianChartModel();
        LineChartSeries series = new LineChartSeries();
        Map<Date, DataminingData> samples; // todos los datos por fecha

        String name = "task." + "id_usuario" + "." + "sprint" + ".remaining"; // Con id_usuario y sprint pasádo por parámetro
        samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));

        for (Date date : samples.keySet()) {
            series.set(df.format(date), samples.get(date).getDataSum());
        }

        esfuerzoModel.addSeries(series);

    }

    private void createTareasModel() {

        tareasModel = new CartesianChartModel();
        LineChartSeries series = new LineChartSeries();
        Map<Date, DataminingData> samples; // todos los datos por fecha

        String name = "task." + "id_usuario" + "." + "sprint" + ".remaining"; // Con id_usuario y sprint pasádo por parámetro
        samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));

        for (Date date : samples.keySet()) {
            series.set(df.format(date), samples.get(date).getDataCount());
        }

        tareasModel.addSeries(series);

    }

  /*  private void createIndividualModel() {

        individualModel = new CartesianChartModel();
        new Project().getUsers();
        List<User> listUsers = project.getUsers();  //Con idSprint el id del Sprint 
        Iterator iter = listUsers.iterator(); 
        while (iter.hasNext()) {


            LineChartSeries series = new LineChartSeries();
            series.setLabel(iter.nickname); //Con nickname el nombre del Usuario


 
            Map<Date, Long> samples; // todos los datos por fecha
            String name = "task." + "id_usuario" + "." + "sprint" + ".remaining";
            samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));

            for (Date date : samples.keySet()) {
                series.set(df.format(date), samples.get(date));
            }

            individualModel.addSeries(series);
        }
    }  */
}
 