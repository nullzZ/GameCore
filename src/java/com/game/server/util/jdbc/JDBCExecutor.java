package com.game.server.util.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExecutor {
	// 获得驱动
	private String driver = "";
	// 获得url
	private String url = "";
	// 获得连接数据库的用户名
	private String user = "";
	// 获得连接数据库的密码
	private String password = "";
	// 连接对象
	private Connection connection;
	// 维护一个本类型的对象
	// private static JDBCExecutor jdbcExecutor;
	// Statement对象,可以执行SQL语句并返回结果
	private Statement stmt;

	// 私有构造器
	public JDBCExecutor(String driver, String url, String user, String password) {
		try {
			this.driver = driver;
			this.url = url;
			this.user = user;
			this.password = password;
			// 初始化JDBC驱动并让驱动加载到jvm中
			Class.forName(driver);
			// 创建数据库连接
			connection = DriverManager.getConnection(url, user, password);
			// 创建Statement对象
			stmt = connection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// // 提供一个静态方法返回本类的实例
	// public static JDBCExecutor getJDBCExecutor() {
	// // 如果本类所维护jdbcExecutor属性为空,则调用私有的构造器获得实例
	// if (jdbcExecutor == null) {
	// jdbcExecutor = new JDBCExecutor();
	// }
	// return jdbcExecutor;
	// }

	/*
	 * 执行一句查询的sql
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		// 利用Statement对象执行参数的sql
		ResultSet result = stmt.executeQuery(sql);
		return result;
	}

	// 执行单句INSERT、UPDATE 或 DELETE 语句, 如果执行INSERT时, 返回主键
	public int executeUpdate(String sql) throws SQLException {
		int result = -1;
		// 执行SQL语句
		stmt.executeUpdate(sql);
		// 获得主键
		ResultSet rs = stmt.getGeneratedKeys();
		while (rs.next()) {
			// 返回最后一个主键
			result = rs.getInt(1);
		}
		rs.close();
		return result;
	}
}
