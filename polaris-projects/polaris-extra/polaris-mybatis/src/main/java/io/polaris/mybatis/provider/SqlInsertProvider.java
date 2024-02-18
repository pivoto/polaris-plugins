package io.polaris.mybatis.provider;

import java.util.Map;

import io.polaris.core.annotation.Published;
import io.polaris.core.jdbc.sql.EntityStatements;
import io.polaris.core.jdbc.sql.consts.BindingKeys;
import io.polaris.core.jdbc.sql.statement.DeleteStatement;
import io.polaris.core.jdbc.sql.statement.InsertStatement;
import io.polaris.mybatis.scripting.ProviderSqlSourceDriver;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;

/**
 * @author Qt
 * @since 1.8,  Sep 11, 2023
 */
@Slf4j
public class SqlInsertProvider extends BaseProviderMethodResolver {

	@Published
	public static String provideSql(Object parameterObject, ProviderContext context) {
		Map<String, Object> map = ProviderSqlSourceDriver.toParameterBindings(context.getMapperMethod(), parameterObject);
		InsertStatement<?> st = (InsertStatement<?>) map.get(BindingKeys.INSERT);
		if (st == null) {
			st = (InsertStatement<?>) map.get(BindingKeys.SQL);
		}
		String sql = EntityStatements.asSqlWithBindings(map, st);
		if (log.isDebugEnabled()) {
			log.debug("<sql>\n{}\n<bindings>\n{}", sql, map);
		}
		return sql;
	}

}
