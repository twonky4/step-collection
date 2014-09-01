package de.alexanderkose.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StepArrayListTest {
	@Test
	public void testIsEmpty() {
		StepArrayList<String> list = new StepArrayList<String>(5);
		assertTrue(list.isEmpty());

		list = new StepArrayList<String>(5);
		assertTrue(list.add("1"));
		assertFalse(list.isEmpty());

		list = new StepArrayList<String>(5);
		assertTrue(list.add("1"));
		assertTrue(list.add("2"));
		assertTrue(list.add("3"));
		assertTrue(list.add("4"));
		assertTrue(list.add("5"));
		assertTrue(list.add("6"));
		assertFalse(list.isEmpty());
	}

	@Test
	public void testRemoveObject() {
		StepArrayList<String> list = new StepArrayList<String>(5);
		assertFalse(list.remove("0"));

		list = new StepArrayList<String>(5);
		list.add("1");
		assertTrue(list.remove("1"));
		assertFalse(list.remove("1"));

		list = new StepArrayList<String>(5);
		list.add("1");
		list.add("2");

		assertTrue(list.remove("2"));
		assertTrue(list.remove("1"));

		list = new StepArrayList<String>(5);
		list.add("1");
		list.add("2");

		assertTrue(list.remove("1"));
		assertTrue(list.remove("2"));

		list = new StepArrayList<String>(5);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");

		assertTrue(list.remove("3"));
		assertTrue(list.remove("7"));
		assertTrue(list.remove("5"));
		assertTrue(list.remove("1"));
		assertFalse(list.remove("0"));
	}

	@Test
	public void testContains() {
		StepArrayList<String> list = new StepArrayList<String>(5);
		assertFalse(list.contains("0"));

		list = new StepArrayList<String>(5);
		list.add("1");
		assertTrue(list.contains("1"));

		list = new StepArrayList<String>(5);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");

		assertFalse(list.contains("1"));
		assertFalse(list.contains("2"));
		assertTrue(list.contains("3"));
		assertTrue(list.contains("4"));
		assertTrue(list.contains("5"));
		assertTrue(list.contains("6"));
		assertTrue(list.contains("7"));
	}
}
