package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.manager.RoomManagerInstance;
import net.dlogic.kryonet.common.response.ErrorResponse;
import net.dlogic.kryonet.common.response.GetRoomsResponse;
import net.dlogic.kryonet.common.response.JoinRoomFailureResponse;
import net.dlogic.kryonet.common.response.JoinRoomSuccessResponse;
import net.dlogic.kryonet.common.response.LeaveRoomResponse;
import net.dlogic.kryonet.common.response.LoginFailureResponse;
import net.dlogic.kryonet.common.response.LoginSuccessResponse;
import net.dlogic.kryonet.common.response.LogoutResponse;
import net.dlogic.kryonet.common.response.PrivateMessageResponse;
import net.dlogic.kryonet.common.response.PublicMessageResponse;
import net.dlogic.kryonet.common.utility.IForEach;
import net.dlogic.kryonet.server.KryonetServerException;
import net.dlogic.kryonet.server.KryonetServerInstance;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public abstract class BaseEventHandler {
	public Server endpoint;
	public User sender;
	public BaseEventHandler() {
		try {
			endpoint = KryonetServerInstance.getInstance().endpoint;
		} catch (KryonetServerException e) {
			e.printStackTrace();
		}
	}
	public void sendErrorResponse(String errorMessage) {
		ErrorResponse response = new ErrorResponse();
		response.errorMessage = errorMessage;
		endpoint.sendToTCP(sender.id, response);
	}
	public void sendGetRoomsResponse(Room[] rooms) {
		GetRoomsResponse response = new GetRoomsResponse();
		response.rooms = rooms;
		endpoint.sendToTCP(sender.id, response);
	}
	public void sendJoinRoomSuccessResponse(final User joinedUser, final Room joinedRoom) {
		Log.debug("BaseEventHandler.sendJoinRoomSuccessResponse()");
		joinedRoom.forEachUser(new IForEach<User>() {
			public void exec(User entity) {
				JoinRoomSuccessResponse response = new JoinRoomSuccessResponse();
				response.userJoined = joinedUser;
				response.roomJoined = joinedRoom;
				endpoint.sendToTCP(entity.id, response);
			}
		});
	}
	public void sendJoinRoomFailureResponse(String errorMessage) {
		JoinRoomFailureResponse response = new JoinRoomFailureResponse();
		response.errorMessage = errorMessage;
		endpoint.sendToTCP(sender.id, response);
	}
	public void sendLeaveRoomResponse() {
		RoomManagerInstance.manager.forEachRoom(new IForEach<Room>() {
			public void exec(Room entity) {
				if (entity.users.containsKey(sender.id)) {
					sendLeaveRoomResponse(entity);
				}
			}
		});
	}
	public void sendLeaveRoomResponse(final Room roomToLeave) {
		roomToLeave.forEachUser(new IForEach<User>() {
			public void exec(User entity) {
				LeaveRoomResponse response = new LeaveRoomResponse();
				response.userLeft = sender;
				response.roomLeft = roomToLeave;
				endpoint.sendToTCP(entity.id, response);
			}
		});
	}
	public final void sendLoginSuccessResponse() {
		Log.debug("BaseEventHandler.sendLoginSuccessResponse()");
		LoginSuccessResponse response = new LoginSuccessResponse();
		response.myself = sender;
		endpoint.sendToTCP(sender.id, response);
	}
	public final void sendLoginFailureResponse(String errorMessage) {
		LoginFailureResponse response = new LoginFailureResponse();
		response.errorMessage = errorMessage;
		endpoint.sendToTCP(sender.id, response);
	}
	public void sendLogoutResponse() {
		LogoutResponse response = new LogoutResponse();
		endpoint.sendToTCP(sender.id, response);
	}
	public void sendPrivateMessageResponse(User targetUser, String message) {
		PrivateMessageResponse response = new PrivateMessageResponse();
		response.sender = sender;
		response.message = message;
		endpoint.sendToTCP(targetUser.id, response);
	}
	public void sendPublicMessageResponse(Room targetRoom, final String message) {
		targetRoom.forEachUser(new IForEach<User>() {
			public void exec(User entity) {
				if (sender.id != entity.id) {
					PublicMessageResponse response = new PublicMessageResponse();
					response.sender = sender;
					response.message = message;
					endpoint.sendToTCP(entity.id, response);
				}
			}
		});
	}
}
