package de.alexanderkose.util;

import static de.alexanderkose.util.TestUtils.assertEmpty;
import static de.alexanderkose.util.TestUtils.assertNotEmpty;
import static de.alexanderkose.util.TestUtils.newGreatestList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

public class StepCollectionGreatestTest {
	@Test
	public void testRemoveObject() {
		StepCollection<String> list = newGreatestList(5);
		assertFalse(list.remove("0"));

		list = newGreatestList(5);
		assertFalse(list.remove(null));

		list = newGreatestList(5);
		list.add("1");
		assertTrue(list.remove("1"));
		assertFalse(list.remove("1"));

		list = newGreatestList(5);
		list.add("1");
		list.add("2");

		assertTrue(list.remove("2"));
		assertTrue(list.remove("1"));

		list = newGreatestList(5);
		list.add("1");
		list.add("2");

		assertTrue(list.remove("1"));
		assertTrue(list.remove("2"));

		list = newGreatestList(5);
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

		list = newGreatestList(5);
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
	public void testRemoveIndex() {
		StepCollection<String> list = newGreatestList(5);
		list.add("1");
		String remove = list.remove(0);
		assertNotNull(remove);
		assertEquals("1", remove);

		list = newGreatestList(5);
		list.add("1");
		list.add("2");

		remove = list.remove(0);
		assertNotNull(remove);
		assertEquals("1", remove);
		remove = list.remove(0);
		assertNotNull(remove);
		assertEquals("2", remove);

		list = newGreatestList(5);
		list.add("1");
		list.add("2");

		remove = list.remove(1);
		assertNotNull(remove);
		assertEquals("2", remove);
		remove = list.remove(0);
		assertNotNull(remove);
		assertEquals("1", remove);

		list = newGreatestList(5);
		list.add("1");
		list.add("2");

		try {
			list.remove(2);
			fail();
		} catch (IndexOutOfBoundsException e) {
		}
	}

	@Test
	public void testAddObject() {
		StepCollection<String> list = newGreatestList(5);
		assertTrue(list.add("0"));

		list = newGreatestList(5);
		assertFalse(list.add(null));

		list = newGreatestList(5);
		assertTrue(list.add("1"));
		assertFalse(list.add("1"));

		list = newGreatestList(5);
		assertTrue(list.add("1"));
		assertTrue(list.add("2"));
		assertTrue(list.add("3"));
		assertTrue(list.add("4"));
		assertTrue(list.add("5"));
		assertTrue(list.add("6"));
		assertFalse(list.add("6"));
		assertFalse(list.add("1"));

		list = newGreatestList(5);
		assertTrue(list.add("6"));
		assertTrue(list.add("5"));
		assertTrue(list.add("4"));
		assertTrue(list.add("3"));
		assertTrue(list.add("2"));
		assertTrue(list.add("1"));

		list = newGreatestList(2);
		list.add("1");
		list.add("3");
		list.remove("1");
		list.add("4");
	}

	@Test
	public void testNextStep() {
		StepCollection<String> list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");

		assertEquals(2, list.size());

		Collection<String> nextStep = list.nextStep();
		assertNotEmpty(nextStep);
		assertEquals(2, nextStep.size());
		Iterator<String> i = nextStep.iterator();
		assertEquals("5", i.next());
		assertEquals("4", i.next());

		assertEquals(4, list.size());

		nextStep = list.nextStep();
		assertNotEmpty(nextStep);
		assertEquals(2, nextStep.size());
		i = nextStep.iterator();
		assertEquals("3", i.next());
		assertEquals("2", i.next());

		assertEquals(6, list.size());

		nextStep = list.nextStep();
		assertNotEmpty(nextStep);
		assertEquals(1, nextStep.size());
		i = nextStep.iterator();
		assertEquals("1", i.next());

		assertEquals(7, list.size());

		nextStep = list.nextStep();
		assertEmpty(nextStep);

		list = newGreatestList(2);
		nextStep = list.nextStep();
		assertEmpty(nextStep);

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep();
		list.remove("1");
		list.add("1");

		nextStep = list.nextStep();
		assertNotEmpty(nextStep);
		assertEquals(1, nextStep.size());
		i = nextStep.iterator();
		assertEquals("1", i.next());
	}

	@Test
	public void testNextStepParam() {
		StepCollection<String> list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");

		assertEquals(2, list.size());

		Collection<String> nextStep = list.nextStep(3);
		assertNotEmpty(nextStep);
		assertEquals(3, nextStep.size());
		Iterator<String> i = nextStep.iterator();
		assertEquals("5", i.next());
		assertEquals("4", i.next());
		assertEquals("3", i.next());

		assertEquals(5, list.size());

		nextStep = list.nextStep();
		assertNotEmpty(nextStep);
		assertEquals(2, nextStep.size());
		i = nextStep.iterator();
		assertEquals("2", i.next());
		assertEquals("1", i.next());

		assertEquals(7, list.size());

		nextStep = list.nextStep();
		assertEmpty(nextStep);

		list = newGreatestList(2);
		nextStep = list.nextStep();
		assertEmpty(nextStep);

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep(1);
		list.remove("1");
		list.add("1");

		nextStep = list.nextStep();
		assertEmpty(nextStep);
	}

	@Test
	public void testPrevStep() {
		StepCollection<String> list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");

		assertEquals(2, list.size());

		Collection<String> prevStep = list.prevStep();
		assertEmpty(prevStep);

		list.nextStep();

		prevStep = list.prevStep();
		assertNotEmpty(prevStep);
		assertEquals(2, prevStep.size());
		Iterator<String> i = prevStep.iterator();
		assertEquals("4", i.next());
		assertEquals("5", i.next());
	}

	@Test
	public void testPrevStepParam() {
		StepCollection<String> list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");

		assertEquals(2, list.size());

		Collection<String> prevStep = list.prevStep();
		assertEmpty(prevStep);

		list.nextStep();

		prevStep = list.prevStep(1);
		assertNotEmpty(prevStep);
		assertEquals(1, prevStep.size());
		Iterator<String> i = prevStep.iterator();
		assertEquals("4", i.next());
	}

	@Test
	public void testAddAll() {
		StepCollection<String> list = newGreatestList(5);
		assertFalse(list.addAll(null));

		ArrayList<String> tmpList = new ArrayList<>();

		list = newGreatestList(5);
		assertFalse(list.addAll(tmpList));

		tmpList.add("1");
		tmpList.add("5");

		list = newGreatestList(5);
		assertTrue(list.addAll(tmpList));
		assertFalse(list.addAll(tmpList));

		tmpList.add("2");
		tmpList.add("3");
		tmpList.add("4");

		list = newGreatestList(5);
		assertTrue(list.addAll(tmpList));

		tmpList.add("6");

		assertTrue(list.addAll(tmpList));
	}

	@Test
	public void testSet() {
		StepCollection<String> list = newGreatestList(5);
		assertFalse(list.set(null));

		ArrayList<String> tmpList = new ArrayList<>();

		list = newGreatestList(5);
		assertFalse(list.set(tmpList));

		list = newGreatestList(5);
		list.add("1");
		assertTrue(list.set(tmpList));
		assertEmpty(list);

		tmpList.add("1");
		tmpList.add("5");

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.nextStep();
		list.nextStep();
		assertTrue(list.set(tmpList));
		assertNotEmpty(list);
		assertEquals("1", list.get(0));
		assertEquals("5", list.get(1));

		list = newGreatestList(5);
		assertTrue(list.set(tmpList));
		assertNotEmpty(list);
		assertEquals("1", list.get(0));
		assertEquals("5", list.get(1));
		assertFalse(list.set(tmpList));

		tmpList.add("2");
		tmpList.add("8");
		tmpList.add("9");

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.nextStep();
		assertTrue(list.set(tmpList));
		assertEquals(4, list.size());
		assertEquals("2", list.get(0));
		assertEquals("5", list.get(1));
		assertEquals("8", list.get(2));
		assertEquals("9", list.get(3));

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		assertTrue(list.set(tmpList));
		assertEquals(2, list.size());
		assertEquals("8", list.get(0));
		assertEquals("9", list.get(1));
	}

	@Test
	public void testIteratorSorted() {
		StepCollection<String> list = newGreatestList(5);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");

		Iterator<String> iterator = list.iterator();

		assertTrue(iterator.hasNext());
		String next = iterator.next();
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
	public void testIteratorUnsorted() {
		StepCollection<String> list = newGreatestList(5);
		list.add("7");
		list.add("6");
		list.add("5");
		list.add("4");
		list.add("3");
		list.add("2");
		list.add("1");

		Iterator<String> iterator = list.iterator();

		assertTrue(iterator.hasNext());
		String next = iterator.next();
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
	public void testRemoveAll() {
		StepCollection<String> list = newGreatestList(5);
		assertFalse(list.removeAll(null));

		ArrayList<String> tmpList = new ArrayList<String>();

		list = newGreatestList(5);
		assertFalse(list.removeAll(tmpList));

		list = newGreatestList(5);
		ArrayList<Integer> intList = new ArrayList<Integer>();
		intList.add(5);
		assertFalse(list.removeAll(intList));

		tmpList.add("5");

		list = newGreatestList(5);
		assertFalse(list.removeAll(tmpList));

		list = newGreatestList(5);
		list.add("5");
		assertTrue(list.removeAll(tmpList));
	}

	@Test
	public void testToArray() {
		StepCollection<String> list = newGreatestList(2);
		list.add("3");
		list.add("1");
		list.add("2");

		Object[] array = list.toArray();
		assertNotNull(array);
		assertEquals(2, array.length);
		assertNotNull(array[0]);
		assertNotNull(array[1]);
		assertEquals("2", array[0]);
		assertEquals("3", array[1]);
	}

	@Test
	public void testToArrayType() {
		StepCollection<String> list = newGreatestList(2);
		list.add("3");
		list.add("1");
		list.add("2");

		String[] array = list.toArray(new String[] {});
		assertNotNull(array);
		assertEquals(2, array.length);
		assertNotNull(array[0]);
		assertNotNull(array[1]);
		assertEquals("2", array[0]);
		assertEquals("3", array[1]);
	}

	@Test
	public void testIsStepable() {
		StepCollection<String> list = newGreatestList(2);
		assertFalse(list.isStepable());

		list = newGreatestList(2);
		list.add("1");
		assertFalse(list.isStepable());

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		assertFalse(list.isStepable());

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		assertTrue(list.isStepable());

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep();
		assertFalse(list.isStepable());

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep();
		list.remove("3");
		list.add("3");
		assertTrue(list.isStepable());
	}

	@Test
	public void testIsPrevStepable() {
		StepCollection<String> list = newGreatestList(2);
		assertFalse(list.isPrevStepable());

		list = newGreatestList(2);
		list.add("1");
		assertFalse(list.isPrevStepable());

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		assertFalse(list.isPrevStepable());

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		assertFalse(list.isPrevStepable());

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep();
		assertTrue(list.isPrevStepable());

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep();
		list.remove("3");
		assertFalse(list.isPrevStepable());

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep();
		list.remove("3");
		list.add("3");
		assertFalse(list.isPrevStepable());
	}

	@Test
	public void testRetainAll() {
		StepCollection<String> list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");

		assertTrue(list.retainAll(null));
		assertTrue(list.isEmpty());

		ArrayList<String> tmpList = new ArrayList<>();
		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");

		assertTrue(list.retainAll(tmpList));
		assertTrue(list.isEmpty());

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");

		tmpList.add("3");
		tmpList.add("4");

		assertTrue(list.retainAll(tmpList));
		assertFalse(list.contains("1"));
		assertFalse(list.contains("2"));
		assertTrue(list.contains("3"));
		assertTrue(list.contains("4"));

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");

		tmpList = new ArrayList<>();
		tmpList.add("1");
		tmpList.add("2");

		assertTrue(list.retainAll(tmpList));
		assertTrue(list.contains("1"));
		assertTrue(list.contains("2"));
		assertFalse(list.contains("3"));
		assertFalse(list.contains("4"));

		list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");

		tmpList = new ArrayList<>();
		tmpList.add("2");
		tmpList.add("3");

		assertTrue(list.retainAll(tmpList));
		assertFalse(list.contains("1"));
		assertTrue(list.contains("2"));
		assertTrue(list.contains("3"));
		assertFalse(list.contains("4"));
	}

	@Test
	public void testGet() {
		StepCollection<String> list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");

		assertEquals("2", list.get(0));
		assertEquals("3", list.get(1));

		try {
			assertEquals("4", list.get(2));
		} catch (IndexOutOfBoundsException e) {
		}
	}

	@Test
	public void testReorder() {
		StepCollection<ReorderEntity> list = newGreatestList(2);
		ReorderEntity r1 = new ReorderEntity("1", "1");
		ReorderEntity r2 = new ReorderEntity("2", "2");
		ReorderEntity r3 = new ReorderEntity("3", "3");
		ReorderEntity r4 = new ReorderEntity("4", "4");
		ReorderEntity r5 = new ReorderEntity("5", "5");

		list.add(r1);
		list.add(r2);
		list.add(r3);
		list.add(r4);
		list.add(r5);
		list.nextStep();

		Iterator<ReorderEntity> i = list.iterator();
		assertEquals(r2, i.next());
		assertEquals(r3, i.next());
		assertEquals(r4, i.next());
		assertEquals(r5, i.next());

		assertFalse(list.reorder());

		r2.setSort("3");
		r3.setSort("2");
		assertTrue(list.reorder());

		i = list.iterator();
		assertEquals(r3, i.next());
		assertEquals(r2, i.next());
		assertEquals(r4, i.next());
		assertEquals(r5, i.next());

		r2.setSort("0");
		assertTrue(list.reorder());

		i = list.iterator();
		assertEquals(r1, i.next());
		assertEquals(r3, i.next());
		assertEquals(r4, i.next());
		assertEquals(r5, i.next());
	}

	@Test
	public void testAutoResizeWindowForNew() {
		StepCollection<String> list = newGreatestList(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.setAutoResizeWindowForNew(true);
		list.add("4");
		assertEquals(3, list.size());
		assertEquals("2", list.get(0));
		assertEquals("3", list.get(1));
		assertEquals("4", list.get(2));
	}
}
