package de.alexanderkose.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class StepCollection<T extends Comparable<T>> implements Collection<T> {
	private final ArrayList<T> current;
	private final ArrayList<T> prev;
	private int size;
	private final int steps;

	private T maxCurrent;
	private T minCurrent;

	public StepCollection(int size) {
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

		if (current.contains(object) || prev.contains(object)) {
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
				prev.add(minCurrent);
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

	public boolean nextStep() {
		if (!isStepable()) {
			return false;
		}

		boolean added = false;
		if (prev.size() <= steps) {
			added = current.addAll(prev);
			prev.clear();
			refreshMin();
		} else {
			for (int s = 0; s < steps && !prev.isEmpty(); s++) {
				T newLast = getLastPrev();
				if (newLast != null) {
					boolean oneAdded = current.add(newLast);
					if (oneAdded) {
						added = true;
						prev.remove(newLast);
					}
				}
				minCurrent = newLast;
			}
		}
		if (added) {
			size += steps;
		}
		return added;
	}

	private void refreshMax() {
		maxCurrent = null;
		for (T object : current) {
			if (maxCurrent == null) {
				maxCurrent = object;
			} else if (object.compareTo(maxCurrent) >= 0) {
				maxCurrent = object;
			}
		}
	}

	private void refreshMin() {
		minCurrent = null;
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
		if (collection == null || collection.isEmpty()) {
			return false;
		}

		boolean added = false;
		for (T object : collection) {
			boolean oneAdded = add(object);
			if (oneAdded) {
				added = true;
			}
		}
		return added;
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
		return current.contains(object);
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
					boolean oneAdded = current.add(newLast);
					if (oneAdded) {
						prev.remove(newLast);
					}
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
	public boolean containsAll(Collection<?> c) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new RuntimeException("Not yet implemented");
	}
}
