package view.swing;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author hectoracevedo
 */
public class ButtonAction extends JButton {
    
    private final Animator animator;
    private int targetSize;
    private float animatSize;
    private Point pressedPoint;
    private float alpha;
    private Color effectColor = new Color(173, 173, 173);

    public ButtonAction() {
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(8, 10, 8, 10));
        setHorizontalAlignment(JButton.LEFT);
        setBackground(new Color(40, 90, 165));
        setForeground(new Color(250, 250, 250));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                targetSize = Math.max(getWidth(), getHeight()) * 2;
                animatSize = 0;
                pressedPoint = evt.getPoint();
                alpha = 0.5f;

                if (animator.isRunning()) {
                    animator.stop();
                }

                animator.start();
            }

        });

        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (fraction > 0.5f) {
                    alpha = 1 - fraction;
                }

                animatSize = fraction * targetSize;
                repaint();
            }
        };

        animator = new Animator(400, target);
        animator.setResolution(0);
    }

    public void setEffectColor(Color effectColor) {
        this.effectColor = effectColor;
    }

    public Color getEffectColor() {
        return effectColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width, height, 10, 10);

        if (pressedPoint != null) {
            Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, 10, 10));
            g2.setColor(effectColor);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            area.intersect(new Area(new Ellipse2D.Double((pressedPoint.x - animatSize / 2), (pressedPoint.y - animatSize / 2), animatSize, animatSize)));
            g2.fill(area);
        }

        g2.setComposite(AlphaComposite.SrcOver);
        super.paintComponent(g);
    }

    @Override
    public void paint(Graphics g) {
        if (isSelected()) {
            int width = getWidth();
            int height = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(25, 25, 25));
            g2.fillRoundRect(0, 0, width - 1, height - 1, 10, 10);
        }

        super.paint(g);
    }

}
