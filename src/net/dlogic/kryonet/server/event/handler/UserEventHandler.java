package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.exception.LoginException;

public abstract class UserEventHandler extends BaseEventHandler {
	public abstract void onLogin(String username, String password) throws LoginException;
	public abstract void onLogout();
}
