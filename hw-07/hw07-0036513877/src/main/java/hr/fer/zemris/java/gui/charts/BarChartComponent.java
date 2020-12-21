package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Objects;

import javax.swing.JComponent;

import hr.fer.zemris.java.gui.layouts.Util;

public class BarChartComponent extends JComponent {
	
	private static final Color BAR_COLOR = new Color(77, 166, 255);
	
	private BarChart chart;
	
	public BarChartComponent(BarChart chart) {
		this.chart = Objects.requireNonNull(chart);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		//super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		Insets ins = this.getInsets();
		FontMetrics fm = g2d.getFontMetrics();
		int t = ins.top;
		int b = ins.bottom;
		int l = ins.left;
		int r = ins.right;
		int w = this.getWidth() - (l+r);
		int h = this.getHeight() - (t+b);
		
		// ispisi opise osi
		String yDesc = chart.getyDesc();
		int yDescW = fm.stringWidth(yDesc);
		String xDesc = chart.getxDesc();
		int xDescW = fm.stringWidth(xDesc);
		int asc = fm.getAscent();
		int desc = fm.getDescent();
		AffineTransform orig = g2d.getTransform();
		AffineTransform at = new AffineTransform(orig);
		double rot = Math.PI / 2;
		at.rotate(-rot);
		g2d.setTransform(at);
		g2d.drawString(yDesc, -(t+h/2+yDescW/2), l+asc);
		
		g2d.setTransform(orig);
		g2d.drawString(xDesc, l+w/2-xDescW/2, t+h-desc);
		
		
		int textValueGap = 20;
		int vyGap = 15;
		int vxGap = 8;
		int notch = 5;
		int colGap = 1;
		int topGap = 5;
		int trside = 10;
		int yMin = chart.getyMin();
		int yMax = chart.getyMax();
		int yStep = chart.getyStep();
		List<XYValue> xyValues = chart.getXyValues();
		String yMaxStr = Integer.toString(yMax);
		int yMaxStrW = fm.stringWidth(yMaxStr);
		
		int offx = l+asc+desc+textValueGap+vyGap+yMaxStrW;
		int offy = t+h-desc-asc-textValueGap-vxGap-desc-asc;
		int ytr = topGap+trside+t;
		g2d.drawLine(offx, offy+notch, offx, ytr);
		int tr = trside/2;
		Polygon triangleY = new Polygon(new int[]{offx-tr, offx+tr, offx}, 
				new int[]{ytr, ytr, ytr-trside}, 3);
		g2d.fillPolygon(triangleY);
		int xtr = w-(topGap+trside);
		g2d.drawLine(offx, offy, xtr, offy);
		Polygon triangleX = new Polygon(new int[]{xtr, xtr, xtr+trside}, 
				new int[]{offy+tr, offy-tr, offy}, 3);
		g2d.fillPolygon(triangleX);
		
		int trgap = 5;
		int rows = yMax - yMin;
		int[] heights = new int[rows];
		Util.distributeEvenly(heights, offy-(ytr+trgap));
		int cols = xyValues.size();
		int[] widths = new int[cols];
		Util.distributeEvenly(widths, xtr-trgap-offx-cols*colGap);
		
		int[] cumulativeHeights = new int[rows+1];
		for (int i=0, ch=0; i<=rows; i++) {
			ch += i==0 ? 0 : heights[i-1];
			cumulativeHeights[i] = ch;
			
			if (i%yStep == 0) {
				int oy = offy - ch;
				g2d.drawLine(offx, oy, offx-notch, oy);
				int yval = yMin + i;
				String yValStr = Integer.toString(yval);
				int xb = offx-vyGap-fm.stringWidth(yValStr);
				int yb = oy + asc/2 -1;
				g2d.drawString(yValStr, xb, yb);
			}
		}
		
		
		int[] posx = new int[cols];
		for (int i=0, px=offx+1; i<cols; i++) {
			XYValue xyv = xyValues.get(i);
			int y = xyv.getY();
			int x = xyv.getX();
			
			int yidx = y - yMin;
			int barHeight = yidx > rows ? cumulativeHeights[rows] + trgap + trside
					: cumulativeHeights[y - yMin];
			int barWidth = widths[i];
			if (barHeight > yMin) {
				int rx = px;
				int ry = offy - barHeight;
				Color before = g2d.getColor();
				g2d.setColor(BAR_COLOR);
				g2d.fillRect(rx, ry, barWidth, barHeight);
				g2d.setColor(before);
			}
			
			int pxNext = px + barWidth;
			g2d.drawLine(pxNext, offy, pxNext, offy+notch);
			String xstr = Integer.toString(x);
			int xstrW = fm.stringWidth(xstr);
			int xb = px + barWidth/2;
			int yb = offy + vxGap + asc;
			g2d.drawString(xstr, xb, yb);
			
			px = pxNext + colGap;
		}
	}
	
}
