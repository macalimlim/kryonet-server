package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;

public abstract class PersonMessageEventHandler extends BaseEventHandler {
	public abstract void onPrivateMessage(String message, User targetUser);
	public abstract void onPublicMessage(String message, Room targetRoom);
}
