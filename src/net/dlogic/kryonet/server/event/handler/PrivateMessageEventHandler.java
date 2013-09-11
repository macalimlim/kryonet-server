package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.response.PrivateMessageResponse;

public abstract class PrivateMessageEventHandler extends BaseEventHandler {
	public abstract void onPrivateMessage(String message, User targetUser);
	public void sendPrivateMessageResponse(String message, User sender, User target) {
		PrivateMessageResponse response = new PrivateMessageResponse();
		response.sender = sender;
		response.message = message;
		target.getConnection().sendTCP(response);
	}
}
