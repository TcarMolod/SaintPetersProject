import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.List;

public class Chart extends ApplicationFrame {
    private static final long serialVersionUID = 1L;

    public Chart(final String title, List<List<String>> values) {
        super(title);
        final CategoryDataset dataset = createDataset(values);
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(960, 880));
        setContentPane(chartPanel);
    }
    public static CategoryDataset createDataset(List<List<String>> values)
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (var el: values){
        dataset.addValue(Integer.parseInt(el.get(1)), "", el.get(0).substring(0,el.get(0).length()-8));}
        return dataset;
    }

    private static JFreeChart createChart(CategoryDataset dataset)
    {
        JFreeChart chart = ChartFactory.createBarChart(
                "Количество N-этажных домов",
                "Кол-во этажей",                   // x-axis label
                "Количество домов",                // y-axis label
                dataset);

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);

        return chart;
    }

    public static void make(List<List<String>> values) {
        final Chart demo = new Chart("график", values);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
