package net.dlogic.kryonet.server;

import java.util.Iterator;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.exception.LoginException;
import net.dlogic.kryonet.common.manager.RoomManager;
import net.dlogic.kryonet.common.manager.UserManager;
import net.dlogic.kryonet.common.request.JoinRoomRequest;
import net.dlogic.kryonet.common.request.LeaveRoomRequest;
import net.dlogic.kryonet.common.request.LoginRequest;
import net.dlogic.kryonet.common.request.LogoutRequest;
import net.dlogic.kryonet.common.request.PrivateMessageRequest;
import net.dlogic.kryonet.common.request.PublicMessageRequest;
import net.dlogic.kryonet.server.event.handler.JoinRoomEventHandler;
import net.dlogic.kryonet.server.event.handler.LoginEventHandler;
import net.dlogic.kryonet.server.event.handler.LogoutEventHandler;
import net.dlogic.kryonet.server.event.handler.PrivateMessageEventHandler;
import net.dlogic.kryonet.server.event.handler.PublicMessageEventHandler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.reflectasm.ConstructorAccess;

public class KryonetServerListener extends Listener {
	private UserManager userManager;
	private RoomManager roomManager;
	private Class<? extends JoinRoomEventHandler> joinRoomEventHandler;
	private Class<? extends LoginEventHandler> loginEventHandler;
	private Class<? extends LogoutEventHandler> logoutEventHandler;
	private Class<? extends PrivateMessageEventHandler> privateMessageEventHandler;
	private Class<? extends PublicMessageEventHandler> publicMessageEventHandler;
	public KryonetServerListener() {
		userManager = new UserManager();
		roomManager = new RoomManager();
	}
	public void setJoinRoomEventHandler(Class<? extends JoinRoomEventHandler> handler) {
		joinRoomEventHandler = handler;
	}
	public void setLoginEventHandler(Class<? extends LoginEventHandler> handler) {
		loginEventHandler = handler;
	}
	public void setLogoutEventHandler(Class<? extends LogoutEventHandler> handler) {
		logoutEventHandler = handler;
	}
	public void setPrivateMessageEventHandler(Class<? extends PrivateMessageEventHandler> handler) {
		privateMessageEventHandler = handler;
	}
	public void setPublicMessageEventHandler(Class<? extends PublicMessageEventHandler> handler) {
		publicMessageEventHandler = handler;
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
			ConstructorAccess<? extends JoinRoomEventHandler> access = ConstructorAccess.get(joinRoomEventHandler);
			JoinRoomEventHandler handler = access.newInstance();
			handler.sender = sender;
			handler.onJoinRoom(request.roomToJoin, request.password);
		} else if (object instanceof LeaveRoomRequest) {
			
		} else if (object instanceof LoginRequest) {
			LoginRequest request = (LoginRequest)object;
			ConstructorAccess<? extends LoginEventHandler> access = ConstructorAccess.get(loginEventHandler);
			LoginEventHandler handler = access.newInstance();
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
			ConstructorAccess<? extends LogoutEventHandler> access = ConstructorAccess.get(logoutEventHandler);
			LogoutEventHandler handler = access.newInstance();
			handler.sender = sender;
			handler.onLogout();
			Iterator<Room> it = roomManager.iterator();
			while (it.hasNext()) {
				if (it.next().containsUser(sender)) {
					//send leave room requests to every user in the room
				}
			}
			handler.sendLogoutResponse();
		} else if (object instanceof PrivateMessageRequest) {
			PrivateMessageRequest request = (PrivateMessageRequest)object;
			ConstructorAccess<? extends PrivateMessageEventHandler> access = ConstructorAccess.get(privateMessageEventHandler);
			PrivateMessageEventHandler handler = access.newInstance();
			handler.sender = sender;
			handler.onPrivateMessage(request.message, request.targetUser);
		} else if (object instanceof PublicMessageRequest) {
			PublicMessageRequest request = (PublicMessageRequest)object;
			ConstructorAccess<? extends PublicMessageEventHandler> access = ConstructorAccess.get(publicMessageEventHandler);
			PublicMessageEventHandler handler = access.newInstance();
			handler.sender = sender;
			handler.onPublicMessage(request.message, request.targetRoom);
		}
	}
	public void idle(Connection connection) {
		// TODO Auto-generated method stub
		super.idle(connection);
	}
}
