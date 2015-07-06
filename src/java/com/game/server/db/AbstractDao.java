package com.game.server.db;

import java.util.List;

public abstract class AbstractDao {

	protected Object selectOne(String id, Object parameterObject)
			throws Exception {
		return BaseDao.getInstance().selectOne(id, parameterObject);
	}

	protected Object selectOne(String id) throws Exception {
		return BaseDao.getInstance().selectOne(id);
	}

	@SuppressWarnings("rawtypes")
	protected List selectList(String id, Object parameterObject)
			throws Exception {
		return BaseDao.getInstance().selectList(id, parameterObject);
	}

	@SuppressWarnings("rawtypes")
	protected List selectList(String id) throws Exception {
		return BaseDao.getInstance().selectList(id);
	}

	protected void delete(String id, Object parameterObject) {
		MyBatisFactory.getInstance().execute(
				new DBTask(id, DBType.delete, parameterObject));
	}

	public void synDelete(String id) throws Exception {
		BaseDao.getInstance().delete(id);
	}

	public void synDelete(String id, Object parameterObject) throws Exception {
		BaseDao.getInstance().delete(id, parameterObject);
	}

	protected int insert(String id, Object parameterObject) throws Exception {
		// MyBatisFactory.getInstance().getExecutor()
		// .execute(new DBThread(id, DBType.insert, parameterObject));
		return BaseDao.getInstance().insert(id, parameterObject);
	}

	protected int synInsert(String id, Object parameterObject) throws Exception {
		return BaseDao.getInstance().insert(id, parameterObject);
	}

	protected void update(String id, Object parameterObject) {
		MyBatisFactory.getInstance().execute(
				new DBTask(id, DBType.update, parameterObject));
	}

	protected int synUpdate(String id, Object parameterObject) throws Exception {
		return BaseDao.getInstance().update(id, parameterObject);
	}
}
