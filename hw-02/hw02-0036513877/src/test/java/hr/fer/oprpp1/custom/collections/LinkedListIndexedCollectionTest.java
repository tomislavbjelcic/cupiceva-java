package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LinkedListIndexedCollectionTest {
	private static Object[][] arrays;
	private static final int SAMPLE_COUNT;
	private static final int someSample = 3;
	
	static {
		Object[] a0 = {};
		Object[] a1 = {4};
		Object[] a2 = {4, "Java"};
		Object[] a3 = {4, "Java", true};
		Object[] a4 = {4, "Java", true, -1L};
		Object[] a5 = {4, "Java", true, -1L, 5, -2, 4, 2, 1, -3, 5, 100, 1, -23, 62, -113, 53, 1, 41, -6};
		arrays = new Object[][] {a0, a1, a2, a3, a4, a5};
		
		SAMPLE_COUNT = arrays.length;
	}

	@Test
	public void testDefaultConstructorDoesNotThrow() {
		assertDoesNotThrow(() -> {
			new LinkedListIndexedCollection();
		});
	}

	@Test
	public void testConstructorNullColThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new LinkedListIndexedCollection(null);
		});
	}

	private LinkedListIndexedCollection[] samples() {
		LinkedListIndexedCollection[] samples = new LinkedListIndexedCollection[SAMPLE_COUNT];
		for (int i=0; i<SAMPLE_COUNT; i++) {
			var c = new LinkedListIndexedCollection();
			for (Object obj : arrays[i]) {
				c.add(obj);
			}
			samples[i] = c;
		}
		return samples;
	}
	
	private LinkedListIndexedCollection sample() {
		return samples()[someSample];
	}
	
	private LinkedListIndexedCollection sample(int i) {
		return samples()[i];
	}

	@Test
	public void testConstructorCollectionArgumentShouldAddElements() {
		for (int i=0; i<SAMPLE_COUNT; i++) {
			Collection col1 = sample(i);
	
			Collection col2 = new LinkedListIndexedCollection(col1);
			Object[] expectedArray = arrays[i];
			Object[] actualArray = col2.toArray();	// pretpostavljajući da toArray() radi ispravno
			assertArrayEquals(expectedArray, actualArray);
			assertEquals(col1.size(), col2.size());
		}
	}


	@Test
	public void testAddShouldThrowNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			LinkedListIndexedCollection col = sample();
			col.add(null);
		});
	}
	
	@Test
	public void testAddAndToArray() {
		LinkedListIndexedCollection col = sample(0);
		col.add("Something");
		col.add(2);
		Object[] expected = {"Something", 2};
		Object[] actual = col.toArray();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testAddAllThrowsNullPointerException() {
		LinkedListIndexedCollection col = sample();
		assertThrows(NullPointerException.class, () -> col.addAll(null));
	};
	
	@Test
	public void testAddAll() {
		LinkedListIndexedCollection col1 = sample(3);
		LinkedListIndexedCollection col2 = sample(4);
		col1.addAll(col2);
		Object[] expected = {4, "Java", true, 4, "Java", true, -1L};
		Object[] actual = col1.toArray();
		assertArrayEquals(expected, actual);
		int expectedSize = 7;
		int actualSize = col1.size();
		assertEquals(expectedSize, actualSize);
	}
	
	@Test
	public void testClear() {
		for (int i=0; i<SAMPLE_COUNT; i++) {
			LinkedListIndexedCollection col = sample(i);
			col.clear();
			Object[] expected = {};
			Object[] actual = col.toArray();
			assertArrayEquals(expected, actual);
		}
	}
	
	@Test
	public void testContainsReturnsMinusOne() {
		LinkedListIndexedCollection col = sample();
		boolean shouldBeTrue = !col.contains(null) && !col.contains("Ovo sigurno ne sadrži");
		assertTrue(shouldBeTrue);
	}
	
	@Test
	public void testContains() {
		LinkedListIndexedCollection col = sample();
		boolean shouldBeTrue = col.contains(4);	// ovo sadrži
		assertTrue(shouldBeTrue);
	}
	
	@Test
	public void testForEachThrowsNullPointerException() {
		LinkedListIndexedCollection col = sample();
		assertThrows(NullPointerException.class, () -> {
			col.forEach(null);
		});
	}
	
	@Test
	public void testForEach() {
		LinkedListIndexedCollection col = sample();
		
		class ConcatProcessor implements Processor {
			StringBuilder sb = new StringBuilder();
			@Override
			public void process(Object value) {
				sb.append(value.toString());
			}
		}
		ConcatProcessor cp = new ConcatProcessor();
		String expected = "4Javatrue";
		col.forEach(cp);
		String actual = cp.sb.toString();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetThrowsIndexOutOfBoundsException() {
		LinkedListIndexedCollection col = sample();
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(col.size()));
	}
	
	@Test
	public void testGetDoesNotThrow() {
		LinkedListIndexedCollection col = sample();
		assertDoesNotThrow(() -> {
			for (int i=0, len=col.size(); i<len; i++) {
				col.get(i);
			}
		});
	}
	
	@Test
	public void testGet() {
		LinkedListIndexedCollection col = sample();
		Object expected = "Java";
		Object actual = col.get(1);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testIndexOfReturnsMinusOne() {
		LinkedListIndexedCollection col = sample();
		boolean shouldBeTrue = col.indexOf(null) == -1 && col.indexOf("Nema me") == -1;
		assertTrue(shouldBeTrue);
	}
	
	@Test
	public void testIndexOf() {
		LinkedListIndexedCollection col = sample(SAMPLE_COUNT - 1);
		int expected = 1;
		int actual = col.indexOf("Java");
		assertEquals(expected, actual);
		
		expected = 15;
		actual = col.indexOf(-113);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testInsertThrowsNullPointerException() {
		LinkedListIndexedCollection col = sample();
		assertThrows(NullPointerException.class, () -> col.insert(null, 0));
	}
	
	@Test
	public void testInsertThrowsIndexOutOfBoundsException() {
		LinkedListIndexedCollection col = sample();
		int size = col.size();
		assertThrows(IndexOutOfBoundsException.class, () -> col.insert("Umetnuto", -1));
		assertThrows(IndexOutOfBoundsException.class, () -> col.insert("Umetnuto", size+1));
	}
	
	@Test
	public void testInsertBeginning() {
		LinkedListIndexedCollection col = sample();
		String inserted = "Inserted";
		
		col.insert(inserted, 0);
		Object[] expected = {"Inserted", 4, "Java", true};
		Object[] actual = col.toArray();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testInsertMid() {
		LinkedListIndexedCollection col = sample();
		String inserted = "Inserted";
		
		col.insert(inserted, 2);
		Object[] expected = {4, "Java", "Inserted", true};
		Object[] actual = col.toArray();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testInsertEnd() {
		LinkedListIndexedCollection col = sample();
		String inserted = "Inserted";
		
		col.insert(inserted, col.size());
		Object[] expected = {4, "Java", true, "Inserted"};
		Object[] actual = col.toArray();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testRemoveThrowsIndexOutOfBoundsException() {
		LinkedListIndexedCollection col = sample();
		assertThrows(IndexOutOfBoundsException.class, () -> col.remove(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> col.remove(col.size()));
	}
	
	@Test
	public void testRemoveBeginning() {
		LinkedListIndexedCollection col = sample();
		
		col.remove(0);
		Object[] expected = {"Java", true};
		Object[] actual = col.toArray();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testRemoveEnd() {
		LinkedListIndexedCollection col = sample();
		
		col.remove(col.size() - 1);
		Object[] expected = {4, "Java"};
		Object[] actual = col.toArray();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testRemoveMid() {
		LinkedListIndexedCollection col = sample();
		
		col.remove(1);
		Object[] expected = {4, true};
		Object[] actual = col.toArray();
		assertArrayEquals(expected, actual);
	}
}
