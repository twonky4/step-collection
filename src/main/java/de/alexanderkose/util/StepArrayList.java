package de.alexanderkose.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class StepArrayList<T extends Comparable<T>> extends AbstractList<T> {
	private final ArrayList<T> current;
	private final ArrayList<T> prev;
	private int size;
	private final int steps;

	private T maxCurrent;
	private T minCurrent;

	public StepArrayList(int size) {
		current = new ArrayList<>(size);
		prev = new ArrayList<>();

		this.size = size;
		steps = size;
	}

	@Override
	public boolean add(T object) {
		if (object == null) {
			return false;
		}

		// first element
		if (maxCurrent == null) {
			maxCurrent = object;
			minCurrent = object;
			return current.add(object);
		}
		// second element, array full
		else if (size() >= size) {
			if (object.compareTo(minCurrent) <= 0) {
				return prev.add(object);
			} else {
				current.remove(minCurrent);
				minCurrent = null;
				boolean added = current.add(object);
				if (object.compareTo(maxCurrent) >= 0) {
					maxCurrent = object;
				}
				refreshMin();

				return added;
			}
		}
		// second element, array not full
		else {
			if (object.compareTo(maxCurrent) >= 0) {
				maxCurrent = object;
			} else if (object.compareTo(minCurrent) < 0) {
				minCurrent = object;
			}

			return current.add(object);
		}
	}

	@Override
	public void clear() {
		size = steps;
		maxCurrent = null;
		minCurrent = null;
		current.clear();
		prev.clear();
	}

	public void nextStep() {
		if (prev.isEmpty()) {
			return;
		}
		if (prev.size() <= steps) {
			addAll(prev);
			prev.clear();
			size += steps;
			refreshMin();
		} else {
			for (int s = 0; s < steps && !prev.isEmpty(); s++) {
				T newLast = getLastPrev();
				if (newLast != null) {
					current.add(newLast);
				}
				minCurrent = newLast;
			}
		}
	}

	private void refreshMax() {
		for (T object : current) {
			if (maxCurrent == null) {
				maxCurrent = object;
			} else if (object.compareTo(maxCurrent) >= 0) {
				maxCurrent = object;
			}
		}
	}

	private void refreshMin() {
		for (T object : current) {
			if (minCurrent == null) {
				minCurrent = object;
			} else if (object.compareTo(minCurrent) < 0) {
				minCurrent = object;
			}
		}
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		if (collection.isEmpty()) {
			return false;
		}

		for (T object : collection) {
			add(object);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> collection) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void add(int index, T object) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public T get(int index) {
		return current.get(index);
	}

	@Override
	public int size() {
		return current.size();
	}

	@Override
	public boolean isEmpty() {
		return current.isEmpty();
	}

	public boolean isStepable() {
		return !prev.isEmpty();
	}

	@Override
	public boolean contains(Object object) {
		return current.contains(object) || prev.contains(object);
	}

	@Override
	public int indexOf(Object object) {
		return current.indexOf(object);
	}

	@Override
	public int lastIndexOf(Object object) {
		return current.indexOf(object);
	}

	private T getLastPrev() {
		T newLast = null;
		for (T object : prev) {
			if (newLast == null) {
				newLast = object;
			} else if (object.compareTo(newLast) >= 0) {
				newLast = object;
			}
		}
		return newLast;
	}

	@Override
	public T remove(int index) {
		T removedObj = current.remove(index);

		T newLast = getLastPrev();
		if (newLast != null) {
			prev.remove(newLast);
			current.add(newLast);
		}
		minCurrent = newLast;
		if (removedObj == maxCurrent) {
			refreshMax();
		}

		return removedObj;
	}

	@Override
	public boolean remove(Object object) {
		if (object == null) {
			return false;
		}

		if (prev.contains(object)) {
			return prev.remove(object);
		} else if (current.contains(object)) {
			boolean removed = current.remove(object);
			if (removed) {
				T newLast = getLastPrev();
				if (newLast != null) {
					prev.remove(newLast);
					current.add(newLast);
				}
				minCurrent = newLast;

				if (object == maxCurrent) {
					refreshMax();
				}
			}
			return removed;
		}
		return false;
	}

	@Override
	public T set(int index, T object) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Object[] toArray() {
		return current.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] contents) {
		return current.toArray(contents);
	}

	@Override
	public Iterator<T> iterator() {
		return current.iterator();
	}

	@Override
	public int hashCode() {
		return current.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return current.equals(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public ListIterator<T> listIterator(int location) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public List<T> subList(int start, int end) {
		throw new RuntimeException("Not yet implemented");
	}
}
