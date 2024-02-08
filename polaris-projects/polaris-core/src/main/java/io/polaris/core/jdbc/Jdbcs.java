package io.polaris.core.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import io.polaris.core.collection.ObjectArrays;
import io.polaris.core.collection.PrimitiveArrays;
import io.polaris.core.jdbc.base.BeanMapping;
import io.polaris.core.jdbc.base.JdbcOptions;
import io.polaris.core.jdbc.base.ResultExtractor;
import io.polaris.core.jdbc.base.ResultExtractors;
import io.polaris.core.jdbc.base.ResultRowSimpleMapper;
import io.polaris.core.jdbc.base.ResultSetVisitor;
import io.polaris.core.jdbc.base.StatementPreparer;
import io.polaris.core.jdbc.base.StatementPreparers;
import io.polaris.core.jdbc.executor.BatchResult;
import io.polaris.core.jdbc.executor.JdbcBatch;
import io.polaris.core.jdbc.executor.JdbcExecutors;
import io.polaris.core.jdbc.sql.PreparedSql;
import io.polaris.core.jdbc.sql.node.SqlNode;
import io.polaris.core.lang.bean.MetaObject;
import io.polaris.core.log.ILogger;
import io.polaris.core.log.ILoggers;
import io.polaris.core.string.Strings;

/**
 * @author Qt
 * @since 1.8
 */
@SuppressWarnings({"unused"})
public class Jdbcs {
	private static final ILogger log = ILoggers.of(Jdbcs.class);
	private static final JdbcOptions DEFAULT_OPTIONS = JdbcOptions.ofDefault();
	private static final ThreadLocal<JdbcBatch> currentBatch = new ThreadLocal<>();

	public static DataSource getDataSource(String jndiName) throws SQLException {
		try {
			Context ctx = new InitialContext();
			return (DataSource) ctx.lookup(jndiName);
		} catch (NamingException e) {
			try {
				return (DataSource) ((Context) new InitialContext().lookup("java:comp/env"))
					.lookup(jndiName);
			} catch (Exception e1) {
				throw new SQLException("Can't lookup " + jndiName);
			}
		}
	}

	public static Connection getConnection(String jndiName) throws SQLException {
		return getDataSource(jndiName).getConnection();
	}

	public static Connection getConnection(String driver, String url, Properties info) throws SQLException {
		try {
			if (Strings.isBlank(driver)) {
				driver = JdbcDriver.parse(url).getDriverClassName();
				if (Strings.isBlank(driver)) {
					throw new IllegalArgumentException("无法从url中获得驱动类");
				}
			}
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("找不到驱动：" + driver);
		}
		return DriverManager.getConnection(url, info);
	}

	public static Connection getConnection(String url, Properties info) throws SQLException {
		return getConnection(null, url, info);
	}

	public static Connection getConnection(String driver, String url, String user, String password, boolean remark) throws SQLException {
		if (remark) {
			Properties info = new Properties();
			info.setProperty("remarks", "true");//common
			info.setProperty("remarksReporting", "true");//oracle
			info.setProperty("useInformationSchema", "true");//mysql
			info.setProperty("user", user);
			info.setProperty("password", password);
			return getConnection(driver, url, info);
		}
		try {
			if (Strings.isBlank(driver)) {
				driver = JdbcDriver.parse(url).getDriverClassName();
				if (Strings.isBlank(driver)) {
					throw new IllegalArgumentException("无法从url中获得驱动类");
				}
			}
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("找不到驱动：" + driver);
		}
		return DriverManager.getConnection(url, user, password);
	}

	public static Connection getConnection(String driver, String url, String user, String password) throws SQLException {
		return getConnection(driver, url, user, password, true);
	}

	public static Connection getConnection(String url, String user, String password, boolean remark) throws SQLException {
		return getConnection(null, url, user, password, remark);
	}

	public static Connection getConnection(String url, String user, String password) throws SQLException {
		return getConnection(null, url, user, password, true);
	}


	public static <R extends AutoCloseable> void close(R r) {
		try {
			if (r != null) {
				r.close();
			}
		} catch (Exception ignored) {
		}
	}

	public static void close(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception ignored) {
		}
	}

	public static <T> T createExecutor(Class<T> interfaceClass) {
		return JdbcExecutors.createExecutor(interfaceClass);
	}

	public static <T> T createExecutor(Class<T> interfaceClass, Connection connection, boolean batch) {
		return JdbcExecutors.createExecutor(interfaceClass, connection, batch);
	}

	public static <T> T query(Connection conn, SqlNode sqlNode
		, ResultExtractor<T> extractor) throws SQLException {
		PreparedSql sql = sqlNode.asPreparedSql();
		return query(conn, sql.getText(), StatementPreparers.of(sql.getBindings()), extractor);
	}

	public static <T> T query(Connection conn, SqlNode sqlNode, @Nonnull JdbcOptions options
		, ResultExtractor<T> extractor) throws SQLException {
		PreparedSql sql = sqlNode.asPreparedSql();
		return query(conn, sql.getText(), StatementPreparers.of(sql.getBindings()), extractor);
	}

	public static void query(Connection conn, SqlNode sqlNode, @Nonnull JdbcOptions options
		, ResultSetVisitor visitor) throws SQLException {
		PreparedSql sql = sqlNode.asPreparedSql();
		query(conn, sql.getText(), options, StatementPreparers.of(sql.getBindings()), visitor);
	}

	public static <T> T query(Connection conn, String sql, Iterable<?> parameters
		, ResultExtractor<T> extractor) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), extractor);
	}

	public static <T> T query(Connection conn, String sql, Object[] parameters
		, ResultExtractor<T> extractor) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), extractor);
	}

	public static <T> T query(Connection conn, String sql, ResultExtractor<T> extractor) throws SQLException {
		return query(conn, sql, (StatementPreparer) null, extractor);
	}

	public static <T> List<T> query(Connection conn, String sql, ResultRowSimpleMapper<T> mapper) throws SQLException {
		return query(conn, sql, (StatementPreparer) null, rs -> {
			List<T> list = new ArrayList<>();
			while (rs.next()) {
				list.add(mapper.map(rs));
			}
			return list;
		});
	}


	public static List<Map<String, Object>> queryForMapList(Connection conn, String sql) throws SQLException {
		return query(conn, sql, (StatementPreparer) null, ResultExtractors.ofMapList());
	}

	public static List<Map<String, Object>> queryForMapList(Connection conn, String sql, Iterable<?> parameters) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), ResultExtractors.ofMapList());
	}

	public static List<Map<String, Object>> queryForMapList(Connection conn, String sql, Object[] parameters) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), ResultExtractors.ofMapList());
	}

	public static <T> List<T> queryForList(Connection conn, String sql, Class<T> beanType) throws SQLException {
		return query(conn, sql, (StatementPreparer) null, ResultExtractors.ofBeanList(beanType));
	}

	public static <T> List<T> queryForList(Connection conn, String sql, Iterable<?> parameters, Class<T> beanType) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), ResultExtractors.ofBeanList(beanType));
	}

	public static <T> List<T> queryForList(Connection conn, String sql, Object[] parameters, Class<T> beanType) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), ResultExtractors.ofBeanList(beanType));
	}

	public static <T> List<T> queryForList(Connection conn, String sql, BeanMapping<T> mapping) throws SQLException {
		return query(conn, sql, (StatementPreparer) null, ResultExtractors.ofMappingList(mapping));
	}

	public static <T> List<T> queryForList(Connection conn, String sql, Iterable<?> parameters, BeanMapping<T> mapping) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), ResultExtractors.ofMappingList(mapping));
	}

	public static <T> List<T> queryForList(Connection conn, String sql, Object[] parameters, BeanMapping<T> mapping) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), ResultExtractors.ofMappingList(mapping));
	}

	public static Map<String, Object> queryForMap(Connection conn, String sql) throws SQLException {
		return query(conn, sql, (StatementPreparer) null, ResultExtractors.ofMap());
	}

	public static Map<String, Object> queryForMap(Connection conn, String sql, Iterable<?> parameters) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), ResultExtractors.ofMap());
	}

	public static Map<String, Object> queryForMap(Connection conn, String sql, Object[] parameters) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), ResultExtractors.ofMap());
	}

	public static <T> T queryForObject(Connection conn, String sql, Class<T> beanType) throws SQLException {
		return query(conn, sql, (StatementPreparer) null, ResultExtractors.ofBean(beanType));
	}

	public static <T> T queryForObject(Connection conn, String sql, Iterable<?> parameters, Class<T> beanType) throws SQLException {
		return query(conn, sql, parameters, ResultExtractors.ofBean(beanType));
	}

	public static <T> T queryForObject(Connection conn, String sql, Object[] parameters, Class<T> beanType) throws SQLException {
		return query(conn, sql, parameters, ResultExtractors.ofBean(beanType));
	}

	public static <T> T queryForMapping(Connection conn, String sql, BeanMapping<T> mapping) throws SQLException {
		return query(conn, sql, (StatementPreparer) null, ResultExtractors.ofMapping(mapping));
	}

	public static <T> T queryForMapping(Connection conn, String sql, Iterable<?> parameters, BeanMapping<T> mapping) throws SQLException {
		return query(conn, sql, parameters, ResultExtractors.ofMapping(mapping));
	}

	public static <T> T queryForMapping(Connection conn, String sql, Object[] parameters, BeanMapping<T> mapping) throws SQLException {
		return query(conn, sql, parameters, ResultExtractors.ofMapping(mapping));
	}

	public static Object queryForSingle(Connection conn, String sql) throws SQLException {
		return query(conn, sql, (StatementPreparer) null, ResultExtractors.ofSingle());
	}

	public static Object queryForSingle(Connection conn, String sql, Iterable<?> parameters) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), ResultExtractors.ofSingle());
	}

	public static Object queryForSingle(Connection conn, String sql, Object[] parameters) throws SQLException {
		return query(conn, sql, StatementPreparers.of(parameters), ResultExtractors.ofSingle());
	}

	public static int update(Connection conn, SqlNode sql) throws SQLException {
		PreparedSql preparedSql = sql.asPreparedSql();
		return update(conn, preparedSql.getText(), StatementPreparers.of(preparedSql.getBindings()));
	}

	public static int update(Connection conn, SqlNode sql, @Nonnull JdbcOptions options) throws SQLException {
		PreparedSql preparedSql = sql.asPreparedSql();
		return update(conn, preparedSql.getText(), options, StatementPreparers.of(preparedSql.getBindings()), null);
	}

	public static int update(Connection conn, SqlNode sql, @Nonnull JdbcOptions options, Object generatedKeyBinding) throws SQLException {
		PreparedSql preparedSql = sql.asPreparedSql();
		return update(conn, preparedSql.getText(), options, StatementPreparers.of(preparedSql.getBindings()), generatedKeyBinding);
	}

	public static int update(Connection conn, String sql) throws SQLException {
		return update(conn, sql, DEFAULT_OPTIONS, null, null);
	}

	public static int update(Connection conn, String sql, Iterable<?> parameters) throws SQLException {
		return update(conn, sql, DEFAULT_OPTIONS, StatementPreparers.of(parameters), null);
	}

	public static int update(Connection conn, String sql, Object[] parameters) throws SQLException {
		return update(conn, sql, DEFAULT_OPTIONS, StatementPreparers.of(parameters), null);
	}


	public static <T> T query(Connection conn, String sql, StatementPreparer preparer, ResultExtractor<T> extractor) throws SQLException {
		return query(conn, sql, DEFAULT_OPTIONS, preparer, extractor);
	}

	public static void query(Connection conn, String sql, StatementPreparer preparer, ResultSetVisitor visitor) throws SQLException {
		query(conn, sql, DEFAULT_OPTIONS, preparer, visitor);
	}

	public static <T> T query(@Nonnull Connection conn, @Nonnull String sql, @Nonnull JdbcOptions options,
		StatementPreparer preparer, ResultExtractor<T> extractor) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			log.debug("执行查询SQL：{}", sql);
			st = prepareStatement(conn, sql, options);
			if (preparer != null) {
				preparer.set(st);
			}
			rs = st.executeQuery();
			if (extractor != null) {
				return extractor.extract(rs);
			}
			return null;
		} catch (SQLException e) {
			log.error("查询方法执行异常，语句：{}", sql);
			throw e;
		} finally {
			Jdbcs.close(rs);
			Jdbcs.close(st);
		}
	}

	public static void query(@Nonnull Connection conn, @Nonnull String sql, @Nonnull JdbcOptions options,
		StatementPreparer preparer, ResultSetVisitor visitor) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			log.debug("执行查询SQL：{}", sql);
			st = prepareStatement(conn, sql, options);
			if (preparer != null) {
				preparer.set(st);
			}
			rs = st.executeQuery();
			if (visitor != null) {
				visitor.visit(rs);
			}
		} catch (SQLException e) {
			log.error("查询方法执行异常，语句：{}", sql);
			throw e;
		} finally {
			Jdbcs.close(rs);
			Jdbcs.close(st);
		}
	}


	public static int update(Connection conn, String sql, StatementPreparer preparer) throws SQLException {
		return update(conn, sql, DEFAULT_OPTIONS, preparer, null);
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	public static int update(@Nonnull Connection conn, @Nonnull String sql, @Nonnull JdbcOptions options,
		StatementPreparer preparer, Object generatedKeyBinding) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			log.debug("执行SQL：{}", sql);
			st = prepareStatement(conn, sql, options);
			if (preparer != null) {
				preparer.set(st);
			}
			int rows = st.executeUpdate();
			String[] keyProperties = options.getKeyProperties();
			if (generatedKeyBinding != null && options.isUseGeneratedKeys() && ObjectArrays.isNotEmpty(keyProperties)) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					MetaObject metaObject = MetaObject.of((Class) generatedKeyBinding.getClass());
					for (int i = 0; i < keyProperties.length; i++) {
						Object val = rs.getObject(i + 1);
						metaObject.setPathProperty(generatedKeyBinding, keyProperties[i], val);
					}
				}
			}
			return rows;
		} catch (SQLException e) {
			log.error("更新方法执行异常，语句：{}", sql);
			throw e;
		} finally {
			Jdbcs.close(rs);
			Jdbcs.close(st);
		}
	}

	public static void call(@Nonnull Connection conn, @Nonnull String sql, @Nonnull JdbcOptions options, Object[] parameters, int[] outIndexes, int[] outTypes) throws SQLException {
		call(conn, sql, options, parameters, outIndexes, outTypes, null);
	}

	@SuppressWarnings("SqlSourceToSinkFlow")
	public static <T> T call(@Nonnull Connection conn, @Nonnull String sql, @Nonnull JdbcOptions options, Object[] parameters, int[] outIndexes, int[] outTypes, ResultExtractor<T> extractor) throws SQLException {
		CallableStatement st = null;
		ResultSet rs = null;
		try {
			log.debug("执行SQL：{}", sql);
			st = conn.prepareCall(sql);
			if (options.getTimeout() >= 0) {
				st.setQueryTimeout(options.getTimeout());
			}
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					Object parameter = parameters[i];
					if (PrimitiveArrays.contains(outIndexes, i)) {
						if (parameter != null) {
							st.setObject(i + 1, parameter);
						}
					} else {
						if (parameter != null) {
							st.setObject(i + 1, parameter);
						} else {
							st.setNull(i + 1, Types.VARCHAR);
						}
					}
				}
			}
			for (int i = 0; i < outIndexes.length; i++) {
				st.registerOutParameter(outIndexes[i], outTypes[i]);
			}

			if (extractor != null) {
				rs = st.executeQuery();
				return extractor.extract(rs);
			} else {
				st.executeUpdate();
			}
			return null;
		} finally {
			Jdbcs.close(rs);
			Jdbcs.close(st);
		}
	}

	@SuppressWarnings("SqlSourceToSinkFlow")
	public static PreparedStatement prepareStatement(@Nonnull Connection conn, @Nonnull String sql, @Nonnull JdbcOptions options) throws SQLException {
		PreparedStatement st;
		if (options.isUseGeneratedKeys()) {
			String[] keyColumns = options.getKeyColumns();
			if (ObjectArrays.isEmpty(keyColumns)) {
				st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			} else {
				st = conn.prepareStatement(sql, keyColumns);
			}
		} else {
			st = conn.prepareStatement(sql);
		}
		if (options.getFetchSize() >= 0) {
			st.setFetchSize(options.getFetchSize());
		}
		if (options.getTimeout() >= 0) {
			st.setQueryTimeout(options.getTimeout());
		}
		if (options.getMaxRows() >= 0) {
			st.setMaxRows(options.getMaxRows());
		}
		return st;
	}


	public static JdbcBatch updateBatch(Connection conn, SqlNode sql) throws SQLException {
		PreparedSql preparedSql = sql.asPreparedSql();
		return updateBatch(conn, preparedSql.getText(), StatementPreparers.of(preparedSql.getBindings()));
	}


	public static JdbcBatch updateBatch(Connection conn, SqlNode sql, @Nonnull JdbcOptions options) throws SQLException {
		PreparedSql preparedSql = sql.asPreparedSql();
		return updateBatch(conn, preparedSql.getText(), options, StatementPreparers.of(preparedSql.getBindings()), null);
	}

	public static JdbcBatch updateBatch(Connection conn, SqlNode sql, @Nonnull JdbcOptions options, Object generatedKeyBinding) throws SQLException {
		PreparedSql preparedSql = sql.asPreparedSql();
		return updateBatch(conn, preparedSql.getText(), options, StatementPreparers.of(preparedSql.getBindings()), generatedKeyBinding);
	}

	public static JdbcBatch updateBatch(Connection conn, String sql) throws SQLException {
		return updateBatch(conn, sql, DEFAULT_OPTIONS, null, null);
	}

	public static JdbcBatch updateBatch(Connection conn, String sql, Iterable<?> parameters) throws SQLException {
		return updateBatch(conn, sql, DEFAULT_OPTIONS, StatementPreparers.of(parameters), null);
	}

	public static JdbcBatch updateBatch(Connection conn, String sql, Object[] parameters) throws SQLException {
		return updateBatch(conn, sql, DEFAULT_OPTIONS, StatementPreparers.of(parameters), null);
	}

	public static JdbcBatch updateBatch(Connection conn, String sql, StatementPreparer preparer) throws SQLException {
		return updateBatch(conn, sql, DEFAULT_OPTIONS, preparer, null);
	}

	public static JdbcBatch updateBatch(@Nonnull Connection conn, @Nonnull String sql, @Nonnull JdbcOptions options,
		StatementPreparer preparer, Object generatedKeyBinding) throws SQLException {
		JdbcBatch batch = currentBatch.get();
		if (batch == null) {
			batch = new JdbcBatch();
			currentBatch.set(batch);
		}
		batch.update(conn, sql, options, preparer, generatedKeyBinding);
		return batch;
	}

	public static List<BatchResult> flushBatch() throws SQLException {
		JdbcBatch batch = currentBatch.get();
		try {
			if (batch == null) {
				return null;
			}
			return batch.flush();
		} finally {
			currentBatch.remove();
		}
	}


}
