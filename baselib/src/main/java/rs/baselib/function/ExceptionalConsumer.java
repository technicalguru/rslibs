package rs.baselib.function;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result and can throw exceptions. Unlike most other functional interfaces, 
 * {@code ExceptionalConsumer} is expected to operate via side-effects.
 *
 * <p>This is a functional interface
 * whose functional method is {@link #accept(Object)}.
 *
 * @param <T> the type of the input to the operation
 *
 */
@FunctionalInterface
public interface ExceptionalConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t) throws Exception;

}