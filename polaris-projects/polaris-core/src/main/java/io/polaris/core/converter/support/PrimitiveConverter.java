package io.polaris.core.converter.support;

import io.polaris.core.converter.AbstractSimpleConverter;
import io.polaris.core.converter.ConverterRegistry;
import io.polaris.core.lang.JavaType;
import io.polaris.core.string.Strings;

/**
 * @author Qt
 * @since 1.8
 */
public class PrimitiveConverter extends AbstractSimpleConverter<Object> {
	private final JavaType<Object> targetType;

	public PrimitiveConverter(Class<?> clazz) {
		if (!clazz.isPrimitive()) {
			throw new IllegalArgumentException();
		}
		this.targetType = (JavaType<Object>) JavaType.of(clazz);
	}

	@Override
	public JavaType<Object> getTargetType() {
		return targetType;
	}

	@Override
	protected String asString(Object value) {
		return Strings.trimToEmpty(super.asString(value));
	}

	@Override
	protected Object doConvert(Object value, JavaType<Object> targetType) {
		Class<?> primitiveClass = targetType.getRawClass();
		if (byte.class == primitiveClass) {
			return ConverterRegistry.INSTANCE.convertQuietly(Byte.class, value, (byte) 0).byteValue();
		} else if (short.class == primitiveClass) {
			return ConverterRegistry.INSTANCE.convertQuietly(Short.class, value, (short) 0).shortValue();
		} else if (int.class == primitiveClass) {
			return ConverterRegistry.INSTANCE.convertQuietly(Integer.class, value, (int) 0).intValue();
		} else if (long.class == primitiveClass) {
			return ConverterRegistry.INSTANCE.convertQuietly(Long.class, value, (long) 0L).longValue();
		} else if (float.class == primitiveClass) {
			return ConverterRegistry.INSTANCE.convertQuietly(Float.class, value, 0f).floatValue();
		} else if (double.class == primitiveClass) {
			return ConverterRegistry.INSTANCE.convertQuietly(Double.class, value, 0d).doubleValue();
		} else if (char.class == primitiveClass) {
			return ConverterRegistry.INSTANCE.convertQuietly(Character.class, value, '\0').charValue();
		} else if (boolean.class == primitiveClass) {
			return ConverterRegistry.INSTANCE.convertQuietly(Boolean.class, value, false).booleanValue();
		}
		throw new IllegalArgumentException();
	}
}
