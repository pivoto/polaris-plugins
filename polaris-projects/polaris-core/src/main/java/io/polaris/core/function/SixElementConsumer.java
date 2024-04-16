package io.polaris.core.function;

/**
 * @author Qt
 * @since 1.8
 */
@FunctionalInterface
public interface SixElementConsumer<A, B, C, D, E, F> {

	void accept(A a, B b, C c, D d, E e, F f);
}