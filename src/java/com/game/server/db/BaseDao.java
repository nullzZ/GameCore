package com.game.server.db;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public final class BaseDao implements IBaseDao {
	private static Logger logger = Logger.getLogger(BaseDao.class);

	public static BaseDao instance = new BaseDao();

	//
	public static BaseDao getInstance() {
		return instance;
	}

	// @Override
	// public Object select(String id, Object parameterObject) {
	// long tmpNanoTime = System.nanoTime();
	// SqlSession session = MyBatisFactory.getInstance()
	// .getSqlSessionFactory().openSession();
	// try {
	// Object obj = session.selectOne(id, parameterObject);
	// session.commit();
	// logger.debug("数据库操作:" + id + "|"
	// + (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
	// return obj;
	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.error("查询失败");
	// } finally {
	// session.close();
	// }
	// return null;
	// }

	/**
	 * 执行指定的插入操作
	 * 
	 * @param id
	 * @param parameterObject
	 * @return
	 */

	public int insert(String id, Object parameterObject) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		try {
			int i = session.insert(id, parameterObject);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("执行[insert]操作发生异常");
		} finally {
			session.close();
		}
	}

	/**
	 * 执行修改操作
	 * 
	 * @param id
	 * @param parameterObject
	 * @return
	 */
	@Override
	public int update(String id, Object parameterObject) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		try {
			int i = session.update(id, parameterObject);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[update]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[update]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 执行修改操作
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public int update(String id) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		try {
			int i = session.update(id);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[update]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[update]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 执行删除操作
	 * 
	 * @param id
	 * @param parameterObject
	 * @return
	 */
	@Override
	public int delete(String id, Object parameterObject) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		try {
			int i = session.delete(id, parameterObject);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[delete]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[delete]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 执行删除操作
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public int delete(String id) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		try {
			int i = session.delete(id);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[delete]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[delete]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 查询一个对象
	 * 
	 * @param id
	 * @param parameterObject
	 * @return
	 */
	@Override
	public Object selectOne(String id, Object parameterObject) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		try {
			Object i = session.selectOne(id, parameterObject);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[selectOne]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[selectOne]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 查询一个对象
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Object selectOne(String id) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		try {
			Object i = session.selectOne(id);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[selectOne]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[selectOne]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 查询一个数据集合
	 * 
	 * @param id
	 * @param parameterObject
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List selectList(String id, Object parameterObject) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		try {
			List i = session.selectList(id, parameterObject);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[selectList]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[selectList]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 查询一个数据集合
	 * 
	 * @param id
	 * @return
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List selectList(String id) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		try {
			List i = session.selectList(id);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[selectList]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[selectList]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 根据开始位置返回指定数量的记录集合
	 * 
	 * @param id
	 * @param parameterObject
	 * @param offset
	 *            ：开始位置
	 * @param limit
	 *            ：返回上限
	 * @return
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List selectList(String id, Object parameterObject, int offset,
			int limit) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		RowBounds bounds = new RowBounds(offset, limit);
		try {
			List i = session.selectList(id, parameterObject, bounds);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[selectList]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[selectList]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 返回指定位置的数据
	 * 
	 * @param id
	 * @param offset
	 *            ：开始位置
	 * @param limit
	 *            ：数据上限
	 * @return
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List selectList(String id, int offset, int limit) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		RowBounds bounds = new RowBounds(offset, limit);
		try {
			List i = session.selectList(id, bounds);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[selectList]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[selectList]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 返回以指定列作为key的数据集合
	 * 
	 * @param id
	 * @param parameterObject
	 * @param mapKey
	 * @return
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map selectMap(String id, Object parameterObject, String mapKey)
			throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		try {
			Map i = session.selectMap(id, parameterObject, mapKey);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[selectMap]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[selectMap]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 查询指定范围的数据集合
	 * 
	 * @param id
	 * @param parameterObject
	 * @param mapKey
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map selectMap(String id, Object parameterObject, String mapKey,
			int offset, int limit) throws Exception {
		long tmpNanoTime = System.nanoTime();
		SqlSession session = MyBatisFactory.getInstance()
				.getSqlSessionFactory().openSession();
		RowBounds bounds = new RowBounds(offset, limit);
		try {
			Map i = session.selectMap(id, parameterObject, mapKey, bounds);
			session.commit();
			logger.debug("数据库操作:" + id + "|"
					+ (System.nanoTime() - tmpNanoTime) / 1000000000.0f + "秒");
			return i;
		} catch (Exception e) {
			logger.error("执行[selectMap]操作发生异常", e);
			e.printStackTrace();
			throw new Exception("执行[selectMap]操作发生异常");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
