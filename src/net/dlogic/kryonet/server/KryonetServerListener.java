package net.dlogic.kryonet.server;

import net.dlogic.kryonet.common.request.JoinRoomRequest;
import net.dlogic.kryonet.common.request.LoginRequest;
import net.dlogic.kryonet.common.request.LogoutRequest;
import net.dlogic.kryonet.common.request.PrivateMessageRequest;
import net.dlogic.kryonet.server.event.handler.JoinRoomEventHandler;
import net.dlogic.kryonet.server.event.handler.LoginEventHandler;
import net.dlogic.kryonet.server.event.handler.LogoutEventHandler;
import net.dlogic.kryonet.server.event.handler.PrivateMessageEventHandler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.reflectasm.ConstructorAccess;

public class KryonetServerListener extends Listener {
	private Class<? extends JoinRoomEventHandler> joinRoomEventHandler;
	private Class<? extends LoginEventHandler> loginEventHandler;
	private Class<? extends LogoutEventHandler> logoutEventHandler;
	private Class<? extends PrivateMessageEventHandler> privateMessageEventHandler;
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
	public void connected(Connection connection) {
		// TODO Auto-generated method stub
		super.connected(connection);
	}
	public void disconnected(Connection connection) {
		// TODO Auto-generated method stub
		super.disconnected(connection);
	}
	public void received(Connection connection, Object object) {
		if (object instanceof JoinRoomRequest) {
			JoinRoomRequest request = (JoinRoomRequest)object;
			ConstructorAccess<? extends JoinRoomEventHandler> access = ConstructorAccess.get(joinRoomEventHandler);
			JoinRoomEventHandler handler = access.newInstance();
			handler.setConnection(connection);
			handler.onJoinRoom(request.roomToJoin, request.password);
		} else if (object instanceof LoginRequest) {
			LoginRequest request = (LoginRequest)object;
			ConstructorAccess<? extends LoginEventHandler> access = ConstructorAccess.get(loginEventHandler);
			LoginEventHandler handler = access.newInstance();
			handler.setConnection(connection);
			handler.onLogin(request.username, request.password);
		} else if (object instanceof LogoutRequest) {
			LogoutRequest request = (LogoutRequest)object;
			ConstructorAccess<? extends LogoutEventHandler> access = ConstructorAccess.get(logoutEventHandler);
			LogoutEventHandler handler = access.newInstance();
			handler.setConnection(connection);
			handler.onLogout(request.userLoggingOut);
		} else if (object instanceof PrivateMessageRequest) {
			PrivateMessageRequest request = (PrivateMessageRequest)object;
			ConstructorAccess<? extends PrivateMessageEventHandler> access = ConstructorAccess.get(privateMessageEventHandler);
			PrivateMessageEventHandler handler = access.newInstance();
			handler.setConnection(connection);
			handler.onPrivateMessage(request.message, request.targetUser);
		}
	}
	public void idle(Connection connection) {
		// TODO Auto-generated method stub
		super.idle(connection);
	}
}
