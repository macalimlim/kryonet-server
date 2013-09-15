import com.esotericsoftware.minlog.Log;

import net.dlogic.kryonet.common.constant.ErrorMessage;
import net.dlogic.kryonet.common.exception.LoginException;
import net.dlogic.kryonet.server.event.handler.UserEventHandler;

public class MyUserEventHandler extends UserEventHandler {

	@Override
	public void onLogin(String username, String password) throws LoginException {
		Log.info("MyUserEventHandler.onLogin(" + username + ", " + password + ")");
		if (username.equalsIgnoreCase("mike") && password.equalsIgnoreCase("mike")) {
			Log.info("Yaaay!");
		} else {
			throw new LoginException(ErrorMessage.INVALID_USERNAME_ANDOR_PASSWORD);
		}
	}

	@Override
	public void onLogout() {
		// TODO Auto-generated method stub
		
	}

}
