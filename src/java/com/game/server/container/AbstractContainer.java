package com.game.server.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.server.container.comparator.ContainerComparator;
import com.game.server.container.excute.ContainerIsFulExcution;
import com.game.server.container.excute.ContainerTypeNotIsSame;
import com.game.server.container.excute.GridIsErroExcution;
import com.game.server.container.excute.ItemIsLockedExcution;
import com.game.server.container.excute.ItemIsNullExcution;
import com.game.server.container.excute.ItemNotFindExcution;
import com.game.server.container.excute.ItemNunNotEnoughExcution;
import com.game.server.container.excute.LimtSizeInsufficientExcution;
import com.game.server.container.excute.TargetContainerIsFulExcution;
import com.game.server.container.inf.AbstractContainerInterface;
import com.game.server.container.log.LogDebug;

/**
 * 容器抽象类
 * 
 * @author null
 * 
 * @param <T>
 */
public class AbstractContainer<T extends AbstractContainerItem> implements
		AbstractContainerInterface<T> {
	private Map<Integer, T> items = new HashMap<>();// key:格子位置,value:物品
	private int type;// 容器类型
	private int id;// 容器ID
	private int maxSize;
	private boolean isMove;// 可以在本容器中移动
	private boolean isInput; // 可放入物品
	private boolean isOutput; // 可以拿出物品
	private boolean isDelete;// 可以在本容器中删除
	private int[] limt; // 格子装备限制

	public AbstractContainer(int id, int type, int maxSize, boolean isMove) {
		this.id = id;
		this.maxSize = maxSize;
		this.isMove = isMove;
		this.type = type;
		this.limt = new int[maxSize];
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public Map<Integer, T> getItems() {
		return items;
	}

	/**
	 * 获取第一个空格子
	 * 
	 * @return
	 */
	@Override
	public int getFirstEmptyGrid() {
		for (int i = 1; i <= this.maxSize; i++) {
			if (!items.containsKey(i))
				return i;
		}
		return -1;
	}

	@Override
	public T getFirstItem() {
		for (int i = 1; i <= this.maxSize; i++) {
			T item = items.get(i);
			if (item != null)
				return item;
		}
		return null;
	}

	public T getItemByUid(long uId) {
		for (T t : items.values()) {
			if (t.getuId() == uId) {
				return t;
			}
		}
		return null;
	}

	public List<T> getItemBySn(int sn) {
		List<T> ls = new ArrayList<>();
		for (T t : items.values()) {
			if (t.getId() == sn) {
				ls.add(t);
			}
		}
		return ls;
	}

	public int getItemTotalNumberBySn(int sn) {
		int n = 0;
		for (T t : items.values()) {
			if (t.getId() == sn) {
				n += t.getNumber();
			}
		}
		return n;
	}

	/**
	 * 获取格子上的物品
	 * 
	 * @param gridId
	 * @return
	 */
	private T getGridItem(int gridId) {
		return items.get(gridId);
	}

	@Override
	public int getSize(boolean isFree) {
		if (isFree)
			return (maxSize - items.size());

		return items.size();
	}

	@Override
	public int getMaxSize() {
		return maxSize;
	}

	@Override
	public boolean isFull() {
		return getSize(false) >= maxSize;
	}

	@Override
	public T getItem(int gridId) {
		return this.items.get(gridId);
	}

	public int[] getLimt() {
		return limt;
	}

	public void setLimt(int[] limt) throws LimtSizeInsufficientExcution {
		this.limt = limt;
		if (this.limt.length != maxSize)
			throw new LimtSizeInsufficientExcution();
	}

	private void putItem(T item, int gridId) {
		item.setContainerId(this.id);
		item.setGridId(gridId);
		items.put(item.getGridId(), item);
	}

	@Override
	public void createItemToInit(T t) throws GridIsErroExcution {
		if (t.getGridId() <= 0) {
			throw new GridIsErroExcution();
		}
		t.setContainerId(this.id);
		items.put(t.getGridId(), t);
	}

	@Override
	public ContainerResult<T> createItemToEmtyGrid(T t)
			throws ItemIsNullExcution, GridIsErroExcution,
			ContainerIsFulExcution, ItemIsLockedExcution,
			TargetContainerIsFulExcution, ContainerTypeNotIsSame {
		ContainerResult<T> containerResult = new ContainerResult();
		int gridId = getFirstEmptyGrid();
		if (gridId <= 0) {
			throw new ContainerIsFulExcution();
		}
		itemToOtherGrid(t, gridId);
		containerResult.addDBInsert(t);
		return containerResult;
	}

	private ContainerResult<T> itemToOtherGrid(T t, int gridId)
			throws ItemIsNullExcution, GridIsErroExcution,
			ItemIsLockedExcution, TargetContainerIsFulExcution,
			ContainerTypeNotIsSame {
		return itemToOtherGrid(t, this, gridId);
	}

	private ContainerResult<T> itemToOtherGrid(T t,
			AbstractContainer<T> container) throws ItemIsNullExcution,
			GridIsErroExcution, ItemIsLockedExcution,
			TargetContainerIsFulExcution, ContainerTypeNotIsSame {
		int contaninerGridId = container.getFirstEmptyGrid();
		if (contaninerGridId <= 0) {
			throw new TargetContainerIsFulExcution();
		}
		return itemToOtherGrid(t, container, contaninerGridId);
	}

	private ContainerResult<T> itemToOtherGrid(T t,
			AbstractContainer<T> container, int contaninerGridId)
			throws ItemIsNullExcution, GridIsErroExcution,
			ItemIsLockedExcution, TargetContainerIsFulExcution,
			ContainerTypeNotIsSame {
		// List<ContainerResult<T>> resultList = new ArrayList<>();
		ContainerResult<T> containerResult = new ContainerResult<>();
		if (this.type != container.getType()) {
			throw new ContainerTypeNotIsSame();
		}
		if (t == null) {
			throw new ItemIsNullExcution();
		}

		if (container.isFull()) {
			throw new TargetContainerIsFulExcution();
		}

		// if (this.isFull()) {// 对面容器是否满了
		// throw new ContainerIsFulExcution();
		// }

		if (t.isLockSate()) {
			throw new ItemIsLockedExcution();
		}

		int itemGrid = t.getGridId();// 这里有问题可能 这个物品是新创建出来的他的位置就是默认值了
		T targetItem = container.getGridItem(contaninerGridId);
		if (targetItem == null) {// 目标格子没有物品
			this.outputContainerItemByGridId(itemGrid);// 移除以前容器内的物品
			container.putItem(t, contaninerGridId);//
			containerResult.addDBUpdate(t);
			containerResult.putInsertMap(container.id, t);
			containerResult.putDeleteMap(this.id, t);
		} else {// 格子上有物品
			LogDebug.debug("格子上有物品↓");
			if (targetItem.isLockSate()) {
				LogDebug.debug("物品" + targetItem.getuId() + "已经被锁定");
				throw new ItemIsLockedExcution();
			}

			if (t.getId() == targetItem.getId()) {// 如果物品ID相同,代表是可以判断叠加的
				LogDebug.debug("如果物品ID相同,代表是可以判断叠加的" + "itemId:" + t.getId());
				if (targetItem.getNumber() < targetItem.maxNum) {// 可以叠加
					LogDebug.debug("可以叠加" + "num:" + targetItem.getNumber()
							+ "maxNum:" + targetItem.maxNum);
					int diffNum = targetItem.maxNum - targetItem.getNumber();// 还差几个满
					LogDebug.debug("还差几个满" + diffNum);
					int num = t.getNumber() > diffNum ? diffNum : t.getNumber();// 增几个
					targetItem.setNumber(targetItem.getNumber() + num);
					LogDebug.debug(targetItem.getuId() + "添加num:" + num + "|总:"
							+ targetItem.getNumber());
					int n = t.getNumber() - num;
					LogDebug.debug(t.getuId() + "剩余num:" + n);

					containerResult.addDBUpdate(targetItem);
					containerResult.putUpdateMap(container.id, targetItem);

					if (n <= 0) {
						this.outputContainerItem(t);// 都叠加到目标格子上了

						containerResult.addDBDelete(t);
						containerResult.putDeleteMap(this.id, t);
						LogDebug.debug(t.getuId() + "删除" + "从容器:" + this.id);
					} else {
						t.setNumber(n);// 剩下的个数

						containerResult.addDBUpdate(t);
						containerResult.putUpdateMap(this.id, t);
						LogDebug.debug("剩下的个数" + n + "itemId:" + t.getId());
					}
				}
			} else {// 如果过物品ID不同,就要交换
				// System.out.println("如果过物品ID不同,就要交换|grid" + itemGrid +
				// "itemId:"
				// + targetItem.getId() + "TGrid:" + contaninerGridId);
				this.putItem(targetItem, itemGrid);
				container.putItem(t, contaninerGridId);

				containerResult.addDBUpdate(t);
				containerResult.addDBUpdate(targetItem);
				containerResult.putUpdateMap(this.id, targetItem);
				containerResult.putUpdateMap(container.id, t);

			}
		}
		return containerResult;
	}

	@Override
	public void clear() {
		this.items.clear();
	}

	/**
	 * 是否还有count个空位置
	 * 
	 * @return
	 */
	@Override
	public boolean isHasCountEmptyGrid(int count) {
		return this.maxSize - items.size() >= count;
	}

	@Override
	public T outputContainerItemByGridId(int gridId) {
		T item = items.get(gridId);
		return outputContainerItem(item);
	}

	@Override
	public T outputContainerItemByUid(long uId) {
		T item = this.getItemByUid(uId);
		return outputContainerItem(item);
	}

	@Override
	public T outputContainerItem(T item) {
		if (item == null) {
			return null;
		}
		items.remove(item.getGridId());
		return item;
	}

	@Override
	public List<T> outputContainerItemByGridIdAll(List<T> itemList) {
		List<T> removeItems = new ArrayList<>();
		for (T t : itemList) {
			if (items.remove(t.getGridId()) != null) {
				removeItems.add(t);
			}

		}
		return removeItems;
	}

	@Override
	public T lockItem(int gridId) {
		T item = this.items.get(gridId);
		if (item == null) {
			return null;
		}
		if (item.isLockSate()) {
			item.setLockSate(false);
		} else {
			item.setLockSate(true);
		}
		return item;
	}

	public List<T> removeContainerItemByGridIdAll(List<T> itemList) {
		List<T> removeItems = new ArrayList<>();
		for (T t : itemList) {
			if (items.remove(t.getGridId()) != null) {
				removeItems.add(t);
			}

		}
		return removeItems;
	}

	public boolean removeContainerItem(T t) {
		if (t.isLockSate()) {
			return false;
		}
		items.remove(t.getGridId());
		return true;
	}

	@Override
	public ContainerResult<T> sort() {
		ContainerResult<T> result = new ContainerResult<>();
		Map<Integer, T> newItems = new HashMap<>();// key:格子位置,value:物品
		int gridId = 1;
		for (int i = 1; i <= this.maxSize; i++) {
			T item = items.get(i);
			if (item != null) {
				if (gridId != item.getGridId()) {
					item.setGridId(gridId);
					result.addDBUpdate(item);
					result.putUpdateMap(this.id, item);
				}
				newItems.put(gridId, item);
				gridId++;
			}
		}
		items.clear();
		items.putAll(newItems);
		return result;
	}

	@Override
	public ContainerResult<T> sort(ContainerComparator<T> containerComparator) {
		ContainerResult<T> result = new ContainerResult<T>();
		for (int i = 1; i <= this.maxSize; i++) {
			for (int j = i + 1; j <= this.maxSize; j++) {
				T t = items.get(i);
				T t2 = items.get(j);
				if (containerComparator.compare(t, t2) > 0) {
					if (t != null && t2 != null) {
						t.setGridId(j);
						items.put(j, t);
						result.addDBUpdate(t);
						result.putUpdateMap(this.id, t);

						t2.setGridId(i);
						items.put(i, t2);
						result.addDBUpdate(t2);
						result.putUpdateMap(this.id, t2);
					} else if (t == null && t2 != null) {
						t2.setGridId(i);
						items.put(i, t2);
						result.addDBUpdate(t2);
						result.putUpdateMap(this.id, t2);
						items.remove(j);
					} else {
						t.setGridId(j);
						items.put(j, t);
						result.addDBUpdate(t);
						result.putUpdateMap(this.id, t);
						items.remove(i);
					}
				}
			}
		}
		return result;
	}

	@Override
	public ContainerResult<T> moveToOtherGrid(T t,
			AbstractContainer<T> container, int contaninerGridId)
			throws ItemIsNullExcution, GridIsErroExcution,
			ItemIsLockedExcution, TargetContainerIsFulExcution,
			ContainerTypeNotIsSame {
		return itemToOtherGrid(t, container, contaninerGridId);
	}

	@Override
	public ContainerResult<T> moveToOtherGrid(T t,
			AbstractContainer<T> container) throws ItemIsNullExcution,
			GridIsErroExcution, ItemIsLockedExcution,
			TargetContainerIsFulExcution, ContainerTypeNotIsSame {
		return itemToOtherGrid(t, container);
	}

	/**
	 * 获取未满最大堆叠数量的物品
	 * 
	 * @param sn
	 * @return
	 */
	public T getDeficitMaxSum(int sn) {
		List<T> snList = getItemBySn(sn);
		for (int i = 0; i < snList.size(); i++) {
			T t = snList.get(i);
			if (t.getNumber() < t.maxNum)
				return t;
		}

		return null;
	}

	/**
	 * 获取未满最大堆叠数量的物品
	 * 
	 * @param sn
	 * @return list
	 */
	public List<T> getDeficitMaxSumList(int sn) {

		List<T> deficitMaxSumList = new ArrayList<T>();
		List<T> snList = getItemBySn(sn);
		for (int i = 0; i < snList.size(); i++) {
			T t = snList.get(i);
			if (t.getNumber() < t.maxNum)
				deficitMaxSumList.add(t);
		}

		return deficitMaxSumList;
	}

	/**
	 * 添加物品进容器类
	 * 
	 * @param t
	 */
	private void putInContainer(T t, int grid) {
		t.setGridId(grid);
		t.setContainerId(getId());
		items.put(t.getGridId(), t);
	}

	/**
	 * 是否可以添加物品
	 * 
	 * @param t
	 * @param id
	 * @return
	 */
	private boolean isCanAdd(T t, int id) {

		if (limt[id] == 0)
			return true;

		if (limt[id] == t.equipment_class)
			return true;

		return false;
	}

	/**
	 * 插入一件物品
	 * 
	 * @param t
	 * @param id
	 * @return
	 */
	private boolean insert(T t, int id) {
		if (items.get(id) == null) {
			if (isCanAdd(t, (id - 1))) {
				putInContainer(t, id);
				return true;
			}
		}

		return false;
	}

	public boolean add(T t) {
		if (null == t) {
			return false;
		}

		if (isFull())
			return false;

		for (int i = 1; i <= getMaxSize(); i++) {
			boolean isAdd = add(t, i);
			if (isAdd)
				return true;
		}

		return false;
	}

	private boolean add(T t, int id) {
		if (id > 0) {
			return insert(t, id);
		} else {
			return add(t);
		}
	}

	/**
	 * 按分组创建格子的对象
	 * 
	 * @param t
	 * @param number
	 * @return
	 * @throws ItemIsNullExcution
	 */
	public ContainerResult<T> createChildren(T t, int number)
			throws ItemIsNullExcution {
		ContainerResult<T> result = new ContainerResult<T>();

		int itemMaxSum = t.maxNum;
		T inBagItem = getDeficitMaxSum(t.getId());

		int c_size = number / itemMaxSum;
		int excess = (number % itemMaxSum);
		if (excess != 0) {
			c_size++;
		}

		if (inBagItem != null) {
			int dValue = itemMaxSum - inBagItem.getNumber();
			if (dValue >= number) {
				inBagItem.addSum(number);
				result.createUpdateResult(getId(), inBagItem);

				return result;
			} else {
				// 背包已满无法新加物品
				if (getSize(true) < c_size)
					return null;

				number = number - dValue;
				inBagItem.setNumber(itemMaxSum);
				result.createUpdateResult(getId(), inBagItem);

				List<T> newItems = checkItemGroup(inBagItem, number);
				for (int i = 0; i < newItems.size(); i++) {
					T nt = newItems.get(i);
					result.createInsertResult(getId(), nt);
				}
				return result;
			}
		} else {
			// 背包已满无法新加物品
			if (getSize(true) < c_size)
				return null;

			List<T> newItems = checkItemGroup(t, number);
			for (int i = 0; i < newItems.size(); i++) {
				T nt = newItems.get(i);
				result.createInsertResult(getId(), nt);
			}
			return result;
		}

	}

	/**
	 * 获取配置id删除物品结果
	 * 
	 * @param t
	 * @param num
	 * @return
	 * @throws ItemNotFindExcution
	 */
	private ContainerResult<T> getRemoveResultBySn(T t, int num)
			throws ItemNotFindExcution {
		ContainerResult<T> result = new ContainerResult<T>();

		List<T> snItems = getItemBySn(t.getId());

		int delSet = num / t.maxNum;
		int delNum = num - delSet * t.maxNum;

		for (int i = 0; i < delSet; i++) {
			outputContainerItem(snItems.get(i));
			result.createDeleteResult(this.id, snItems.get(i));
		}

		while (delNum > 0) {
			snItems = getItemBySn(t.getId());

			// 优先删除不满组的物品
			for (int i = 0; i < snItems.size(); i++) {

				if (snItems.get(i).getNumber() < snItems.get(i).maxNum) {
					int value = snItems.get(i).getNumber() - delNum;
					if (value <= 0) {
						delNum -= snItems.get(i).getNumber();
						outputContainerItem(snItems.get(i));
						result.createDeleteResult(this.id, snItems.get(i));
					} else {
						snItems.get(i).addSum(-delNum);
						delNum -= num;
						result.createUpdateResult(this.id, snItems.get(i));
					}
				}
			}

			if (delNum > 0) {
				snItems = getItemBySn(t.getId());
				if (snItems.size() > 0) {
					T deficitItem = snItems.get(snItems.size() - 1);
					deficitItem.addSum(-delNum);
					delNum = 0;
					result.createUpdateResult(this.id, deficitItem);
				}
			}
		}

		// for (int i = 0; i < snItems.size(); i++) {
		// T deficitItem = snItems.get(i);
		// if (deficitItem.getNumber() == delNum) {
		// outputContainerItem(deficitItem);
		// result.createDeleteResult(this.id, deficitItem);
		// break;
		// } else if (deficitItem.getNumber() > delNum) {
		// deficitItem.addSum(-delNum);
		// result.createUpdateResult(this.id, deficitItem);
		// break;
		// }
		// }

		// while (delNum > 0) {
		// snItems = getItemBySn(t.getId());
		// if (snItems.size() > 0) {
		//
		// T deficitItem = snItems.get((snItems.size() - 1));
		// if (deficitItem.getNumber() <= delNum) {
		// outputContainerItem(deficitItem);
		// result.createDeleteResult(this.id, deficitItem);
		// delNum -= deficitItem.getNumber();
		// } else {
		// deficitItem.addSum(-delNum);
		// result.createUpdateResult(this.id, deficitItem);
		// delNum -= deficitItem.getNumber();
		// }
		// } else {
		// break;
		// }
		// }

		return result;
	}

	/**
	 * 通过配置id删除指定数量物品
	 * 
	 * @param sn
	 * @param num
	 * @return
	 * @throws ItemNunNotEnoughExcution
	 * @throws ItemNotFindExcution
	 */
	public ContainerResult<T> removeChildrenBySn(int sn, int num)
			throws Exception {
		ContainerResult<T> result = new ContainerResult<T>();

		int n = getItemTotalNumberBySn(sn);
		// 未发现此配置物品
		if (n <= 0) {
			return result;
		}

		if (num == 0)
			return result;

		if (num > n) {
			return result;
		}

		T t = getDeficitMaxSum(sn);
		if (t == null) {
			t = getItemBySn(sn).get(0);
		}

		result = getRemoveResultBySn(t, num);

		// T t = getDeficitMaxSum(sn);
		// if (t == null) {
		// List<T> snItems = getItemBySn(sn);
		// int index = (snItems.size() - 1);
		// t = snItems.get(index);
		//
		// result = getRemoveResultBySn(t, num);
		//
		// } else {
		//
		// result = getRemoveResultBySn(t, num);
		//
		// }

		return result;
	}

	/**
	 * 自动分组创建物品
	 * 
	 * @param t
	 * @param number
	 * @return
	 * @throws ItemIsNullExcution
	 */
	private List<T> checkItemGroup(T t, int number) throws ItemIsNullExcution {

		List<T> list = new ArrayList<>();

		if (t == null) {
			throw new ItemIsNullExcution();
		}

		int itemMaxSum = t.maxNum;

		int c_size = number / itemMaxSum;
		for (int i = 0; i < c_size; i++) {
			@SuppressWarnings("unchecked")
			T clone = (T) t.split();
			clone.setNumber(itemMaxSum);
			add(clone);

			list.add(clone);
		}

		int excess = (number % itemMaxSum);
		if (excess != 0) {
			@SuppressWarnings("unchecked")
			T clone = (T) t.split();
			clone.setNumber(excess);
			add(clone);

			list.add(clone);
		}
		return list;
	}

	/**
	 * 类型匹配
	 * 
	 * @param part
	 * @param id
	 * @return
	 */
	public boolean checkLimt(int part, int id) {

		if ((limt[id] == part)) {
			return true;
		}

		return false;
	}

	public void pirnt() {
		for (T t : items.values()) {
			System.out.println("[bagId:" + getId() + "]" + ",[uId:"
					+ t.getuId() + "],[num:" + t.getNumber() + "/" + t.maxNum
					+ "]" + ",[grid:" + t.getGridId() + "]" + ",[sn: "
					+ t.getId() + "]");
		}

		// System.out.println("limt length = " + limt.length);
		//
		// for (int i = 0; i < limt.length; i++) {
		// System.out.println("limt[" + i + "] = " + limt[i]);
		// }
	}
}
