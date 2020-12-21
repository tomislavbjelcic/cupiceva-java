package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class BarChartDemo extends JFrame {
	
	private String pathStr;
	private BarChart chart;
	
	public BarChartDemo(String pathStr, BarChart chart) {
		this.pathStr = pathStr;
		this.chart = chart;
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(500, 500);
		initGUI();
	}
	
	private void initGUI() {
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		
		JLabel topLabel = new JLabel(pathStr);
		topLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		cp.add(topLabel, BorderLayout.PAGE_START);
		
		BarChartComponent chartComponent = new BarChartComponent(chart);
		cp.add(chartComponent, BorderLayout.CENTER);
	}
	
	private static void runMain(String[] args) {
		if (args.length != 1)
			throw new IllegalArgumentException("Argumenti glavnog programa moraju sadržavati točno 1 argument: putanju do datoteke.");
		
		String pathArg = args[0];
		Path p = Paths.get(pathArg);
		BarChart chart = BarChartLoader.loadFromFile(p);
		String pathAbsolute = p.toAbsolutePath().toString();
		
		SwingUtilities.invokeLater(() -> {
			BarChartDemo frame = new BarChartDemo(pathAbsolute, chart);
			frame.setVisible(true);
		});
	}
	
	public static void main(String[] args) {
		try {
			runMain(args);
		} catch (RuntimeException ex) {
			System.out.println("Dogodila se pogreška: " + ex.getMessage());
		}
	}
	
}
