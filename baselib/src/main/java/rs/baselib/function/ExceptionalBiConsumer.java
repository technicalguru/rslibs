package rs.baselib.function;

/**
 * Represents an operation that accepts two input arguments and returns no
 * result and can throw exceptions.  This is the two-arity specialization of {@link ExceptionalConsumer}.
 * Unlike most other functional interfaces, {@code ExceptionalBiConsumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a functional interface
 * whose functional method is {@link #accept(Object, Object)}.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 *
 */
@FunctionalInterface
public interface ExceptionalBiConsumer<T, U> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(T t, U u) throws Exception;

}
