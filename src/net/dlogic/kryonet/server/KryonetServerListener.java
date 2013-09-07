package net.dlogic.kryonet.server;

import net.dlogic.kryonet.common.request.LoginRequest;
import net.dlogic.kryonet.server.event.handler.LoginEventHandler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.reflectasm.ConstructorAccess;

public class KryonetServerListener extends Listener {
	private Class<? extends LoginEventHandler> loginEventHandler;
	public void setLoginEventHandler(Class<LoginEventHandler> handler) {
		loginEventHandler = handler;
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
		if (object instanceof LoginRequest) {
			LoginRequest loginRequest = (LoginRequest)object;
			ConstructorAccess<? extends LoginEventHandler> access = ConstructorAccess.get(loginEventHandler);
			LoginEventHandler handler = access.newInstance();
			handler.setConnection(connection);
			handler.onLogin(loginRequest.username, loginRequest.password);
		}
	}
	public void idle(Connection connection) {
		// TODO Auto-generated method stub
		super.idle(connection);
	}
}
