import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.exception.JoinRoomException;
import net.dlogic.kryonet.server.event.handler.RoomEventHandler;


public class MyRoomEventHandler extends RoomEventHandler {

	@Override
	public void onJoinRoom(Room roomToJoin, String password)
			throws JoinRoomException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeaveRoom(Room roomToLeave) {
		// TODO Auto-generated method stub

	}

}
