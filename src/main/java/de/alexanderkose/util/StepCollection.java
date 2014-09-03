package de.alexanderkose.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

public class StepCollection<T extends Comparable<T>> implements Cloneable,
		Set<T> {
	private final Object lock = new Object();
	private TreeSet<T> current;
	private TreeSet<T> prev;
	private final int steps;

	private int size;
	private int modCount = 0;

	private T maxCurrent;
	private T minCurrent;

	public StepCollection(int size) {
		current = new TreeSet<>();
		prev = new TreeSet<>();

		this.size = size;
		steps = size;
	}

	@Override
	public boolean add(T object) {
		if (object == null) {
			return false;
		}

		synchronized (lock) {
			if (current.contains(object) || prev.contains(object)) {
				return false;
			}

			// first element
			if (maxCurrent == null) {
				maxCurrent = object;
				minCurrent = object;

				current.add(object);
			}
			// second element, array full
			else if (size() >= size) {
				if (object.compareTo(minCurrent) > 0) {
					prev.add(object);
				} else {
					current.remove(minCurrent);
					prev.add(minCurrent);
					minCurrent = null;
					current.add(object);

					if (object.compareTo(maxCurrent) < 0) {
						maxCurrent = object;
					}
					refreshMin();
				}
			}
			// second element, array not full
			else {
				if (object.compareTo(maxCurrent) < 0) {
					maxCurrent = object;
				} else if (object.compareTo(minCurrent) > 0) {
					minCurrent = object;
				}

				current.add(object);

			}

			modCount++;
		}

		return true;
	}

	@Override
	public void clear() {
		synchronized (lock) {
			size = steps;
			maxCurrent = null;
			minCurrent = null;
			current.clear();
			prev.clear();
			modCount++;
		}
	}

	public Collection<T> nextStep() {
		Collection<T> list = new ArrayList<>();
		synchronized (lock) {
			if (!isStepable()) {
				return list;
			}

			if (prev.size() <= steps) {
				current.addAll(prev);
				list.addAll(prev);
				prev.clear();
				refreshMin();
			} else {
				for (int s = 0; s < steps; s++) {
					minCurrent = getLastPrev();
					current.add(minCurrent);
					list.add(minCurrent);
					prev.remove(minCurrent);
				}
			}

			size += steps;
			modCount++;
		}
		return list;
	}

	public Collection<T> prevStep() {
		Collection<T> list = new ArrayList<>();

		synchronized (lock) {
			if (!isPrevStepable()) {
				return list;
			}

			for (int i = 0; i < steps; i++) {
				T last = current.last();
				current.remove(last);
				list.add(last);
				prev.add(last);
			}

			size -= steps;
			modCount++;

		}
		return list;
	}

	private void refreshMax() {
		maxCurrent = null;
		if (!current.isEmpty()) {
			maxCurrent = current.first();
		}
	}

	private void refreshMin() {
		minCurrent = null;
		if (!current.isEmpty()) {
			minCurrent = current.last();
		}
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		if (collection == null || collection.isEmpty()) {
			return false;
		}

		boolean added = false;
		synchronized (lock) {
			for (T object : collection) {
				boolean oneAdded = add(object);
				if (oneAdded) {
					added = true;
				}
			}
		}
		return added;
	}

	@Override
	public int size() {
		int size;
		synchronized (lock) {
			size = current.size();
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		boolean empty;
		synchronized (lock) {
			empty = current.isEmpty();
		}
		return empty;
	}

	public boolean isStepable() {
		boolean stepable;
		synchronized (lock) {
			stepable = !prev.isEmpty();
		}
		return stepable;
	}

	public boolean isPrevStepable() {
		boolean prevStepable;
		synchronized (lock) {
			prevStepable = size > steps;
		}
		return prevStepable;
	}

	@Override
	public boolean contains(Object object) {
		boolean contains;
		synchronized (lock) {
			contains = current.contains(object) || prev.contains(object);
		}
		return contains;
	}

	private T getLastPrev() {
		if (prev.isEmpty()) {
			return null;
		}
		return prev.first();
	}

	@Override
	public boolean remove(Object object) {
		if (object == null) {
			return false;
		}

		synchronized (lock) {
			// remove from prev
			if (prev.contains(object)) {
				prev.remove(object);
				modCount++;
				return true;
			} else
			// remove from current
			if (current.contains(object)) {
				current.remove(object);
				minCurrent = getLastPrev();
				if (minCurrent != null) {
					current.add(minCurrent);
					prev.remove(minCurrent);
				} else {
					refreshMin();
				}

				if (object == maxCurrent) {
					refreshMax();
				}

				// reset size
				if (current.size() == size - steps) {
					size -= steps;
				}

				modCount++;
				return true;
			}
		}
		return false;
	}

	@Override
	public Object[] toArray() {
		Object[] array;
		synchronized (lock) {
			array = current.toArray();
		}
		return array;
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] contents) {
		T[] array;
		synchronized (lock) {
			array = current.toArray(contents);
		}
		return array;
	}

	@Override
	public Iterator<T> iterator() {
		Itr itr;
		synchronized (lock) {
			itr = new Itr();
		}
		return itr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		synchronized (lock) {
			result = prime * result + getTreeSetHashCode(current);
			result = prime * result + getTreeSetHashCode(prev);
			result = prime * result + size;
			result = prime * result + steps;
		}
		return result;
	}

	private int getTreeSetHashCode(TreeSet<T> set) {
		Iterator<T> it = set.iterator();
		if (!it.hasNext()) {
			return 0;
		}

		StringBuilder sb = new StringBuilder();
		for (;;) {
			T e = it.next();
			sb.append(String.valueOf(e.hashCode()));
			if (!it.hasNext()) {
				return sb.toString().hashCode();
			}
			sb.append(',');
		}
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
		@SuppressWarnings("rawtypes")
		StepCollection other = (StepCollection) obj;

		synchronized (lock) {
			synchronized (other.lock) {
				if (!current.equals(other.current)) {
					return false;
				}
				if (!prev.equals(other.prev)) {
					return false;
				}
				if (size != other.size) {
					return false;
				}
				if (steps != other.steps) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (c == null) {
			return false;
		}

		boolean containsAll;
		TreeSet<Object> set = new TreeSet<>();
		synchronized (lock) {
			set.addAll(current);
			set.addAll(prev);
			containsAll = set.containsAll(c);
		}
		return containsAll;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed;
		synchronized (lock) {
			if (c == null) {
				c = new ArrayList<>();
			}
			changed = prev.retainAll(c);
			if (changed) {
				modCount++;
			}

			Iterator<T> i = iterator();
			while (i.hasNext()) {
				T next = i.next();
				if (!c.contains(next)) {
					i.remove();
					changed = true;
				}
			}
		}
		return changed;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (c == null || c.isEmpty()) {
			return false;
		}

		boolean removed = false;
		synchronized (lock) {
			for (Object object : c) {
				@SuppressWarnings("unchecked")
				boolean oneRemoved = remove((T) object);
				if (oneRemoved) {
					removed = true;
				}
			}
		}
		return removed;
	}

	/**
	 * Refresh inserted element order by compare all objects again.
	 * 
	 * @return true, if order has changed
	 */
	public boolean reorder() {
		boolean changed;
		synchronized (lock) {
			int oldHash = hashCode();
			ArrayList<T> backUpList = new ArrayList<T>();
			backUpList.addAll(current);
			backUpList.addAll(prev);
			int backUpSize = size;

			clear();

			size = backUpSize;
			for (T o : backUpList) {
				add(o);
			}

			changed = oldHash != hashCode();
		}
		return changed;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		StepCollection<T> o;
		synchronized (lock) {
			o = new StepCollection<>(steps);
			o.current = (TreeSet<T>) current.clone();
			o.prev = (TreeSet<T>) prev.clone();
			o.maxCurrent = maxCurrent;
			o.minCurrent = minCurrent;
			o.size = size;

		}
		return o;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("StepCollection [currentWindow=");
		synchronized (lock) {
			sb.append(current);
			sb.append(", outOfWindow=");
			sb.append(prev);
			sb.append(", steps=");
			sb.append(steps);
			sb.append(", size=");
			sb.append(size);
		}
		sb.append(']');

		return sb.toString();
	}

	private class Itr implements Iterator<T> {
		private int expectedModCount = modCount;
		private int cursor = -1;
		private int lastRet = -1;

		public boolean hasNext() {
			boolean hasNext;
			synchronized (this) {
				checkForComodification();

				ArrayList<T> array = getArray();

				hasNext = cursor + 1 < array.size();
			}
			return hasNext;
		}

		public T next() {
			T next;
			synchronized (this) {
				cursor++;
				ArrayList<T> array = getArray();

				if (cursor >= array.size()) {
					throw new NoSuchElementException();
				}

				checkForComodification();

				lastRet = cursor;

				next = array.get(cursor);
			}
			return next;
		}

		public void remove() {
			synchronized (this) {
				if (lastRet == -1) {
					throw new IllegalStateException();
				}

				checkForComodification();

				ArrayList<T> array = getArray();
				T del = array.get(lastRet);
				StepCollection.this.remove(del);
				expectedModCount++;
				cursor--;
			}
		}

		private ArrayList<T> getArray() {
			return new ArrayList<T>(current);
		}

		final void checkForComodification() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
		}
	}
}
