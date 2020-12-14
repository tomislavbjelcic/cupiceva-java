package hr.fer.zemris.java.fractals;

import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexRootedPolynomial;
import static hr.fer.zemris.java.fractals.FractalConstants.*;

public class SequentialFractalProducer extends AbstractFractalProducer {
	
	protected SequentialFractalProducer(ComplexRootedPolynomial functionRooted) {
		super(functionRooted);
	}

	@Override
	public void produce(double reMin, double reMax, double imMin, double imMax,
			int width, int height, 
			long requestNo, IFractalResultObserver observer, 
			AtomicBoolean cancel) {
		
		short[] data = new short[width * height];
		int ymin = 0;
		int ymax = height - 1;
		
		NewtonRaphson.calculate​(reMin, reMax, imMin, imMax, ROOT_TRESHOLD, 
				CONVERGENCE_TRESHOLD, function, functionRooted, derivative, 
				width, height, MAX_ITERATIONS, ymin, ymax, data, cancel);
		
		observer.acceptResult(data, (short)(function.order() + 1), requestNo);
	}
	
}
