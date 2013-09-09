package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.response.PublicMessageResponse;

public abstract class PublicMessageEventHandler extends BaseEventHandler {
	public abstract void onPublicMessage(String message, Room targetRoom);
	public void sendPublicMessageResponse(String message, User fromUser) {
		PublicMessageResponse response = new PublicMessageResponse();
		response.fromUser = fromUser;
		response.message = message;
		connection.sendTCP(response);
	}
}
