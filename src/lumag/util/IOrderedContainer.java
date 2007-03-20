package lumag.util;

import java.util.Collection;
import java.util.NoSuchElementException;

public interface IOrderedContainer<E> extends Collection<E> {

	boolean hasNext(E element) throws NoSuchElementException;
	boolean hasPrevious(E element) throws NoSuchElementException;

	E getNext(E element) throws NoSuchElementException;
	E getPrevious(E element) throws NoSuchElementException;

	int getNumber(E element) throws NoSuchElementException;
	E get(int index);
}
