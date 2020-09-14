package io.polaris.core.hash;

/**
 * @author Qt
 * @since 1.8,  Aug 01, 2023
 */
@FunctionalInterface
public interface Hash32<T> extends Hash<T> {
	/**
	 * 计算Hash值
	 *
	 * @param t 对象
	 * @return hash
	 */
	int hash32(T t);

	@Override
	default Number hash(T t) {
		return hash32(t);
	}
}
