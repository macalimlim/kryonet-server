import com.esotericsoftware.minlog.Log;

import net.dlogic.kryonet.common.exception.LoginException;
import net.dlogic.kryonet.server.event.handler.LoginOrLogoutEventHandler;

public class MyLoginOrLogoutEventHandler extends LoginOrLogoutEventHandler {

	@Override
	public void onLogin(String username, String password) throws LoginException {
		Log.info("MyLoginOrLogoutEventHandler.onLogin(" + username + ", " + password + ")");
	}

	@Override
	public void onLogout() {
		// TODO Auto-generated method stub
		
	}

}
