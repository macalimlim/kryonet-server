package net.dlogic.kryonet.server.event.handler;

import java.util.Iterator;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.response.PublicMessageResponse;

public abstract class PublicMessageEventHandler extends BaseEventHandler {
	public abstract void onPublicMessage(String message, Room targetRoom);
	public void sendPublicMessageResponse(String message, User sender, Room target) {
		PublicMessageResponse response = new PublicMessageResponse();
		response.sender = sender;
		response.message = message;
		Iterator<User> it = target.getUserList().iterator();
		while (it.hasNext()) {
			it.next().getConnection().sendTCP(response);
		}
	}
}
