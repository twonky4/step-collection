package de.alexanderkose.util;

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

public class StepCollectionTest {
	@Test
	public void testIsEmpty() {
		StepCollection<String> list = new StepCollection<>(5);
		assertTrue(list.isEmpty());

		list = new StepCollection<>(5);
		assertTrue(list.add("1"));
		assertFalse(list.isEmpty());

		list = new StepCollection<>(5);
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
		StepCollection<String> list = new StepCollection<>(5);
		assertFalse(list.remove("0"));

		list = new StepCollection<>(5);
		assertFalse(list.remove(null));

		list = new StepCollection<>(5);
		list.add("1");
		assertTrue(list.remove("1"));
		assertFalse(list.remove("1"));

		list = new StepCollection<>(5);
		list.add("1");
		list.add("2");

		assertTrue(list.remove("2"));
		assertTrue(list.remove("1"));

		list = new StepCollection<>(5);
		list.add("1");
		list.add("2");

		assertTrue(list.remove("1"));
		assertTrue(list.remove("2"));

		list = new StepCollection<>(5);
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

		list = new StepCollection<>(5);
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
		StepCollection<String> list = new StepCollection<>(5);
		assertTrue(list.add("0"));

		list = new StepCollection<>(5);
		assertFalse(list.add(null));

		list = new StepCollection<>(5);
		assertTrue(list.add("1"));
		assertFalse(list.add("1"));

		list = new StepCollection<>(5);
		assertTrue(list.add("1"));
		assertTrue(list.add("2"));
		assertTrue(list.add("3"));
		assertTrue(list.add("4"));
		assertTrue(list.add("5"));
		assertTrue(list.add("6"));
		assertFalse(list.add("6"));
		assertFalse(list.add("1"));

		list = new StepCollection<>(5);
		assertTrue(list.add("6"));
		assertTrue(list.add("5"));
		assertTrue(list.add("4"));
		assertTrue(list.add("3"));
		assertTrue(list.add("2"));
		assertTrue(list.add("1"));

		list = new StepCollection<>(2);
		list.add("1");
		list.add("3");
		list.remove("1");
		list.add("4");
	}

	@Test
	public void testClear() {
		StepCollection<String> list = new StepCollection<>(5);
		list.add("0");
		assertFalse(list.isEmpty());

		list.clear();

		assertTrue(list.isEmpty());

		list = new StepCollection<>(5);
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
		StepCollection<String> list = new StepCollection<>(2);
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
		assertEquals("3", i.next());
		assertEquals("4", i.next());

		assertEquals(4, list.size());

		nextStep = list.nextStep();
		assertNotEmpty(nextStep);
		assertEquals(2, nextStep.size());
		i = nextStep.iterator();
		assertEquals("5", i.next());
		assertEquals("6", i.next());

		assertEquals(6, list.size());

		nextStep = list.nextStep();
		assertNotEmpty(nextStep);
		assertEquals(1, nextStep.size());
		i = nextStep.iterator();
		assertEquals("7", i.next());

		assertEquals(7, list.size());

		nextStep = list.nextStep();
		assertEmpty(nextStep);

		list = new StepCollection<>(2);
		nextStep = list.nextStep();
		assertEmpty(nextStep);

		list = new StepCollection<>(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep();
		list.remove("3");
		list.add("3");

		nextStep = list.nextStep();
		assertNotEmpty(nextStep);
		assertEquals(1, nextStep.size());
		i = nextStep.iterator();
		assertEquals("3", i.next());
	}

	@Test
	public void testPrevStep() {
		StepCollection<String> list = new StepCollection<>(2);
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
		assertEquals("3", i.next());
	}

	@Test
	public void testContains() {
		StepCollection<String> list = new StepCollection<>(5);
		assertFalse(list.contains("0"));

		list = new StepCollection<>(5);
		list.add("1");
		assertTrue(list.contains("1"));

		list = new StepCollection<>(5);
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
		assertFalse(list.contains("6"));
		assertFalse(list.contains("7"));
	}

	@Test
	public void testAddAll() {
		StepCollection<String> list = new StepCollection<>(5);
		assertFalse(list.addAll(null));

		ArrayList<String> tmpList = new ArrayList<>();

		list = new StepCollection<>(5);
		assertFalse(list.addAll(tmpList));

		tmpList.add("1");
		tmpList.add("5");

		list = new StepCollection<>(5);
		assertTrue(list.addAll(tmpList));
		assertFalse(list.addAll(tmpList));

		tmpList.add("2");
		tmpList.add("3");
		tmpList.add("4");

		list = new StepCollection<>(5);
		assertTrue(list.addAll(tmpList));

		tmpList.add("6");

		assertTrue(list.addAll(tmpList));
	}

	@Test
	public void testIterator1() {
		StepCollection<String> list = new StepCollection<>(5);
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

		assertFalse(iterator.hasNext());
		try {
			next = iterator.next();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testIterator2() {
		StepCollection<String> list = new StepCollection<>(5);
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

		assertFalse(iterator.hasNext());
		try {
			next = iterator.next();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testIterator3() {
		StepCollection<String> list = new StepCollection<>(5);
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

		list.nextStep();

		assertFalse(iterator.hasNext());
		try {
			next = iterator.next();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testIterator4() {
		StepCollection<String> list = new StepCollection<>(5);
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
	public void testRemoveAll() {
		StepCollection<String> list = new StepCollection<>(5);
		assertFalse(list.removeAll(null));

		ArrayList<String> tmpList = new ArrayList<String>();

		list = new StepCollection<>(5);
		assertFalse(list.removeAll(tmpList));

		list = new StepCollection<>(5);
		ArrayList<Integer> intList = new ArrayList<Integer>();
		intList.add(5);
		assertFalse(list.removeAll(intList));

		tmpList.add("5");

		list = new StepCollection<>(5);
		assertFalse(list.removeAll(tmpList));

		list = new StepCollection<>(5);
		list.add("5");
		assertTrue(list.removeAll(tmpList));
	}

	@Test
	public void testHashCode() {
		StepCollection<String> list1 = new StepCollection<>(2);
		StepCollection<String> list2 = new StepCollection<>(2);

		assertTrue(list1.hashCode() == list2.hashCode());

		list1 = new StepCollection<>(2);
		list2 = new StepCollection<>(2);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list2.add("3");
		list2.add("2");
		list2.add("1");

		assertTrue(list1.hashCode() == list2.hashCode());

		list1 = new StepCollection<>(2);
		list2 = new StepCollection<>(2);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list2.add("3");
		list2.add("2");
		list2.add("1");
		list2.add("4");

		assertFalse(list1.hashCode() == list2.hashCode());
	}

	@Test
	public void testToArray() {
		StepCollection<String> list = new StepCollection<>(2);
		list.add("3");
		list.add("1");
		list.add("2");

		Object[] array = list.toArray();
		assertNotNull(array);
		assertEquals(2, array.length);
		assertNotNull(array[0]);
		assertNotNull(array[1]);
		assertEquals("1", array[0]);
		assertEquals("2", array[1]);
	}

	@Test
	public void testToArrayType() {
		StepCollection<String> list = new StepCollection<>(2);
		list.add("3");
		list.add("1");
		list.add("2");

		String[] array = list.toArray(new String[] {});
		assertNotNull(array);
		assertEquals(2, array.length);
		assertNotNull(array[0]);
		assertNotNull(array[1]);
		assertEquals("1", array[0]);
		assertEquals("2", array[1]);
	}

	@Test
	public void testEuqals() {
		StepCollection<String> list1 = new StepCollection<>(2);
		assertFalse(list1.equals(null));

		list1 = new StepCollection<>(2);
		assertFalse(list1.equals(new Object()));

		list1 = new StepCollection<>(2);
		assertTrue(list1.equals(list1));

		list1 = new StepCollection<>(2);
		StepCollection<String> list2 = new StepCollection<>(2);
		assertTrue(list1.equals(list2));

		list1 = new StepCollection<>(2);
		list2 = new StepCollection<>(3);
		assertFalse(list1.equals(list2));

		list1 = new StepCollection<>(2);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list2 = new StepCollection<>(2);
		list2.add("1");
		list2.add("2");
		list2.add("3");
		assertTrue(list1.equals(list2));

		list1 = new StepCollection<>(2);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list1.nextStep();
		list2 = new StepCollection<>(2);
		list2.add("1");
		list2.add("2");
		list2.add("3");
		assertFalse(list1.equals(list2));

		list1 = new StepCollection<>(2);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list2 = new StepCollection<>(2);
		list2.add("1");
		list2.add("2");
		assertFalse(list1.equals(list2));

		list1 = new StepCollection<>(4);
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list1.add("4");
		list2 = new StepCollection<>(2);
		list2.add("1");
		list2.add("2");
		list2.add("3");
		list2.add("4");
		list2.nextStep();
		assertFalse(list1.equals(list2));
	}

	@Test
	public void testIsStepable() {
		StepCollection<String> list = new StepCollection<>(2);
		assertFalse(list.isStepable());

		list = new StepCollection<>(2);
		list.add("1");
		assertFalse(list.isStepable());

		list = new StepCollection<>(2);
		list.add("1");
		list.add("2");
		assertFalse(list.isStepable());

		list = new StepCollection<>(2);
		list.add("1");
		list.add("2");
		list.add("3");
		assertTrue(list.isStepable());

		list = new StepCollection<>(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep();
		assertFalse(list.isStepable());

		list = new StepCollection<>(2);
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
		StepCollection<String> list = new StepCollection<>(2);
		assertFalse(list.isPrevStepable());

		list = new StepCollection<>(2);
		list.add("1");
		assertFalse(list.isPrevStepable());

		list = new StepCollection<>(2);
		list.add("1");
		list.add("2");
		assertFalse(list.isPrevStepable());

		list = new StepCollection<>(2);
		list.add("1");
		list.add("2");
		list.add("3");
		assertFalse(list.isPrevStepable());

		list = new StepCollection<>(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep();
		assertTrue(list.isPrevStepable());

		list = new StepCollection<>(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep();
		list.remove("3");
		assertFalse(list.isPrevStepable());

		list = new StepCollection<>(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.nextStep();
		list.remove("3");
		list.add("3");
		assertFalse(list.isPrevStepable());
	}

	@Test
	public void testContainsAll() {
		StepCollection<String> list = new StepCollection<>(2);

		assertFalse(list.containsAll(null));

		list = new StepCollection<>(2);
		list.add("1");
		ArrayList<String> tmpList = new ArrayList<String>();
		tmpList.add("1");

		assertTrue(list.containsAll(tmpList));
	}

	@Test
	public void testRetainAll() {
		StepCollection<String> list = new StepCollection<>(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");

		assertTrue(list.retainAll(null));
		assertTrue(list.isEmpty());

		ArrayList<String> tmpList = new ArrayList<>();
		list = new StepCollection<>(2);
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");

		assertTrue(list.retainAll(tmpList));
		assertTrue(list.isEmpty());

		list = new StepCollection<>(2);
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

		list = new StepCollection<>(2);
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

		list = new StepCollection<>(2);
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
	public void testReorder() {
		StepCollection<ReorderEntity> list = new StepCollection<>(2);
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
		assertEquals(r1, i.next());
		assertEquals(r2, i.next());
		assertEquals(r3, i.next());
		assertEquals(r4, i.next());

		assertFalse(list.reorder());

		r2.setSort("3");
		r3.setSort("2");
		assertTrue(list.reorder());

		i = list.iterator();
		assertEquals(r1, i.next());
		assertEquals(r3, i.next());
		assertEquals(r2, i.next());
		assertEquals(r4, i.next());

		r2.setSort("6");
		assertTrue(list.reorder());

		i = list.iterator();
		assertEquals(r1, i.next());
		assertEquals(r3, i.next());
		assertEquals(r4, i.next());
		assertEquals(r5, i.next());
	}

	private class ReorderEntity implements Comparable<ReorderEntity> {
		private final String value;
		private String sort;

		public ReorderEntity(String value, String sort) {
			this.value = value;
			this.sort = sort;
		}

		public void setSort(String sort) {
			this.sort = sort;
		}

		@Override
		public int compareTo(ReorderEntity o) {
			return sort.compareTo(o.sort);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ReorderEntity other = (ReorderEntity) obj;
			if (value == null) {
				if (other.value != null) {
					return false;
				}
			} else if (!value.equals(other.value)) {
				return false;
			}
			return true;
		}
	}

	private static void assertNotEmpty(Collection<String> nextStep) {
		assertNotNull(nextStep);
		assertFalse(nextStep.isEmpty());
	}

	private static void assertEmpty(Collection<String> nextStep) {
		assertNotNull(nextStep);
		assertTrue(nextStep.isEmpty());
	}
}
