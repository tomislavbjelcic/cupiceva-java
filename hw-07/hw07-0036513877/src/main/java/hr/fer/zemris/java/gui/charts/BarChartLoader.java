package hr.fer.zemris.java.gui.charts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BarChartLoader {
	
	private static final int LINE_COUNT = 6;
	
	private BarChartLoader() {}
	
	public static BarChart loadFromFile(Path file) {
		validateFilePath(file);
		List<String> list;
		try (BufferedReader r = Files.newBufferedReader(file)) {
			list = r.lines().limit(LINE_COUNT)
					.collect(Collectors.toCollection(ArrayList::new));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		
		if (list.size() != LINE_COUNT)
			throw new IllegalArgumentException("Datoteka mora sadržavati najmanje " + LINE_COUNT + " linija.");
		
		
		String xDesc = list.get(0);
		String yDesc = list.get(1);
		String data = list.get(2);
		int yMin = Integer.parseInt(list.get(3));
		int yMax = Integer.parseInt(list.get(4));
		int yStep = Integer.parseInt(list.get(5));
		
		List<XYValue> xyValues = new LinkedList<>();
		try (Scanner sc = new Scanner(data)) {
			while (sc.hasNext()) {
				String xyvStr = sc.next();
				XYValue xyv = XYValue.parse(xyvStr);
				xyValues.add(xyv);
			}
		}
		
		BarChart chart = new BarChart(xyValues, xDesc, yDesc, yMin, yMax, yStep);
		return chart;
	}
	
	private static void validateFilePath(Path path) {
		Objects.requireNonNull(path);
		
		if (!Files.exists(path))
			throw new IllegalArgumentException("Putanja \""+path+"\" ne postoji.");
		if (!Files.isRegularFile(path))
			throw new IllegalArgumentException("Putanja \""+path+"\" ne predstavlja datoteku.");
		
	}
	
}
