package com.game.server.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 容器抽象方法返回类
 * 
 * @author nullzZ
 * 
 * @param <T>
 */
public class ContainerResult<T extends AbstractContainerItem> {

	private List<T> dbUpdate = new ArrayList<>();
	private List<T> dbInsert = new ArrayList<>();
	private List<T> dbDelete = new ArrayList<>();

	/**
	 * 容器内的物品变化
	 */
	private Map<Integer, List<T>> updateMap = new HashMap<>();
	/**
	 * 容器内的物品变化
	 */
	private Map<Integer, List<T>> insertMap = new HashMap<>();
	/**
	 * 容器内的物品变化
	 */
	private Map<Integer, List<T>> deleteMap = new HashMap<>();

	public void addDBUpdate(T t) {
		if (!dbUpdate.contains(t)) {
			dbUpdate.add(t);
		}

	}

	public void addDBInsert(T t) {
		if (!dbInsert.contains(t)) {
			dbInsert.add(t);
		}
	}

	public void addDBDelete(T t) {
		if (!dbDelete.contains(t)) {
			dbDelete.add(t);
		}
	}

	public List<T> getDbUpdate() {
		return dbUpdate;
	}

	public List<T> getDbInsert() {
		return dbInsert;
	}

	public List<T> getDbDelete() {
		return dbDelete;
	}

	public Map<Integer, List<T>> getUpdateMap() {
		return updateMap;
	}

	public Map<Integer, List<T>> getInsertMap() {
		return insertMap;
	}

	public Map<Integer, List<T>> getDeleteMap() {
		return deleteMap;
	}

	public List<T> getUpdateMap(int contarnerId) {
		List<T> list = updateMap.get(contarnerId);
		if (list != null) {
			return list;
		} else {
			return new ArrayList<>();
		}
	}

	public List<T> getInsertMap(int contarnerId) {
		List<T> list = insertMap.get(contarnerId);
		if (list != null) {
			return list;
		} else {
			return new ArrayList<>();
		}
	}

	public List<T> getDeleteMap(int contarnerId) {
		List<T> list = deleteMap.get(contarnerId);
		if (list != null) {
			return list;
		} else {
			return new ArrayList<>();
		}
	}

	public void putUpdateMap(int containerId, T t) {
		List<T> list = updateMap.get(containerId);
		if (list == null) {
			list = new ArrayList<>();
			updateMap.put(containerId, list);
		}
		if (!list.contains(t)) {
			list.add(t);
		}
	}

	public void putInsertMap(int containerId, T t) {
		List<T> list = insertMap.get(containerId);
		if (list == null) {
			list = new ArrayList<>();
			insertMap.put(containerId, list);
		}
		if (!list.contains(t)) {
			list.add(t);
		}

	}

	public void putDeleteMap(int containerId, T t) {
		List<T> list = updateMap.get(containerId);
		if (list == null) {
			list = new ArrayList<>();
			deleteMap.put(containerId, list);
		}
		if (!list.contains(t)) {
			list.add(t);
		}
	}

	public void createInsertResult(int containerId, T t) {
		addDBInsert(t);
		putInsertMap(containerId, t);
	}

	public void createUpdateResult(int containerId, T t) {
		addDBUpdate(t);
		putUpdateMap(containerId, t);
	}

	public void createDeleteResult(int containerId, T t) {
		addDBDelete(t);
		putDeleteMap(containerId, t);
	}

	public void merge(ContainerResult<T> rt) {
		// insertMap.putAll(rt.getInsertMap());
		// updateMap.putAll(rt.getUpdateMap());
		// deleteMap.putAll(rt.getDeleteMap());
		Iterator<Entry<Integer, List<T>>> it = rt.getInsertMap().entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<Integer, List<T>> entry = it.next();
			int key = entry.getKey();
			List<T> list = entry.getValue();

			if (insertMap.get(key) == null)
				insertMap.put(key, list);
			else
				insertMap.get(key).addAll(list);
		}

		it = rt.getUpdateMap().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, List<T>> entry = it.next();
			int key = entry.getKey();
			List<T> list = entry.getValue();

			if (updateMap.get(key) == null)
				updateMap.put(key, list);
			else
				updateMap.get(key).addAll(list);
		}

		it = rt.getDeleteMap().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, List<T>> entry = it.next();
			int key = entry.getKey();
			List<T> list = entry.getValue();

			if (deleteMap.get(key) == null)
				deleteMap.put(key, list);
			else
				deleteMap.get(key).addAll(list);
		}

		dbInsert.addAll(rt.getDbInsert());
		dbUpdate.addAll(rt.getDbUpdate());
		dbDelete.addAll(rt.getDbDelete());
	}

	public void Print() {

		System.out.println("---- inser map ----");

		for (int key : insertMap.keySet()) {
			List<T> t = insertMap.get(key);
			System.out.println("  -| size |-  " + t.size());
			for (int i = 0; i < t.size(); i++) {
				System.out.println("  item  [id] = " + t.get(i).getId()
						+ ",[girdId] = " + t.get(i).getGridId() + "[bagId] = "
						+ key);
			}
		}

		System.out.println("---- update map ----");
		for (int key : updateMap.keySet()) {
			List<T> t = updateMap.get(key);
			System.out.println("  -| size |-  " + t.size());
			for (int i = 0; i < t.size(); i++) {
				System.out.println("  item  [id] = " + t.get(i).getId()
						+ ",[girdId] = " + t.get(i).getGridId() + "[bagId] = "
						+ key);
			}
		}

		// for (List<T> t : updateMap.values()) {
		// System.out.println("  -| size |-  " + t.size());
		// for (int i = 0; i < t.size(); i++) {
		// System.out.println("  item  [id] = " + t.get(i).getId()
		// + ",[girdId] = " + t.get(i).getGridId());
		// }
		//
		// }

		System.out.println("---- delete map ----");
		for (int key : deleteMap.keySet()) {
			List<T> t = deleteMap.get(key);
			System.out.println("  -| size |-  " + t.size());
			for (int i = 0; i < t.size(); i++) {
				System.out.println("  item  [id] = " + t.get(i).getId()
						+ ",[girdId] = " + t.get(i).getGridId() + "[bagId] = "
						+ key);
			}
		}
		// for (List<T> t : deleteMap.values()) {
		// System.out.println("  -| size |-  " + t.size());
		// for (int i = 0; i < t.size(); i++) {
		// System.out.println("  item  [id] = " + t.get(i).getId()
		// + ",[girdId] = " + t.get(i).getGridId());
		// }
		//
		// }

	}
}
