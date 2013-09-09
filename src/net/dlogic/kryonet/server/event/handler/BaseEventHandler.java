package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.User;

public abstract class BaseEventHandler {
	protected User sender;
	public void setSender(User sender) {
		this.sender = sender;
	}
}
