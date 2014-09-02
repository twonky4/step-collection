package de.alexanderkose.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class StepCollectionTest {
	@Test
	public void testIsEmpty() {
		StepCollection<String> list = new StepCollection<String>(5);
		assertTrue(list.isEmpty());

		list = new StepCollection<String>(5);
		assertTrue(list.add("1"));
		assertFalse(list.isEmpty());

		list = new StepCollection<String>(5);
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
		StepCollection<String> list = new StepCollection<String>(5);
		assertFalse(list.remove("0"));

		list = new StepCollection<String>(5);
		assertFalse(list.remove(null));

		list = new StepCollection<String>(5);
		list.add("1");
		assertTrue(list.remove("1"));
		assertFalse(list.remove("1"));

		list = new StepCollection<String>(5);
		list.add("1");
		list.add("2");

		assertTrue(list.remove("2"));
		assertTrue(list.remove("1"));

		list = new StepCollection<String>(5);
		list.add("1");
		list.add("2");

		assertTrue(list.remove("1"));
		assertTrue(list.remove("2"));

		list = new StepCollection<String>(5);
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

		list = new StepCollection<String>(5);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");

		assertTrue(list.remove("1"));
	}

	@Test
	public void testAddObject() {
		StepCollection<String> list = new StepCollection<String>(5);
		assertTrue(list.add("0"));

		list = new StepCollection<String>(5);
		assertFalse(list.add(null));

		list = new StepCollection<String>(5);
		assertTrue(list.add("1"));
		assertFalse(list.add("1"));

		list = new StepCollection<String>(5);
		assertTrue(list.add("1"));
		assertTrue(list.add("2"));
		assertTrue(list.add("3"));
		assertTrue(list.add("4"));
		assertTrue(list.add("5"));
		assertTrue(list.add("6"));
		assertFalse(list.add("6"));
		assertFalse(list.add("1"));

		list = new StepCollection<String>(5);
		assertTrue(list.add("6"));
		assertTrue(list.add("5"));
		assertTrue(list.add("4"));
		assertTrue(list.add("3"));
		assertTrue(list.add("2"));
		assertTrue(list.add("1"));
	}

	@Test
	public void testClear() {
		StepCollection<String> list = new StepCollection<String>(5);
		list.add("0");
		assertFalse(list.isEmpty());

		list.clear();

		assertTrue(list.isEmpty());

		list = new StepCollection<String>(5);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");

		assertFalse(list.isEmpty());

		list.clear();

		assertTrue(list.isEmpty());
	}

	@Test
	public void testNextStep() {
		StepCollection<String> list = new StepCollection<String>(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");

		assertEquals(2, list.size());

		assertTrue(list.nextStep());

		assertEquals(4, list.size());

		assertTrue(list.nextStep());

		assertEquals(6, list.size());

		assertTrue(list.nextStep());

		assertEquals(7, list.size());

		assertFalse(list.nextStep());
	}

	@Test
	public void testContains() {
		StepCollection<String> list = new StepCollection<String>(5);
		assertFalse(list.contains("0"));

		list = new StepCollection<String>(5);
		list.add("1");
		assertTrue(list.contains("1"));

		list = new StepCollection<String>(5);
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

	@Test
	public void testAddAll() {
		StepCollection<String> list = new StepCollection<String>(5);
		assertFalse(list.addAll(null));

		ArrayList<String> tmpList = new ArrayList<>();

		list = new StepCollection<String>(5);
		assertFalse(list.addAll(tmpList));

		tmpList.add("1");
		tmpList.add("5");

		list = new StepCollection<String>(5);
		assertTrue(list.addAll(tmpList));
		assertFalse(list.addAll(tmpList));

		tmpList.add("2");
		tmpList.add("3");
		tmpList.add("4");

		list = new StepCollection<String>(5);
		assertTrue(list.addAll(tmpList));

		tmpList.add("6");

		assertTrue(list.addAll(tmpList));
	}
}
