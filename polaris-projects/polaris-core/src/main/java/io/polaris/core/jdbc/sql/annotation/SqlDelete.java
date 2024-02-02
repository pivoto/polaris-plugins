package io.polaris.core.jdbc.sql.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.polaris.core.jdbc.sql.annotation.segment.ColumnPredicate;
import io.polaris.core.jdbc.sql.annotation.segment.Where;
import io.polaris.core.jdbc.sql.consts.BindingKeys;

/**
 * @author Qt
 * @since 1.8,  Jan 27, 2024
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
@Inherited
public @interface SqlDelete {

	/**
	 * @return 标识目标实体类型
	 */
	Class<?> table();

	/**
	 * @return 表别名
	 */
	String alias() default "";

	/**
	 * @return Where条件，默认使用实体ID
	 */
	Where where() default @Where(byEntityIdKey = BindingKeys.ENTITY);

	ColumnPredicate columnPredicate() default @ColumnPredicate();
}
