package hr.fer.oprpp1.hw02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.Node;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptTester {
	
	private String readExample(int n) {
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
			if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
			byte[] data = is.readAllBytes();
			String text = new String(data, StandardCharsets.UTF_8);
			return text;
		} catch(IOException ex) {
			throw new RuntimeException("Greška pri čitanju datoteke.", ex);
		}
	}
	
	@Test
	public void test1() {
		int n = 1;
		String doc = readExample(n);
		
		SmartScriptParser parser = new SmartScriptParser(doc);
		DocumentNode document = parser.getDocumentNode();
		
		int expected = 1;
		int actual = document.numberOfChildren();
		assertEquals(expected, actual);
		
		Node child = document.getChild(0);
		assertEquals(child.getClass(), TextNode.class);
	}
	
	@Test
	public void test2() {
		int n = 2;
		String doc = readExample(n);
		
		SmartScriptParser parser = new SmartScriptParser(doc);
		DocumentNode document = parser.getDocumentNode();
		
		int expected = 1;
		int actual = document.numberOfChildren();
		assertEquals(expected, actual);
		
		Node child = document.getChild(0);
		assertEquals(child.getClass(), TextNode.class);
	}
	
	@Test
	public void test3() {
		int n = 3;
		String doc = readExample(n);
		
		SmartScriptParser parser = new SmartScriptParser(doc);
		DocumentNode document = parser.getDocumentNode();
		
		int expected = 1;
		int actual = document.numberOfChildren();
		assertEquals(expected, actual);
		
		Node child = document.getChild(0);
		assertEquals(child.getClass(), TextNode.class);
	}
	
	@Test
	public void test4() {
		int n = 4;
		String doc = readExample(n);
		
		assertThrows(SmartScriptParserException.class, 
				() -> new SmartScriptParser(doc));
	}
	
	@Test
	public void test5() {
		int n = 5;
		String doc = readExample(n);
		
		assertThrows(SmartScriptParserException.class, 
				() -> new SmartScriptParser(doc));
	}
	
	@Test
	public void test6() {
		int n = 6;
		String doc = readExample(n);
		
		SmartScriptParser parser = new SmartScriptParser(doc);
		DocumentNode document = parser.getDocumentNode();
		
		int expected = 3;	// zbog \n na kraju dokumenta
		int actual = document.numberOfChildren();
		assertEquals(expected, actual);
		
		Node n0 = document.getChild(0);
		Node n1 = document.getChild(1);
		Node n2 = document.getChild(2);
		assertEquals(n0.getClass(), TextNode.class);
		assertEquals(n1.getClass(), EchoNode.class);
		assertEquals(n2.getClass(), TextNode.class);
		
		assertEquals(((EchoNode) n1).getElements().length, 1);
		
	}
	
	@Test
	public void test7() {
		int n = 7;
		String doc = readExample(n);
		
		SmartScriptParser parser = new SmartScriptParser(doc);
		DocumentNode document = parser.getDocumentNode();
		
		int expected = 3;	// zbog \n na kraju dokumenta
		int actual = document.numberOfChildren();
		assertEquals(expected, actual);
		
		Node n0 = document.getChild(0);
		Node n1 = document.getChild(1);
		Node n2 = document.getChild(2);
		assertEquals(n0.getClass(), TextNode.class);
		assertEquals(n1.getClass(), EchoNode.class);
		assertEquals(n2.getClass(), TextNode.class);
		
		assertEquals(((EchoNode) n1).getElements().length, 1);
	}
	
	@Test
	public void test8() {
		int n = 8;
		String doc = readExample(n);
		
		assertThrows(SmartScriptParserException.class, 
				() -> new SmartScriptParser(doc));
	}
	
	@Test
	public void test9() {
		int n = 9;
		String doc = readExample(n);
		
		assertThrows(SmartScriptParserException.class, 
				() -> new SmartScriptParser(doc));
	}
	
	public static void main(String[] args) {
		int lastExample = 10;
		int[] noExceptionTests = {0, 1, 2, 3, 6, 7, lastExample};
		SmartScriptTester tester = new SmartScriptTester(); // zbog metode readExample
		for (int i : noExceptionTests) {
			String docBody = tester.readExample(i);
			
			SmartScriptParser parser = new SmartScriptParser(docBody);
			DocumentNode document = parser.getDocumentNode();
			String originalDocumentBody = document.toString();
			
			SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
			DocumentNode document2 = parser2.getDocumentNode();
			
			// now document and document2 should be structurally identical trees
			boolean same = document.equals(document2); // ==> "same" must be true
			System.out.println("Example " + i + ": " + same);
			if (i == lastExample) {
				System.out.println();
				DocumentNode d = document;
				System.out.println(d);
			}
		}
	}
	
}