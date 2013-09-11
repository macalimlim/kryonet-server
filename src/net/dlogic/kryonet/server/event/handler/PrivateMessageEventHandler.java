package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.User;

public abstract class PrivateMessageEventHandler extends BaseEventHandler {
	public abstract void onPrivateMessage(String message, User targetUser);
}
