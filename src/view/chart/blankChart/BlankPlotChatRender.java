package view.chart.blankChart;

import java.awt.*;

/**
 *
 * @author Andres Cardenas
 */
public abstract class BlankPlotChatRender {
    
    public abstract String getLabelText(int index);
    public abstract void renderSeries(BlankPlotChart chart, Graphics2D gg2, SeriesSize size, int index);
    
}
