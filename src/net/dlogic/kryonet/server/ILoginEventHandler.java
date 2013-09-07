package net.dlogic.kryonet.server;

import com.esotericsoftware.kryonet.Connection;

public interface ILoginEventHandler {
	public void onLogin(Connection connection, String username, String password);
}
