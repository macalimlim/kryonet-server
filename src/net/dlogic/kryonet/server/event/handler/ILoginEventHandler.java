package net.dlogic.kryonet.server.event.handler;

import com.esotericsoftware.kryonet.Connection;

public interface ILoginEventHandler extends IEventHandler {
	public void onLogin(Connection connection, String username, String password);
	public void sendLoginSuccess();
	public void sendLoginFailure();
}
