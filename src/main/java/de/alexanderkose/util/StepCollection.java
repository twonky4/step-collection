package de.alexanderkose.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class StepCollection<T extends Comparable<T>> implements Collection<T> {
    private final Object lock = new Object();
    private final TreeSet<T> current;
    private final ArrayList<T> prev;
    private final int steps;

    private int size;

    private T maxCurrent;
    private T minCurrent;

    public StepCollection(int size) {
        current = new TreeSet<>();
        prev = new ArrayList<>();

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
                return current.add(object);
            }
            // second element, array full
            else if (size() >= size) {
                if (object.compareTo(minCurrent) > 0) {
                    return prev.add(object);
                } else {
                    current.remove(minCurrent);
                    prev.add(minCurrent);
                    minCurrent = null;
                    boolean added = current.add(object);
                    if (object.compareTo(maxCurrent) < 0) {
                        maxCurrent = object;
                    }
                    refreshMin();

                    return added;
                }
            }
            // second element, array not full
            else {
                if (object.compareTo(maxCurrent) < 0) {
                    maxCurrent = object;
                } else if (object.compareTo(minCurrent) > 0) {
                    minCurrent = object;
                }

                return current.add(object);
            }
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {

            size = steps;
            maxCurrent = null;
            minCurrent = null;
            current.clear();
            prev.clear();

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

            return list;
        }
    }

    private void refreshMax() {
        maxCurrent = null;
        for (T object : current) {
            if (maxCurrent == null) {
                maxCurrent = object;
            } else if (object.compareTo(maxCurrent) < 0) {
                maxCurrent = object;
            }
        }
    }

    private void refreshMin() {
        minCurrent = null;
        for (T object : current) {
            if (minCurrent == null) {
                minCurrent = object;
            } else if (object.compareTo(minCurrent) >= 0) {
                minCurrent = object;
            }
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        if (collection == null || collection.isEmpty()) {
            return false;
        }

        synchronized (lock) {
            boolean added = false;
            for (T object : collection) {
                boolean oneAdded = add(object);
                if (oneAdded) {
                    added = true;
                }
            }
            return added;
        }
    }

    @Override
    public int size() {
        synchronized (lock) {
            return current.size();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (lock) {
            return current.isEmpty();
        }
    }

    public boolean isStepable() {
        synchronized (lock) {
            return !prev.isEmpty();
        }
    }

    @Override
    public boolean contains(Object object) {
        synchronized (lock) {
            return current.contains(object);
        }
    }

    private T getLastPrev() {
        T newLast = null;
        for (T object : prev) {
            if (newLast == null) {
                newLast = object;
            } else if (object.compareTo(newLast) < 0) {
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

        synchronized (lock) {
            // remove from prev
            if (prev.contains(object)) {
                return prev.remove(object);
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

                return true;
            }
            return false;
        }
    }

    @Override
    public Object[] toArray() {
        synchronized (lock) {
            return current.toArray();
        }
    }

    @SuppressWarnings("hiding")
    @Override
    public <T> T[] toArray(T[] contents) {
        synchronized (lock) {
            return current.toArray(contents);
        }
    }

    @Override
    public Iterator<T> iterator() {
        synchronized (lock) {
            ArrayList<T> list = new ArrayList<>(current);
            return list.iterator();
        }
    }

    @Override
    public int hashCode() {
        synchronized (lock) {
            final int prime = 31;
            int result = 1;
            result = prime * result + current.hashCode();
            result = prime * result + prev.hashCode();
            result = prime * result + size;
            result = prime * result + steps;
            return result;
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
                return true;
            }
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            return false;
        }

        synchronized (lock) {
            return current.containsAll(c);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }

        synchronized (lock) {
            boolean removed = false;
            for (Object object : c) {
                @SuppressWarnings("unchecked")
                boolean oneRemoved = remove((T) object);
                if (oneRemoved) {
                    removed = true;
                }
            }
            return removed;
        }
    }
}
