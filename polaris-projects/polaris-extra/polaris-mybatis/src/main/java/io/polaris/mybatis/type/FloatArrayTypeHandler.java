package io.polaris.mybatis.type;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author Qt
 * @since 1.8,  Aug 28, 2023
 */
@MappedTypes(Float[].class)
@MappedJdbcTypes({JdbcType.VARCHAR,JdbcType.CHAR})
public class FloatArrayTypeHandler extends StringTokenizerTypeHandler<Float> {
	public FloatArrayTypeHandler() {
		super(Float.class);
	}

	@Override
	Float parseString(String value) {
		return Float.valueOf(value);
	}
}
