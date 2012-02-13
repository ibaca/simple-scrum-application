package org.inftel.ssa.web.chart;



import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

/** 
 
 **/
public class ChartCreatorServlet extends HttpServlet {

    /**
     * * Default constructor.
     */
    public ChartCreatorServlet() { // nothing required
    }

    /**
     * * Process a GET request.
     *
     * * @param request the request.
     * @param response the response.
     * * @throws ServletException if there is a servlet related problem.
     * @throws IOException if there is an I/O problem.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputStream out = response.getOutputStream();
        try {

            String type = request.getParameter("type");
            int width = Integer.parseInt(request.getParameter("width"));
            int height = Integer.parseInt(request.getParameter("height"));
            JFreeChart chart = null;
            if (type.equals("grafInd")) {
                chart = grafInd();
            } else if (type.equals("grafUser")) {
                chart = grafEsf();
            } else if (type.equals("grafTareas")) {
                chart = grafTareas();
            }


            if (chart != null) {
                response.setContentType("image/png");
                ChartUtilities.writeChartAsPNG(out, chart, width, height);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        } finally {
            out.close();
        }
    }

    
    private static JFreeChart grafInd() {

        XYDataset data = datosIndividuales();
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Grafico Individual", "Fecha", "Horas Restantes", data, true, true, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1));
        axis.setDateFormatOverride(new SimpleDateFormat("d-MM"));
        return chart;
    }

    private static XYDataset datosIndividuales() {

        TimeSeries s1 = new TimeSeries("Ignacio", Day.class);
        s1.add(new Day(1, 2, 2012), 100);
        s1.add(new Day(2, 2, 2012), 90);
        s1.add(new Day(3, 2, 2012), 80);
        s1.add(new Day(4, 2, 2012), 70);
        s1.add(new Day(5, 2, 2012), 60);
        s1.add(new Day(6, 2, 2012), 50);
        s1.add(new Day(7, 2, 2012), 30);
        s1.add(new Day(8, 2, 2012), 15);
        s1.add(new Day(9, 2, 2012), 0);



        TimeSeries s2 = new TimeSeries("Jesús Barriga", Day.class);

        s2.add(new Day(1, 2, 2012), 100);
        s2.add(new Day(2, 2, 2012), 95);
        s2.add(new Day(3, 2, 2012), 90);
        s2.add(new Day(4, 2, 2012), 87);
        s2.add(new Day(5, 2, 2012), 60);
        s2.add(new Day(6, 2, 2012), 46);
        s2.add(new Day(7, 2, 2012), 29);
        s2.add(new Day(8, 2, 2012), 5);
        s2.add(new Day(9, 2, 2012), 0);

        TimeSeries s3 = new TimeSeries("Jesús", Day.class);

        s3.add(new Day(1, 2, 2012), 100);
        s3.add(new Day(2, 2, 2012), 70);
        s3.add(new Day(3, 2, 2012), 65);
        s3.add(new Day(4, 2, 2012), 60);
        s3.add(new Day(5, 2, 2012), 55);
        s3.add(new Day(6, 2, 2012), 39);
        s3.add(new Day(7, 2, 2012), 22);
        s3.add(new Day(8, 2, 2012), 11);
        s3.add(new Day(9, 2, 2012), 0);

        TimeSeries s4 = new TimeSeries("Juan Antonio", Day.class);

        s4.add(new Day(1, 2, 2012), 100);
        s4.add(new Day(2, 2, 2012), 80);
        s4.add(new Day(3, 2, 2012), 90);
        s4.add(new Day(4, 2, 2012), 70);
        s4.add(new Day(5, 2, 2012), 58);
        s4.add(new Day(6, 2, 2012), 34);
        s4.add(new Day(7, 2, 2012), 28);
        s4.add(new Day(8, 2, 2012), 13);
        s4.add(new Day(9, 2, 2012), 0);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        dataset.addSeries(s3);
        dataset.addSeries(s4);

        dataset.setDomainIsPointsInTime(true);
        return dataset;


    }

    private static JFreeChart grafEsf() {

        XYDataset data = datosUsuario();

        JFreeChart chart = ChartFactory.createTimeSeriesChart("Grafico de Esfuerzo", "Fecha", "Horas Restantes", data, true, true, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);

        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1));
        axis.setDateFormatOverride(new SimpleDateFormat("d-MM"));
        return chart;
    }

    private static XYDataset datosUsuario() {

        TimeSeries s1 = new TimeSeries("Tareas Pendientes", Day.class);
        s1.add(new Day(1, 2, 2012), 250);
        s1.add(new Day(2, 2, 2012), 228);
        s1.add(new Day(3, 2, 2012), 140);
        s1.add(new Day(4, 2, 2012), 133);
        s1.add(new Day(5, 2, 2012), 120);
        s1.add(new Day(6, 2, 2012), 60);
        s1.add(new Day(7, 2, 2012), 40);
        s1.add(new Day(8, 2, 2012), 30);
        s1.add(new Day(9, 2, 2012), 0);



        TimeSeries s2 = new TimeSeries("Ideal", Day.class);

        s2.add(new Day(1, 2, 2012), 250);
        s2.add(new Day(10, 2, 2012), 0);


        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        dataset.setDomainIsPointsInTime(true);
        return dataset;


    }

    private static JFreeChart grafTareas() {

        XYDataset data = datosUsuarioTareas();

        JFreeChart chart = ChartFactory.createTimeSeriesChart("Grafico de Tareas", "Fecha", "Tareas Restantes", data, true, true, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1));
        axis.setDateFormatOverride(new SimpleDateFormat("d-MM"));
        return chart;
    }

    private static XYDataset datosUsuarioTareas() {

        TimeSeries s1 = new TimeSeries("Tareas Restantes", Day.class);
        s1.add(new Day(1, 2, 2012), 25);
        s1.add(new Day(2, 2, 2012), 22);
        s1.add(new Day(3, 2, 2012), 14);
        s1.add(new Day(4, 2, 2012), 13);
        s1.add(new Day(5, 2, 2012), 12);
        s1.add(new Day(6, 2, 2012), 6);
        s1.add(new Day(7, 2, 2012), 4);
        s1.add(new Day(8, 2, 2012), 3);
        s1.add(new Day(9, 2, 2012), 0);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);

        dataset.setDomainIsPointsInTime(true);
        return dataset;


    }
}
