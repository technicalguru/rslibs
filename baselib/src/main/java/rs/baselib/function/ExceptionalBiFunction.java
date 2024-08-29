package rs.baselib.function;

/**
 * Represents a function that accepts two arguments and produces a result
 * and can throw exceptions.
 * This is the two-arity specialization of {@link ExceptionalFunction}.
 *
 * <p>This is a functional interface
 * whose functional method is {@link #apply(Object, Object)}.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 *
 */
@FunctionalInterface
public interface ExceptionalBiFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(T t, U u) throws Exception;

}
