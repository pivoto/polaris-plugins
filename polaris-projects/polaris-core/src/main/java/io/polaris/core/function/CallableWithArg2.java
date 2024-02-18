package io.polaris.core.function;

/**
 * @author Qt
 * @since 1.8
 */
@FunctionalInterface
public interface CallableWithArg2<V, A, B> {

	V call(A a, B b) throws Exception;

}
