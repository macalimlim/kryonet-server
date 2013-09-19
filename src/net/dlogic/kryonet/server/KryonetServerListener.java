package net.dlogic.kryonet.server;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.exception.JoinRoomException;
import net.dlogic.kryonet.common.exception.LoginException;
import net.dlogic.kryonet.common.manager.RoomManager;
import net.dlogic.kryonet.common.manager.RoomManagerException;
import net.dlogic.kryonet.common.manager.RoomManagerInstance;
import net.dlogic.kryonet.common.manager.UserManager;
import net.dlogic.kryonet.common.manager.UserManagerInstance;
import net.dlogic.kryonet.common.request.GetRoomsRequest;
import net.dlogic.kryonet.common.request.JoinRoomRequest;
import net.dlogic.kryonet.common.request.LeaveRoomRequest;
import net.dlogic.kryonet.common.request.LoginRequest;
import net.dlogic.kryonet.common.request.LogoutRequest;
import net.dlogic.kryonet.common.request.PrivateMessageRequest;
import net.dlogic.kryonet.common.request.PublicMessageRequest;
import net.dlogic.kryonet.server.event.handler.ConnectionEventHandler;
import net.dlogic.kryonet.server.event.handler.GenericEventHandler;
import net.dlogic.kryonet.server.event.handler.PersonMessageEventHandler;
import net.dlogic.kryonet.server.event.handler.RoomEventHandler;
import net.dlogic.kryonet.server.event.handler.UserEventHandler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.reflectasm.ConstructorAccess;

public class KryonetServerListener extends Listener {
	private UserManager userManager;
	private RoomManager roomManager;
	private Class<GenericEventHandler> genereicEventHandler = GenericEventHandler.class;
	private Class<? extends ConnectionEventHandler> connectionEventHandler;
	private Class<? extends RoomEventHandler> roomEventHandler;
	private Class<? extends UserEventHandler> userEventHandler;
	private Class<? extends PersonMessageEventHandler> personMessageEventHandler;
	public KryonetServerListener() {
		Log.info("KryonetServerListener()");
		userManager = UserManagerInstance.manager;
		roomManager = RoomManagerInstance.manager;
	}
	public void setConnectionEventHandler(Class<? extends ConnectionEventHandler> handler) {
		connectionEventHandler = handler;
	}
	public void setRoomEventHandler(Class<? extends RoomEventHandler> handler) {
		roomEventHandler = handler;
	}
	public void setUserEventHandler(Class<? extends UserEventHandler> handler) {
		userEventHandler = handler;
	}
	public void setPersonMessageEventHandler(Class<? extends PersonMessageEventHandler> handler) {
		personMessageEventHandler = handler;
	}
	public void connected(Connection connection) {
		ConnectionEventHandler handler = ConstructorAccess.get(connectionEventHandler).newInstance();
		User sender = new User();
		sender.id = connection.getID();
		sender.isAdmin = false;
		sender.isItMe = true;
		sender.isPlayer = false;
		handler.sender = sender;
		handler.onConnected();
		userManager.map.put(sender.id, sender);
	}
	public void disconnected(Connection connection) {
		ConnectionEventHandler handler = ConstructorAccess.get(connectionEventHandler).newInstance();
		User sender = userManager.map.get(connection.getID());
		handler.sender = sender;
		handler.onDisconnected();
		handler.sendLeaveRoomResponse();
		userManager.map.remove(sender.id);
	}
	public void received(Connection connection, Object object) {
		User sender = userManager.map.get(connection.getID());
		if (object instanceof GetRoomsRequest) {
			GetRoomsRequest request = (GetRoomsRequest)object;
			GenericEventHandler handler = ConstructorAccess.get(genereicEventHandler).newInstance();
			handler.sender = sender;
			Room[] rooms = RoomManagerInstance.manager.getRooms(request.search);
			handler.sendGetRoomsResponse(rooms);
		} else if (object instanceof JoinRoomRequest) {
			JoinRoomRequest request = (JoinRoomRequest)object;
			RoomEventHandler handler = ConstructorAccess.get(roomEventHandler).newInstance();
			try {
				handler.sender = sender;
				Room targetRoom = roomManager.map.get(request.targetRoomName);
				handler.onJoinRoom(targetRoom, request.password);
				roomManager.addUserToRoom(sender, targetRoom.name);
				handler.sendJoinRoomSuccessResponse(sender, targetRoom);
			} catch (JoinRoomException e) {
				handler.sendJoinRoomFailureResponse(e.getMessage());
			} catch (RoomManagerException e) {
				handler.sendJoinRoomFailureResponse(e.getMessage());
			}
		} else if (object instanceof LeaveRoomRequest) {
			LeaveRoomRequest request = (LeaveRoomRequest)object;
			RoomEventHandler handler = ConstructorAccess.get(roomEventHandler).newInstance();
			try {
				handler.sender = sender;
				Room targetRoom = roomManager.map.get(request.targetRoomName); 
				handler.onLeaveRoom(targetRoom);
				handler.sendLeaveRoomResponse(targetRoom);
				roomManager.removeUserFromRoom(sender, targetRoom.name);
			} catch (RoomManagerException e) {
				handler.sendErrorResponse(e.getMessage());
			}
		} else if (object instanceof LoginRequest) {
			LoginRequest request = (LoginRequest)object;
			UserEventHandler handler = ConstructorAccess.get(userEventHandler).newInstance();
			try {
				handler.sender = sender;
				handler.onLogin(request.username, request.password);
				sender.username = request.username;
				handler.sendLoginSuccessResponse();
			} catch (LoginException e) {
				handler.sendLoginFailureResponse(e.getMessage());
			}
		} else if (object instanceof LogoutRequest) {
			UserEventHandler handler = ConstructorAccess.get(userEventHandler).newInstance();
			try {
				handler.sender = sender;
				handler.onLogout();
				handler.sendLeaveRoomResponse();
				handler.sendLogoutResponse();
				userManager.map.remove(sender.id);
				roomManager.removeUserFromAllRooms(sender);
			} catch (RoomManagerException e) {
				handler.sendErrorResponse(e.getMessage());
			}
		} else if (object instanceof PrivateMessageRequest) {
			PrivateMessageRequest request = (PrivateMessageRequest)object;
			PersonMessageEventHandler handler = ConstructorAccess.get(personMessageEventHandler).newInstance();
			handler.sender = sender;
			User targetUser = userManager.map.get(request.targetUserId);
			handler.onPrivateMessage(targetUser, request.message);
			handler.sendPrivateMessageResponse(targetUser, request.message);
		} else if (object instanceof PublicMessageRequest) {
			PublicMessageRequest request = (PublicMessageRequest)object;
			PersonMessageEventHandler handler = ConstructorAccess.get(personMessageEventHandler).newInstance();
			handler.sender = sender;
			Room targetRoom = roomManager.map.get(request.targetRoomName);
			handler.onPublicMessage(targetRoom, request.message);
			handler.sendPublicMessageResponse(targetRoom, request.message);
		}
	}
	public void idle(Connection connection) {
		ConnectionEventHandler handler = ConstructorAccess.get(connectionEventHandler).newInstance();
		User sender = userManager.map.get(connection.getID());
		handler.sender = sender;
		handler.onIdle();
	}
}
