package net.dlogic.kryonet.server.event.handler;

import java.util.Iterator;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.response.JoinRoomFailureResponse;
import net.dlogic.kryonet.common.response.JoinRoomSuccessResponse;
import net.dlogic.kryonet.common.response.LoginFailureResponse;
import net.dlogic.kryonet.common.response.LoginSuccessResponse;
import net.dlogic.kryonet.common.response.LogoutResponse;
import net.dlogic.kryonet.common.response.PrivateMessageResponse;
import net.dlogic.kryonet.common.response.PublicMessageResponse;

public abstract class BaseEventHandler {
	public User sender;
	public void sendJoinRoomSuccessResponse(Room joinedRoom) {
		JoinRoomSuccessResponse response = new JoinRoomSuccessResponse();
		response.joinedRoom = joinedRoom;
		Iterator<User> it = joinedRoom.getUserList().iterator();
		while (it.hasNext()) {
			it.next().getConnection().sendTCP(response);
		}
	}
	public void sendJoinRoomFailureResponse(String errorMessage) {
		JoinRoomFailureResponse response = new JoinRoomFailureResponse();
		response.errorMessage = errorMessage;
		sender.getConnection().sendTCP(response);
	}
	public final void sendLoginSuccessResponse() {
		LoginSuccessResponse response = new LoginSuccessResponse();
		response.myself = sender;
		sender.getConnection().sendTCP(response);
	}
	public final void sendLoginFailureResponse(String errorMessage) {
		LoginFailureResponse response = new LoginFailureResponse();
		response.errorMessage = errorMessage;
		sender.getConnection().sendTCP(response);
	}
	public void sendLogoutResponse() {
		LogoutResponse response = new LogoutResponse();
		sender.getConnection().sendTCP(response);
	}
	public void sendPrivateMessageResponse(String message, User sender, User target) {
		PrivateMessageResponse response = new PrivateMessageResponse();
		response.sender = sender;
		response.message = message;
		target.getConnection().sendTCP(response);
	}
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
