package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

public abstract class AbstractFractalProducer implements IFractalProducer {
	
	protected ComplexRootedPolynomial functionRooted;
	protected ComplexPolynomial function;
	protected ComplexPolynomial derivative;
	
	protected AbstractFractalProducer(ComplexRootedPolynomial functionRooted) {
		this.functionRooted = functionRooted;
		function = functionRooted.toComplexPolynom();
		derivative = function.derive();
	}
	
}
