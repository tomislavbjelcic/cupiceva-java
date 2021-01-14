package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class SimpleHashtableTest {
    @Test
    public void testHashTablePutsValues() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        assertEquals(2, testTable.get("Kristina"));
        assertEquals(5, testTable.get("Ivana"));
        assertEquals(5, testTable.size());
    }

    @Test
    public void testHashTableToString() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana

        assertEquals("[Ante=2, Ivana=5, Jasna=2]", testTable.toString());
    }

    @Test
    public void testContainsKey() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        assertTrue(testTable.containsKey("Kristina"));
    }

    @Test
    public void testContainsValue() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        assertTrue(testTable.containsValue(100));
    }

    @Test
    public void testRemoveElement() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        testTable.remove("Ivana");

        assertFalse(testTable.containsKey("Ivana"));
        assertTrue(testTable.containsKey("Jasna"));
    }

    @Test
    public void testRemoveElement2() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        testTable.remove("Ante");

        assertFalse(testTable.containsKey("Ante"));
        assertTrue(testTable.containsKey("Ivana"));
    }

    @Test
    public void testHashtableIteratorInForEach() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        StringBuilder result = new StringBuilder();

        for (var element : testTable) {
            result.append(element.getKey()).append(element.getValue());
        }

        assertEquals("Josip100Ante2Ivana5Jasna2Kristina2", result.toString());
    }

    @Test
    public void testHashtableIteratorRemoveValid() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        var it = testTable.iterator();

        while (it.hasNext()) {
            if (it.next().getKey().equals("Ivana"))
                it.remove();
        }
        assertFalse(testTable.containsKey("Ivana"));
    }

    @Test
    public void testHashtableIteratorNextThrowsException() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);

        var it = testTable.iterator();

        it.next();
        it.next();

        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void testHashtableIteratorConcurrentModificationError() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);

        var it = testTable.iterator();

        it.next();
        testTable.put("Lucija", 2);

        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    public void testHashtableIteratorRemoveCalledTwiceThrowsException() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        var it = testTable.iterator();


        assertThrows(IllegalStateException.class, () -> {
            while (it.hasNext()) {
                if (it.next().getKey().equals("Ivana")) {
                    it.remove();
                    it.remove();
                }
            }
        });
    }

    @Test
    public void testHashtableDoubleIterator() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        StringBuilder sb = new StringBuilder();
        for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
            sb.append(String.format("%s => %d\n", pair.getKey(), pair.getValue()));
        }

        assertEquals("""
                Ante => 2
                Ivana => 5
                Jasna => 2
                Kristina => 5
                """,
                sb.toString());


    }
    
    @Test
    public void testClear() {
    	SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);
        
        testTable.clear();
        assertEquals(0, testTable.size());
        assertFalse(testTable.containsKey("Ante"));
        assertEquals("[]", testTable.toString());
    }
    
    @Test
    public void testNextRemove() {
    	// create collection:
    	SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
    	// fill data:
    	examMarks.put("Ivana", 2);
    	examMarks.put("Ante", 2);
    	examMarks.put("Jasna", 2);
    	examMarks.put("Kristina", 5);
    	examMarks.put("Ivana", 5); // overwrites old grade for Ivana
    	examMarks.put("Paško", 3);
    	examMarks.put("Zvonko", null);
    	examMarks.put("Tomislav", 5);
    	examMarks.put("Patrik", null);
    	
    	/*
    	 * 
    	 * 0 - 
    	 * 1 - Zvonko
    	 * 2 - 
    	 * 3 -
    	 * 4 - Paško
    	 * 5 - 
    	 * 6 - 
    	 * 7 - Ivana, Kristina
    	 * 8 - 
    	 * 9 - 
    	 * 10 - 
    	 * 11 - Tomislav
    	 * 12 - 
    	 * 13 - 
    	 * 14 - Ante
    	 * 15 - Jasna, Patrik
    	 * */
    	
    	var it = examMarks.iterator();
    	it.next(); it.next(); it.next();
    	it.remove();
    	
    	var actual = it.next().toString();
    	String expected = "Kristina=5";
    	assertEquals(expected, actual);
    	
    	it.next();
    	it.remove();
    	it.next();
    	it.remove();
    	assertThrows(IllegalStateException.class, it::remove);
    	it.next();
    	it.next();
    	it.remove();
    	
    	var expectedSize = 4;
    	var actualSize = examMarks.size();
    	assertEquals(expectedSize, actualSize);
    	
    	String actArr = examMarks.toString();
    	String expArr = "[Zvonko=null, Paško=3, Kristina=5, Jasna=2]";
    	assertEquals(expectedSize, actualSize);
    	
    	assertThrows(NoSuchElementException.class, it::next);
    }
}