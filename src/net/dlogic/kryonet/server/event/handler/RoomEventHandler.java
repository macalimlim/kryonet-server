package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;

public abstract class RoomEventHandler extends BaseEventHandler {
	public abstract void onJoinRoom(Room roomToJoin, String password);
	public abstract void onLeaveRoom(User userToLeave, Room roomToLeave);
}
