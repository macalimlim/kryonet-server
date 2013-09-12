package net.dlogic.kryonet.server;

import java.util.Iterator;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.exception.JoinRoomException;
import net.dlogic.kryonet.common.exception.LoginException;
import net.dlogic.kryonet.common.manager.RoomManager;
import net.dlogic.kryonet.common.manager.UserManager;
import net.dlogic.kryonet.common.request.JoinRoomRequest;
import net.dlogic.kryonet.common.request.LeaveRoomRequest;
import net.dlogic.kryonet.common.request.LoginRequest;
import net.dlogic.kryonet.common.request.LogoutRequest;
import net.dlogic.kryonet.common.request.PrivateMessageRequest;
import net.dlogic.kryonet.common.request.PublicMessageRequest;
import net.dlogic.kryonet.server.event.handler.LoginOrLogoutEventHandler;
import net.dlogic.kryonet.server.event.handler.PersonMessageEventHandler;
import net.dlogic.kryonet.server.event.handler.RoomEventHandler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.reflectasm.ConstructorAccess;

public class KryonetServerListener extends Listener {
	private UserManager userManager;
	private RoomManager roomManager;
	private Class<? extends RoomEventHandler> roomEventHandler;
	private Class<? extends LoginOrLogoutEventHandler> loginOrLogoutEventHandler;
	private Class<? extends PersonMessageEventHandler> personMessageEventHandler;
	public KryonetServerListener() {
		userManager = new UserManager();
		roomManager = new RoomManager();
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
		user.setConnection(connection);
		userManager.put(connection.getID(), user);
	}
	public void disconnected(Connection connection) {
		// TODO Auto-generated method stub
		super.disconnected(connection);
	}
	public void received(Connection connection, Object object) {
		User sender = userManager.get(connection.getID());
		if (object instanceof JoinRoomRequest) {
			JoinRoomRequest request = (JoinRoomRequest)object;
			ConstructorAccess<? extends RoomEventHandler> access = ConstructorAccess.get(roomEventHandler);
			RoomEventHandler handler = access.newInstance();
			try {
				handler.sender = sender;
				Room targetRoom = roomManager.get(request.roomToJoin.getId());
				handler.onJoinRoom(targetRoom, request.password);
				handler.sendJoinRoomSuccessResponse(targetRoom);
			} catch (JoinRoomException ex) {
				handler.sendJoinRoomFailureResponse(ex.getMessage());
			}
		} else if (object instanceof LeaveRoomRequest) {
			LeaveRoomRequest request = (LeaveRoomRequest)object;
			ConstructorAccess<? extends RoomEventHandler> access = ConstructorAccess.get(roomEventHandler);
			RoomEventHandler handler = access.newInstance();
			handler.onLeaveRoom(sender, request.roomToLeave);
		} else if (object instanceof LoginRequest) {
			LoginRequest request = (LoginRequest)object;
			ConstructorAccess<? extends LoginOrLogoutEventHandler> access = ConstructorAccess.get(loginOrLogoutEventHandler);
			LoginOrLogoutEventHandler handler = access.newInstance();
			try {
				handler.sender = sender;
				handler.onLogin(request.username, request.password);
				sender.setUsername(request.username);
				handler.sendLoginSuccessResponse();
			} catch (LoginException ex) {
				handler.sendLoginFailureResponse(ex.getMessage());
			}
		} else if (object instanceof LogoutRequest) {
			//LogoutRequest request = (LogoutRequest)object;
			ConstructorAccess<? extends LoginOrLogoutEventHandler> access = ConstructorAccess.get(loginOrLogoutEventHandler);
			LoginOrLogoutEventHandler handler = access.newInstance();
			handler.sender = sender;
			handler.onLogout();
			Iterator<Room> it = roomManager.iterator();
			while (it.hasNext()) {
				Room room = it.next();
				if (room.containsUser(sender)) {
					handler.sendLeaveRoomResponse(sender, room);
				}
			}
			handler.sendLogoutResponse();
		} else if (object instanceof PrivateMessageRequest) {
			PrivateMessageRequest request = (PrivateMessageRequest)object;
			ConstructorAccess<? extends PersonMessageEventHandler> access = ConstructorAccess.get(personMessageEventHandler);
			PersonMessageEventHandler handler = access.newInstance();
			handler.sender = sender;
			handler.onPrivateMessage(request.message, request.targetUser);
		} else if (object instanceof PublicMessageRequest) {
			PublicMessageRequest request = (PublicMessageRequest)object;
			ConstructorAccess<? extends PersonMessageEventHandler> access = ConstructorAccess.get(personMessageEventHandler);
			PersonMessageEventHandler handler = access.newInstance();
			handler.sender = sender;
			handler.onPublicMessage(request.message, request.targetRoom);
		}
	}
	public void idle(Connection connection) {
		// TODO Auto-generated method stub
		super.idle(connection);
	}
}
