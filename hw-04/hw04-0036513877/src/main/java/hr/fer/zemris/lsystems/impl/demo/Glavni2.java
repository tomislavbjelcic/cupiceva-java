package hr.fer.zemris.lsystems.impl.demo;

import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;
import static hr.fer.zemris.lsystems.impl.demo.LSystemExamples.createKochCurve2; 

public class Glavni2 {
	
	public static void main(String[] args) {
		LSystemViewer.showLSystem(createKochCurve2(LSystemBuilderImpl::new));
	}
	
	
}
