package com.game.server.container.comparator;

import com.game.server.container.AbstractContainerItem;

/**
 * 容器比较类
 * 
 * @author nullzZ
 * 
 */
public abstract class ContainerComparator<T extends AbstractContainerItem> {

	public abstract int compare(T t, T t2);
}
