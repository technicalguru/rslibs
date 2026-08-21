/**
 * 
 */
package rs.jerseyclient.data;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Special list that also holds page information.
 * <p>The class is usually required for correct JSON (de)serialization while easing the use.</p>
 * 
 * @param <T> the type of result
 *
 * @author ralph
 *
 */
public class ResultList<T> implements List<T> {

	private List<T>  results;
	private PageInfo pageInfo;
	
	/**
	 * Default constructor.
	 */
	public ResultList() {}
	
	/**
	 * Constructor.
	 * @param results - results of the response
	 * @param pageInfo - page information
	 */
	public ResultList(List<T> results, PageInfo pageInfo) {
		this.results  = results;
		this.pageInfo = pageInfo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Consumer<? super T> action) {
		results.forEach(action);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return results.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return results.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		return results.contains(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return results.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X> X[] toArray(X[] a) {
		return results.toArray(a);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(T e) {
		return results.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		return results.remove(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return results.containsAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return results.addAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return results.addAll(index, c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return results.removeAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		return results.retainAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void replaceAll(UnaryOperator<T> operator) {
		results.replaceAll(operator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X> X[] toArray(IntFunction<X[]> generator) {
		return results.toArray(generator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sort(Comparator<? super T> c) {
		results.sort(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		results.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return results.equals(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return results.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get(int index) {
		return results.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T set(int index, T element) {
		return results.set(index, element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(int index, T element) {
		results.add(index, element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeIf(Predicate<? super T> filter) {
		return results.removeIf(filter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T remove(int index) {
		return results.remove(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(Object o) {
		return results.indexOf(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int lastIndexOf(Object o) {
		return results.lastIndexOf(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<T> listIterator() {
		return results.listIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<T> listIterator(int index) {
		return results.listIterator(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return results.subList(fromIndex, toIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Spliterator<T> spliterator() {
		return results.spliterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<T> stream() {
		return results.stream();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<T> parallelStream() {
		return results.parallelStream();
	}

	/**
	 * Returns the results page size.
	 * @return the results page size
	 */
	public int getPageSize() {
		return pageInfo.getSize();
	}

	/**
	 * Returns the total number of results.
	 * @return the total number of results
	 */
	public int getTotalElements() {
		return pageInfo.getTotalElements();
	}

	/**
	 * Returns the total number of pages.
	 * @return the total number of pages
	 */
	public int getTotalPages() {
		return pageInfo.getTotalPages();
	}

	/**
	 * Returns the number of the current page.
	 * @return the number of the current page
	 */
	public int getPageNumber() {
		return pageInfo.getNumber();
	}

	
}
