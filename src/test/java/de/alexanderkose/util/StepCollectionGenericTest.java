package de.alexanderkose.util;

import static de.alexanderkose.util.TestUtils.newSmallestList;
import static de.alexanderkose.util.TestUtils.newGreatestList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

public class StepCollectionGenericTest {
	@Test
	public void testIsEmpty() {
		StepCollection<String> list = newSmallestList(5);
		assertTrue(list.isEmpty());

		list = newSmallestList(5);
		assertTrue(list.add("1"));
		assertFalse(list.isEmpty());

		list = newSmallestList(5);
		assertTrue(list.add("1"));
		assertTrue(list.add("2"));
		assertTrue(list.add("3"));
		assertTrue(list.add("4"));
		assertTrue(list.add("5"));
		assertTrue(list.add("6"));
		assertFalse(list.isEmpty());
	}

	@Test
	public void testClear() {
		StepCollection<String> list = newSmallestList(5);
		list.add("0");
		assertFalse(list.isEmpty());

		list.clear();

		assertTrue(list.isEmpty());

		list = newSmallestList(5);
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
	public void testContains() {
		StepCollection<String> list = newSmallestList(5);
		assertFalse(list.contains("0"));

		list = newSmallestList(5);
		list.add("1");
		assertTrue(list.contains("1"));

		list = newSmallestList(5);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");

		assertTrue(list.contains("1"));
		assertTrue(list.contains("2"));
		assertTrue(list.contains("3"));
		assertTrue(list.contains("4"));
		assertTrue(list.contains("5"));
		assertTrue(list.contains("6"));
		assertTrue(list.contains("7"));
	}

	@Test
	public void testHashCode() {
		StepCollection<String> list1 = newSmallestList(2);
		StepCollection<String> list2 = newSmallestList(2);

		assertTrue(list1.hashCode() == list2.hashCode());

		list1 = newSmallestList(2);
		list2 = newSmallestList(2);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list2.add("3");
		list2.add("2");
		list2.add("1");

		assertTrue(list1.hashCode() == list2.hashCode());

		list1 = newSmallestList(2);
		list2 = newSmallestList(2);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list2.add("3");
		list2.add("2");
		list2.add("1");
		list2.add("4");

		assertFalse(list1.hashCode() == list2.hashCode());

		list1 = newSmallestList(2);
		list1.setAutoResizeWindowForNew(true);
		list2 = newSmallestList(2);
		list2.setAutoResizeWindowForNew(true);

		assertTrue(list1.hashCode() == list2.hashCode());

		list1 = newSmallestList(2);
		list1.setAutoResizeWindowForNew(true);
		list2 = newSmallestList(2);

		assertFalse(list1.hashCode() == list2.hashCode());
	}

	@Test
	public void testEquals() {
		StepCollection<String> list1 = newSmallestList(2);
		assertFalse(list1.equals(null));

		list1 = newSmallestList(2);
		assertFalse(list1.equals(new Object()));

		list1 = newSmallestList(2);
		assertTrue(list1.equals(list1));

		list1 = newSmallestList(2);
		StepCollection<String> list2 = newSmallestList(2);
		assertTrue(list1.equals(list2));

		list1 = newSmallestList(2);
		list2 = newSmallestList(3);
		assertFalse(list1.equals(list2));

		list1 = newSmallestList(2);
		list2 = newSmallestList(2);
		list2.setAutoResizeWindowForNew(true);
		assertFalse(list1.equals(list2));

		list1 = newSmallestList(2);
		list2 = newGreatestList(2);
		assertFalse(list1.equals(list2));

		list1 = newSmallestList(2);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list2 = newSmallestList(2);
		list2.add("1");
		list2.add("2");
		list2.add("3");
		assertTrue(list1.equals(list2));

		list1 = newSmallestList(2);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list1.nextStep();
		list2 = newSmallestList(2);
		list2.add("1");
		list2.add("2");
		list2.add("3");
		assertFalse(list1.equals(list2));

		list1 = newSmallestList(2);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list2 = newSmallestList(2);
		list2.add("1");
		list2.add("2");
		assertFalse(list1.equals(list2));

		list1 = newSmallestList(4);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list1.add("4");
		list2 = newSmallestList(2);
		list2.add("1");
		list2.add("2");
		list2.add("3");
		list2.add("4");
		list2.nextStep();
		assertFalse(list1.equals(list2));
	}

	@Test
	public void testContainsAll() {
		StepCollection<String> list = newSmallestList(2);

		assertFalse(list.containsAll(null));

		list = newSmallestList(2);
		list.add("1");
		ArrayList<String> tmpList = new ArrayList<String>();
		tmpList.add("1");

		assertTrue(list.containsAll(tmpList));
	}

	@Test
	public void testClone() {
		StepCollection<String> list = newSmallestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.nextStep();
		int oHash = list.hashCode();

		@SuppressWarnings("unchecked")
		StepCollection<String> clone = (StepCollection<String>) list.clone();

		assertTrue(oHash == clone.hashCode());

		clone.add("6");

		assertFalse(oHash == clone.hashCode());
	}

	@Test
	public void testToString() {
		StepCollection<String> list = newSmallestList(2);
		list.add("1");
		list.add("2");

		assertEquals(
				"StepCollection [currentWindow=[1, 2], outOfWindow=[], steps=2, size=2, autoResizeWindowForNew=false]",
				list.toString());

		list = newGreatestList(2);
		list.add("1");
		list.add("2");

		assertEquals(
				"StepCollection [currentWindow=[1, 2], outOfWindow=[], steps=2, size=2, autoResizeWindowForNew=false, windowOnEnd=true]",
				list.toString());

		list = newSmallestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");

		assertEquals(
				"StepCollection [currentWindow=[1, 2], outOfWindow=[3, 4, 5], steps=2, size=2, autoResizeWindowForNew=false]",
				list.toString());

		list = newSmallestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.nextStep();

		assertEquals(
				"StepCollection [currentWindow=[1, 2, 3, 4], outOfWindow=[5], steps=2, size=4, autoResizeWindowForNew=false]",
				list.toString());
	}

	@Test
	public void testIteratorFull() {
		StepCollection<String> list = newGreatestList(5);
		list.add("7");
		list.add("6");
		list.add("5");
		list.add("4");
		list.add("3");
		list.add("2");
		list.add("1");

		list.nextStep();

		Iterator<String> iterator = list.iterator();

		assertTrue(iterator.hasNext());
		String next = iterator.next();
		assertNotNull(next);
		assertEquals("1", next);

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertNotNull(next);
		assertEquals("2", next);

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertNotNull(next);
		assertEquals("3", next);

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertNotNull(next);
		assertEquals("4", next);

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertNotNull(next);
		assertEquals("5", next);

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertNotNull(next);
		assertEquals("6", next);

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertNotNull(next);
		assertEquals("7", next);

		assertFalse(iterator.hasNext());
		try {
			next = iterator.next();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testIteratorHasNextModify() {
		StepCollection<String> list = newSmallestList(5);
		list.add("7");
		list.add("6");
		list.add("5");
		list.add("4");
		list.add("3");
		list.add("2");
		list.add("1");

		Iterator<String> iterator = list.iterator();

		list.nextStep();

		try {
			assertFalse(iterator.hasNext());
			fail();
		} catch (ConcurrentModificationException e) {
		}
	}

	@Test
	public void testIteratorRemove() {
		StepCollection<String> list = newGreatestList(5);
		list.add("7");
		list.add("6");
		list.add("5");
		list.add("4");
		list.add("3");
		list.add("2");
		list.add("1");

		list.nextStep();

		Iterator<String> i = list.iterator();
		i.next();
		i.remove();
	}

	@Test
	public void testSetSteps() {
		StepCollection<String> list = newSmallestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("8");
		list.add("9");

		list.nextStep();

		assertEquals(4, list.size());

		list.setSteps(1);

		list.nextStep();

		assertEquals(5, list.size());

		list.setSteps(3);

		list.nextStep();

		assertEquals(8, list.size());
	}

	@Test
	public void testIteratorNextModify() {
		StepCollection<String> list = newGreatestList(5);
		list.add("7");
		list.add("6");
		list.add("5");
		list.add("4");
		list.add("3");
		list.add("2");
		list.add("1");

		Iterator<String> i1 = list.iterator();
		Iterator<String> i2 = list.iterator();

		i1.next();
		i2.next();
		list.remove("1");
		i1.next();
		list.remove("7");

		try {
			i1.next();
			fail();
		} catch (ConcurrentModificationException e) {
		}
	}

	@Test
	public void testIteratorRemoveModify() {
		StepCollection<String> list = newGreatestList(5);
		list.add("7");
		list.add("6");
		list.add("5");
		list.add("4");
		list.add("3");
		list.add("2");
		list.add("1");

		Iterator<String> i1 = list.iterator();
		Iterator<String> i2 = list.iterator();

		i1.next();
		i2.next();
		list.remove("1");
		i1.next();
		list.remove("7");

		try {
			i1.remove();
			fail();
		} catch (ConcurrentModificationException e) {
		}
	}

	@Test
	public void testIteratorRemoveWrongState() {
		StepCollection<String> list = newSmallestList(2);

		Iterator<String> i = list.iterator();

		try {
			i.remove();
			fail();
		} catch (IllegalStateException e) {
		}
	}

	@Test
	public void testEasyConstruct() {
		StepCollection<String> list = new StepCollection<>();
		list.add("6");
		list.add("5");
		list.add("4");
		list.add("3");
		list.add("2");
		list.add("1");
		list.add("7");

		assertEquals(6, list.size());
	}
}
