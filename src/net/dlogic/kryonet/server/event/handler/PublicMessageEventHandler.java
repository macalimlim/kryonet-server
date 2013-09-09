package net.dlogic.kryonet.server.event.handler;

import java.util.Iterator;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.response.PublicMessageResponse;

public abstract class PublicMessageEventHandler extends BaseEventHandler {
	public abstract void onPublicMessage(String message, Room targetRoom);
	public void sendPublicMessageResponse(String message, User fromUser, Room targetRoom) {
		PublicMessageResponse response = new PublicMessageResponse();
		response.fromUser = fromUser;
		response.message = message;
		Iterator<User> it = targetRoom.getUserList().iterator();
		while (it.hasNext()) {
			it.next().getConnection().sendTCP(response);
		}
	}
}
