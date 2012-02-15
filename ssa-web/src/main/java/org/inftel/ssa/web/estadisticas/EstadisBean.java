package org.inftel.ssa.web.estadisticas;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean
@RequestScoped
public class EstadisBean implements Serializable {

    private CartesianChartModel esfuerzoModel;
    private CartesianChartModel tareasModel;
    private CartesianChartModel individualModel;

    public EstadisBean() {
        createEsfuerzoModel();
        createTareasModel();
        createIndividualModel();
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

        LineChartSeries series1 = new LineChartSeries();
        series1.setMarkerStyle("diamond");
        series1.setLabel("Horas Pendientes");
        series1.set(1, 250);
        series1.set(2, 228);
        series1.set(3, 140);
        series1.set(4, 133);
        series1.set(5, 120);
        series1.set(6, 60);
        series1.set(7, 40);
        series1.set(8, 30);
        series1.set(9, 0);

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Ideal");
        series2.setMarkerStyle(null);
        series2.set(1, 250);
        series2.set(9, 0);

        esfuerzoModel.addSeries(series1);
        esfuerzoModel.addSeries(series2);
    }

    private void createTareasModel() {
        tareasModel = new CartesianChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Tareas Pendientes");
        series1.set(1, 25);
        series1.set(2, 22);
        series1.set(3, 14);
        series1.set(4, 13);
        series1.set(5, 12);
        series1.set(6, 6);
        series1.set(7, 4);
        series1.set(8, 2);
        series1.set(9, 0);

        tareasModel.addSeries(series1);
    }

    private void createIndividualModel() {
        individualModel = new CartesianChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Ignacio");
        series1.set(1, 100);
        series1.set(2, 80);
        series1.set(3, 50);
        series1.set(4, 30);
        series1.set(5, 0);

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Jesús Barriga");
        series2.set(1, 100);
        series2.set(2, 70);
        series2.set(3, 60);
        series2.set(4, 25);
        series2.set(5, 0);

        LineChartSeries series3 = new LineChartSeries();
        series3.setLabel("Jesús");
        series3.set(1, 100);
        series3.set(2, 90);
        series3.set(3, 80);
        series3.set(4, 50);
        series3.set(5, 0);

        LineChartSeries series4 = new LineChartSeries();
        series4.setLabel("Juan Antonio");
        series4.set(1, 100);
        series4.set(2, 70);
        series4.set(3, 40);
        series4.set(4, 10);
        series4.set(5, 0);

        individualModel.addSeries(series1);
        individualModel.addSeries(series2);
        individualModel.addSeries(series3);
        individualModel.addSeries(series4);

    }
}
