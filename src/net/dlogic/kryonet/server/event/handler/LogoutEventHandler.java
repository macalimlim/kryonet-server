package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.response.LogoutResponse;

public abstract class LogoutEventHandler extends BaseEventHandler {
	public abstract void onLogout(User userLoggingOut);
	public void sendLogoutResponse() {
		LogoutResponse response = new LogoutResponse();
		response.userLoggingOut = sender;
		sender.getConnection().sendTCP(response);
	}
}
