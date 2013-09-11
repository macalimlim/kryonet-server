package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.Room;

public abstract class JoinRoomEventHandler extends BaseEventHandler {
	public abstract void onJoinRoom(Room roomToJoin, String password);
}
