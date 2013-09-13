package net.dlogic.kryonet.server;

import java.util.Iterator;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.exception.JoinRoomException;
import net.dlogic.kryonet.common.exception.LoginException;
import net.dlogic.kryonet.common.manager.RoomManager;
import net.dlogic.kryonet.common.manager.RoomManagerInstance;
import net.dlogic.kryonet.common.manager.UserManager;
import net.dlogic.kryonet.common.manager.UserManagerInstance;
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
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.reflectasm.ConstructorAccess;

public class KryonetServerListener extends Listener {
	private Server server;
	private UserManager userManager;
	private RoomManager roomManager;
	private Class<? extends RoomEventHandler> roomEventHandler;
	private Class<? extends LoginOrLogoutEventHandler> loginOrLogoutEventHandler;
	private Class<? extends PersonMessageEventHandler> personMessageEventHandler;
	public KryonetServerListener(Server server) {
		this.server = server;
		userManager = UserManagerInstance.getInstance();
		roomManager = RoomManagerInstance.getInstance();
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
				handler.server = server;
				handler.sender = sender;
				Room targetRoom = roomManager.get(request.roomToJoin.id);
				handler.onJoinRoom(targetRoom, request.password);
				targetRoom.userList.add(sender);
				handler.sendJoinRoomSuccessResponse(sender, targetRoom);
			} catch (JoinRoomException ex) {
				handler.sendJoinRoomFailureResponse(ex.getMessage());
			}
		} else if (object instanceof LeaveRoomRequest) {
			LeaveRoomRequest request = (LeaveRoomRequest)object;
			ConstructorAccess<? extends RoomEventHandler> access = ConstructorAccess.get(roomEventHandler);
			RoomEventHandler handler = access.newInstance();
			handler.server = server;
			handler.sender = sender;
			Room targetRoom = roomManager.get(request.roomToLeave.id); 
			handler.onLeaveRoom(targetRoom);
			handler.sendLeaveRoomResponse(sender, targetRoom);
			targetRoom.userList.remove(sender);
		} else if (object instanceof LoginRequest) {
			LoginRequest request = (LoginRequest)object;
			ConstructorAccess<? extends LoginOrLogoutEventHandler> access = ConstructorAccess.get(loginOrLogoutEventHandler);
			LoginOrLogoutEventHandler handler = access.newInstance();
			try {
				handler.server = server;
				handler.sender = sender;
				handler.onLogin(request.username, request.password);
				sender.username = request.username;
				handler.sendLoginSuccessResponse();
			} catch (LoginException ex) {
				handler.sendLoginFailureResponse(ex.getMessage());
			}
		} else if (object instanceof LogoutRequest) {
			//LogoutRequest request = (LogoutRequest)object;
			ConstructorAccess<? extends LoginOrLogoutEventHandler> access = ConstructorAccess.get(loginOrLogoutEventHandler);
			LoginOrLogoutEventHandler handler = access.newInstance();
			handler.server = server;
			handler.sender = sender;
			handler.onLogout();
			Iterator<Room> it = roomManager.iterator();
			while (it.hasNext()) {
				Room room = it.next();
				if (room.userList.contains(sender)) {
					handler.sendLeaveRoomResponse(sender, room);
					room.userList.remove(sender);
				}
			}
			handler.sendLogoutResponse();
		} else if (object instanceof PrivateMessageRequest) {
			PrivateMessageRequest request = (PrivateMessageRequest)object;
			ConstructorAccess<? extends PersonMessageEventHandler> access = ConstructorAccess.get(personMessageEventHandler);
			PersonMessageEventHandler handler = access.newInstance();
			handler.server = server;
			handler.sender = sender;
			User targetUser = userManager.get(request.targetUserId);
			handler.onPrivateMessage(targetUser, request.message);
			handler.sendPrivateMessageResponse(targetUser, request.message);
		} else if (object instanceof PublicMessageRequest) {
			PublicMessageRequest request = (PublicMessageRequest)object;
			ConstructorAccess<? extends PersonMessageEventHandler> access = ConstructorAccess.get(personMessageEventHandler);
			PersonMessageEventHandler handler = access.newInstance();
			handler.server = server;
			handler.sender = sender;
			Room targetRoom = roomManager.get(request.targetRoomId);
			handler.onPublicMessage(targetRoom, request.message);
		}
	}
	public void idle(Connection connection) {
		// TODO Auto-generated method stub
		super.idle(connection);
	}
}
