package com.game.server.container.inf;

import java.util.List;

import com.game.server.container.AbstractContainer;
import com.game.server.container.AbstractContainerItem;
import com.game.server.container.ContainerResult;
import com.game.server.container.comparator.ContainerComparator;
import com.game.server.container.excute.ContainerIsFulExcution;
import com.game.server.container.excute.ContainerTypeNotIsSame;
import com.game.server.container.excute.GridIsErroExcution;
import com.game.server.container.excute.ItemIsLockedExcution;
import com.game.server.container.excute.ItemIsNullExcution;
import com.game.server.container.excute.TargetContainerIsFulExcution;

public interface AbstractContainerInterface<T extends AbstractContainerItem> {

	/**
	 * 获取第一个空位置
	 * 
	 * @return
	 */
	public abstract int getFirstEmptyGrid();

	/**
	 * 获取第一个物品
	 * 
	 * @return
	 */
	public abstract T getFirstItem();

	/**
	 * 是否有{count}多个物品
	 * 
	 * @param count
	 * @return
	 */
	public abstract boolean isHasCountEmptyGrid(int count);

	/**
	 * 是否满
	 * 
	 * @return
	 */
	public abstract boolean isFull();

	/**
	 * 获取容器的物品数量
	 * 
	 * @return
	 */
	public abstract int getSize(boolean isFree);

	/**
	 * 获取容器的最大数量
	 * 
	 * @return
	 */
	public abstract int getMaxSize();

	/**
	 * 创建新的物品初始化到容器中
	 * 
	 * @param t
	 * @return
	 * @throws GridIsErroExcution
	 */
	public void createItemToInit(T t) throws GridIsErroExcution;

	/**
	 * 创建新的物品放入空位置上
	 * 
	 * @param t
	 * @return
	 * @throws ItemIsNullExcution
	 * @throws GridIsErroExcution
	 * @throws ContainerIsFulExcution
	 * @throws ItemIsLockedExcution
	 * @throws TargetContainerIsFulExcution
	 */
	public abstract ContainerResult<T> createItemToEmtyGrid(T t)
			throws ItemIsNullExcution, GridIsErroExcution,
			ContainerIsFulExcution, ItemIsLockedExcution,
			TargetContainerIsFulExcution, ContainerTypeNotIsSame;

	public abstract void clear();

	/**
	 * 移动到其他容器的一个位置上
	 * 
	 * @param item
	 * @param container
	 * @param contaninerGridId
	 * @return
	 * @throws ItemIsNullExcution
	 * @throws GridIsErroExcution
	 * @throws ContainerIsFulExcution
	 * @throws ItemIsLockedExcution
	 * @throws TargetContainerIsFulExcution
	 */
	public ContainerResult<T> moveToOtherGrid(T t,
			AbstractContainer<T> container, int contaninerGridId)
			throws ItemIsNullExcution, GridIsErroExcution,
			ItemIsLockedExcution, TargetContainerIsFulExcution,
			ContainerTypeNotIsSame;

	/**
	 * 移动到其他容器的空位置上
	 * 
	 * @param item
	 * @param container
	 * @return
	 * @throws ItemIsNullExcution
	 * @throws GridIsErroExcution
	 * @throws ContainerIsFulExcution
	 * @throws ItemIsLockedExcution
	 * @throws TargetContainerIsFulExcution
	 */
	public ContainerResult<T> moveToOtherGrid(T t,
			AbstractContainer<T> container) throws ItemIsNullExcution,
			GridIsErroExcution, ItemIsLockedExcution,
			TargetContainerIsFulExcution, ContainerTypeNotIsSame;

	public abstract T outputContainerItemByGridId(int gridId);

	public abstract T outputContainerItemByUid(long uId);

	public abstract T outputContainerItem(T t);

	public abstract List<T> outputContainerItemByGridIdAll(List<T> items);

	public abstract T lockItem(int gridId);

	public abstract ContainerResult<T> sort();

	public abstract ContainerResult<T> sort(
			ContainerComparator<T> containerComparator);

	public abstract T getItem(int gridId);

}
