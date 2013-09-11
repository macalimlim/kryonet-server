package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.Room;

public abstract class PublicMessageEventHandler extends BaseEventHandler {
	public abstract void onPublicMessage(String message, Room targetRoom);
}
