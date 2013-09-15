import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.server.event.handler.PersonMessageEventHandler;


public class MyPersonMessageEventHandler extends PersonMessageEventHandler {

	@Override
	public void onPrivateMessage(User targetUser, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPublicMessage(Room targetRoom, String message) {
		// TODO Auto-generated method stub

	}

}
