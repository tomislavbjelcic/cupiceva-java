package hr.fer.zemris.java.fractals;

import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

public class NewtonRaphson {
	
	
	public static void calculate​(
			double reMin, 
			double reMax, 
			double imMin, 
			double imMax, 
			double rootTreshold, 
			double convergenceTreshold, 
			ComplexPolynomial function, 
			ComplexRootedPolynomial functionRooted, 
			ComplexPolynomial derivative, 
			int width, 
			int height, 
			int maxIter, 
			int ymin, 
			int ymax, 
			short[] data, 
			AtomicBoolean cancel) {
		
		int offset = ymin * width;
		for(int y = ymin; y <= ymax; y++) {
			if(cancel.get()) break;
			for(int x = 0; x < width; x++) {
				double reZ0 = x / (width-1.0) * (reMax - reMin) + reMin;
				double imZ0 = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
				Complex z0 = new Complex(reZ0, imZ0);
				Complex zn = z0;
				
				for (int iters=0; iters<maxIter; iters++) {
					Complex fzn = function.apply(zn);
					Complex derivativeFzn = derivative.apply(zn);
					Complex diff = fzn.divide(derivativeFzn);
					double diffModule = diff.module();
					
					zn = zn.sub(diff);
					if (diffModule <= convergenceTreshold)
						break;
				}
				int index = functionRooted.indexOfClosestRootFor(zn, rootTreshold);
				data[offset] = (short) (index + 1);
				offset++;
			}
		}
	}
	
	
}
