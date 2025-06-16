package javaapplication17;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import javax.swing.JFrame;
import java.util.List;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;

public class Visualizer {

    /** Histogram grafiği */
    public static void showHistogram(List<Double> data, String title) {
        double[] values = data.stream().mapToDouble(Double::doubleValue).toArray();
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("MPG", values, 20);

        JFreeChart histogram = ChartFactory.createHistogram(
                title,
                "Combined MPG",
                "Frekans",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        displayChart(histogram, title);
    }

    /** Boxplot (dikey) - PDF’e uygun görünüm */
    public static void showBoxPlot(List<Double> data, String title) {
        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        dataset.add(data, "Combined MPG", "");  // Y ekseninde değerler

        JFreeChart boxPlot = ChartFactory.createBoxAndWhiskerChart(
                "Combined MPG Boxplot (From Vehicles.csv)", // Grafik başlığı
                "",       // X ekseni (kategori)
                "MPG",    // Y ekseni
                dataset,
                false
        );

        CategoryPlot plot = boxPlot.getCategoryPlot();
        plot.setOrientation(PlotOrientation.VERTICAL); // DİKEY görünüm

        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(true);
        renderer.setSeriesPaint(0, new Color(255, 153, 0)); // Turuncu kutu
        renderer.setUseOutlinePaintForWhiskers(true);
        renderer.setMeanVisible(false);
        renderer.setMedianVisible(true);
        renderer.setMaxOutlierVisible(true);
        renderer.setMinOutlierVisible(true);
        

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        // Ekseni sıfırdan başlatmaması için
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);

        displayChart(boxPlot, title);
    }

    /** Grafiği ekranda göster */
    private static void displayChart(JFreeChart chart, String title) {
        JFrame window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.add(new ChartPanel(chart));
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
