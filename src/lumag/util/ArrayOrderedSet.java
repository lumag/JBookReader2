package lumag.util;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArrayOrderedSet<E> extends AbstractSet<E> implements IOrderedContainer<E> {
	
	private class SetIterator implements Iterator<E> {

		private int number = -1;

		public boolean hasNext() {
			return number != size() - 1;
		}

		public E next() {
			return elements.get(++number);
		}

		public void remove() {
			if (number == -1) {
				throw new IllegalStateException();
			}
			
			ArrayOrderedSet.this.remove(number);
			number --;
		}
		
	}
	
	private List<E> elements = new ArrayList<E>();
	private Map<E, Integer> orderMap = new HashMap<E, Integer>();

	@Override
	public Iterator<E> iterator() {
		return new SetIterator();
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public boolean add(E element) {
		if (contains(element)) {
			return false;
		}
		int num = size();
		elements.add(element);
		orderMap.put(element, num);
		return true;
	}

	@Override
	public void clear() {
		elements.clear();
		orderMap.clear();
	}

	@Override
	public boolean contains(Object o) {
		return orderMap.containsKey(o);
	}

	@Override
	public boolean remove(Object o) {
		if (!contains(o)) {
			return false;
		}

		int number = elements.indexOf(o);
		return remove(number);
	}

	private boolean remove(int idx) {
		orderMap.remove(elements.remove(idx));

		int i = idx;
		for (Iterator<E> it = elements.listIterator(i); it.hasNext();) {
			orderMap.put(it.next(), i ++);
		}

		return true;
	}


	public E get(int index) {
		return elements.get(index);
	}
	
	public E getNext(E element) throws NoSuchElementException {
		if (!contains(element)) {
			throw new NoSuchElementException();
		}
		int num = getNumber(element);
		if (num == size() - 1) {
			throw new NoSuchElementException();
		}
		return elements.get(num + 1);
	}

	public E getPrevious(E element) throws NoSuchElementException {
		if (!contains(element)) {
			throw new NoSuchElementException();
		}
		int num = getNumber(element);
		if (num == 0) {
			throw new NoSuchElementException();
		}
		return elements.get(num - 1);
	}

	public int getNumber(E element) throws NoSuchElementException {
		return orderMap.get(element);
	}

	public boolean hasNext(E element) throws NoSuchElementException {
		if (!contains(element)) {
			throw new NoSuchElementException();
		}
		int num = getNumber(element);
		return num != (size() - 1);
	}

	public boolean hasPrevious(E element) throws NoSuchElementException {
		if (!contains(element)) {
			throw new NoSuchElementException();
		}
		int num = getNumber(element);
		return num != 0;
	}

}
