import com.esotericsoftware.minlog.Log;

import net.dlogic.kryonet.common.exception.LoginException;
import net.dlogic.kryonet.server.event.handler.UserEventHandler;

public class MyUserEventHandler extends UserEventHandler {

	@Override
	public void onLogin(String username, String password) throws LoginException {
		Log.info("MyLoginOrLogoutEventHandler.onLogin(" + username + ", " + password + ")");
	}

	@Override
	public void onLogout() {
		// TODO Auto-generated method stub
		
	}

}
