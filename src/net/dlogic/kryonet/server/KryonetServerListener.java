package net.dlogic.kryonet.server;

import net.dlogic.kryonet.common.request.JoinRoomRequest;
import net.dlogic.kryonet.common.request.LoginRequest;
import net.dlogic.kryonet.server.event.handler.JoinRoomEventHandler;
import net.dlogic.kryonet.server.event.handler.LoginEventHandler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.reflectasm.ConstructorAccess;

public class KryonetServerListener extends Listener {
	private Class<? extends LoginEventHandler> loginEventHandler;
	private Class<? extends JoinRoomEventHandler> joinRoomEventHandler;
	public void setLoginEventHandler(Class<? extends LoginEventHandler> handler) {
		loginEventHandler = handler;
	}
	public void setJoinRoomEventHandler(Class<? extends JoinRoomEventHandler> handler) {
		joinRoomEventHandler = handler;
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
			LoginRequest reuqest = (LoginRequest)object;
			ConstructorAccess<? extends LoginEventHandler> access = ConstructorAccess.get(loginEventHandler);
			LoginEventHandler handler = access.newInstance();
			handler.setConnection(connection);
			handler.onLogin(reuqest.username, reuqest.password);
		} 
	}
	public void idle(Connection connection) {
		// TODO Auto-generated method stub
		super.idle(connection);
	}
}
