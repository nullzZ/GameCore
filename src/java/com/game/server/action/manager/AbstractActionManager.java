package com.game.server.action.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;

import com.game.server.action.AbstractAction;
import com.game.server.action.BaseAction;

@SuppressWarnings("rawtypes")
public abstract class AbstractActionManager implements InitializingBean {

	@Resource
	private List<BaseAction> all;

	protected Map<String, AbstractAction> actions = new HashMap<String, AbstractAction>();

	@Override
	public void afterPropertiesSet() {
		for (BaseAction b : all) {
			actions.put(b.getClass().getCanonicalName(), b);
		}
	}

	public AbstractAction getAction(String actionName) {
		return actions.get(actionName);
	}

}
