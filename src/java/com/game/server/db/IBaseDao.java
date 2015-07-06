package com.game.server.db;

import java.util.List;
import java.util.Map;

public interface IBaseDao {
	// public Object select(String id, Object parameterObject);

	public int insert(String id, Object parameterObject) throws Exception;

	public int update(String id, Object parameterObject) throws Exception;

	public int update(String id) throws Exception;

	public int delete(String id, Object parameterObject) throws Exception;

	public int delete(String id) throws Exception;

	public Object selectOne(String id, Object parameterObject) throws Exception;

	public Object selectOne(String id) throws Exception;

	@SuppressWarnings("rawtypes")
	public List selectList(String id, Object parameterObject) throws Exception;

	@SuppressWarnings("rawtypes")
	public List selectList(String id) throws Exception;

	@SuppressWarnings("rawtypes")
	public List selectList(String id, Object parameterObject, int offset,
			int limit) throws Exception;

	@SuppressWarnings("rawtypes")
	public List selectList(String id, int offset, int limit) throws Exception;

	@SuppressWarnings("rawtypes")
	public Map selectMap(String id, Object parameterObject, String mapKey)
			throws Exception;

	@SuppressWarnings("rawtypes")
	public Map selectMap(String id, Object parameterObject, String mapKey,
			int offset, int limit) throws Exception;
}
