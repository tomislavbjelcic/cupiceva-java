package hr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import javax.swing.ListModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PrimListModelTest {
	
	private PrimListModel model;
	
	@BeforeEach
	public void newModel() {
		model = new PrimListModel();
	}
	
	private static int[] getModelListArray(ListModel<? extends Integer> model) {
		int size = model.getSize();
		int[] arr = new int[size];
		for (int i=0; i<size; i++) {
			Integer el = model.getElementAt(i);
			arr[i] = el.intValue();
		}
		return arr;
	}
	
	private static void invokeNextTimes(int n, PrimListModel model) {
		for (int i=0; i<n; i++)
			model.next();
	}
	
	@Test
	public void testNoNext() {
		int[] expected = {1};
		int[] actual = getModelListArray(model);
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testThreeNext() {
		int times = 3;
		invokeNextTimes(times, model);
		int[] expected = {1, 2, 3, 5};
		int[] actual = getModelListArray(model);
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void test25Next() {
		int times = 25;
		invokeNextTimes(times, model);
		int[] expected = {1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};
		int[] actual = getModelListArray(model);
		assertArrayEquals(expected, actual);
	}
	
}
