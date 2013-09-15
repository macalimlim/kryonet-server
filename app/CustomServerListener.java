import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class CustomServerListener extends Listener {

	@Override
	public void connected(Connection connection) {
		// TODO Auto-generated method stub
		super.connected(connection);
	}

	@Override
	public void disconnected(Connection connection) {
		// TODO Auto-generated method stub
		super.disconnected(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		// TODO Auto-generated method stub
		super.received(connection, object);
	}

	@Override
	public void idle(Connection connection) {
		// TODO Auto-generated method stub
		super.idle(connection);
	}

}
