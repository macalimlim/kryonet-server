package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.exception.LoginException;
import net.dlogic.kryonet.common.response.LoginFailureResponse;
import net.dlogic.kryonet.common.response.LoginSuccessResponse;

public abstract class LoginEventHandler extends BaseEventHandler {
	public abstract void onLogin(String username, String password) throws LoginException;
	public final void sendLoginSuccessResponse() {
		LoginSuccessResponse response = new LoginSuccessResponse();
		response.myself = sender;
		sender.getConnection().sendTCP(response);
	}
	public final void sendLoginFailureResponse(String errorMessage) {
		LoginFailureResponse response = new LoginFailureResponse();
		response.errorMessage = errorMessage;
		sender.getConnection().sendTCP(response);
	}
}
