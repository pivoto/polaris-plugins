package io.polaris.mybatis.provider;

import io.polaris.core.annotation.Published;
import io.polaris.core.jdbc.sql.consts.BindingKeys;
import io.polaris.core.jdbc.sql.EntityStatements;
import io.polaris.core.jdbc.sql.statement.SelectStatement;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.Map;

/**
 * @author Qt
 * @since 1.8,  Sep 11, 2023
 */
public class SqlCountProvider extends BaseProviderMethodResolver{

	@Published
	public static String provideSql(Map<String, Object> map, ProviderContext context) {
		SelectStatement<?> st = (SelectStatement<?>) map.get(BindingKeys.SELECT);
		return EntityStatements.getSqlWithBindings(map, st::toCountSqlNode);
	}

}
