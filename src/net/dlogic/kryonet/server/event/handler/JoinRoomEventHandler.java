package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.response.JoinRoomFailureResponse;
import net.dlogic.kryonet.common.response.JoinRoomSuccessResponse;

public abstract class JoinRoomEventHandler extends BaseEventHandler {
	public abstract void onJoinRoom(Room roomToJoin, String password);
	public void sendJoinRoomSuccess(Room joinedRoom) {
		JoinRoomSuccessResponse response = new JoinRoomSuccessResponse();
		response.joinedRoom = joinedRoom;
		connection.sendTCP(response);
	}
	public void sendJoinRoomFailure(String errorMessage) {
		JoinRoomFailureResponse response = new JoinRoomFailureResponse();
		response.errorMessage = errorMessage;
		connection.sendTCP(response);
	}
}
