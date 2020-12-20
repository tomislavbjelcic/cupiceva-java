package hr.fer.zemris.java.gui.charts;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class BarChart {

	private List<XYValue> xyValues;
	private String xDesc;
	private String yDesc;
	private int yMin;
	private int yMax;
	private int yStep;

	public BarChart(List<XYValue> xyValues, String xDesc, String yDesc, 
			int yMin, int yMax, int yStep) {
		if (yMin < 0)
			throw new IllegalArgumentException("Minimalni y negativan.");
		if (yMax <= yMin)
			throw new IllegalArgumentException("Maksimalni y nije strogo veći od minimalnog y.");
		if (yStep < 1)
			throw new IllegalArgumentException("Razmak između y vrijednosti mora biti barem 1.");

		int diff = yMax - yMin;
		int remainder = diff % yStep;
		this.yMin = yMin;
		this.yMax = yMax + (remainder == 0 ? 0 : (yStep - remainder));
		this.yStep = yStep;

		this.xDesc = Objects.requireNonNull(xDesc, "Opis uz x os je null.");
		this.yDesc = Objects.requireNonNull(yDesc, "Opis uz y os je null.");

		Objects.requireNonNull(xyValues);
		this.xyValues = new ArrayList<>(xyValues.size());
		ListIterator<XYValue> li = xyValues.listIterator();
		while(li.hasNext()) {
			int idx = li.nextIndex();
			XYValue xyv = li.next();
			Objects.requireNonNull(xyv, "XY vrijednost na poziciji " + idx + " je null.");

			if (xyv.getY() < yMin)
				throw new IllegalArgumentException("XY vrijednost " + xyv + " ima y vrijednost manju od minimalne.");

			this.xyValues.add(xyv);
		}

		Comparator<XYValue> comp = Comparator.comparingInt(XYValue::getX);
		// sortiraj po x vrijednosti
		this.xyValues.sort(comp);
		// onemogući mijenjanje liste
		this.xyValues = Collections.unmodifiableList(this.xyValues);


	}

	public List<XYValue> getXyValues() {
		return xyValues;
	}

	public String getxDesc() {
		return xDesc;
	}

	public String getyDesc() {
		return yDesc;
	}

	public int getyMin() {
		return yMin;
	}

	public int getyMax() {
		return yMax;
	}
	
	public int getyStep() {
		return yStep;
	}

	public static void main(String[] args) {
		/*
		BarChart model = new BarChart(
				Arrays.asList(
						new XYValue(1,8), new XYValue(2,20), new XYValue(3,22),
						new XYValue(10,10), new XYValue(5,4)
						),
				"Number of people in the car",
				"Frequency",
				1,
				// y-os kreće od 0
				26,
				// y-os ide do 22
				5
				);
		*/
		Path p = Paths.get("chart.txt");
		BarChart model = BarChartLoader.loadFromFile(p);
		
		System.out.println(model.getXyValues());
		System.out.println(model.getyMin());
		System.out.println(model.getyMax());
		System.out.println(model.getyDesc());
		System.out.println(model.getxDesc());
		System.out.println(model.getyStep());
	}

}
