package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.exception.JoinRoomException;

public abstract class RoomEventHandler extends BaseEventHandler {
	public abstract void onJoinRoom(Room roomToJoin, String password) throws JoinRoomException;
	public abstract void onLeaveRoom(User userToLeave, Room roomToLeave);
}
