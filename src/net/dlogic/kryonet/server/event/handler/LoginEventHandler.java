package net.dlogic.kryonet.server.event.handler;

import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.response.LoginFailureResponse;
import net.dlogic.kryonet.common.response.LoginSuccessResponse;

public abstract class LoginEventHandler extends BaseEventHandler {
	public abstract void onLogin(String username, String password);
	public final void sendLoginSuccess(User user) {
		LoginSuccessResponse response = new LoginSuccessResponse();
		response.myself = user;
		connection.sendTCP(response);
	}
	public final void sendLoginFailure(String errorMessage) {
		LoginFailureResponse response = new LoginFailureResponse();
		response.errorMessage = errorMessage;
		connection.sendTCP(response);
	}
}
