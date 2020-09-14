package io.polaris.core.reflect;

import java.io.Serializable;

/**
 * @author Qt
 * @since 1.8
 */
@FunctionalInterface
public interface SerializableTernaryFunction<A, B, C, R> extends Serializable, MethodReferenceReflection {

	R apply(A a, B b, C c);
}
