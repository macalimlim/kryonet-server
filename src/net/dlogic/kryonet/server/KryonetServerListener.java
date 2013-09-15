package net.dlogic.kryonet.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import net.dlogic.kryonet.server.event.handler.LoginOrLogoutEventHandler;
import net.dlogic.kryonet.server.event.handler.PersonMessageEventHandler;
import net.dlogic.kryonet.server.event.handler.RoomEventHandler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.reflectasm.ConstructorAccess;

public class KryonetServerListener extends Listener {
	private UserManager userManager;
	private RoomManager roomManager;
	private Class<GenericEventHandler> genereicEventHandler;
	private Class<? extends ConnectionEventHandler> connectionEventHandler;
	private Class<? extends RoomEventHandler> roomEventHandler;
	private Class<? extends LoginOrLogoutEventHandler> loginOrLogoutEventHandler;
	private Class<? extends PersonMessageEventHandler> personMessageEventHandler;
	public KryonetServerListener() {
		Log.info("KryonetServerListener()");
		userManager = UserManagerInstance.userManager;
		roomManager = RoomManagerInstance.roomManager;
	}
	public void setConnectionEventHandler(Class<? extends ConnectionEventHandler> handler) {
		connectionEventHandler = handler;
	}
	public void setRoomEventHandler(Class<? extends RoomEventHandler> handler) {
		roomEventHandler = handler;
	}
	public void setLoginOrLogoutEventHandler(Class<? extends LoginOrLogoutEventHandler> handler) {
		loginOrLogoutEventHandler = handler;
	}
	public void setPersonMessageEventHandler(Class<? extends PersonMessageEventHandler> handler) {
		personMessageEventHandler = handler;
	}
	public void connected(Connection connection) {
		User user = new User();
		user.id = connection.getID();
		userManager.put(user.id, user);
	}
	public void disconnected(Connection connection) {
		ConstructorAccess<? extends ConnectionEventHandler> access = ConstructorAccess.get(connectionEventHandler);
		ConnectionEventHandler handler = access.newInstance();
		User sender = userManager.get(connection.getID());
		handler.sender = sender;
		handler.onDisconnected();
		handler.sendLeaveRoomResponse();
		userManager.remove(sender.id);
	}
	public void received(Connection connection, Object object) {
		User sender = userManager.get(connection.getID());
		if (object instanceof GetRoomsRequest) {
			GetRoomsRequest request = (GetRoomsRequest)object;
			ConstructorAccess<GenericEventHandler> access = ConstructorAccess.get(genereicEventHandler);
			GenericEventHandler handler = access.newInstance();
			Iterator<Room> it = RoomManagerInstance.roomManager.iterator();
			List<Room> roomList = new ArrayList<Room>();
			while (it.hasNext()) {
				Room room = it.next();
				if (room.name.equalsIgnoreCase(request.search)) {
					roomList.add(room);
				}
			}
			handler.sendGetRoomsResponse(roomList);
		} else if (object instanceof JoinRoomRequest) {
			JoinRoomRequest request = (JoinRoomRequest)object;
			ConstructorAccess<? extends RoomEventHandler> access = ConstructorAccess.get(roomEventHandler);
			RoomEventHandler handler = access.newInstance();
			try {
				handler.sender = sender;
				Room targetRoom = roomManager.get(request.targetRoomId);
				handler.onJoinRoom(targetRoom, request.password);
				roomManager.addUserToRoom(sender, targetRoom.id);
				handler.sendJoinRoomSuccessResponse(sender, targetRoom);
			} catch (JoinRoomException e) {
				handler.sendJoinRoomFailureResponse(e.getMessage());
			} catch (RoomManagerException e) {
				handler.sendJoinRoomFailureResponse(e.getMessage());
			}
		} else if (object instanceof LeaveRoomRequest) {
			LeaveRoomRequest request = (LeaveRoomRequest)object;
			ConstructorAccess<? extends RoomEventHandler> access = ConstructorAccess.get(roomEventHandler);
			RoomEventHandler handler = access.newInstance();
			handler.sender = sender;
			Room targetRoom = roomManager.get(request.targetRoomId); 
			handler.onLeaveRoom(targetRoom);
			handler.sendLeaveRoomResponse(targetRoom);
			roomManager.removeUserToRoom(sender, targetRoom.id);
		} else if (object instanceof LoginRequest) {
			LoginRequest request = (LoginRequest)object;
			ConstructorAccess<? extends LoginOrLogoutEventHandler> access = ConstructorAccess.get(loginOrLogoutEventHandler);
			LoginOrLogoutEventHandler handler = access.newInstance();
			try {
				handler.sender = sender;
				handler.onLogin(request.username, request.password);
				sender.username = request.username;
				handler.sendLoginSuccessResponse();
			} catch (LoginException e) {
				handler.sendLoginFailureResponse(e.getMessage());
			}
		} else if (object instanceof LogoutRequest) {
			ConstructorAccess<? extends LoginOrLogoutEventHandler> access = ConstructorAccess.get(loginOrLogoutEventHandler);
			LoginOrLogoutEventHandler handler = access.newInstance();
			handler.sender = sender;
			handler.onLogout();
			handler.sendLeaveRoomResponse();
			handler.sendLogoutResponse();
			userManager.remove(sender.id);
		} else if (object instanceof PrivateMessageRequest) {
			PrivateMessageRequest request = (PrivateMessageRequest)object;
			ConstructorAccess<? extends PersonMessageEventHandler> access = ConstructorAccess.get(personMessageEventHandler);
			PersonMessageEventHandler handler = access.newInstance();
			handler.sender = sender;
			User targetUser = userManager.get(request.targetUserId);
			handler.onPrivateMessage(targetUser, request.message);
			handler.sendPrivateMessageResponse(targetUser, request.message);
		} else if (object instanceof PublicMessageRequest) {
			PublicMessageRequest request = (PublicMessageRequest)object;
			ConstructorAccess<? extends PersonMessageEventHandler> access = ConstructorAccess.get(personMessageEventHandler);
			PersonMessageEventHandler handler = access.newInstance();
			handler.sender = sender;
			Room targetRoom = roomManager.get(request.targetRoomId);
			handler.onPublicMessage(targetRoom, request.message);
			handler.sendPublicMessageResponse(targetRoom, request.message);
		}
	}
	public void idle(Connection connection) {
		super.idle(connection);
	}
}
