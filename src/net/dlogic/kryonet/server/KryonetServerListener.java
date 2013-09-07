package net.dlogic.kryonet.server;

import net.dlogic.kryonet.common.request.LoginRequest;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

public class KryonetServerListener extends Listener {
	private Class<ILoginEventHandler> loginEventHandler;
	public void setLoginEventHandler(Class<ILoginEventHandler> handler) {
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
			ConstructorAccess<ILoginEventHandler> access = ConstructorAccess.get(loginEventHandler);
			ILoginEventHandler handler = access.newInstance();
			handler.onLogin(connection, loginRequest.username, loginRequest.password);
		}
	}
	public void idle(Connection connection) {
		// TODO Auto-generated method stub
		super.idle(connection);
	}
}
